package dev.tmpfs.libcoresyscall.core.impl.arch;

import dev.tmpfs.libcoresyscall.core.impl.ByteArrayUtils;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.BaseShellcode;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.ISimpleInlineHook;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.ISyscallNumberTable;

public class ShellcodeImpl_Arm64 extends BaseShellcode implements ISimpleInlineHook, ISyscallNumberTable {

    public static final ShellcodeImpl_Arm64 INSTANCE = new ShellcodeImpl_Arm64();

    private ShellcodeImpl_Arm64() {
        super();
    }

    @Override
    public byte[] getShellcodeBytes() {
        //0550 l     O .text  0018 get_hook_info.sHookInfo
        //0000 g     F .text  0008 NativeBridge_breakpoint
        //0008 g     F .text  0068 __clear_cache
        //0070 g     F .text  0028 syscall_ext
        //0098 g     F .text  0024 NativeBridge_nativeSyscall
        //00bc g     F .text  0064 NativeBridge_nativeClearCache
        //0120 g     F .text  0004 NativeBridge_nativeCallPointerFunction0
        //0124 g     F .text  0008 NativeBridge_nativeCallPointerFunction1
        //012c g     F .text  000c NativeBridge_nativeCallPointerFunction2
        //0138 g     F .text  0014 NativeBridge_nativeCallPointerFunction3
        //014c g     F .text  0018 NativeBridge_nativeCallPointerFunction4
        //0164 g     F .text  0038 NativeBridge_nativeGetJavaVM
        //019c g     F .text  000c get_hook_info
        //01a8 g     F .text  0024 lsw_pread64
        //01cc g     F .text  0034 lsw_mprotect
        //0200 g     F .text  00fc fake_fstat64
        //02fc g     F .text  0248 fake_mmap64
        //0544 g     F .text  0004 fake_mmap
        String b64 =
                "AAA+1MADX9YfAAHrKAA71aICAFQJTRBTigCAUkkhyRrqAwCqKnsL1UoBCYtfAQHro///VB8AAeuf\n" +
                        "OwPVAgEAVAgNABKJAIBSKCHIGiB1C9UAAAiLHwAB66P//1TfPwPVwANf1p87A9XfPwPVwANf1gl8\n" +
                        "QJPgAwGq4QMCquIDA6rjAwSq5AMFquUDBqroAwmqAQAA1MADX9boAwaq5gNA+eEDA6rgAwIq4gME\n" +
                        "quMDBarkAwiq5QMHqu7//xdoAAKLKQA71V8ACOtiAgBUKk0QU4sAgFJqIcoa6wMCqit7C9VrAQqL\n" +
                        "fwEI66P//1QpDQASigCAUp87A9VJIckaInUL1UIACYtfAAjro///VN8/A9XAA1/WnzsD1d8/A9XA\n" +
                        "A1/WQAAf1uADA6pAAB/W4QMEquADA6pAAB/W4QMEquADA6rjAwKq4gMFqmAAH9bhAwSq4AMDquQD\n" +
                        "AqriAwWq4wMGqoAAH9b/gwDR/XsBqf1DAJEIAED54SMAkf8HAPkIbUP5AAE/1ugHQPkfAABxAAGf\n" +
                        "mv17Qan/gwCRwANf1h8gA9WAHQAQwANf1gh8QJPkAwOq4wMCqmAIgFLiAwGq5QMfquEDCKrmAx+q\n" +
                        "qv//F/17v6n9AwCR6AMBqkN8QJPhAwCqQByAUuIDCKrkAx+q5QMfquYDH6qf//+X/XvBqMADX9b9\n" +
                        "e72p9QsA+fRPAqn9AwCRFHxAk/MDAarjAx+qAAqAUuIDE6rkAx+q4QMUquUDH6rmAx+qj///lx/8\n" +
                        "P7HiAQBUCAAAkGkCQPngAx8qCKlC+T8BCOuhAABUiAAAtGgaQPkoAgC04AMfKvRPQqn1C0D5/XvD\n" +
                        "qMADX9YIAACQ9QMAqgitQvkAAT/W6AMVSwgAALkAAIAS9E9CqfULQPn9e8OowANf1qADgFLhAxSq\n" +
                        "guCOUuMDH6rkAx+q5QMfquYDH6ps//+XHwRAsQgBAFToAwCq4AMfKmgaAPn0T0Kp9QtA+f17w6jA\n" +
                        "A1/W4AMfKvRPQqn1C0D5/XvDqMADX9b/gwPR/XsIqfxvCan6Zwqp+F8LqfZXDKn0Tw2p/QMCkfcD\n" +
                        "Bar4AwQq+gMDKvMDAir0AwGq+QMAqvsDH6oVAIAShAL4N0gEgFL2Axuq/AMTKkgDCAofCQBxoQMA\n" +
                        "VADkAG/hAwCR4AMYKuADAK3gAwGt4AMCreADA62j//+X+wMfquAAADT2Ax+q/AMTKhAAABT2Axuq\n" +
                        "/AMTKg0AABQIAACQ6QNA+fYDG6oIqUL5/AMTKj8BCOvBAABUqAAAtHwGADL7AxSq9gMXqvUDGCoI\n" +
                        "AACQCK1C+QABP9b/LkDyoAAAVMgCgFIIAAC5AACAkk8AABSDf0CTRH9AkwV/QJPiAxSq9AMAqsAb\n" +
                        "gFLhAxmq5gMXqvkDAqoY//+XH/w/saIHAFS/BgAxIAgAVPgDAKr0AxmqWwIAtBkAglL3AxiqBQAA\n" +
                        "FHsDAMsWABaLFwAXi3sBALR/B0Dx4AMVKuEDF6piM5ma4wMWqlL//5cfAADxrP7/VB8QALHA/v9U\n" +
                        "FgAAkJUGANHgAxiqyLJC+eIDEyr0AxiqqQIIi+gDCMshAQiKTf//l+ADGKozBBA2yLJC+akCAIvq\n" +
                        "AwjLKQEIiwgACoopAQqKKgA71R8BCevCAgBUS00QU4wAgFKLIcsa7AMIqix7C9WMAQuLnwEJ66P/\n" +
                        "/1RKDQASiwCAUp87A9VqIcoaKHUL1QgBCosfAQnro///VAYAABToAwBLAACAkogCALkDAAAUnzsD\n" +
                        "1d8/A9X0T02p9ldMqfhfS6n6Z0qp/G9Jqf17SKn/gwORwANf1m7//xcAAAAAAAAAAO++r94AAAAA\n" +
                        "FEURAAAAAAAAAAAAAAAAAA==";
        byte[] bytes = android.util.Base64.decode(b64, android.util.Base64.DEFAULT);
        int hookInfoOffset = 0x0550;
        fillInHookInfo(bytes, hookInfoOffset);
        return bytes;
    }

    @Override
    public int getNativeDebugBreakOffset() {
        return 0x0000;
    }

    @Override
    public int getNativeClearCacheOffset() {
        return 0x00bc;
    }

    @Override
    public int getNativeSyscallOffset() {
        return 0x0098;
    }

    @Override
    public int getNativeCallPointerFunction0Offset() {
        return 0x0120;
    }

    @Override
    public int getNativeCallPointerFunction1Offset() {
        return 0x0124;
    }

    @Override
    public int getNativeCallPointerFunction2Offset() {
        return 0x012c;
    }

    @Override
    public int getNativeCallPointerFunction3Offset() {
        return 0x0138;
    }

    @Override
    public int getNativeCallPointerFunction4Offset() {
        return 0x014c;
    }

    @Override
    public int getNativeGetJavaVmOffset() {
        return 0x0164;
    }

    @Override
    public int getFakeStat64Offset() {
        return 0x0200;
    }

    @Override
    public int getFakeMmap64Offset() {
        return 0x02fc;
    }

    @Override
    public int getFakeMmapOffset() {
        return 0x0544;
    }

    @Override
    public int __NR_mprotect() {
        // mprotect arm64: 226
        return 226;
    }

    @Override
    public int __NR_memfd_create() {
        // memfd_create arm64: 279
        return 279;
    }

    @Override
    public int __NR_ioctl() {
        // ioctl arm64: 29
        return 29;
    }

    @Override
    public int __NR_tgkill() {
        // tgkill arm64: 131
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
        if (address % 4 != 0 || hook % 4 != 0) {
            throw new IllegalArgumentException("address or hook is not aligned, address: " + address + ", hook: " + hook);
        }
        // 51 00 00 58  ldr x17, [pc, #8]
        // 20 02 1F D6  br x17
        // qword. address
        byte[] trampoline = {
                0x51, 0x00, 0x00, 0x58,
                0x20, 0x02, 0x1F, (byte) 0xD6,
                0, 0, 0, 0, 0, 0, 0, 0
        };
        ByteArrayUtils.writeInt64(trampoline, 8, hook);
        if (address % 8 != 0) {
            byte[] old = trampoline;
            // add a nop to align address
            // 1f 20 03 d5  nop
            byte[] aligned = new byte[old.length + 4];
            aligned[0] = 0x1f;
            aligned[1] = 0x20;
            aligned[2] = 0x03;
            aligned[3] = (byte) 0xd5;
            System.arraycopy(old, 0, aligned, 4, old.length);
            trampoline = aligned;
        }
        writeByteArrayToTextSection(trampoline, address);
    }

}
