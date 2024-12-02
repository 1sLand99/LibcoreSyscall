#include "syscall_wrapper.h"

#include <sys/syscall.h>
#include <sys/mman.h>

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
