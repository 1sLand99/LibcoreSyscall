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
        //0000 g    DF .text  0004 NativeBridge_breakpoint
        //0000 g    D  .text  0000 ___text_section
        //0004 g    DF .text  0012 NativeBridge_nativeSyscall
        //0016 g    DF .text  0014 syscall_ext
        //002a g    DF .text  0016 NativeBridge_nativeClearCache
        //0040 g    DF .text  0010 __clear_cache
        //0050 g    DF .text  0002 NativeBridge_nativeCallPointerFunction0
        //0052 g    DF .text  0004 NativeBridge_nativeCallPointerFunction1
        //0056 g    DF .text  0006 NativeBridge_nativeCallPointerFunction2
        //005c g    DF .text  000a NativeBridge_nativeCallPointerFunction3
        //0066 g    DF .text  000c NativeBridge_nativeCallPointerFunction4
        //0072 g    DF .text  0030 NativeBridge_nativeGetJavaVM
        //00a2 g    DF .text  0014 ashmem_dev_get_size_region
        //00b6 g    DF .text  000a get_hook_info
        //00c0 g    DF .text  0004 get_current_pc
        //00c4 g    DF .text  0088 fake_fstat64
        //014c g    DF .text  02ac fake_mmap64
        //0478 g    DF .text  0002 fake_mmap
        //0740 l     O .rodata  0018 _ZZ13get_hook_infoE9sHookInfo
        String b64 =
                "ApCCgIJitoUyhTqGvoZCh8aHFogJoKqILoWyhTaGuoY+h8KHcwAAAIKAMoWzhcYAkwgwEAFGcwAA\n" +
                        "ABHhgoAAAJMIMBABRnMAAAAR4YKAAAAChjaFAoa6hTaFAoa6hTaFMoc+hgKHuoU2hTKHPobChgKH\n" +
                        "AREG7CLoABAMYQO2hW0jNAT+kwWE/gKWgzWE/jM1oAB9FW2NEwEE/uJgQmQFYYKAqoUdZRsGRXB1\n" +
                        "RYFGAUeBRwFIjbcXBQAAEwWlaIKABoWCgHlxBvQi8CbsSuhO5AAYrokqiRMFAAXKhU6GgUYBR4FH\n" +
                        "AUjv8B/z/XWFJWNttQCqhBcFAAADNUVlApW7BZBADMF9VS2gA7YJABcFAACDNUVjAUVjHbYAmckD\n" +
                        "tQkDGeVKhe/w//d9dmN8pgABRRMBBP2icAJ04mRCaaJpRWGCgKqFAUUjuLkC3bdJcYb2ovKm7srq\n" +
                        "zubS4lb+Wvpe9mLyZu5q6m7mgBq+i7qJNosyiS6KqoqBTIVNFwUAABMMZVxjSwcOE3UrAolFYxa1\n" +
                        "DldwuM1XNABeEwUE8Sd0BQITBQAFEwYE8c6FgUYBR4FHAUjv8F/m/XVj/KUAqoQDNYwAApWBTLsF\n" +
                        "kEAMwQVFXaCDNQTxAzUMAGOepQABzQM1BPQJ6U6F7/Df6/11Y+SlACMwpPQDNQTxgzUMAC2NEzUV\n" +
                        "ALM1sACzfLUAk3VJAAVFtcETBQTvV3CBzVc0AF4ndAUCEwUE7id0BQITBQTtJ3QFAldwpM1XNABe\n" +
                        "EwUE6Sd0BQIjMATwEwXAAhMGBOnOhYFGAUeBRwFI7/D/24M1BOk7BQUINyYCARsGRpmxjU2NMzWg\n" +
                        "ALN9lUEDNYwAApWTlUsD2UTx5SqNE5UcALNmJQETBeAN1oVShlqHzodeiO/wn9f9dYUlY2m1ApsF\n" +
                        "BQC1BbM1sAATRvv/BYKTVlsAE0f5/wmD2Y5VjgWKM2a2AdGNvcW7BKBApahjjgwGqopjCgoCBWvx\n" +
                        "XFaN0oQpoImMqpsqnYXAs9ZkCxMFMATOhWqGXoeBRwFI7/Af0eNBoP7jApX/AzUMAX0aswWqADMG\n" +
                        "oEBtjhMFIA7WhcqGAUeBRwFI7/B/zhN1SQAd7VaFIagTdSsAEzUVADNltQG1RDnFaoUEwX1VEwEE\n" +
                        "6bZwFnT2ZFZptmkWavJ6UnuyexJ88mxSbbJtdWGCgIM1DAEzBrBAM3VWAVaa0pXxjZMIMBABRnMA\n" +
                        "AACqhVaF3d25qJN2uf8TBeAN1oVShlqHzodeiO/wv8b9fKqFaoXj7bz4EwVwDVKGgUYBR4FHAUjv\n" +
                        "8P/EASWFLGNzlQOTZikAE2cLAhMF4A39V9aFUoYBSO/w/8L9dYUl43G17uW1AABBEQbkIuAACC6G\n" +
                        "qoUTBcACgUYBR4FHAUjv8H/AASUTAQT/omACZEEBgoAqiBMF4A2+iLqHNoeyhi6GwoVGiMW2KocT\n" +
                        "BTAEtoeyhi6GuoU+h4FHAUjptkERBuQi4AAIsoYuhqqFEwUgDgFHgUcBSO/wH7sBJRMBBP+iYAJk\n" +
                        "QQGCgNG5QREG5CLgAAguhqqFEwVwDYFGAUeBRwFI7/BfuAElEwEE/6JgAmRBAYKAqoYTBfADMocu\n" +
                        "hraFuoYBR4FHAUi5vqqGEwUABDKHLoa2hbqGAUeBRwFIobZBEQbkIuAACLKHLoaqhTuHBggTBYAD\n" +
                        "voaBRwFI7/C/sgElEwEE/6JgAmRBAYKAQREG5CLgAAg2h7KGLoaqhRMF8ASBRwFI7/AfsAElEwEE\n" +
                        "/6JgAmRBAYKAQREG5CLgAAiuhiqGEwXwBJMFwPkBR4FHAUjv8F+tASUTAQT/omACZEEBgoBBEQbk\n" +
                        "IuAACC6GqoUTBQAFgUYBR4FHAUjv8L+qASUTAQT/omACZEEBgoBBEQbkIuAACKqFEwWQAwFGgUYB\n" +
                        "R4FHAUjv8B+oASUTAQT/omACZEEBgoBBEQbkIuAACLKGLoaqhXVFAUeBRwFI7/CfpQElEwEE/6Jg\n" +
                        "AmRBAYKAQREG5CLgAAiqhRMF4AUBRoFGAUeBRwFI7/D/ogAAYwYGFCMAtQCzBsUADUejj7b+Y23m\n" +
                        "EqMAtQAjAbUAI4+2/h1Ho462/mNi5hKjAbUAJUcjjrb+Y2vmELsGoECT9zYAswb1ADMI9kATdsj/\n" +
                        "4hW3FxAQkgeThwcQs7X1AozCs4fGACOut/5jYuYOzMKMxiOqt/5lRyOst/5jaeYMzMaMyszKjM4j\n" +
                        "orf+I6S3/iOmt/4T90YAkwiHAbMCFkETBgACI6i3/mPjwgoTlgUCu4XFCDMG6EATBob8FYITCBYA\n" +
                        "cyYgwhNfJgCzhxYBY3ToAb6GiaizBuBBs3jYAJOWWACzgtJAvpZXd5ANV8QFXg4GV6UIUle1opYT\n" +
                        "AwACoUNBTuFORodXxqcCJ/RnCif0ww4ndM4OJ/TODjMH50Gyl33zYwYYAxOGAv59RzNW5goTRvb/\n" +
                        "FpYBmjaWEwYGAozijOaM6ozuk4YGAuOaxv6CgAAAAADvvq/eAAAAABRFEQAAAAAAABAAAAAAAAA=\n";
        byte[] bytes = android.util.Base64.decode(b64, android.util.Base64.DEFAULT);
        int hookInfoOffset = 0x0740;
        fillInHookInfo(bytes, hookInfoOffset);
        return bytes;
    }

    @Override
    public int getNativeDebugBreakOffset() {
        return 0x0000;
    }

    @Override
    public int getNativeClearCacheOffset() {
        return 0x002a;
    }

    @Override
    public int getNativeSyscallOffset() {
        return 0x0004;
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
        return 0x00c4;
    }

    @Override
    public int getFakeMmap64Offset() {
        return 0x014c;
    }

    @Override
    public int getFakeMmapOffset() {
        return 0x0478;
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
    public int __NR_ioctl() {
        // ioctl riscv64 29
        return 29;
    }

    @Override
    public int __NR_tgkill() {
        // tgkill riscv64 131
        return 131;
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
