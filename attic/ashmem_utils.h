#ifndef LIBCORE_SHELLCODE_ASHMEM_UTILS_H
#define LIBCORE_SHELLCODE_ASHMEM_UTILS_H

#include <sys/types.h>

#include "common_macros.h"

EXPORT ssize_t ashmem_dev_get_size_region(int fd);

#endif //LIBCORE_SHELLCODE_ASHMEM_UTILS_H
