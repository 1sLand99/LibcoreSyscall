#include <sys/mman.h>

#include "common_macros.h"
#include "syscall_wrapper.h"
#include "syscall_ext.h"
#include "hook_info.h"
#include "fake_fstat64.h"
#include "clear_cache.h"


EXPORT void* fake_mmap64(void* addr, size_t length, int prot, int flags, int fd, uint64_t offset) {
    auto* info = (HookInfo*) get_hook_info();
    // we need have an eye on MAP_PRIVATE on ashmem because it is not supported
    int ashmemFd = -1;
    uint64_t ashmemOffset = 0;
    uint64_t ashmemMapSize = 0;
    int targetProtect = prot;
    if (fd >= 0 && (flags & MAP_PRIVATE) != 0 && (flags & MAP_ANONYMOUS) == 0) {
        // check if the fd is an ashmem fd
        kernel_stat64_compat stat = {0};
        if (fake_fstat64(fd, &stat) == 0) {
            if (stat.st_dev == info->ashmem_dev_v && info->ashmem_dev_v != 0) {
                // since ashmem does not support MAP_PRIVATE, we need to manually handle it
                // if the later mmap succeeds, we need to copy the content from the ashmem with pread64
                ashmemFd = fd;
                ashmemOffset = offset;
                ashmemMapSize = length;

                // we need to use pread to fill the content
                prot |= PROT_READ | PROT_WRITE;
            }
        }
    }
    // do the real mmap64 syscall for the modified flags
    int* the_errno = info->fn_dl_errno();
    if (offset % 4096u != 0) {
        *the_errno = EINVAL;
        return MAP_FAILED;
    }
    uintptr_t res;
#if defined(__LP64__)
    res = syscall_ext(__NR_mmap, (uintptr_t) addr, length, prot, flags, fd, offset);
#else
    // check whether offset >> 12u exceeds the range of uintptr_t
    if ((offset >> 12u) > UINTPTR_MAX) {
        *the_errno = EINVAL;
        return MAP_FAILED;
    }
    res = syscall_ext(__NR_mmap2, (uintptr_t) addr, length, prot, flags, fd, offset >> 12u);
#endif
    if (is_error(res)) {
        // set errno and return MAP_FAILED
        *the_errno = -(int) (ssize_t) res;
        return MAP_FAILED;
    } else {
        if (ashmemFd != -1) {
            uint64_t off = ashmemOffset;
            uint64_t remaining = ashmemMapSize;
            void* p = (void*) res;
            // fill the content from ashmem to the private mapping
            while (remaining > 0) {
                size_t toRead = remaining > 4096u ? 4096u : (size_t) remaining;
                ssize_t r = lsw_pread64(ashmemFd, p, toRead, off);
                if (r <= 0) {
                    if (r == -EINTR) {
                        // for EINTR, we just retry
                        continue;
                    }
                    // For r < 0, an error occurs,
                    // we just break the loop and return the original mmap result,
                    // as if we did not handle the MAP_PRIVATE on ashmem.
                    // For r == 0 it means EOF, we just break.
                    break;
                }
                remaining -= r;
                off += r;
                p = (void*) ((uintptr_t) p + r);
            }
            // restore the original protect
            lsw_mprotect((void*) res, align_up(length, info->page_size), targetProtect);
            if ((targetProtect & PROT_EXEC) != 0) {
                uintptr_t pageStart = align_down((uintptr_t) res, info->page_size);
                uintptr_t pageEnd = align_up((uintptr_t) res + length, info->page_size);
                // flush the icache
                __clear_cache((void*) pageStart, (void*) pageEnd);
            }
        }
    }
    return (void*) res;
}

EXPORT void* fake_mmap(void* addr, size_t length, int prot, int flags, int fd, uintptr_t offset) {
    return fake_mmap64(addr, length, prot, flags, fd, offset);
}
