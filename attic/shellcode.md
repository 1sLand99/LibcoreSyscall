# About Shellcode

Here are some explanations of the shellcode used in the library.

## Make the text section page aligned

The `.text` section had better be page aligned, in case there are some adrp+add instructions in the shellcode.

```c
// add this to the beginning of the C file
//__asm__(".if 0\n.endif"); /* ICC fix */
__asm__(".text\n.p2align 12,0,4095\n___text_dummy: .globl ___text_dummy");
//__asm__(".data\n.p2align 12,0,4095\n___data_dummy: .globl ___data_dummy");
//__asm__(".rodata\n.p2align 12,0,4095\n___rodata_dummy: .globl ___rodata_dummy");
```

Note that this line should be placed at the beginning of the C file, before any other code.

## JNI Wrappers

The JNI wrappers are just some simple function calls to the native functions.

Let's have a look at `NativeBridge_nativeCallPointerFunction4`,
all the arguments and the return value are `jlong` which is a 64-bit integer in JNI.
And they are casted to `void*` in the native function.

Any integer types or pointer which have the same or smaller size as `void*` can be passed as arguments into the function,
and any integer types which has the same or smaller size as `void*` as well as pointer/void can be used as the return value.
This is guaranteed by the x86-sysv/amd64/aapcs32/aapcs64/riscv64/mips calling conventions.

```c
#include <stdint.h>
#include <stddef.h>
#include <jni.h>

void __clear_cache(void* start, void* end);
uintptr_t syscall_ext(int number, uintptr_t arg1, uintptr_t arg2, uintptr_t arg3, uintptr_t arg4, uintptr_t arg5, uintptr_t arg6);

JNIEXPORT void JNICALL
NativeBridge_breakpoint
        (JNIEnv* env, jclass _k) {
    __builtin_debugtrap();
}

JNIEXPORT jlong JNICALL
NativeBridge_nativeSyscall
        (JNIEnv* env, jclass _k, jint number, jlong arg1, jlong arg2, jlong arg3, jlong arg4, jlong arg5, jlong arg6) {
    return (jlong) syscall_ext(number, (uintptr_t) arg1, (uintptr_t) arg2, (uintptr_t) arg3,
                               (uintptr_t) arg4, (uintptr_t) arg5, (uintptr_t) arg6);
}

JNIEXPORT void JNICALL
NativeBridge_nativeClearCache
        (JNIEnv* env, jclass _, jlong address, jlong size) {
    void* start = (void*) address;
    size_t sz = (size_t) size;
    void* end = (void*) ((uintptr_t) start + sz);
    __clear_cache(start, end);
}

JNIEXPORT jlong JNICALL
NativeBridge_nativeCallPointerFunction0
        (JNIEnv* env, jclass _, jlong function) {
    typedef void* (* FunctionType)();
    FunctionType f = (FunctionType) function;
    return (jlong) f();
}

JNIEXPORT jlong JNICALL
NativeBridge_nativeCallPointerFunction1
        (JNIEnv* env, jclass _, jlong function, jlong arg1) {
    typedef void* (* FunctionType)(void*);
    FunctionType f = (FunctionType) function;
    return (jlong) f((void*) arg1);
}

JNIEXPORT jlong JNICALL
NativeBridge_nativeCallPointerFunction2
        (JNIEnv* env, jclass _, jlong function, jlong arg1, jlong arg2) {
    typedef void* (* FunctionType)(void*, void*);
    FunctionType f = (FunctionType) function;
    return (jlong) f((void*) arg1, (void*) arg2);
}

JNIEXPORT jlong JNICALL
NativeBridge_nativeCallPointerFunction3
        (JNIEnv* env, jclass _, jlong function, jlong arg1, jlong arg2, jlong arg3) {
    typedef void* (* FunctionType)(void*, void*, void*);
    FunctionType f = (FunctionType) function;
    return (jlong) f((void*) arg1, (void*) arg2, (void*) arg3);
}

JNIEXPORT jlong JNICALL
NativeBridge_nativeCallPointerFunction4
        (JNIEnv* env, jclass _, jlong function, jlong arg1, jlong arg2, jlong arg3, jlong arg4) {
    typedef void* (* FunctionType)(void*, void*, void*, void*);
    FunctionType f = (FunctionType) function;
    return (jlong) f((void*) arg1, (void*) arg2, (void*) arg3, (void*) arg4);
}

JNIEXPORT jlong JNICALL
NativeBridge_nativeGetJavaVM
        (JNIEnv* env, jclass _) {
    JavaVM* vm = NULL;
    if ((*env)->GetJavaVM(env, &vm) == 0) {
        return (jlong) vm;
    } else {
        return 0;
    }
}

```

## Syscall

Thanks to the linux-syscall-support library, things are much easier now.

But keep in mind that shellcode has no libc, so there is neither Thread-Local-Storage nor `errno`.

```c
__attribute__((always_inline))
static inline int is_error(uintptr_t res) {
    ssize_t r = (ssize_t) res;
    return r < 0 && r >= -4095;
}

#include <linux_syscall_support.h>

// remove the errno, we do not have TLS, return -errno if error
#undef  LSS_RETURN
#if defined(__i386__) || defined(__x86_64__) || defined(__ARM_ARCH_3__) \
 || defined(__ARM_EABI__) || defined(__aarch64__) || defined(__s390__) \
 || defined(__e2k__) || defined(__riscv) || defined(__loongarch_lp64)
/* Failing system calls return a negative result in the range of
 * -1..-4095. These are "errno" values with the sign inverted.
 */
#define LSS_RETURN(type, res)                                               \
    do {                                                                      \
      return (type) (res);                                                    \
    } while (0)
#elif defined(__mips__)
/* On MIPS, failing system calls return -1, and set errno in a
 * separate CPU register.
 */
#define LSS_RETURN(type, res, err)                                          \
    do {                                                                      \
      if (err) {                                                              \
        unsigned long __errnovalue = (res);                                   \
        res = -(signed long)__errnovalue;                                                 \
      }                                                                       \
      return (type) (res);                                                    \
    } while (0)
#endif
// we do not support powerpc

#if defined(__x86_64__)
#undef  _LSS_RETURN
#define _LSS_RETURN(type, res, cast)                                      \
      do {                                                                    \
        return (type)(cast)(res);                                             \
      } while (0)
#undef  LSS_RETURN
#define LSS_RETURN(type, res) _LSS_RETURN(type, res, uintptr_t)
#endif

__attribute__((noinline))
uintptr_t syscall_ext
        (int number, uintptr_t arg1, uintptr_t arg2, uintptr_t arg3, uintptr_t arg4, uintptr_t arg5, uintptr_t arg6) {
    uintptr_t nr = (uintptr_t) number;
    uintptr_t __NR_name = nr;
#if defined(__x86_64__)
    LSS_BODY(6, jlong, name, LSS_SYSCALL_ARG(arg1), LSS_SYSCALL_ARG(arg2),
             LSS_SYSCALL_ARG(arg3), LSS_SYSCALL_ARG(arg4),
             LSS_SYSCALL_ARG(arg5), LSS_SYSCALL_ARG(arg6));
#elif defined(__i386__)
    long __res;
    struct {
        long __a1;
        long __a6;
        long __nr;
    } __s = {(long) arg1, (long) arg6, (long) __NR_name};
    __asm__ __volatile__("push %%ebp\n"
                         "push %%ebx\n"
                         "movl 4(%1),%%ebp\n"
                         "movl 0(%1),%%ebx\n"
                         "movl 8(%1),%%eax\n"
                         LSS_ENTRYPOINT
                         "pop  %%ebx\n"
                         "pop  %%ebp"
            : "=a" (__res)
            :
            "0" ((long) (&__s)),
            "c" ((long) (arg2)), "d" ((long) (arg3)),
            "S" ((long) (arg4)), "D" ((long) (arg5))
            : "memory");
    LSS_RETURN(uintptr_t, __res);
#elif defined(__aarch64__)

#undef  LSS_BODY
#define LSS_BODY(type,name,args...)                                       \
          register int64_t __res_x0 __asm__("x0");                            \
          int64_t __res;                                                      \
          __asm__ __volatile__ ("mov x8, %1\n"                                \
                                "svc 0x0\n"                                   \
                                : "=r"(__res_x0)                              \
                                : "r"(__NR_##name) , ## args                  \
                                : "x8", "memory");                            \
          __res = __res_x0;                                                   \
          LSS_RETURN(type, __res)

    LSS_REG(0, arg1); LSS_REG(1, arg2); LSS_REG(2, arg3);
    LSS_REG(3, arg4); LSS_REG(4, arg5); LSS_REG(5, arg6);
    LSS_BODY(jlong, name, "r"(__r0), "r"(__r1), "r"(__r2), "r"(__r3),
                         "r"(__r4), "r"(__r5));
#elif defined(__ARM_EABI__)

#undef  LSS_BODY
#define LSS_BODY(type,name,args...)                                       \
          register long __res_r0 __asm__("r0");                               \
          long __res;                                                         \
          __asm__ __volatile__ ("push {r7}\n"                                 \
                                "mov r7, %1\n"                                \
                                "swi 0x0\n"                                   \
                                "pop {r7}\n"                                  \
                                : "=r"(__res_r0)                              \
                                : "r"(__NR_##name) , ## args                  \
                                : "lr", "memory");                            \
          __res = __res_r0;                                                   \
          LSS_RETURN(type, __res)


    LSS_REG(0, arg1); LSS_REG(1, arg2); LSS_REG(2, arg3);
    LSS_REG(3, arg4); LSS_REG(4, arg5); LSS_REG(5, arg6);
    LSS_BODY(jlong, name, "r"(__r0), "r"(__r1), "r"(__r2), "r"(__r3),
                         "r"(__r4), "r"(__r5));
#elif defined(__ARM_ARCH_3__)
LSS_REG(0, arg1); LSS_REG(1, arg2); LSS_REG(2, arg3);
    LSS_REG(3, arg4); LSS_REG(4, arg5); LSS_REG(5, arg6);
    LSS_BODY(jlong, name, "r"(__r0), "r"(__r1), "r"(__r2), "r"(__r3),
                         "r"(__r4), "r"(__r5));
#elif defined(__riscv) && __riscv_xlen == 64
LSS_REG(0, arg1); LSS_REG(1, arg2); LSS_REG(2, arg3);
    LSS_REG(3, arg4); LSS_REG(4, arg5); LSS_REG(5, arg6);
    LSS_BODY(jlong, name, "r"(__r0), "r"(__r1), "r"(__r2), "r"(__r3),
                         "r"(__r4), "r"(__r5));
#elif defined(__mips__)
#if _MIPS_SIM == _MIPS_SIM_ABI32
LSS_REG(4, arg1); LSS_REG(5, arg2); LSS_REG(6, arg3);
    LSS_REG(7, arg4);
    register unsigned long __v0 __asm__("$2") = nr;
    __asm__ __volatile__ (".set noreorder\n"
                          "subu  $29, 32\n"
                          "sw    %5, 16($29)\n"
                          "sw    %6, 20($29)\n"
                          "syscall\n"
                          "addiu $29, 32\n"
                          ".set reorder\n"
                          : "+r"(__v0), "+r" (__r7)
                          : "r"(__r4), "r"(__r5),
                            "r"(__r6), "r" ((unsigned long)arg5),
                            "r" ((unsigned long)arg6)
                          : "$8", "$9", "$10", "$11", "$12",
                            "$13", "$14", "$15", "$24", "$25",
                            "memory");
    LSS_RETURN(jlong, __v0, __r7);
#else
LSS_REG(4, arg1); LSS_REG(5, arg2); LSS_REG(6, arg3);
    LSS_REG(7, arg4); LSS_REG(8, arg5); LSS_REG(9, arg6);
    LSS_BODY(jlong, name, "+r", "r"(__r4), "r"(__r5), "r"(__r6),
             "r"(__r8), "r"(__r9));
#endif

#else
#error "Unsupported architecture"
#endif
}
```

## Clear Cache

While the `__clear_cache` function is used without libc, a platform-specific implementation is needed.

```c
#if !defined(assert)
#define assert(x) do { if (!(x)) __builtin_trap(); } while (0)
#endif

/*
 * The compiler generates calls to __clear_cache() when creating
 * trampoline functions on the stack for use with nested functions.
 * It is expected to invalidate the instruction cache for the
 * specified range.
 */

void __clear_cache(void* start, void* end) {
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

```

## Readonly Data

To make things easier, the shellcode should only have a `.text` section,
and no `.data`/`.bss`/`.rodata`/`.got`/`.plt` sections.
The readonly data in the `.text` section can be populated from Java side at runtime.

```c

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

_Static_assert(sizeof(struct HookInfo) == 24, "HookInfo size");

typedef struct HookInfo HookInfo;

__attribute__((noinline))
volatile HookInfo* get_hook_info() {
    // place the hook info in the .text section, it will be filled before the shellcode is executed
    __attribute__((aligned(16), section(".text")))
    static volatile HookInfo sHookInfo = {0xdeafbeef, {(int* (*)()) 0x114514}, {0x1000}};
    return &sHookInfo;
}
```

## Linker Hook

It obvious that the ashmem neither supports mmap in `MAP_PRIVATE` mode nor shows its size in the `fstat64` syscall.

However, the linker does need them to load the shared object from ashmem.

```c
#include <unistd.h>
#include <sys/ioctl.h>
#include <sys/mman.h>
#include <sys/syscall.h>
#include <linux/ioctl.h>

#define __ASHMEMIOC 0x77
#define ASHMEM_SET_NAME _IOW(__ASHMEMIOC, 1, char[ASHMEM_NAME_LEN])
#define ASHMEM_GET_NAME _IOR(__ASHMEMIOC, 2, char[ASHMEM_NAME_LEN])
#define ASHMEM_SET_SIZE _IOW(__ASHMEMIOC, 3, size_t)
#define ASHMEM_GET_SIZE _IO(__ASHMEMIOC, 4)
#define ASHMEM_SET_PROT_MASK _IOW(__ASHMEMIOC, 5, unsigned long)
#define ASHMEM_GET_PROT_MASK _IO(__ASHMEMIOC, 6)
#define ASHMEM_PIN _IOW(__ASHMEMIOC, 7, struct ashmem_pin)
#define ASHMEM_UNPIN _IOW(__ASHMEMIOC, 8, struct ashmem_pin)
#define ASHMEM_GET_PIN_STATUS _IO(__ASHMEMIOC, 9)
#define ASHMEM_PURGE_ALL_CACHES _IO(__ASHMEMIOC, 10)
#define ASHMEM_NAME_LEN 256

__attribute__((noinline))
ssize_t ashmem_dev_get_size_region(int fd) {
    int NR_ioctl = __NR_ioctl;
    ssize_t res = (ssize_t) syscall_ext(NR_ioctl, fd, ASHMEM_GET_SIZE, 0, 0, 0, 0);
    return res;
}

static inline uintptr_t align_up(uintptr_t ptr, size_t alignment) {
    return (((uintptr_t) ptr + alignment - 1u) & ~(alignment - 1u));
}

static inline uintptr_t align_down(uintptr_t ptr, size_t alignment) {
    return ((uintptr_t) ptr & ~(alignment - 1u));
}

__attribute__((noinline))
ssize_t lsw_pread64(int fd, void* buf, size_t count, uint64_t offset) {
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

__attribute__((noinline))
int lsw_mprotect(void* addr, size_t len, int prot) {
    const int NR_mprotect = __NR_mprotect;
    return (int) syscall_ext(NR_mprotect, (uintptr_t) addr, len, prot, 0, 0, 0);
}

__attribute__((noinline))
int fake_fstat64(int fd, void* buf) {
    HookInfo* info = (HookInfo*) get_hook_info();
#if defined(__LP64__)
    struct kernel_stat* stat = buf;
    const int NR_fstat = __NR_fstat;
#else
    struct kernel_stat64* stat = buf;
    const int NR_fstat = __NR_fstat64;
#endif
    uintptr_t res = syscall_ext(NR_fstat, fd, (uintptr_t) stat, 0, 0, 0, 0);
    if (is_error(res)) {
        // set errno and return -1
        int* the_errno = info->fn_dl_errno();
        *the_errno = -(int) (ssize_t) res;
        return -1;
    }
    // check if the fd is an ashmem fd
    if (stat->st_dev == info->ashmem_dev_v && info->ashmem_dev_v != 0 && stat->st_size == 0) {
        // fake the stat.st_size with ashmem size
        ssize_t ssize = ashmem_dev_get_size_region(fd);
        if (!is_error(ssize)) {
            // overwrite the st_size
            stat->st_size = (kernel_off_t) (size_t) ssize;
        }
    }
    return 0;
}

__attribute__((noinline))
void* fake_mmap64(void* addr, size_t length, int prot, int flags, int fd, uint64_t offset) {
    HookInfo* info = (HookInfo*) get_hook_info();
    // we need have an eye on MAP_PRIVATE on ashmem because it is not supported
    int ashmemFd = -1;
    uint64_t ashmemOffset = 0;
    uint64_t ashmemMapSize = 0;
    int targetProtect = prot;
    if (fd >= 0 && (flags & MAP_PRIVATE) != 0 && (flags & MAP_ANONYMOUS) == 0) {
        // check if the fd is an ashmem fd
#if defined(__LP64__)
        struct kernel_stat stat = {0};
#else
        struct kernel_stat64 stat = {0};
#endif
        if (fake_fstat64(fd, &stat) == 0) {
            if (stat.st_dev == info->ashmem_dev_v && info->ashmem_dev_v != 0) {
                // since ashmem does not support MAP_PRIVATE, we need to manually handle it
                // if the later mmap succeeds, we need to copy the content from the ashmem with pread64
                ashmemFd = fd;
                ashmemOffset = offset;
                ashmemMapSize = length;

                // we need to use pread to fill the content
                prot |= PROT_READ | PROT_WRITE;
            }
        }
    }
    // do the real mmap64 syscall for the modified flags
    int* the_errno = info->fn_dl_errno();
    if (offset % 4096u != 0) {
        *the_errno = EINVAL;
        return MAP_FAILED;
    }
    uintptr_t res;
#if defined(__LP64__)
    res = syscall_ext(__NR_mmap, (uintptr_t) addr, length, prot, flags, fd, offset);
#else
    // check whether offset >> 12u exceeds the range of uintptr_t
    if ((offset >> 12u) > UINTPTR_MAX) {
        *the_errno = EINVAL;
        return MAP_FAILED;
    }
    res = syscall_ext(__NR_mmap2, (uintptr_t) addr, length, prot, flags, fd, offset >> 12u);
#endif
    if (is_error(res)) {
        // set errno and return MAP_FAILED
        *the_errno = -(int) (ssize_t) res;
        return MAP_FAILED;
    } else {
        if (ashmemFd != -1) {
            uint64_t off = ashmemOffset;
            uint64_t remaining = ashmemMapSize;
            void* p = (void*) res;
            // fill the content from ashmem to the private mapping
            while (remaining > 0) {
                size_t toRead = remaining > 4096u ? 4096u : (size_t) remaining;
                ssize_t r = lsw_pread64(ashmemFd, p, toRead, off);
                if (r <= 0) {
                    if (r == -EINTR) {
                        // for EINTR, we just retry
                        continue;
                    }
                    // For r < 0, an error occurs,
                    // we just break the loop and return the original mmap result,
                    // as if we did not handle the MAP_PRIVATE on ashmem.
                    // For r == 0 it means EOF, we just break.
                    break;
                }
                remaining -= r;
                off += r;
                p = (void*) ((uintptr_t) p + r);
            }
            // restore the original protect
            lsw_mprotect((void*) res, align_up(length, info->page_size), targetProtect);
            if ((targetProtect & PROT_EXEC) != 0) {
                uintptr_t pageStart = align_down((uintptr_t) res, info->page_size);
                uintptr_t pageEnd = align_up((uintptr_t) res + length, info->page_size);
                // flush the icache
                __clear_cache((void*) pageStart, (void*) pageEnd);
            }
        }
    }
    return (void*) res;
}

__attribute__((noinline))
void* fake_mmap(void* addr, size_t length, int prot, int flags, int fd, uintptr_t offset) {
    return fake_mmap64(addr, length, prot, flags, fd, offset);
}
```

It's really dirty to hook the linker, but it is the only simple way to load the shared object from ashmem.

## Linker Complaint about Missing Symbols

Linker may complain about missing symbols, we can just copy them from the musl libc.

```c
__attribute__((weak))
void* memset(void* dest, int c, size_t n) {
    unsigned char* s = dest;
    size_t k;

    /* Fill head and tail with minimal branching. Each
     * conditional ensures that all the subsequently used
     * offsets are well-defined and in the dest region. */

    if (!n) return dest;
    s[0] = c;
    s[n - 1] = c;
    if (n <= 2) return dest;
    s[1] = c;
    s[2] = c;
    s[n - 2] = c;
    s[n - 3] = c;
    if (n <= 6) return dest;
    s[3] = c;
    s[n - 4] = c;
    if (n <= 8) return dest;

    /* Advance pointer to align it at a 4-byte boundary,
     * and truncate n to a multiple of 4. The previous code
     * already took care of any head/tail that get cut off
     * by the alignment. */

    k = -(uintptr_t) s & 3;
    s += k;
    n -= k;
    n &= -4;

#if defined(__GNUC__)
    typedef uint32_t __attribute__((__may_alias__)) u32;
    typedef uint64_t __attribute__((__may_alias__)) u64;

    u32 c32 = ((u32) -1) / 255 * (unsigned char) c;

    /* In preparation to copy 32 bytes at a time, aligned on
     * an 8-byte bounary, fill head/tail up to 28 bytes each.
     * As in the initial byte-based head/tail fill, each
     * conditional below ensures that the subsequent offsets
     * are valid (e.g. !(n<=24) implies n>=28). */

    *(u32*) (s + 0) = c32;
    *(u32*) (s + n - 4) = c32;
    if (n <= 8) return dest;
    *(u32*) (s + 4) = c32;
    *(u32*) (s + 8) = c32;
    *(u32*) (s + n - 12) = c32;
    *(u32*) (s + n - 8) = c32;
    if (n <= 24) return dest;
    *(u32*) (s + 12) = c32;
    *(u32*) (s + 16) = c32;
    *(u32*) (s + 20) = c32;
    *(u32*) (s + 24) = c32;
    *(u32*) (s + n - 28) = c32;
    *(u32*) (s + n - 24) = c32;
    *(u32*) (s + n - 20) = c32;
    *(u32*) (s + n - 16) = c32;

    /* Align to a multiple of 8 so we can fill 64 bits at a time,
     * and avoid writing the same bytes twice as much as is
     * practical without introducing additional branching. */

    k = 24 + ((uintptr_t) s & 4);
    s += k;
    n -= k;

    /* If this loop is reached, 28 tail bytes have already been
     * filled, so any remainder when n drops below 32 can be
     * safely ignored. */

    u64 c64 = c32 | ((u64) c32 << 32);
    for (; n >= 32; n -= 32, s += 32) {
        *(u64*) (s + 0) = c64;
        *(u64*) (s + 8) = c64;
        *(u64*) (s + 16) = c64;
        *(u64*) (s + 24) = c64;
    }
#else
    /* Pure C fallback with no aliasing violations. */
    for (; n; n--, s++) *s = c;
#endif

    return dest;
}
```

## Compile the Shellcode

Some compilers/linker specific flags are needed to compile the shellcode.

- Symbolic linking, do not use GOT/PLT: `-Wl,-Bsymbolic`
- No libc: `-nostdlib`
- Shared Object: `-shared -fPIC`
- Visibility: `-fvisibility=default`
- Make linker complain about missing symbols: `-Wl,--no-allow-shlib-undefined,--no-undefined -Wl,-z,defs,-z,now,-z,relro`

Get the symbol table: `llvm-objdump -Tt libcore_syscall.so`

Dump the text section: `llvm-objcopy -O binary --only-section=.text libcore_syscall.so shellcode.bin`

That's all for the shellcode.
