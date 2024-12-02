#ifndef LIBCORE_SHELLCODE_HOOK_INFO_H
#define LIBCORE_SHELLCODE_HOOK_INFO_H

#include "common_macros.h"

#ifdef __cplusplus
extern "C" {
#endif

// @formatter:off
struct HookInfo {
    // dev_t for ashmem
    uint64_t ashmem_dev_v;
    union {
        int* (* fn_dl_errno)();
        uint64_t _padding_1;
    };
    union {
        size_t page_size;
        uint64_t _padding_2;
    };
};
// @formatter:on

volatile HookInfo* get_hook_info();

#ifdef __cplusplus
}
#endif

#ifdef __cplusplus

static_assert(sizeof(HookInfo) == 24, "HookInfo size");

#endif

#endif //LIBCORE_SHELLCODE_HOOK_INFO_H
