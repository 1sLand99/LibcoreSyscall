package dev.tmpfs.libcoresyscall.core.impl.arch;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import dev.tmpfs.libcoresyscall.core.impl.NativeBridge;
import dev.tmpfs.libcoresyscall.core.impl.ReflectHelper;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.BaseTrampolineCreator;

public class TrampolineCreator_ARM64 extends BaseTrampolineCreator {

    private TrampolineCreator_ARM64() {
    }

    public static final TrampolineCreator_ARM64 INSTANCE = new TrampolineCreator_ARM64();

    private static byte[] instructionsToBytes(int... inst) {
        byte[] result = new byte[inst.length * 4];
        for (int i = 0; i < inst.length; i++) {
            result[i * 4] = (byte) (inst[i] & 0xff);
            result[i * 4 + 1] = (byte) ((inst[i] >> 8) & 0xff);
            result[i * 4 + 2] = (byte) ((inst[i] >> 16) & 0xff);
            result[i * 4 + 3] = (byte) ((inst[i] >> 24) & 0xff);
        }
        return result;
    }

    @Override
    public byte[] getPaddingInstruction() {
        //0000000000001938 <NativeBridge_breakpoint>:
        //    1938: d43e0000      brk     #0xf000
        return instructionsToBytes(0xd43e0000);
    }

    @Override
    public Map<Method, byte[]> getNativeMethods() {
        HashMap<Method, byte[]> result = new HashMap<>();
        try {
            //0000000000001594 <NativeBridge_nativeClearCache>:
            //    1594: 8b020068      add     x8, x3, x2
            //    1598: d53b0029      mrs     x9, CTR_EL0
            //    159c: eb08005f      cmp     x2, x8
            //    15a0: 54000262      b.hs    0x15ec <NativeBridge_nativeClearCache+0x58>
            //    15a4: 53104d2a      ubfx    w10, w9, #16, #4
            //    15a8: 5280008b      mov     w11, #0x4               // =4
            //    15ac: 1aca216a      lsl     w10, w11, w10
            //    15b0: aa0203eb      mov     x11, x2
            //    15b4: d50b7b2b      dc      cvau, x11
            //    15b8: 8b0a016b      add     x11, x11, x10
            //    15bc: eb08017f      cmp     x11, x8
            //    15c0: 54ffffa3      b.lo    0x15b4 <NativeBridge_nativeClearCache+0x20>
            //    15c4: 12000d29      and     w9, w9, #0xf
            //    15c8: 5280008a      mov     w10, #0x4               // =4
            //    15cc: d5033b9f      dsb     ish
            //    15d0: 1ac92149      lsl     w9, w10, w9
            //    15d4: d50b7522      ic      ivau, x2
            //    15d8: 8b090042      add     x2, x2, x9
            //    15dc: eb08005f      cmp     x2, x8
            //    15e0: 54ffffa3      b.lo    0x15d4 <NativeBridge_nativeClearCache+0x40>
            //    15e4: d5033fdf      isb
            //    15e8: d65f03c0      ret
            //    15ec: d5033b9f      dsb     ish
            //    15f0: d5033fdf      isb
            //    15f4: d65f03c0      ret
            result.put(
                    NativeBridge.class.getMethod("nativeClearCache", long.class, long.class),
                    instructionsToBytes(
                            0x8b020068, // add x8, x3, x2
                            0xd53b0029, // mrs x9, CTR_EL0
                            0xeb08005f, // cmp x2, x8
                            0x54000262, // b.hs 0x15ec
                            0x53104d2a, // ubfx w10, w9, #16, #4
                            0x5280008b, // mov w11, #0x4
                            0x1aca216a, // lsl w10, w11, w10
                            0xaa0203eb, // mov x11, x2
                            0xd50b7b2b, // dc cvau, x11
                            0x8b0a016b, // add x11, x11, x10
                            0xeb08017f, // cmp x11, x8
                            0x54ffffa3, // b.lo 0x15b4
                            0x12000d29, // and w9, w9, #0xf
                            0x5280008a, // mov w10, #0x4
                            0xd5033b9f, // dsb ish
                            0x1ac92149, // lsl w9, w10, w9
                            0xd50b7522, // ic ivau, x2
                            0x8b090042, // add x2, x2, x9
                            0xeb08005f, // cmp x2, x8
                            0x54ffffa3, // b.lo 0x15d4
                            0xd5033fdf, // isb
                            0xd65f03c0, // ret
                            0xd5033b9f, // dsb ish
                            0xd5033fdf, // isb
                            0xd65f03c0  // ret
                    )
            );
            //000000000000000c <NativeBridge_nativeCallPointerFunction0>:
            //       c: d61f0040      br      x2
            result.put(
                    NativeBridge.class.getMethod("nativeCallPointerFunction0", long.class),
                    instructionsToBytes(
                            0xd61f0040  // br x2
                    )
            );
            //0000000000000010 <NativeBridge_nativeCallPointerFunction1>:
            //      10: aa0303e0      mov     x0, x3
            //      14: d61f0040      br      x2
            result.put(
                    NativeBridge.class.getMethod("nativeCallPointerFunction1", long.class, long.class),
                    instructionsToBytes(
                            0xaa0303e0, // mov x0, x3
                            0xd61f0040  // br x2
                    )
            );
            //0000000000000018 <NativeBridge_nativeCallPointerFunction2>:
            //      18: aa0403e1      mov     x1, x4
            //      1c: aa0303e0      mov     x0, x3
            //      20: d61f0040      br      x2
            result.put(
                    NativeBridge.class.getMethod("nativeCallPointerFunction2", long.class, long.class, long.class),
                    instructionsToBytes(
                            0xaa0403e1, // mov x1, x4
                            0xaa0303e0, // mov x0, x3
                            0xd61f0040  // br x2
                    )
            );
            //00000000000018e0 <NativeBridge_nativeCallPointerFunction3>:
            //    18e0: aa0403e1      mov     x1, x4
            //    18e4: aa0303e0      mov     x0, x3
            //    18e8: aa0203e3      mov     x3, x2
            //    18ec: aa0503e2      mov     x2, x5
            //    18f0: d61f0060      br      x3
            result.put(
                    NativeBridge.class.getMethod("nativeCallPointerFunction3", long.class, long.class, long.class, long.class),
                    instructionsToBytes(
                            0xaa0403e1, // mov x1, x4
                            0xaa0303e0, // mov x0, x3
                            0xaa0203e3, // mov x3, x2
                            0xaa0503e2, // mov x2, x5
                            0xd61f0060  // br x3
                    )
            );
            //00000000000018f4 <NativeBridge_nativeCallPointerFunction4>:
            //    18f4: aa0403e1      mov     x1, x4
            //    18f8: aa0303e0      mov     x0, x3
            //    18fc: aa0203e4      mov     x4, x2
            //    1900: aa0503e2      mov     x2, x5
            //    1904: aa0603e3      mov     x3, x6
            //    1908: d61f0080      br      x4
            result.put(
                    NativeBridge.class.getMethod("nativeCallPointerFunction4", long.class, long.class, long.class, long.class, long.class),
                    instructionsToBytes(
                            0xaa0403e1, // mov x1, x4
                            0xaa0303e0, // mov x0, x3
                            0xaa0203e4, // mov x4, x2
                            0xaa0503e2, // mov x2, x5
                            0xaa0603e3, // mov x3, x6
                            0xd61f0080  // br x4
                    )
            );
            //000000000000190c <NativeBridge_nativeSyscall>:
            //    190c: aa0503e8      mov     x8, x5
            //    1910: aa0303e0      mov     x0, x3
            //    1914: aa0403e1      mov     x1, x4
            //    1918: f94003e5      ldr     x5, [sp]
            //    191c: 93407c49      sxtw    x9, w2
            //    1920: aa0803e2      mov     x2, x8
            //    1924: aa0603e3      mov     x3, x6
            //    1928: aa0703e4      mov     x4, x7
            //    192c: aa0903e8      mov     x8, x9
            //    1930: d4000001      svc     #0
            //    1934: d65f03c0      ret
            result.put(
                    NativeBridge.class.getMethod("nativeSyscall", int.class, long.class, long.class, long.class, long.class, long.class, long.class),
                    instructionsToBytes(
                            0xaa0503e8, // mov x8, x5
                            0xaa0303e0, // mov x0, x3
                            0xaa0403e1, // mov x1, x4
                            0xf94003e5, // ldr x5, [sp]
                            0x93407c49, // sxtw x9, w2
                            0xaa0803e2, // mov x2, x8
                            0xaa0603e3, // mov x3, x6
                            0xaa0703e4, // mov x4, x7
                            0xaa0903e8, // mov x8, x9
                            0xd4000001, // svc #0
                            0xd65f03c0  // ret
                    )
            );
        } catch (NoSuchMethodException e) {
            ReflectHelper.unsafeThrow(e);
        }
        return Collections.unmodifiableMap(result);
    }

    @Override
    public long mprotect(long address, long size, int prot) {
        final int _NR_mprotect_arm64 = 226;
        return NativeBridge.nativeSyscall(_NR_mprotect_arm64, address, size, prot, 0, 0, 0);
    }

}
