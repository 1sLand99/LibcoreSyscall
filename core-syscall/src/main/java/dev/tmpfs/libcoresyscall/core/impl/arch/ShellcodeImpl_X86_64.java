package dev.tmpfs.libcoresyscall.core.impl.arch;

import dev.tmpfs.libcoresyscall.core.impl.ByteArrayUtils;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.BaseShellcode;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.ISimpleInlineHook;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.ISyscallNumberTable;

public class ShellcodeImpl_X86_64 extends BaseShellcode implements ISimpleInlineHook, ISyscallNumberTable {

    public static final ShellcodeImpl_X86_64 INSTANCE = new ShellcodeImpl_X86_64();

    private ShellcodeImpl_X86_64() {
        super();
    }

    @Override
    public byte[] getShellcodeBytes() {
        // 0000 g     F .text  0007 NativeBridge_breakpoint
        // 0010 g     F .text  0006 __clear_cache
        // 0020 g     F .text  0031 syscall_ext
        // 0060 g     F .text  0034 NativeBridge_nativeSyscall
        // 00a0 g     F .text  0006 NativeBridge_nativeClearCache
        // 00b0 g     F .text  0009 NativeBridge_nativeCallPointerFunction0
        // 00c0 g     F .text  000a NativeBridge_nativeCallPointerFunction1
        // 00d0 g     F .text  000d NativeBridge_nativeCallPointerFunction2
        // 00e0 g     F .text  0013 NativeBridge_nativeCallPointerFunction3
        // 0100 g     F .text  001a NativeBridge_nativeCallPointerFunction4
        // 0120 g     F .text  0033 NativeBridge_nativeGetJavaVM
        // 0160 g     F .text  000d get_hook_info
        // 0170 g     F .text  002f lsw_pread64
        // 01a0 g     F .text  0032 lsw_mprotect
        // 01e0 g     F .text  00ae fake_fstat64
        // 0290 g     F .text  01cc fake_mmap64
        // 0460 g     F .text  000a fake_mmap
        // 0470 l     O .text  0018 get_hook_info.sHookInfo
        String b64 =
                "VUiJ5cxdw2YPH4QAAAAAAFVIieVdw2YuDx+EAAAAAABVSInlQVdBVlNMictNicZMi30QSGPHSIn3\n" +
                        "SInWSInKTYnySYnYTYn5DwVbQV5BX13DZmZmZmZmLg8fhAAAAAAAVUiJ5UiD7BBNicpMicCJ10yL\n" +
                        "RRBMi00YSItVIEiJFCRIic5IicJMidHokv///0iDxBBdw2ZmZi4PH4QAAAAAAFVIieVdw2YuDx+E\n" +
                        "AAAAAABVSInlMcBd/+IPH4AAAAAAVUiJ5UiJz13/4mYPH0QAAFVIieVMicZIic9d/+IPHwBVSInl\n" +
                        "TInGSInQSInPTInKXf/gZmZmZi4PH4QAAAAAAFVIieVMicZIidBMi0UQSInPTInKTInBXf/gZg8f\n" +
                        "RAAAVUiJ5UiD7BBIx0X4AAAAAEiLB0iNdfj/kNgGAACFwHUKSItF+EiDxBBdwzHASIPEEF3DZmZm\n" +
                        "Zi4PH4QAAAAAAFVIieVIjQUFAwAAXcMPHwBVSInlSIPsEEmJyEiJ0UiJ8khj90jHBCQAAAAAvxEA\n" +
                        "AABFMcnoh/7//0iDxBBdw5BVSInlSIPsEEiJ8EiJ/khjykjHBCQAAAAAvwoAAABIicJFMcBFMcno\n" +
                        "VP7//0iDxBBdw2ZmZmZmLg8fhAAAAAAAVUiJ5UFXQVZBVFNIg+wQSYn2TGP/SMcEJAAAAAAx278F\n" +
                        "AAAATIn+TInyMclFMcBFMcnoCf7//0g9AfD//3NKSIsFSgIAAEk5BnVUSIXAdE9Jg34wAHVISMcE\n" +
                        "JAAAAAAx27oEdwAAvxAAAABMif4xyUUxwEUxyejF/f//SD0A8P//dxxJiUYw6xZJicQxwP8VBAIA\n" +
                        "AEH33ESJILv/////idhIg8QQW0FcQV5BX13DZpBVSInlQVdBVkFVQVRTSIHsyAAAAEyJTbhFiceJ\n" +
                        "00iJdcBFMe3HRdT/////RYXASIl9sIlNzHhticiD4CJBvAAAAABBid6D+AJ1YA9XwA8pRaAPKUWQ\n" +
                        "DylFgA8phXD///8PKYVg////DymFUP///w8phUD///8PKYUw////DymFIP///0iNtSD///9Eif/o\n" +
                        "vv7//8dF1P////+FwA+E6gAAAEUx7UUx5EGJ3old0DHA/xUzAQAASInDSItVuEiJ0EjB4DR0C8cD\n" +
                        "FgAAAOmiAAAASWPOTGNFzE1jz0iJFCS/CQAAAEiLdbBMi3XATIny6J38//9IPQHw//9zc4N91P90\n" +
                        "eEmJx02F7XQ+TIn76xBmkEkpxUkBxEgBw02F7XQpSYH9ABAAALoAEAAASQ9C1Yt91EiJ3kyJ4eik\n" +
                        "/f//SIXAf89Ig/j8dNJIizWiAAAASY0ENkj/yEj33kghxkyJ+0yJ/4tV0Oin/f//TIn46wv32IkD\n" +
                        "SMfA/////0iBxMgAAABbQVxBXUFeQV9dw0iLBU4AAABFMe1IOYUg////D4UC////QbwAAAAAQYne\n" +
                        "SIXAD4T2/v//QYneQYPOA0yLbcBMi2W4RIl91One/v//Dx9AAFVIieVd6Sb+///MzMzMzMzvvq/e\n" +
                        "AAAAABRFEQAAAAAAAAAAAAAAAAA=\n";
        byte[] bytes = android.util.Base64.decode(b64, android.util.Base64.DEFAULT);
        int hookInfoOffset = 0x0470;
        fillInHookInfo(bytes, hookInfoOffset);
        return bytes;
    }

    @Override
    public int getNativeDebugBreakOffset() {
        return 0x0000;
    }

    @Override
    public int getNativeClearCacheOffset() {
        return 0x00a0;
    }

    @Override
    public int getNativeSyscallOffset() {
        return 0x0060;
    }

    @Override
    public int getNativeCallPointerFunction0Offset() {
        return 0x00b0;
    }

    @Override
    public int getNativeCallPointerFunction1Offset() {
        return 0x00c0;
    }

    @Override
    public int getNativeCallPointerFunction2Offset() {
        return 0x00d0;
    }

    @Override
    public int getNativeCallPointerFunction3Offset() {
        return 0x00e0;
    }

    @Override
    public int getNativeCallPointerFunction4Offset() {
        return 0x0100;
    }

    @Override
    public int getNativeGetJavaVmOffset() {
        return 0x0120;
    }

    @Override
    public int getFakeStat64Offset() {
        return 0x01e0;
    }

    @Override
    public int getFakeMmap64Offset() {
        return 0x0290;
    }

    @Override
    public int getFakeMmapOffset() {
        return 0x0460;
    }

    @Override
    public int __NR_mprotect() {
        // mprotect x86_64: 10
        return 10;
    }

    @Override
    public int __NR_memfd_create() {
        // memfd_create x86_64: 319
        return 319;
    }

    @Override
    public void inlineHook(long address, long hook) {
        if (address == 0) {
            throw new IllegalArgumentException("address is 0");
        }
        if (hook == 0) {
            throw new IllegalArgumentException("hook is 0");
        }
        // 49 BA [8bytes]  movabs r10, hook
        // 41 FF E2  jmp r10
        byte[] stub = new byte[2 + 8 + 3];
        stub[0] = 0x49;
        stub[1] = (byte) 0xba;
        ByteArrayUtils.writeInt64(stub, 2, hook);
        stub[10] = 0x41;
        stub[11] = (byte) 0xff;
        stub[12] = (byte) 0xe2;
        writeByteArrayToTextSection(stub, address);
    }

}
