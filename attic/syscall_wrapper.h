#ifndef LIBCORE_SHELLCODE_SYSCALL_WRAPPERS_H
#define LIBCORE_SHELLCODE_SYSCALL_WRAPPERS_H

#include <sys/types.h>

#include "common_macros.h"

// Linux syscall wrappers, are not the same as libc functions. They do not have an errno variable.
// They return the error code directly, and the caller should check the return value to see if it is an error.

#ifdef __LP64__

typedef struct kernel_statfs kernel_statfs64_compat;

#else

typedef struct kernel_statfs64 kernel_statfs64_compat;

#endif

EXPORT ssize_t lsw_pread64(int fd, void* buf, size_t count, uint64_t offset);
EXPORT int lsw_mprotect(void* addr, size_t len, int prot);
EXPORT void* lsw_mmap64(void* addr, size_t len, int prot, int flags, int fd, uint64_t offset);
EXPORT int lsw_fstatfs64(int fd, kernel_statfs64_compat* buf);
EXPORT int lsw_munmap(void* addr, size_t len);

#endif //LIBCORE_SHELLCODE_SYSCALL_WRAPPERS_H
