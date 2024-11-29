package dev.tmpfs.libcoresyscall.core.impl.arch;

import dev.tmpfs.libcoresyscall.core.impl.ByteArrayUtils;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.BaseShellcode;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.ISimpleInlineHook;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.ISyscallNumberTable;

public class ShellcodeImpl_Riscv64 extends BaseShellcode implements ISimpleInlineHook, ISyscallNumberTable {

    public static final ShellcodeImpl_Riscv64 INSTANCE = new ShellcodeImpl_Riscv64();

    private ShellcodeImpl_Riscv64() {
        super();
    }

    @Override
    public byte[] getShellcodeBytes() {
        //0300 l     O .text  0018 get_hook_info.sHookInfo
        //0000 g     F .text  0004 NativeBridge_breakpoint
        //0004 g     F .text  0010 __clear_cache
        //0014 g     F .text  0014 syscall_ext
        //0028 g     F .text  0012 NativeBridge_nativeSyscall
        //003a g     F .text  0016 NativeBridge_nativeClearCache
        //0050 g     F .text  0002 NativeBridge_nativeCallPointerFunction0
        //0052 g     F .text  0004 NativeBridge_nativeCallPointerFunction1
        //0056 g     F .text  0006 NativeBridge_nativeCallPointerFunction2
        //005c g     F .text  000a NativeBridge_nativeCallPointerFunction3
        //0066 g     F .text  000c NativeBridge_nativeCallPointerFunction4
        //0072 g     F .text  0030 NativeBridge_nativeGetJavaVM
        //00a2 g     F .text  000a get_hook_info
        //00ac g     F .text  0016 lsw_pread64
        //00c2 g     F .text  002a lsw_mprotect
        //00ec g     F .text  009a fake_fstat64
        //0186 g     F .text  016e fake_mmap64
        //02f4 g     F .text  0002 fake_mmap
        String b64 =
                "ApCCgJMIMBABRnMAAAAR4YKAAACqiC6FsoU2hrqGPofCh3MAAACCgIJitoUyhTqGvoZCh8aHFojx\n" +
                        "vzKFs4XGAJMIMBABRnMAAAAR4YKAAAAChjaFAoa6hTaFAoa6hTaFMoc+hgKHuoU2hTKHPobChgKH\n" +
                        "AREG7CLoABAMYQO2hW0jNAT+kwWE/gKWgzWE/jM1oAB9FW2NEwEE/uJgQmQFYYKAFwUAABMF5SWC\n" +
                        "gCqHEwUwBLaHsoYuhrqFPoeBRwFIkb9BEQbkIuAACLKGLoaqhRMFIA4BR4FHAUjv8L/zASUTAQT/\n" +
                        "omACZEEBgoB5cQb0IvAm7EroTuQAGK6JKokTBQAFyoVOhoFGAUeBRwFI7/B/8P11hSVjcbUCA7YJ\n" +
                        "ABcFAACDNSUeAUVjErYChcEDtQkDFcUBRRmoqoQXBQAAAzXlHAKVuwWQQAzBfVUTAQT9onACdOJk\n" +
                        "QmmiaUVhgoAdZRsGRXB1RcqFgUYBR4FHAUjv8F/qfXaqhQFF42m2/AFFI7i5AuG3UXGG9aLxpu3K\n" +
                        "6c7l0uFW/Vr5XvVi8Wbtaulu5YAZvoQ6i7aLsokuiSqMAU19WhcFAACTDcUUY0wHAhP1KwKJReqK\n" +
                        "zoxjF7UEV3C4zVc0AF4TBQTxJ3QFApMFBPFahe/wn/B9WgHJAU2BSs6MHaDqis6MBaCDNQTxA7UN\n" +
                        "AAFNgUrOjGOYpQARxZPsOQBKjaaKWooDtY0AApUTlkQD2UVN5kqGKokTBeAN4oUyjOaGXofahyaI\n" +
                        "7/C/3f11hSVjc7UI/VVjBboIKotiiWMHDQKFa3Fc2oQ5oDMNrUCqmqqUYw0NADNWfQtShaaF1obv\n" +
                        "8L/j40Og/uMFhf8DtQ0BfRmzBakAMwWgQOmN2oRahU6G7/D/4lqFk/VJAJXJg7UNAbMGsEAzdtUA\n" +
                        "SpUulbN11QCTCDAQMoUBRnMAAACqhVqFmcUAALsFoEBKhQzBfVUTAQTxrnAOdO5kTmmuaQ5q6npK\n" +
                        "e6p7CnzqbEptqm1tYYKASb0AAAAAAAAAAAAA776v3gAAAAAURREAAAAAAAAAAAAAAAAA";
        byte[] bytes = android.util.Base64.decode(b64, android.util.Base64.DEFAULT);
        int hookInfoOffset = 0x0300;
        fillInHookInfo(bytes, hookInfoOffset);
        return bytes;
    }

    @Override
    public int getNativeDebugBreakOffset() {
        return 0x0000;
    }

    @Override
    public int getNativeClearCacheOffset() {
        return 0x003a;
    }

    @Override
    public int getNativeSyscallOffset() {
        return 0x0028;
    }

    @Override
    public int getNativeCallPointerFunction0Offset() {
        return 0x0050;
    }

    @Override
    public int getNativeCallPointerFunction1Offset() {
        return 0x0052;
    }

    @Override
    public int getNativeCallPointerFunction2Offset() {
        return 0x0056;
    }

    @Override
    public int getNativeCallPointerFunction3Offset() {
        return 0x005c;
    }

    @Override
    public int getNativeCallPointerFunction4Offset() {
        return 0x0066;
    }

    @Override
    public int getNativeGetJavaVmOffset() {
        return 0x0072;
    }

    @Override
    public int getFakeStat64Offset() {
        return 0x00ec;
    }

    @Override
    public int getFakeMmap64Offset() {
        return 0x0186;
    }

    @Override
    public int getFakeMmapOffset() {
        return 0x02f4;
    }

    @Override
    public int __NR_mprotect() {
        // mprotect riscv64 226
        return 226;
    }

    @Override
    public int __NR_memfd_create() {
        // memfd_create riscv64 279
        return 279;
    }

    @Override
    public void inlineHook(long address, long hook) {
        if (address == 0) {
            throw new IllegalArgumentException("address is 0");
        }
        if (hook == 0) {
            throw new IllegalArgumentException("hook is 0");
        }
        if (address % 2 != 0 || hook % 2 != 0) {
            throw new IllegalArgumentException("address or hook is not aligned, address: " + address + ", hook: " + hook);
        }
        int nopCount = ((((int) address % 8) + 8 + 4) % 8) % 2;
        // 01 00         nop
        // nopCount * 2 + 12 + 8
        int nopBytes = nopCount * 2;
        byte[] trampoline = new byte[nopBytes + 20];
        for (int i = 0; i < nopBytes; i += 2) {
            trampoline[i] = 0x01;
            trampoline[i + 1] = 0x00;
        }
        // add jump to hook
        // 17 0e 00 00   auipc   t3, 0x0
        // 03 3e ce 00   ld      t3, 0xc(t3)
        // 67 03 0e 00   jalr    t1, t3
        // .addr hook
        ByteArrayUtils.writeBytes(trampoline, nopBytes, new byte[]{
                0x17, 0x0e, 0x00, 0x00,
                0x03, 0x3e, (byte) 0xce, 0x00,
                0x67, 0x03, 0x0e, 0x00
        });
        ByteArrayUtils.writeInt64(trampoline, nopBytes + 12, hook);
        writeByteArrayToTextSection(trampoline, address);
    }

}
