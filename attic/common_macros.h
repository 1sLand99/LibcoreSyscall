#ifndef LIBCORE_SHELLCODE_MACROS_H
#define LIBCORE_SHELLCODE_MACROS_H

#if defined(__cplusplus)

#include <cstdint>
#include <cstddef>

#define EXPORT extern "C" __attribute__((visibility("default")))

#else

#include <stdint.h>
#include <stddef.h>

#define EXPORT __attribute__((visibility("default")))

#endif

#endif //LIBCORE_SHELLCODE_MACROS_H
