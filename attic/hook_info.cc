#include "hook_info.h"

EXPORT volatile HookInfo* get_hook_info() {
    // place the hook info in the .text section, it will be filled before the shellcode is executed
    __attribute__((aligned(16), section(".text")))
    static volatile HookInfo sHookInfo = {0xdeafbeef, {(int* (*)()) 0x114514}, {0x1000}};
    return &sHookInfo;
}

__attribute__((noinline))
EXPORT const void* get_current_pc() {
    return __builtin_return_address(0);
}
