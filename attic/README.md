# About Shellcode

Here are some explanations of the shellcode used in the library.

Note that all the shellcode has been already compiled and based64-embedded into the Java code,
there is no need to compile the shellcode again if everything works fine.

All these files in this `attic` directory are just for reference purposes.

## Make the text section page aligned

The `.text` section had better be page aligned, in case there are some adrp+add instructions in the shellcode.

```c
// add this to the beginning of the C file to align sections
__asm__(".if 0\n.endif"); /* ICC fix */
__asm__(".text\n.p2align 12,0,4095\n___text_section: .globl ___text_section");
__asm__(".data\n.p2align 12,0,4095\n___data_section: .globl ___data_section");
__asm__(".rodata\n.p2align 12,0,4095\n___rodata_section: .globl ___rodata_section");
```

Since there should be no `.data`/`.bss`/`.rodata`/`.got`/`.plt` sections in the shellcode,
the only section which needs to be page aligned is the `.text` section.

Note that this line should be placed at the beginning of the C file, before any other code.

## JNI Wrappers

The JNI wrappers are just some simple function calls to the native functions.

Let's have a look at `NativeBridge_nativeCallPointerFunction4`,
all the arguments and the return value are `jlong` which is a 64-bit integer in JNI.
And they are casted to `void*` in the native function.

Any integer types or pointer which have the same or smaller size as `void*` can be passed as arguments into the
function,
and any integer types which has the same or smaller size as `void*` as well as pointer/void can be used as the return
value.
This is guaranteed by the x86-sysv/amd64/aapcs32/aapcs64/riscv64/mips calling conventions.

See [jni_wrapper.cc](jni_wrapper.cc).

## Syscall

Thanks to the linux-syscall-support library, things are much easier now.

But keep in mind that shellcode has no libc, so there is neither Thread-Local-Storage nor `errno`.

See [syscall_ext.cc](syscall_ext.cc).

## Clear Cache

While the `__clear_cache` function is used without libc, a platform-specific implementation is needed.

See [clear_cache.cc](clear_cache.cc).

## Readonly Data

To make things easier, the shellcode should only have a `.text` section,
and no `.data`/`.bss`/`.rodata`/`.got`/`.plt` sections.
The readonly data in the `.text` section can be populated from Java side at runtime.

See [hook_info.cc](hook_info.cc).

## Linker Hook

It obvious that the ashmem neither supports mmap in `MAP_PRIVATE` mode nor shows its size in the `fstat64` syscall.

However, the linker does need them to load the shared object from ashmem.

It's a terrible idea to hook the linker, but it is the only simple way to load the shared object from ashmem.

See [fake_fstat64.cc](fake_fstat64.cc) and [fake_mmap64.cc](fake_mmap64.cc).

## Linker Complaint about Missing Symbols

Linker may complain about missing symbols, we can just copy them from the musl libc.

See [libc_support.cc](libc_support.cc).

## Compile the Shellcode

Some compilers/linker specific flags are needed to compile the shellcode.

- Symbolic linking, do not use GOT/PLT: `-Wl,-Bsymbolic`
- No libc: `-nostdlib`
- Disable RTTI and exceptions: `-fno-rtti -fno-exceptions`
- Shared Object: `-shared -fPIC`
- Visibility: `-fvisibility=hidden -fvisibility-inlines-hidden`
- Make linker complain about missing symbols: `-Wl,--no-allow-shlib-undefined,--no-undefined`
- Disable lazy binding: `-Wl,-z,defs,-z,now,-z,relro`
- Remove unused sections: `-Wl,--gc-sections`

An example command to compile the shellcode:

```shell
/path/to/clang++ -shared -fPIC -std=c++14 -O3 \
-fvisibility=hidden -fvisibility-inlines-hidden -fno-omit-frame-pointer -Wall \
-fno-rtti -fno-exceptions -nostdlib \
-Wl,-Bsymbolic,--no-allow-shlib-undefined,--no-undefined,-z,defs,-z,now,-z,relro,--gc-sections \
-I/path/to/jni/include -I/path/to/linux-syscall-support \
all_in_one.cc -o libcore_syscall.so
```

The `file *.so` output may look like this:

```text
shellcode-arm.so:     ELF 32-bit LSB shared object, ARM, EABI5 version 1 (SYSV), static-pie linked, not stripped
shellcode-arm64.so:   ELF 64-bit LSB shared object, ARM aarch64, version 1 (SYSV), static-pie linked, not stripped
shellcode-mips.so:    ELF 32-bit LSB shared object, MIPS, MIPS32 version 1 (SYSV), static-pie linked, not stripped
shellcode-mips64.so:  ELF 64-bit LSB shared object, MIPS, MIPS64 rel6 version 1 (SYSV), static-pie linked, not stripped
shellcode-riscv64.so: ELF 64-bit LSB shared object, UCB RISC-V, RVC, double-float ABI, version 1 (SYSV), static-pie linked, not stripped
shellcode-x86.so:     ELF 32-bit LSB shared object, Intel 80386, version 1 (SYSV), static-pie linked, not stripped
shellcode-x86_64.so:  ELF 64-bit LSB shared object, x86-64, version 1 (SYSV), static-pie linked, not stripped
```

Note that they should be `static-pie linked`, not `dynamically linked`.

Get the symbol table: `llvm-objdump -T libcore_syscall.so`

Dump the text section: `llvm-objcopy -O binary --only-section=.text libcore_syscall.so shellcode.bin`

That's all for the shellcode.
