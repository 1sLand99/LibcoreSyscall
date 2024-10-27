package dev.tmpfs.libcoresyscall.core;


import android.system.ErrnoException;

import dev.tmpfs.libcoresyscall.core.impl.NativeBridge;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.TrampolineCreatorFactory;

public class Syscall {

    static {
        initializeOnce();
    }

    private Syscall() {
        throw new AssertionError("no instances");
    }

    private static void initializeOnce() {
        NativeBridge.initializeOnce();
    }

    public static long getPageSize() {
        return NativeBridge.getPageSize();
    }

    public static boolean isError(long result) {
        // [-4095, -1] is error
        return result < 0 && result >= -4095;
    }

    public static long syscallNoCheck(long number, long... args) {
        long[] args6 = new long[6];
        System.arraycopy(args, 0, args6, 0, args.length);
        return NativeBridge.nativeSyscall((int) number, args6[0], args6[1], args6[2], args6[3], args6[4], args6[5]);
    }

    public static long syscall(long number, long... args) throws ErrnoException {
        long result = syscallNoCheck(number, args);
        if (isError(result)) {
            throw new ErrnoException("syscall-" + number, (int) -result);
        }
        return result;
    }

    public static void mprotect(long address, long size, int prot) throws ErrnoException {
        long result = TrampolineCreatorFactory.create().mprotect(address, size, prot);
        if (isError(result)) {
            throw new ErrnoException("mprotect", (int) -result);
        }
    }

}
