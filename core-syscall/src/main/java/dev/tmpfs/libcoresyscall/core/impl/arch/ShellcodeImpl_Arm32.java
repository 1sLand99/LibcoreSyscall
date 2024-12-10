package dev.tmpfs.libcoresyscall.core.impl.arch;

import dev.tmpfs.libcoresyscall.core.impl.ByteArrayUtils;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.BaseShellcode;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.ISimpleInlineHook;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.ISyscallNumberTable;

public class ShellcodeImpl_Arm32 extends BaseShellcode implements ISimpleInlineHook, ISyscallNumberTable {

    public static final ShellcodeImpl_Arm32 INSTANCE = new ShellcodeImpl_Arm32();

    private ShellcodeImpl_Arm32() {
        super();
    }

    @Override
    public byte[] getShellcodeBytes() {
        //0000 g    DF .text  0008 NativeBridge_breakpoint
        //0000 g    D  .text  0000 ___text_section
        //0008 g    DF .text  0040 NativeBridge_nativeSyscall
        //0048 g    DF .text  0038 syscall_ext
        //0080 g    DF .text  0030 NativeBridge_nativeClearCache
        //00b0 g    DF .text  0020 __clear_cache
        //00d0 g    DF .text  0014 NativeBridge_nativeCallPointerFunction0
        //00e4 g    DF .text  0018 NativeBridge_nativeCallPointerFunction1
        //00fc g    DF .text  001c NativeBridge_nativeCallPointerFunction2
        //0118 g    DF .text  0024 NativeBridge_nativeCallPointerFunction3
        //013c g    DF .text  0028 NativeBridge_nativeCallPointerFunction4
        //0164 g    DF .text  0040 NativeBridge_nativeGetJavaVM
        //01a4 g    DF .text  0038 ashmem_dev_get_size_region
        //01dc g    DF .text  0010 get_hook_info
        //01ec g    DF .text  0008 get_current_pc
        //01f4 g    DF .text  00e0 fake_fstat64
        //02d4 g    DF .text  0480 fake_mmap64
        //0858 g    DF .text  002c fake_mmap
        //0bd0 l     O .rodata  0018 _ZZ13get_hook_infoE9sHookInfo
        String b64 =
                "cAAg4R7/L+EQTC3pCLCN4hDQTeICAKDhCBCb5RAgm+UYMJvlIMCb5TBAm+Uo4JvlAFCN6AhAjeUC\n" +
                        "AADrABCg4wjQS+IQjL3oMEgt6QiwjeIIUIviA+Cg4QDAoOEBAKDhAhCg4TgAlegOIKDhBHAt5Qxw\n" +
                        "oOEAAADvBHCd5DCIveiATC3pCLCN4ggQm+UCcADjAgCg4Q9wQOMCEIHgACCg4wAAAO8AAFDjgIy9\n" +
                        "CP7e/+eAQC3pAnAA4wAgoOMPcEDjAAAA7wAAUOOAgL0I/t7/5wBILekNsKDhMv8v4QAQoOMAiL3o\n" +
                        "AEgt6Q2woOEIAJvlMv8v4QAQoOMAiL3oAEgt6Q2woOEIAJvlEBCb5TL/L+EAEKDjAIi96ABILekN\n" +
                        "sKDhAjCg4QgAm+UQEJvlGCCb5TP/L+EAEKDjAIi96ABILekNsKDhAsCg4QgAm+UQEJvlGCCb5SAw\n" +
                        "m+U8/y/hABCg4wCIvegQTC3pCLCN4gjQTeIAEJDlAECg4wRAjeVsI5HlBBCN4jL/L+EEEJ3lAABQ\n" +
                        "4wQQoBEBAKDhABCg4wjQS+IQjL3oAEgt6Q2woOEQ0E3iABCg4QAAoOMAAI3lBCcH4wQAjeUAMKDj\n" +
                        "CACN5TYAoOOc///rC9Cg4QCIvegEAJ/lAACP4B7/L+HoCQAADgCg4R7/L+HwTS3pGLCN4hDQTeIB\n" +
                        "QKDhAFCg4QBwoOPFAKDjBRCg4QQgoOEAMKDjAHCN5QRwjeUIcI3lhv//6wEKcOMKAACaAGCg4YgA\n" +
                        "n+UAAI/gCACQ5TD/L+EAEGbiABCA5QBw4OMHAKDhGNBL4vCNvehkAJ/l0CDE4QAAj+AEAJDlWBCf\n" +
                        "5QAwI+ABEJ/nASAi4AMgkuHy//8aAACR4fD//wowALTlBBCU5QEAkOHs//8aAIAP4wUAoOH/j0/j\n" +
                        "vP//6wgAUOEAYKCR8GDEkQcAoOEY0Evi8I296IwJAABgCQAAUAkAAPBPLekcsI3i3NBN4hAAjeUD\n" +
                        "UKDhFBCN5QKgoOFMZJ/lAJCg4wgAm+UBcKDjEICb5QZgj+AAAFDjXgAASiIABeICAFDjWwAAGnBw\n" +
                        "jeJQAMDyCBCb5QAwoOMHAKDhByCg4c0KQPTNCkD0zQpA9M0KQPTNCkD0zQpA9ACQgOXFAKDj1JCN\n" +
                        "5QCQjeUEkI3lCJCN5Tf//+sBCnDjBgAAmgBAoOEIAJblMP8v4QAQZOIAEIDlAQCg4z4AAOpwIJ3l\n" +
                        "dDCd5QQAluWoE5/lADAj4AEQn+cBICLgAyCS4Q0AABoAAJHhCwAACqAAneWkEJ3lAQCQ4QcAABoI\n" +
                        "AJvlAEAP4/9PT+Ny///rBABQ4TAQh5IAMKCTCQCBmHAAneV0EJ3lBCCW5VAzn+UCECHgAzCf5wMA\n" +
                        "IOABAIDhAhCT4RAPb+EBEAATBAAa46ACoOEBkADgAQCg4xcAAAoYII3iUADA8gAQoOMAMKDjAgCg\n" +
                        "4WwQjeXNCkD0zQpA9M0KQPTNCkD0zQpA9AAQgOULAQDjABCN5QQQjeUIEI3lCBCb5fX+/+sYEJ3l\n" +
                        "lCkB4wIhQOMCECHgAQCQ4QEAABMJcMDhCACW5TD/L+H/HwDjFmCg4wEAGOEqAAAaFECb5UoQ4OMM\n" +
                        "oI3lJKaw4REAABoIEJvlAGCg4QwwneUoBqDhBBCN5QAAWeMQEJ3lBAqA4RQgneUCMIMTCACN5cAA\n" +
                        "oOMAUI3l1f7/6wAQoOEBCnDjGQAAmgYAoOEMIJ3lpTDg4Q0AceMiIeDhpSKC4QMgguEBIALiByCC\n" +
                        "4QEgABMAAFLjAQAACgBgYeIGAADqAABa4w1goOMCEAUCASCgA6EQIgAHEJEBPQAACgBggOUAEODj\n" +
                        "AQCg4RzQS+Lwj73oAABZ4/r//woUAJ3lEBCN5QAAUOMbAAAKEHCd5QBQoOMUoJ3lAZqg4wBgoOMG\n" +
                        "AADqAICY4AdwgOAAQKTiAKBa4ABgxuIGAJrhDgAACgEKWuMBOqDjCBCb5QowoDEAAFbjtACg4wkw\n" +
                        "oBEHIKDhIAGN6AhAjeWa/v/rAABQ4+v//8oEAHDj7v//Clxhn+UAEKDjABCN5QZgj+AEEI3lCBCN\n" +
                        "5RQQneUQAJblAUBB4gxQneUAEITgAABg4gAgAeAQEJ3lfQCg4wUwoOGF/v/rBAAV4zMAABoQEJ3l\n" +
                        "AQCg4RzQS+Lwj73oKBag4Qggm+UEGoHhDHCd5QQgjeUAoKDhCBCN5QQwx+MQEJ3lwACg4xQgneUA\n" +
                        "UI3lcv7/6wAQoOEKAKDhAQpx47D//4oUkJ3lAGCg41sAoOMAMKDjAGCN5QkgoOEEYI3lCGCN5WX+\n" +
                        "/+sAEA/j/x9P4wEQgeIBAFDhHgAAKiAQheMAEI3lEBCd5QAA4OMCMIfjQQCN6cAAoOMJIKDhV/7/\n" +
                        "6wAQoOEAAA/j/w9P4wAAUeGb//+aAGBh4goAoOGR///qEDCd5RAQluUDcITgACBh4gEQh+ACcADj\n" +
                        "AwAC4AIQAeAPcEDjACCg4wAAAO8DEKDhAABQ44X//wr+3v/nxAgAACgIAADMBwAA0AUAAABILekN\n" +
                        "sKDhENBN4gEgoOEAEKDhAACg4wAwoOMAAI3lBACN5QgAjeULAQDjMP7/6wvQoOEAiL3oEEwt6Qiw\n" +
                        "jeIQ0E3iAsCg4RQgm+UB4KDhABCg4SIGsOFKAODjCQAAGhAAm+UIQJvlGACN6AwwoOEgBqDhAgqA\n" +
                        "4QgAjeXAAKDjDiCg4Rr+/+sI0EviEIy96ABILekNsKDhENBN4gIwoOEBIKDhABCg4QAAoOMI4Jvl\n" +
                        "DMCb5QFAjei0AKDjCMCN5Qv+/+sL0KDhAIi96ABILekNsKDhENBN4gIwoOEBIKDhABCg4QAAoOMA\n" +
                        "AI3lBACN5QgAjeV9AKDj/f3/6wvQoOEAiL3oMEgt6QiwjeIQ0E3iCMCb5QBQoOMMQJvl+EDN4QDA\n" +
                        "jeWV/v/rCNBL4jCIvegASC3pDbCg4RDQTeIBIKDhABCg4QAAoOMAMKDjAACN5QQAjeUIAI3lWwCg\n" +
                        "4+T9/+sL0KDhAIi96ABILekNsKDhENBN4gIwoOEBIKDhABCg4QAAoOMAAI3lBACN5QgAjeUDAKDj\n" +
                        "1v3/6wvQoOEAiL3oAEgt6Q2woOEQ0E3iAjCg4QEgoOEAEKDhAACg4wAAjeUEAI3lCACN5QQAoOPI\n" +
                        "/f/rC9Cg4QCIvegASC3pDbCg4RDQTeICwKDhASCg4QAQoOEAAKDjADCN5QwwoOEEAI3lCACN5UIB\n" +
                        "AOO5/f/rC9Cg4QCIvegASC3pDbCg4RDQTeICwKDhASCg4QAQoOEAAKDjADCN5QwwoOEEAI3lCACN\n" +
                        "5UcBAOOq/f/rC9Cg4QCIvegASC3pDbCg4RDQTeIAIKDhAACg4wEwoOEAAI3lBACN5WMQ4OMIAI3l\n" +
                        "RwEA45z9/+sL0KDhAIi96ABILekNsKDhENBN4gEgoOEAEKDhAACg4wAwoOMAAI3lBACN5QgAjeXF\n" +
                        "AKDjjv3/6wvQoOEAiL3oAEgt6Q2woOEQ0E3iABCg4QAAoOMAAI3lACCg4wQAjeUAMKDjCACN5QYA\n" +
                        "oOOA/f/rC9Cg4QCIvegASC3pDbCg4RDQTeICMKDhASCg4QAQoOEAAKDjAACN5QQAjeUIAI3lNgCg\n" +
                        "43L9/+sL0KDhAIi96ABILekNsKDhENBN4gAQoOEAAKDjAACN5QAgoOMEAI3lADCg4wgAjeX4AKDj\n" +
                        "ZP3/6/7e/+cAAFLjPwAACgAwoOEDAFLjAhDD5gEQQ+U6AAA6BwBS4wIQwOUBEMDlAxBD5QIQQ+U0\n" +
                        "AAA6CQBS4wMQwOUEEEPlHv8vMQBILekNsKDhATEA43EQ7+YBMUDjkQMB4AAwYOIDwAPiADCg4Qwg\n" +
                        "QuAMEKPnA8DC4wwgg+AJAFzjBBAC5R8AADoZAFzjBBCD5QgQg+UMEALlCBAC5RkAADoMEIPlEBCD\n" +
                        "5RQQg+UYEIPlHBAC5RgQAuUUEALlEBAC5QQgA+IY4ILjDiBM4CAAUuMMAAA6DjCD4CAgQuIAEIPl\n" +
                        "BBCD5R8AUuMIEIPlDBCD5RAQg+UUEIPlGBCD5RwQg+UgMIPi8///igBIvege/y/hAAAAAAAAAAAA\n" +
                        "AAAA776v3gAAAAAURREAAAAAAAAQAAAAAAAAGPT/fwEAAAAY9P9/AQAAAFD0/38BAAAAgPT/fwEA\n" +
                        "AACo9P9/AQAAAMD0/38BAAAAzPT/fwEAAADc9P9/AQAAAPD0/38BAAAADPX/fwEAAAAs9f9/AQAA\n" +
                        "AGT1/38BAAAAlPX/fwEAAACc9f9/AQAAAJz1/38BAAAAdPb/fwEAAADs+v9/AQAAABz7/38BAAAA\n" +
                        "bPv/fwEAAACg+/9/AQAAAND7/38BAAAA9Pv/fwEAAAAk/P9/AQAAAFT8/38BAAAAhPz/fwEAAAC4\n" +
                        "/P9/AQAAAOz8/38BAAAAHP3/fwEAAABM/f9/AQAAAHz9/38BAAAArP3/fwEAAADU/f9/AQAAAND9\n" +
                        "/38BAAAA1P7/fwEAAAA=\n";
        byte[] bytes = android.util.Base64.decode(b64, android.util.Base64.DEFAULT);
        int hookInfoOffset = 0x0bd0;
        fillInHookInfo(bytes, hookInfoOffset);
        return bytes;
    }

    @Override
    public int getNativeDebugBreakOffset() {
        return 0x0000;
    }

    @Override
    public int getNativeClearCacheOffset() {
        return 0x0080;
    }

    @Override
    public int getNativeSyscallOffset() {
        return 0x0008;
    }

    @Override
    public int getNativeCallPointerFunction0Offset() {
        return 0x00d0;
    }

    @Override
    public int getNativeCallPointerFunction1Offset() {
        return 0x00e4;
    }

    @Override
    public int getNativeCallPointerFunction2Offset() {
        return 0x00fc;
    }

    @Override
    public int getNativeCallPointerFunction3Offset() {
        return 0x0118;
    }

    @Override
    public int getNativeCallPointerFunction4Offset() {
        return 0x013c;
    }

    @Override
    public int getNativeGetJavaVmOffset() {
        return 0x0164;
    }

    @Override
    public int getFakeStat64Offset() {
        return 0x01f4;
    }

    @Override
    public int getFakeMmap64Offset() {
        return 0x02d4;
    }

    @Override
    public int getFakeMmapOffset() {
        return 0x0858;
    }

    @Override
    public int __NR_mprotect() {
        // mprotect arm: 125
        return 125;
    }

    @Override
    public int __NR_memfd_create() {
        // memfd_create arm: 385
        return 385;
    }

    @Override
    public int __NR_ioctl() {
        // ioctl arm: 36
        return 36;
    }

    @Override
    public int __NR_tgkill() {
        // tgkill arm: 268
        return 268;
    }

    @Override
    public void inlineHook(long address, long hook) {
        if (address == 0) {
            throw new IllegalArgumentException("address is 0");
        }
        if (hook == 0) {
            throw new IllegalArgumentException("hook is 0");
        }
        boolean sourceIsThumb = (address & 1) != 0;
        long sourceAddress = address & ~1;
        byte[] trampoline;
        if (sourceIsThumb) {
            // Thumb 16-bit
            if ((sourceAddress & 4) == 0) {
                // c0 46          nop
                // df f8 04 c0    ldr.w   ip, [pc, #4]
                // 60 47          bx      ip
                // .uint32 address
                trampoline = new byte[]{
                        (byte) 0xc0, 0x46,
                        (byte) 0xdf, (byte) 0xf8, 0x04, (byte) 0xc0,
                        0x60, 0x47,
                        0, 0, 0, 0
                };
                ByteArrayUtils.writeInt32(trampoline, 8, (int) hook);
            } else {
                // df f8 02 c0   ldr.w   ip, [pc, #2]
                // 60 47         bx      ip
                // .uint32 address
                trampoline = new byte[]{
                        (byte) 0xdf, (byte) 0xf8, 0x02, (byte) 0xc0,
                        0x60, 0x47,
                        0, 0, 0, 0
                };
                ByteArrayUtils.writeInt32(trampoline, 6, (int) hook);
            }
        } else {
            // ARM 32-bit
            // 00 c0 9f e5    ldr ip, [pc]
            // 1c ff 2f e1    bx ip
            // .uint32 address
            trampoline = new byte[]{
                    (byte) 0x00, (byte) 0xc0, (byte) 0x9f, (byte) 0xe5,
                    (byte) 0x1c, (byte) 0xff, (byte) 0x2f, (byte) 0xe1,
                    0, 0, 0, 0
            };
            ByteArrayUtils.writeInt32(trampoline, 8, (int) hook);
        }
        writeByteArrayToTextSection(trampoline, sourceAddress);
    }

}
