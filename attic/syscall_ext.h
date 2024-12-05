#ifndef LIBCORE_SHELLCODE_SYSCALL_EXT_H
#define LIBCORE_SHELLCODE_SYSCALL_EXT_H

#include "common_macros.h"

#include <type_traits>

EXPORT uintptr_t syscall_ext(int number, uintptr_t arg1, uintptr_t arg2, uintptr_t arg3,
                             uintptr_t arg4, uintptr_t arg5, uintptr_t arg6);


#ifdef __cplusplus

static inline bool is_error(uintptr_t res) {
    auto r = (ptrdiff_t) res;
    return r < 0 && r >= -4095;
}

static inline bool is_error(void* res) {
    return is_error(reinterpret_cast<uintptr_t>(res));
}

static inline int errno_of(uintptr_t res) {
    if (is_error(res)) {
        return -(int) (ptrdiff_t) res;
    }
    return 0;
}

static inline int errno_of(void* res) {
    return errno_of(reinterpret_cast<uintptr_t>(res));
}

#else

static inline int is_error(uintptr_t res) {
    auto r = (ptrdiff_t) res;
    return r < 0 && r >= -4095;
}

static inline int errno_of(uintptr_t res) {
    if (is_error(res)) {
        return -(int) (ptrdiff_t) res;
    }
    return 0;
}

#endif

static inline uintptr_t align_up(uintptr_t ptr, size_t alignment) {
    return (((uintptr_t) ptr + alignment - 1u) & ~(alignment - 1u));
}

static inline uintptr_t align_down(uintptr_t ptr, size_t alignment) {
    return ((uintptr_t) ptr & ~(alignment - 1u));
}

#endif //LIBCORE_SHELLCODE_SYSCALL_EXT_H
