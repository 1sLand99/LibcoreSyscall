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
        //05e0 l     O .text  0018 get_hook_info.sHookInfo
        //0000 g     F .text  0008 NativeBridge_breakpoint
        //0008 g     F .text  0024 __clear_cache
        //002c g     F .text  0038 syscall_ext
        //0064 g     F .text  0040 NativeBridge_nativeSyscall
        //00a4 g     F .text  0030 NativeBridge_nativeClearCache
        //00d4 g     F .text  0014 NativeBridge_nativeCallPointerFunction0
        //00e8 g     F .text  0018 NativeBridge_nativeCallPointerFunction1
        //0100 g     F .text  001c NativeBridge_nativeCallPointerFunction2
        //011c g     F .text  0024 NativeBridge_nativeCallPointerFunction3
        //0140 g     F .text  0028 NativeBridge_nativeCallPointerFunction4
        //0168 g     F .text  0040 NativeBridge_nativeGetJavaVM
        //01a8 g     F .text  0010 get_hook_info
        //01b8 g     F .text  003c lsw_pread64
        //01f4 g     F .text  0038 lsw_mprotect
        //022c g     F .text  0104 fake_fstat64
        //0330 g     F .text  0284 fake_mmap64
        //05b4 g     F .text  002c fake_mmap
        String b64 =
                "cAAg4R7/L+GATC3pCLCN4gJwAOMAIKDjD3BA4wAAAO8AAFDjgIy9CP7e/+cwSC3pCLCN4ghQi+ID\n" +
                        "4KDhAMCg4QEAoOECEKDhOACV6A4goOEEcC3lDHCg4QAAAO8EcJ3kMIi96BBMLekIsI3iENBN4gIA\n" +
                        "oOEIEJvlECCb5Rgwm+UgwJvlMECb5Sjgm+UAUI3oCECN5eT//+sAEKDjCNBL4hCMveiATC3pCLCN\n" +
                        "4ggQm+UCcADjAgCg4Q9wQOMCEIHgACCg4wAAAO8AAFDjgIy9CP7e/+cASC3pDbCg4TL/L+EAEKDj\n" +
                        "AIi96ABILekNsKDhCACb5TL/L+EAEKDjAIi96ABILekNsKDhCACb5RAQm+Uy/y/hABCg4wCIvegA\n" +
                        "SC3pDbCg4QIwoOEIAJvlEBCb5Rggm+Uz/y/hABCg4wCIvegASC3pDbCg4QLAoOEIAJvlEBCb5Rgg\n" +
                        "m+UgMJvlPP8v4QAQoOMAiL3oEEwt6QiwjeII0E3iABCQ5QBAoOMEQI3lbCOR5QQQjeIy/y/hBBCd\n" +
                        "5QAAUOMEEKARAQCg4QAQoOMI0EviEIy96AQAn+UAAI/gHv8v4SwEAAAASC3pDbCg4RDQTeICMKDh\n" +
                        "ASCg4QAQoOEAAKDjCOCb5QzAm+UBQI3otACg4wjAjeWP///rC9Cg4QCIvegASC3pDbCg4RDQTeIC\n" +
                        "MKDhASCg4QAQoOEAAKDjAACN5QQAjeUIAI3lfQCg44H//+sL0KDhAIi96PBNLekYsI3iENBN4gFA\n" +
                        "oOEAUKDhAHCg48UAoOMFEKDhBCCg4QAwoOMAcI3lBHCN5QhwjeVx///rAQpw4xIAAIq0AJ/l0CDE\n" +
                        "4QAAj+AEAJDlqBCf5QAwI+ABEJ/nASAi4AMgkuEFAAAaAACR4QMAAAowALTlBBCU5QEAkOENAAAK\n" +
                        "BwCg4RjQS+Lwjb3oAGCg4WAAn+UAAI/gCACQ5TD/L+EAEGbiABCA5QBw4OMHAKDhGNBL4vCNvegB\n" +
                        "gA/jNgCg4wUQoOEEJwfjADCg4/+PT+MAcI3lBHCN5QhwjeVH///rCABQ4QBgoDHwYMQxBwCg4RjQ\n" +
                        "S+Lwjb3oGAMAAGQDAABUAwAA8E8t6RywjeKM0E3iFACN5QAA4OMcEI3lAkCg4RgAjeUAoKDjTIKf\n" +
                        "5QCQoOMIEJvlCICP4BAwjeUAAFHjGAAASiIAA+IAYKDjAgBQ4wBwoOMEUKDhFQAAGiAQjeJQAMDy\n" +
                        "hJCN5QEAoOHNCkD0zQpA9M0KQPTNCkD0zQpA9M0KQPQAkIDlCACb5Zv//+sAAFDjZgAACgAA4OME\n" +
                        "UKDhGACN5QIAAOoAYKDjAHCg4wRQoOEIAJjlDECN5TD/L+EQMJvlAICg4f8fAOMWAKDjAQAT4VIA\n" +
                        "ABoAIA/j/y9P4wMgUuAUIJvlAhDR4EwAADoQEJ3lIwag4QAQjeUCCoDhCBCb5QUwoOEEEI3lFBCd\n" +
                        "5RwgneUIAI3lwACg4/f+/+sBCnDjPQAAihhQneUBAHXjOAAAChQAjeUKAJnhFgAAChRAneUBiqDj\n" +
                        "BgAA6gBgluAEQIDgAHCn4gCQWeAAoMriCgCZ4QwAAAoBClnjASqg4wkgoDEAAFrjCCCgEQUAoOEE\n" +
                        "EKDh8GDN4T///+sAAFDj7f//ygQAcOPw//8K4HCf5RwQneUHcI/gDGCd5QFQQeIUQJ3lEACX5QYg\n" +
                        "oOEAEIXgAABg4gAQAeAEAKDhPf//6wQAoOEEABbjDgAAChAQl+UCcADjD3BA4wAwYeIDIADgAACF\n" +
                        "4AEAgOADEADgAgCg4QAgoOMAAADvABCg4RQAneUAAFHjFwAAGhzQS+Lwj73oAABg4gAAiOUAAODj\n" +
                        "HNBL4vCPveggIJ3lJDCd5QQAmOU4EJ/lADAj4AEQn+cBICLgAyCS4Y///xoAAJHhjf//ChyQneUD\n" +
                        "UITjEGCb5RRwm+UIAJvlif//6v7e/+d4AgAAZAAAAAgBAAAwSC3pCLCN4hDQTeIIwJvlAFCg4wxA\n" +
                        "m+X4QM3hAMCN5VX//+sI0EviMIi96O++r94AAAAAFEURAAAAAAAAAAAAAAAAAA==";
        byte[] bytes = android.util.Base64.decode(b64, android.util.Base64.DEFAULT);
        int hookInfoOffset = 0x05e0;
        fillInHookInfo(bytes, hookInfoOffset);
        return bytes;
    }

    @Override
    public int getNativeDebugBreakOffset() {
        return 0x0000;
    }

    @Override
    public int getNativeClearCacheOffset() {
        return 0x00a4;
    }

    @Override
    public int getNativeSyscallOffset() {
        return 0x0064;
    }

    @Override
    public int getNativeCallPointerFunction0Offset() {
        return 0x00d4;
    }

    @Override
    public int getNativeCallPointerFunction1Offset() {
        return 0x00e8;
    }

    @Override
    public int getNativeCallPointerFunction2Offset() {
        return 0x0100;
    }

    @Override
    public int getNativeCallPointerFunction3Offset() {
        return 0x011c;
    }

    @Override
    public int getNativeCallPointerFunction4Offset() {
        return 0x0140;
    }

    @Override
    public int getNativeGetJavaVmOffset() {
        return 0x0168;
    }

    @Override
    public int getFakeStat64Offset() {
        return 0x022c;
    }

    @Override
    public int getFakeMmap64Offset() {
        return 0x0330;
    }

    @Override
    public int getFakeMmapOffset() {
        return 0x05b4;
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
