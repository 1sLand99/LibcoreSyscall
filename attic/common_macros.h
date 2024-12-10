#ifndef LIBCORE_SHELLCODE_MACROS_H
#define LIBCORE_SHELLCODE_MACROS_H

#if defined(__cplusplus)

#include <cstdint>
#include <cstddef>

#define NO_MANGLE extern "C"
#define EXPORT extern "C" __attribute__((visibility("default")))
#define NO_RETURN [[noreturn]]

#else

#include <stdint.h>
#include <stddef.h>

#define NO_MANGLE
#define EXPORT __attribute__((visibility("default")))
#define NO_RETURN __attribute__((noreturn))

#endif

#endif //LIBCORE_SHELLCODE_MACROS_H
