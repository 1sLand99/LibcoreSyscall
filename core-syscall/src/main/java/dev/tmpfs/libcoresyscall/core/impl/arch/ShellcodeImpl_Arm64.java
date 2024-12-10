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
        //0000 g    DF .text  0008 NativeBridge_breakpoint
        //0000 g    D  .text  0000 ___text_section
        //0008 g    DF .text  0024 NativeBridge_nativeSyscall
        //002c g    DF .text  0028 syscall_ext
        //0054 g    DF .text  0064 NativeBridge_nativeClearCache
        //00b8 g    DF .text  0068 __clear_cache
        //0120 g    DF .text  0004 NativeBridge_nativeCallPointerFunction0
        //0124 g    DF .text  0008 NativeBridge_nativeCallPointerFunction1
        //012c g    DF .text  000c NativeBridge_nativeCallPointerFunction2
        //0138 g    DF .text  0014 NativeBridge_nativeCallPointerFunction3
        //014c g    DF .text  0018 NativeBridge_nativeCallPointerFunction4
        //0164 g    DF .text  0038 NativeBridge_nativeGetJavaVM
        //019c g    DF .text  0020 ashmem_dev_get_size_region
        //01bc g    DF .text  000c get_hook_info
        //01c8 g    DF .text  0014 get_current_pc
        //01dc g    DF .text  00e8 fake_fstat64
        //02c4 g    DF .text  03d0 fake_mmap64
        //074c g    DF .text  0004 fake_mmap
        //0a60 l     O .rodata  0018 _ZZ13get_hook_infoE9sHookInfo
        String b64 =
                "AAA+1MADX9boAwaq5gNA+eEDA6rgAwIq4gMEquMDBarkAwiq5QMHqgEAABQJfECT4AMBquEDAqri\n" +
                        "AwOq4wMEquQDBarlAwaq6AMJqgEAANTAA1/WaAACiykAO9VfAAjrYgIAVCpNEFOLAIBSaiHKGusD\n" +
                        "AqorewvVawEKi38BCOuj//9UKQ0AEooAgFKfOwPVSSHJGiJ1C9VCAAmLXwAI66P//1TfPwPVwANf\n" +
                        "1p87A9XfPwPVwANf1h8AAesoADvVogIAVAlNEFOKAIBSSSHJGuoDAKoqewvVSgEJi18BAeuj//9U\n" +
                        "HwAB6587A9UCAQBUCA0AEokAgFIoIcgaIHUL1QAACIsfAAHro///VN8/A9XAA1/WnzsD1d8/A9XA\n" +
                        "A1/WQAAf1uADA6pAAB/W4QMEquADA6pAAB/W4QMEquADA6rjAwKq4gMFqmAAH9bhAwSq4AMDquQD\n" +
                        "AqriAwWq4wMGqoAAH9b/gwDR/XsBqf1DAJEIAED54SMAkf8HAPkIbUP5AAE/1ugHQPkfAABxAAGf\n" +
                        "mv17Qan/gwCRwANf1gF8QJOC4I5S4wMfqqADgFLkAx+q5QMfquYDH6qd//8XHyAD1QBFABDAA1/W\n" +
                        "/g8f+P8gA9XgAx6q/gdB+MADX9b9e72p9QsA+fRPAqn9AwCR9AMAKvMDAaoACoBSiH5Ak+IDE6rj\n" +
                        "Ax+q5AMfquUDH6rmAx+q4QMIqob//5cf/D+xgwEAVAgAAJD1AwCqCDVF+QABP9boAxVLCAAAuQAA\n" +
                        "gBL0T0Kp9QtA+f17w6jAA1/WCAAAkGkCQPngAx8qCDFF+T8BCOvh/v9UyP7/tGgaQPnIAAC04AMf\n" +
                        "KvRPQqn1C0D5/XvDqMADX9bgAxQqxf//lx8EQLHJAABU4AMfKvRPQqn1C0D5/XvDqMADX9boAwCq\n" +
                        "4AMfKmgaAPn0T0Kp9QtA+f17w6jAA1/W/8MF0f17Ean8bxKp+mcTqfhfFKn2VxWp9E8Wqf1DBJH0\n" +
                        "AwWq+AMEKvcDAyrzAwIq9QMBqvYDAKr8Ax8qOgCAUhsAAJDkCPg3SASAUugCCAofCQBxYQgAVADk\n" +
                        "AG/5Axgq4gMCkQAKgFLhAxmq4wMfquQDH6rlAx+q5gMfquADBK3gAwWt4AMGreADB603//+XHwRA\n" +
                        "sSkBAFRoN0X5+gMAqgABP9boAxpL/AMfKggAALkoAIBSKgAAFBoAAJDpQ0D5SDNF+T8BCOshAQBU\n" +
                        "CAEAtOhbQPnIAAC14AMYKn///5cfBECxSAAAVOBbAPnoQ0D5STNF+R8BCeskCUD6/AefGnMAEDco\n" +
                        "AIBSFQAAFADkAG/iAwCRgAWAUuEDGarjAx+q5AMfquUDH6rmAx+q/zsA+eCDAK3ggwGt4IMCreAD\n" +
                        "gD0K//+X6ANA+Ykyg1IfAABxSSCgcgABSfroB58aGgE8Cmg3RfkAAT/Wny5A8oABAFTIAoBSCAAA\n" +
                        "uQAAgJL0T1ap9ldVqfhfVKn6Z1Op/G9Sqf17Uan/wwWRwANf1p8DAHFIAIBS+X5AkwgRnxoYf0CT\n" +
                        "+wMAqggBEyrAG4BS4QMWqgN9QJPiAxWq5AMZquUDGKrmAxSq5f7/lx/8P7GjAQBU6H4FUx80ADEI\n" +
                        "CXMqCAV3KggBABIIARoqCAWfGqgFADToAwBLAACAkmgDALnc//8XfPv/NPYDAKrVAgC0GQCCUvcD\n" +
                        "Fqr6AxWqBQAAFFoDAMsUABSLFwAXi9oBALRfB0DxYAiAUuEDGKpDM5ma4gMXquQDFKrlAx+q5gMf\n" +
                        "qsP+/5cfAADxTP7/VB8QALFg/v9UFwAAkLQGANFjfkCT6DpF+UAcgFLhAxaq5AMfquUDH6rmAx+q\n" +
                        "iQIIi+gDCMsiAQiKsv7/l7MCEDfgAxaqtP//F/8CH3JIF58aiAEAN2h6HRLAG4BS4QMWqgN9QJPi\n" +
                        "AxWq5AMZquUDGKrmAxSqo/7/lx8EQLFpBABUqAGAUgAAgJJoAwC5ov//F+g6RfmJAhaL4AMWquoD\n" +
                        "CMspAQiLSAEWiikBCooqADvVHwEJ60ICAFRLTRBTjACAUoshyxrsAwiqLHsL1YwBC4ufAQnro///\n" +
                        "VEoNABKLAIBSnzsD1WohyhoodQvVCAEKix8BCeuj//9UAgAAFJ87A9XfPwPVhP//F+EDAKrgGoBS\n" +
                        "4gMVquMDH6rkAx+q5QMfquYDH6p3/v+XH/w/McIBAFRoAh8y6QIbMsAbgFIDfUCTJH1Ak+EDFqri\n" +
                        "AxWqBQCAkuYDH6pr/v+XH/w/seLx/1ST//8XIAAg1P17v6n9AwCRCHxAk+IDAarjAx+qgAWAUuQD\n" +
                        "H6rlAx+q4QMIquYDH6pc/v+X/XvBqMADX9ZJfECTanxAk+YDBaroAwGqhXxAk+EDAKrAG4BS4gMI\n" +
                        "quMDCarkAwqqT/7/Fwh8QJPkAwOq4wMCqmAIgFLiAwGq5QMfquEDCKrmAx+qRv7/F/17v6n9AwCR\n" +
                        "6AMBqkN8QJPhAwCqQByAUuIDCKrkAx+q5QMfquYDH6o7/v+X/XvBqMADX9be/v8X/Xu/qf0DAJHi\n" +
                        "AwGq4QMAquAagFLjAx+q5AMfquUDH6rmAx+qLv7/l/17wajAA1/WCHxAk+MDAqriAwGq4AeAUuQD\n" +
                        "H6rlAx+q4QMIquYDH6oj/v8XCHxAk+MDAqriAwGqAAiAUuQDH6rlAx+q4QMIquYDH6oa/v8X/Xu/\n" +
                        "qf0DAJFJfECT6AMBqgF8QJPkAwMqAAeAUuIDCKrjAwmq5QMfquYDH6oO/v+X/XvBqMADX9b9e7+p\n" +
                        "/QMAkQl8QJPoAwKqZHxAk+IDAargCYBS4wMIquEDCarlAx+q5gMfqgD+/5f9e8GowANf1v17v6n9\n" +
                        "AwCR4wMBquIDAKrgCYBSYQyAkuQDH6rlAx+q5gMfqvT9/5f9e8GowANf1v17v6n9AwCRCHxAk+ID\n" +
                        "AarjAx+qAAqAUuQDH6rlAx+q4QMIquYDH6rn/f+X/XvBqMADX9b9e7+p/QMAkQF8QJPiAx+q4wMf\n" +
                        "qiAHgFLkAx+q5QMfquYDH6rb/f+X/XvBqMADX9b9e7+p/QMAkQh8QJPjAwKq4gMBqqADgFLkAx+q\n" +
                        "5QMfquEDCKrmAx+qzv3/l/17wajAA1/W/Xu/qf0DAJEBfECT4gMfquMDH6rAC4BS5AMfquUDH6rm\n" +
                        "Ax+qwv3/lyAAINQiCQC0CAACi18MAPEBAAA5AfEfOIMIAFRfHADxAQQAOQEIADkB4R84AdEfOMMH\n" +
                        "AFRfJADxAQwAOQHBHzhDBwBU6AMASykcABLqwwAyCwVAkil9ChtMAAvLCgALi4j1fpJNAQiLHyUA\n" +
                        "8UkBALmpwR+4owUAVB9lAPFJpQApqaU+KSMFAFROAX6SIA0ETs8FfbIIAQ/LH4EA8UDBgDygQZ48\n" +
                        "IwQAVIwBDsspgQmqSgEPi4zhANGfgQHxowIAVIz9RdMgDQhOywELi2sBAIuMBQCRa2EBkY3lfpKv\n" +
                        "6XvT7gMNqggBD8tKAQ+LYAE+rc4RAPFgAT+tYAEArWABAa1rAQKRQf//VJ8BDevgAABUCIEA0Ukl\n" +
                        "AKkffQDxSSUBqUqBAJFo//9UwANf1gAAAAAAAAAAAAAAAO++r94AAAAAFEURAAAAAAAAEAAAAAAA\n" +
                        "AA==\n";
        byte[] bytes = android.util.Base64.decode(b64, android.util.Base64.DEFAULT);
        int hookInfoOffset = 0x0a60;
        fillInHookInfo(bytes, hookInfoOffset);
        return bytes;
    }

    @Override
    public int getNativeDebugBreakOffset() {
        return 0x0000;
    }

    @Override
    public int getNativeClearCacheOffset() {
        return 0x0054;
    }

    @Override
    public int getNativeSyscallOffset() {
        return 0x0008;
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
        return 0x01dc;
    }

    @Override
    public int getFakeMmap64Offset() {
        return 0x02c4;
    }

    @Override
    public int getFakeMmapOffset() {
        return 0x074c;
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
