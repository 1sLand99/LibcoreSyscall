#include "syscall_wrapper.h"

#include <sys/syscall.h>
#include <sys/mman.h>

#include "fake_fstat64.h"
#include "syscall_ext.h"

EXPORT int lsw_mprotect(void* addr, size_t len, int prot) {
    const int NR_mprotect = __NR_mprotect;
    return (int) syscall_ext(NR_mprotect, (uintptr_t) addr, len, prot, 0, 0, 0);
}

EXPORT ssize_t lsw_pread64(int fd, void* buf, size_t count, uint64_t offset) {
    const int NR_pread64 = __NR_pread64;
#if defined(__LP64__)
    return (ssize_t) syscall_ext(NR_pread64, fd, (uintptr_t) buf, count, offset, 0, 0);
#else
    uintptr_t off_h32 = offset >> 32u;
    uintptr_t off_l32 = offset & 0xffffffffu;
#if defined(__arm__)
    // arm eabi requires 64bit arguments to be passed in pairs
    return (ssize_t) syscall_ext(NR_pread64, fd, (uintptr_t) buf, count, 0, off_l32, off_h32);
#else
    return (ssize_t) syscall_ext(NR_pread64, fd, (uintptr_t) buf, count, off_l32, off_h32, 0);
#endif
#endif
}

EXPORT void* lsw_mmap64(void* addr, size_t length, int prot, int flags, int fd, uint64_t offset) {
    uintptr_t res;
#if defined(__LP64__)
    res = syscall_ext(__NR_mmap, (uintptr_t) addr, length, prot, flags, fd, offset);
#else
    // check whether offset >> 12u exceeds the range of uintptr_t
    if ((offset >> 12u) > UINTPTR_MAX) {
        return reinterpret_cast<void*>(-EOVERFLOW);
    }
    res = syscall_ext(__NR_mmap2, (uintptr_t) addr, length, prot, flags, fd, offset >> 12u);
#endif
    return (void*) res;
}

EXPORT int lsw_fstatfs64(int fd, kernel_statfs64_compat* buf) {
#if defined(__LP64__)
    const int NR_fstatfs = __NR_fstatfs;
#else
    const int NR_fstatfs = __NR_fstatfs64;
#endif
    return (int) syscall_ext(NR_fstatfs, fd, (uintptr_t) buf, 0, 0, 0, 0);
}

EXPORT int lsw_munmap(void* addr, size_t len) {
    const int NR_munmap = __NR_munmap;
    return (int) syscall_ext(NR_munmap, (uintptr_t) addr, len, 0, 0, 0, 0);
}
