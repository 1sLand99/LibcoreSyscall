#ifndef LIBCORE_SHELLCODE_SYSCALL_WRAPPERS_H
#define LIBCORE_SHELLCODE_SYSCALL_WRAPPERS_H

#include <sys/types.h>

#include "common_macros.h"

EXPORT ssize_t lsw_pread64(int fd, void* buf, size_t count, uint64_t offset);
EXPORT int lsw_mprotect(void* addr, size_t len, int prot);

#endif //LIBCORE_SHELLCODE_SYSCALL_WRAPPERS_H
