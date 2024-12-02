#include "clear_cache.h"

#include "common_macros.h"

#if !defined(assert)
#define assert(x) do { if (!(x)) __builtin_trap(); } while (0)
#endif

/*
 * The compiler generates calls to __clear_cache() when creating
 * trampoline functions on the stack for use with nested functions.
 * It is expected to invalidate the instruction cache for the
 * specified range.
 */

EXPORT void __clear_cache(void* start, void* end) {
#if __i386__ || __x86_64__
    /*
     * Intel processors have a unified instruction and data cache
     * so there is nothing to do
     */
#elif defined(__arm__) && !defined(__APPLE__)
#if defined(__FreeBSD__) || defined(__NetBSD__) || defined(__Bitrig__)
    struct arm_sync_icache_args arg;

    arg.addr = (uintptr_t)start;
    arg.len = (uintptr_t)end - (uintptr_t)start;

    sysarch(ARM_SYNC_ICACHE, &arg);
#elif defined(__linux__)
    register int start_reg __asm("r0") = (int) (intptr_t) start;
    const register int end_reg __asm("r1") = (int) (intptr_t) end;
    const register int flags __asm("r2") = 0;
    const register int syscall_nr __asm("r7") = __ARM_NR_cacheflush;
    __asm __volatile("svc 0x0"
                     : "=r"(start_reg)
                     : "r"(syscall_nr), "r"(start_reg), "r"(end_reg), "r"(flags));
    if (start_reg != 0) {
        __builtin_trap();
    }
#elif defined(_WIN32)
    FlushInstructionCache(GetCurrentProcess(), start, end - start);
#else
    __builtin_trap();
#endif
#elif defined(__mips__)
    const uintptr_t start_int = (uintptr_t)start;
    const uintptr_t end_int = (uintptr_t)end;
    uintptr_t synci_step;
    __asm__ volatile("rdhwr %0, $1" : "=r"(synci_step));
    if (synci_step != 0) {
#if __mips_isa_rev >= 6
        for (uintptr_t p = start_int; p < end_int; p += synci_step)
          __asm__ volatile("synci 0(%0)" : : "r"(p));

        // The last "move $at, $0" is the target of jr.hb instead of delay slot.
        __asm__ volatile(".set noat\n"
                         "sync\n"
                         "addiupc $at, 12\n"
                         "jr.hb $at\n"
                         "move $at, $0\n"
                         ".set at");
#elif 0 && (defined(__linux__) || defined(__OpenBSD__))
        // Pre-R6 may not be globalized. And some implementations may give strange
        // synci_step. So, let's use libc call for it.
        _flush_cache(start, end_int - start_int, BCACHE);
#else
        (void)start_int;
        (void)end_int;
        __builtin_trap();
#endif
    }
#elif defined(__aarch64__) && !defined(__APPLE__)
    uint64_t xstart = (uint64_t)(uintptr_t) start;
    uint64_t xend = (uint64_t)(uintptr_t) end;
    uint64_t addr;

    // Get Cache Type Info
    uint64_t ctr_el0;
    __asm __volatile("mrs %0, ctr_el0" : "=r"(ctr_el0));

    /*
    * dc & ic instructions must use 64bit registers so we don't use
    * uintptr_t in case this runs in an IPL32 environment.
    */
    const size_t dcache_line_size = 4 << ((ctr_el0 >> 16) & 15);
    for (addr = xstart; addr < xend; addr += dcache_line_size)
    __asm __volatile("dc cvau, %0" :: "r"(addr));
    __asm __volatile("dsb ish");

    const size_t icache_line_size = 4 << ((ctr_el0 >> 0) & 15);
    for (addr = xstart; addr < xend; addr += icache_line_size)
    __asm __volatile("ic ivau, %0" :: "r"(addr));
    __asm __volatile("isb sy");

#elif defined(__riscv) && defined(__linux__)
    // See: arch/riscv/include/asm/cacheflush.h, arch/riscv/kernel/sys_riscv.c
    register void *start_reg __asm("a0") = start;
    const register void *end_reg __asm("a1") = end;
    // "0" means that we clear cache for all threads (SYS_RISCV_FLUSH_ICACHE_ALL)
    const register long flags __asm("a2") = 0;
    const register long syscall_nr __asm("a7") = __NR_riscv_flush_icache;
    __asm __volatile("ecall"
                     : "=r"(start_reg)
                     : "r"(start_reg), "r"(end_reg), "r"(flags), "r"(syscall_nr));
    assert(start_reg == 0 && "Cache flush syscall failed.");
#else

#if __APPLE__
    /* On Darwin, sys_icache_invalidate() provides this functionality */
    sys_icache_invalidate(start, end-start);
#else
    __builtin_trap();
#endif
#endif
}
