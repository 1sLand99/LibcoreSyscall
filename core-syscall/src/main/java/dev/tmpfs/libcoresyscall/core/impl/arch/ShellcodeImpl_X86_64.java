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
        //0000 g    DF .text  0007 NativeBridge_breakpoint
        //0000 g    D  .text  0000 ___text_section
        //0010 g    DF .text  0034 NativeBridge_nativeSyscall
        //0050 g    DF .text  0031 syscall_ext
        //0090 g    DF .text  0006 NativeBridge_nativeClearCache
        //00a0 g    DF .text  0006 __clear_cache
        //00b0 g    DF .text  0007 NativeBridge_nativeCallPointerFunction0
        //00c0 g    DF .text  000a NativeBridge_nativeCallPointerFunction1
        //00d0 g    DF .text  000d NativeBridge_nativeCallPointerFunction2
        //00e0 g    DF .text  0013 NativeBridge_nativeCallPointerFunction3
        //0100 g    DF .text  001a NativeBridge_nativeCallPointerFunction4
        //0120 g    DF .text  0033 NativeBridge_nativeGetJavaVM
        //0160 g    DF .text  0030 ashmem_dev_get_size_region
        //0190 g    DF .text  000d get_hook_info
        //01a0 g    DF .text  000a get_current_pc
        //01b0 g    DF .text  0090 fake_fstat64
        //0240 g    DF .text  03cf fake_mmap64
        //06f0 g    DF .text  000a fake_mmap
        //0a10 l     O .rodata  0018 _ZZ13get_hook_infoE9sHookInfo
        String b64 =
                "VUiJ5cxdw2YPH4QAAAAAAFVIieVIg+wQTYnKTInAiddMi0UQTItNGEiLVSBIiRQkSInOSInCTInR\n" +
                        "6BIAAABIg8QQXcNmZmYuDx+EAAAAAABVSInlQVdBVlNMictNicZMi30QSGPHSIn3SInWSInKTYny\n" +
                        "SYnYTYn5DwVbQV5BX13DZmZmZmZmLg8fhAAAAAAAVUiJ5V3DZi4PH4QAAAAAAFVIieVdw2YuDx+E\n" +
                        "AAAAAABVSInlXf/iZg8fhAAAAAAAVUiJ5UiJz13/4mYPH0QAAFVIieVMicZIic9d/+IPHwBVSInl\n" +
                        "TInGSInQSInPTInKXf/gZmZmZi4PH4QAAAAAAFVIieVMicZIidBMi0UQSInPTInKTInBXf/gZg8f\n" +
                        "RAAAVUiJ5UiD7BBIx0X4AAAAAEiLB0iNdfj/kNgGAACFwHUKSItF+EiDxBBdwzHASIPEEF3DZmZm\n" +
                        "Zi4PH4QAAAAAAFVIieVIg+wQSGP3SMcEJAAAAAC6BHcAAL8QAAAAMclFMcBFMcnoxv7//0iDxBBd\n" +
                        "w1VIieVIjQV1CAAAXcMPHwBVSInlSItFCF3DZg8fRAAAVUiJ5UFXQVZBVFNIg+wQSYn2QYn/SGP3\n" +
                        "SMcEJAAAAAAx278FAAAATInyMclFMcBFMcnoaf7//0g9AfD//3IWSYnE/xUgCAAAQffcRIkgu///\n" +
                        "///rLEiLBQQIAABJOQZ1IEiFwHQbSYN+MAB1FESJ/+g7////SD0A8P//dwRJiUYwidhIg8QQW0Fc\n" +
                        "QV5BX13DVUiJ5UFXQVZBVUFUU0iB7FgBAABNic5FicdBidRIiXXISIl9uEUx7bMBRYXAiU20D4h1\n" +
                        "AQAAiciD4CKD+AIPhWcBAABEiWXQD1fADylFkA8pRYAPKYVw////DymFYP///w8phVD///8PKYVA\n" +
                        "////DymFMP///w8phSD///8PKYUQ////RIn7SMcEJAAAAABIjZUQ////vwUAAABIid4xyUUxwEUx\n" +
                        "yehl/f//SD0A8P//dh1JicT/FRwHAABB99xEiSCwAUUx7USLZdDp1AAAAEiLBfkGAABIOYUQ////\n" +
                        "dSZIhcB0IUiDvUD///8AdRdEif/oKf7//0g9APD//3cHSImFQP///0iLBcMGAABIOYUQ////D5TB\n" +
                        "SIXAQQ+VxUEgzbABRItl0EH2xAR0dw9XwA8phfD+//8PKYXg/v//DymF0P7//w8phcD+//8PKYWw\n" +
                        "/v//DymFoP7//w8phZD+//9Ix4UA////AAAAAEjHBCQAAAAASI2VkP7//7+KAAAASIneMclFMcBF\n" +
                        "McnogPz//4nAuZQZAgFIM42Q/v//SAnBD5XARInr9tMgw/8VJwYAAEyJ8UjB4TR0IscAFgAAAEnH\n" +
                        "xP////9MieBIgcRYAQAAW0FcQV1BXkFfXcNIiUXAQo0EbQAAAAAPtsBECeBIY8hEifhEi320TWPH\n" +
                        "TGPITIk0JL8JAAAASIt1uEiLVchMiUXQTIlNqOj0+///RInnSYnESD0B8P//ckUxwEGD/PMPlcAx\n" +
                        "yUH2xwIPlMFEifrB6gWD4gEx9kD2xwRAD5TGCdYPttMJygnyCcIPhLIAAABB99xIi0XARIkg6VH/\n" +
                        "//+JfdBFhO0PhEz///9Ig33IAEyLfah0UUyJ40yLbcjrD5BJKcVJAcZIAcNNhe10OUmB/QAQAAC5\n" +
                        "ABAAAEkPQs1IxwQkAAAAAL8RAAAATIn+SInaTYnwRTHJ6ET7//9IhcB/v0iD+Px0wkiLFQIFAABI\n" +
                        "i0XISAHQSP/ISPfaSCHCSGNN0EjHBCQAAAAAvwoAAABMieZFMcBFMcnoA/v//+m1/v//RIn4qAIP\n" +
                        "lMAI2EyLZch1L4n7ifiD4PtIY8hMiTQkvwkAAABIi3W4TIniTItF0EyLTajox/r//0g9APD//3YP\n" +
                        "SItFwMcADQAAAOlg/v//SMcEJAAAAAC/CwAAAEiJxkyJ4jHJRTHARTHJ6JD6//89AfD//3NGiV3Q\n" +
                        "idiDyAJBg88gSGPITWPHSMcEJAAAAAC/CQAAAEiLdbhMieJJx8H/////6Ff6//9JicRIPQHw//8P\n" +
                        "g5j+///prv7//w8LkFVIieVIg+wQSInySGP3SMcEJAAAAAC/igAAADHJRTHARTHJ6Bj6//9Ig8QQ\n" +
                        "XcNmkFVIieVTUEiJ8EiJ/kxj0kxj2Ulj2EyJDCS/CQAAAEiJwkyJ0U2J2EmJ2ejh+f//SIPECFtd\n" +
                        "w2YuDx+EAAAAAABVSInlSIPsEEmJyEiJ0UiJ8khj90jHBCQAAAAAvxEAAABFMcnop/n//0iDxBBd\n" +
                        "w5BVSInlSIPsEEiJ8EiJ/khjykjHBCQAAAAAvwoAAABIicJFMcBFMcnodPn//0iDxBBdw2ZmZmZm\n" +
                        "Lg8fhAAAAAAAVUiJ5V3pRvv//2YPH0QAAFVIieVIg+wQSInySIn+SMcEJAAAAAC/CwAAADHJRTHA\n" +
                        "RTHJ6Cj5//9Ig8QQXcNmkFVIieVIg+wQSInRSInySGP3SMcEJAAAAAAx/0UxwEUxyej6+P//SIPE\n" +
                        "EF3DDx9AAFVIieVIg+wQSInRSInySGP3SMcEJAAAAAC/AQAAAEUxwEUxyejH+P//SIPEEF3DkFVI\n" +
                        "ieVIg+wQSInwSGP3TGPKQYnISMcEJAAAAAC/AQEAAEiJwkyJyUUxyeiR+P//SIPEEF3DZmYuDx+E\n" +
                        "AAAAAABVSInlSIPsEEiJ0EiJ8khj90xjwUjHBCQAAAAAvwYBAABIicFFMcnoVPj//0iDxBBdw2Zm\n" +
                        "ZmZmLg8fhAAAAAAAVUiJ5UiD7BBIifFIifpIxwQkAAAAAL8GAQAASMfGnP///0UxwEUxyegT+P//\n" +
                        "SIPEEF3DZmZmZi4PH4QAAAAAAFVIieVIg+wQSInySGP3SMcEJAAAAAC/BQAAADHJRTHARTHJ6Nj3\n" +
                        "//9Ig8QQXcNmkFVIieVIg+wQSGP3SMcEJAAAAAC/AwAAADHSMclFMcBFMcnoqff//0iDxBBdww8f\n" +
                        "AFVIieVIg+wQSInRSInySGP3SMcEJAAAAAC/EAAAAEUxwEUxyeh39///SIPEEF3DkFVIieVIg+wQ\n" +
                        "SGP3SMcEJAAAAAC/5wAAADHSMclFMcBFMcnoSff//2YPH4QAAAAAAFVIieUPC2YuDx+EAAAAAABI\n" +
                        "ifhIhdIPhOIAAABAiDBAiHQQ/0iD+gMPgtAAAABAiHABQIhwAkCIdBD+QIh0EP1Ig/oHD4K0AAAA\n" +
                        "QIhwA0CIdBD8SIP6CQ+CoQAAAInH99+D5wNIjQw4SCn6SIPi/EAPtvZp9gEBAQGJNDiJdAr8SIP6\n" +
                        "CXJ4iXEEiXEIiXQR9Il0EfhIg/oZcmRmD27GZg9wwADzD39BDPMPf0QR5InPg+cESIPPGEgp+kiD\n" +
                        "+iByPlVIieVBifBMicZIweYgTAnGSAH5ZmZmLg8fhAAAAAAASIkxSIlxCEiJcRBIiXEYSIPC4EiD\n" +
                        "wSBIg/ofd+NdwwDvvq/eAAAAABRFEQAAAAAAABAAAAAAAAA=\n";
        byte[] bytes = android.util.Base64.decode(b64, android.util.Base64.DEFAULT);
        int hookInfoOffset = 0x0a10;
        fillInHookInfo(bytes, hookInfoOffset);
        return bytes;
    }

    @Override
    public int getNativeDebugBreakOffset() {
        return 0x0000;
    }

    @Override
    public int getNativeClearCacheOffset() {
        return 0x0090;
    }

    @Override
    public int getNativeSyscallOffset() {
        return 0x0010;
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
        return 0x01b0;
    }

    @Override
    public int getFakeMmap64Offset() {
        return 0x0240;
    }

    @Override
    public int getFakeMmapOffset() {
        return 0x06f0;
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
    public int __NR_ioctl() {
        // ioctl x86_64: 16
        return 16;
    }

    @Override
    public int __NR_tgkill() {
        // tgkill x86_64: 234
        return 234;
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
