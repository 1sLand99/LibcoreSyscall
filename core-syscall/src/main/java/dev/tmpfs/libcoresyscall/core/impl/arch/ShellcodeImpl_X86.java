package dev.tmpfs.libcoresyscall.core.impl.arch;

import dev.tmpfs.libcoresyscall.core.impl.ByteArrayUtils;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.BaseShellcode;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.ISimpleInlineHook;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.ISyscallNumberTable;

public class ShellcodeImpl_X86 extends BaseShellcode implements ISimpleInlineHook, ISyscallNumberTable {

    public static final ShellcodeImpl_X86 INSTANCE = new ShellcodeImpl_X86();

    private ShellcodeImpl_X86() {
        super();
    }

    @Override
    public byte[] getShellcodeBytes() {
        //0000 g    DF .text  000b NativeBridge_breakpoint
        //0000 g    D  .text  0000 ___text_section
        //0010 g    DF .text  003e NativeBridge_nativeSyscall
        //0050 g    DF .text  0045 syscall_ext
        //00a0 g    DF .text  000a NativeBridge_nativeClearCache
        //00b0 g    DF .text  000a __clear_cache
        //00c0 g    DF .text  0021 NativeBridge_nativeCallPointerFunction0
        //00f0 g    DF .text  0027 NativeBridge_nativeCallPointerFunction1
        //0120 g    DF .text  002d NativeBridge_nativeCallPointerFunction2
        //0150 g    DF .text  0030 NativeBridge_nativeCallPointerFunction3
        //0180 g    DF .text  0030 NativeBridge_nativeCallPointerFunction4
        //01b0 g    DF .text  004a NativeBridge_nativeGetJavaVM
        //0200 g    DF .text  0043 ashmem_dev_get_size_region
        //0250 g    DF .text  001c get_hook_info
        //0270 g    DF .text  000d get_current_pc
        //0280 g    DF .text  00a4 fake_fstat64
        //0330 g    DF .text  040d fake_mmap64
        //0860 g    DF .text  003f fake_mmap
        //0c20 l     O .rodata  0018 _ZZ13get_hook_infoE9sHookInfo
        String b64 =
                "VYnlg+T8zInsXcOQkJCQkFWJ5VOD5PCD7BDoAAAAAFuBwynw//+D7AT/dTz/dTT/dSz/dST/dRz/\n" +
                        "dRT/dRDoDQAAAIPEIDHSjWX8W13DkJBVieVXVoPk/IPsEItNEItVFIt1GIt9HItFDIlEJASLRSCJ\n" +
                        "RCQIi0UIiUQkDI1EJARVU4toBIsYi0AIzYBbXY1l+F5fXcOQkJCQkJCQkJCQkFWJ5YPk/InsXcOQ\n" +
                        "kJCQkJBVieWD5PyJ7F3DkJCQkJCQVYnlU4Pk8IPsEOgAAAAAW4HDee////9VEDHSjWX8W13DkJCQ\n" +
                        "kJCQkJCQkJCQkJCQVYnlU4Pk8IPsEOgAAAAAW4HDSe///4tFGIkEJP9VEDHSjWX8W13DkJCQkJCQ\n" +
                        "kJCQVYnlU4Pk8IPsEOgAAAAAW4HDGe///4PsCP91IP91GP9VEIPEEDHSjWX8W13DkJCQVYnlU4Pk\n" +
                        "8IPsEOgAAAAAW4HD6e7//4PsBP91KP91IP91GP9VEIPEEDHSjWX8W13DVYnlU4Pk8IPsEOgAAAAA\n" +
                        "W4HDue7///91MP91KP91IP91GP9VEIPEEDHSjWX8W13DVYnlU4Pk8IPsEOgAAAAAW4HDie7//4tF\n" +
                        "CMcEJAAAAACLCIPsCI1UJAhSUP+RbAMAAIPEEInBuAAAAACFyXUDiwQkMdKNZfxbXcOQkJCQkJBV\n" +
                        "ieVTg+Twg+ww6AAAAABbgcM57v//i0UIDyiDyBsAAA8RRCQIiUQkBMdEJBgAAAAAxwQkNgAAAOgT\n" +
                        "/v//jWX8W13DkJCQkJCQkJCQkJCQkFWJ5YPk/OgAAAAAWIHA7e3//42A2BsAAInsXcOQkJCQVYnl\n" +
                        "g+T8i0UEiexdw5CQkFWJ5VNXVoPk8IPsIOgAAAAAW4HDt+3//4t9DItFCA9XwA8RRCQMiXwkCIlE\n" +
                        "JATHBCTFAAAA6Jb9//89AfD//3ITicb/k+AbAAD33okwuP/////rSIuL2BsAAIuT3BsAAIt3BDHW\n" +
                        "iz8xzzHACfd1LQnRdCmLdQyLTiwLTjB1HotFCIkEJOj3/v//PQDw//93ColGLMdGMAAAAAAxwI1l\n" +
                        "9F5fW13DkJCQkJCQkJCQkJCQVYnlU1dWg+TwgezgAAAA6AAAAABbgcME7f//i30ci0UUsgExyYN9\n" +
                        "GAAPiGIBAACD4CKD+AIPhVYBAAAPV8APKUQkcA8pRCRgDylEJFAPKUQkQA8pRCQwDylEJCCD7CAP\n" +
                        "EUQkDI1EJECJRCQIi0UYiUQkBMcEJMUAAADoofz//4PEID0A8P//dhWJxv+T4BsAAPfeiTCwATHJ\n" +
                        "6e0AAACLg9gbAACLi9wbAACLVCQkMcqLdCQgMcYJ1nUvCch0K4tEJEwLRCRQdSGD7Az/dRjo/f3/\n" +
                        "/4PEED0A8P//dwyJRCRMx0QkUAAAAACLg9gbAACLi9wbAACLVCQkMcqLdCQgMcYJ1g+UwgnID5XA\n" +
                        "INCJxrABi00Q9sEEdHAPV8APKYQkwAAAAA8phCSwAAAADymEJKAAAAAPKYQkkAAAAA8phCSAAAAA\n" +
                        "x4Qk0AAAAAAAAACD7CAPEUQkDI2EJKAAAACJRCQIi0UYiUQkBMcEJA0BAADoq/v//4PEILmUGQIB\n" +
                        "M4wkgAAAAAnBD5XAifGJyvbSIMKIVCQMic7/k+AbAAD3x/8PAAB0EMcAFgAAALj/////6ekBAACJ\n" +
                        "RCQIi00gi1UMuLX////3wQDw//91NYnwAMAPtsALRRAPpPkUg+wEUf91GP91FFBS/3UIaMAAAADo\n" +
                        "Lfv//4PEID0B8P//D4K4AAAAiVwkEDHJg/jzD5XBiUwkFDHbi00U9sECD5TDic7B7gWD5gExyYtV\n" +
                        "EPbCBA+UwQ+2fCQMCd8J8QnPC3wkFHQS99iLTCQIiQG4/////+lMAQAAi1UU9sICD5TB90UgAPD/\n" +
                        "/4tcJBCLfRx1OgpMJAx1NItNEIPh+4tFIA+k+BSD7ARQ/3UYUlH/dQz/dQhowAAAAOiJ+v//g8Qg\n" +
                        "PQDw//8PhgEBAACLRCQIxwANAAAAuP/////p5QAAAInxhMmLTSAPhNgAAACJfCQciUQkGIN9DAAP\n" +
                        "hJgAAACJzzHSi0QkGIlEJBCLTQzrLZCQkJCQkJCLTCQMKcGLVCQIg9oAAceJfCQci3wkFIPXAAHG\n" +
                        "iXQkEInICdB0Wol8JBSJVCQIiUwkDIH5ABAAALgAEAAAD0LBhdK+ABAAAA9FxoPsBGoAV4t8JChX\n" +
                        "UIt0JCRW/3UYaLQAAADoyPn//4PEIIXAf5GD+PyLfCQUi1QkCItMJAx0oIuD6BsAAItNDAHBSffY\n" +
                        "IciD7ARqAGoAagD/dRBQi3QkMFZqfeiH+f//ifCDxCCNZfReX1tdw4PsIA9XwA8RRCQMi3UMiXQk\n" +
                        "CIlEJATHBCRbAAAA6Fj5//+DxCA9AfD//3M5i0UQg8gCi00Ug8kgg+wEagBq/1FQVv91CGjAAAAA\n" +
                        "6Cv5//+DxCA9AfD//4tNIA+DOv7//+m7/v//DwuQkJBVieVTg+Twg+ww6AAAAABbgcP56P//i0UI\n" +
                        "i00MD1fADxFEJAyJTCQIiUQkBMcEJA0BAADo2Pj//41l/Ftdw5CQVYnlU4Pk8IPsEOgAAAAAW4HD\n" +
                        "uej//4tNILi1////98EA8P//dSeLRRwPpMEUg+wEUf91GP91FP91EP91DP91CGjAAAAA6Ib4//+D\n" +
                        "xCCNZfxbXcOQkJCQkJCQkJCQkJCQVYnlU4Pk8IPsMOgAAAAAW4HDWej//w8oRQiLRRiJRCQUDxFE\n" +
                        "JATHRCQYAAAAAMcEJLQAAADoNvj//41l/Ftdw1WJ5VOD5PCD7BDoAAAAAFuBwxno//+D7ARqAGoA\n" +
                        "agD/dRD/dQz/dQhqfegB+P//g8QgjWX8W13DkJCQkJCQkJBVieVTg+Twg+ww6AAAAABbgcPZ5///\n" +
                        "DyhFCItFGItNHIlMJBSJRCQQDxEEJMdEJBgAAAAA6Jf6//+NZfxbXcOQVYnlU4Pk8IPsMOgAAAAA\n" +
                        "W4HDmef//4tFCItNDA9XwA8RRCQMiUwkCIlEJATHBCRbAAAA6Hj3//+NZfxbXcOQkFWJ5VOD5PCD\n" +
                        "7BDoAAAAAFuBw1nn//+D7ARqAGoAagD/dRD/dQz/dQhqA+hB9///g8QgjWX8W13DkJCQkJCQkJBV\n" +
                        "ieVTg+Twg+wQ6AAAAABbgcMZ5///g+wEagBqAGoA/3UQ/3UM/3UIagToAff//4PEII1l/Ftdw5CQ\n" +
                        "kJCQkJCQVYnlU4Pk8IPsEOgAAAAAW4HD2eb//w+3RRSD7ARqAGoAUP91EP91DP91CGgnAQAA6Lv2\n" +
                        "//+DxCCNZfxbXcOQkFWJ5VOD5PCD7DDoAAAAAFuBw5nm//8PKEUIDxFEJATHRCQYAAAAAMdEJBQA\n" +
                        "AAAAxwQkLAEAAOh19v//jWX8W13DkJCQkJCQkJCQkJCQkJCQVYnlU4Pk8IPsEOgAAAAAW4HDSeb/\n" +
                        "/4PsBGoAagBqAP91DP91CGqcaCwBAADoL/b//4PEII1l/Ftdw5CQkJCQkFWJ5VOD5PCD7DDoAAAA\n" +
                        "AFuBwwnm//+LRQiLTQwPV8APEUQkDIlMJAiJRCQExwQkxQAAAOjo9f//jWX8W13DkJBVieVTg+Tw\n" +
                        "g+ww6AAAAABbgcPJ5f//i0UID1fADxFEJAiJRCQEx0QkGAAAAADHBCQGAAAA6Kf1//+NZfxbXcOQ\n" +
                        "VYnlU4Pk8IPsEOgAAAAAW4HDieX//4PsBGoAagBqAP91EP91DP91CGo26HH1//+DxCCNZfxbXcOQ\n" +
                        "kJCQkJCQkFWJ5VOD5PCD7DDoAAAAAFuBw0nl//+LRQgPV8APEUQkCIlEJATHRCQYAAAAAMcEJPwA\n" +
                        "AADoJ/X//5CQkJCQkJBVieWD5PwPC5CQkJCQkJCQVYnlU1dWg+T8i00Qi0UIhckPhK0AAACLXQyI\n" +
                        "GIhcCP+D+QMPgpsAAACIWAGIWAKIXAj+iFwI/YP5Bw+ChAAAAIhYA4hcCPyD+QlyeInH99+D5wON\n" +
                        "FDgp+YPh/A+282n2AQEBAYk0OIl0EfyD+QlyVIlyBIlyCIl0CvSJdAr4g/kZckFmD27GZg9wwADz\n" +
                        "D39CDPMPf0QK5InWg+YEg84YKfGD+SByHgHykJCQkJCQkJDzD38C8w9/QhCDweCDwiCD+R937I1l\n" +
                        "9F5fW13DAAAAAAR3AAAAAAAAAAAAAAAAAADvvq/eAAAAABRFEQAAAAAAABAAAAAAAAA=\n";
        byte[] bytes = android.util.Base64.decode(b64, android.util.Base64.DEFAULT);
        int hookInfoOffset = 0x0c20;
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
        return 0x0010;
    }

    @Override
    public int getNativeCallPointerFunction0Offset() {
        return 0x00c0;
    }

    @Override
    public int getNativeCallPointerFunction1Offset() {
        return 0x00f0;
    }

    @Override
    public int getNativeCallPointerFunction2Offset() {
        return 0x0120;
    }

    @Override
    public int getNativeCallPointerFunction3Offset() {
        return 0x0150;
    }

    @Override
    public int getNativeCallPointerFunction4Offset() {
        return 0x0180;
    }

    @Override
    public int getNativeGetJavaVmOffset() {
        return 0x01b0;
    }

    @Override
    public int getFakeStat64Offset() {
        return 0x0280;
    }

    @Override
    public int getFakeMmap64Offset() {
        return 0x0330;
    }

    @Override
    public int getFakeMmapOffset() {
        return 0x0860;
    }

    @Override
    public int __NR_mprotect() {
        // __NR_mprotect x86 125
        return 125;
    }

    @Override
    public int __NR_memfd_create() {
        // __NR_memfd_create x86 356
        return 356;
    }

    @Override
    public int __NR_ioctl() {
        // __NR_ioctl x86 54
        return 54;
    }

    @Override
    public int __NR_tgkill() {
        // __NR_tgkill x86 270
        return 270;
    }

    @Override
    public void inlineHook(long address, long hook) {
        if (address == 0) {
            throw new IllegalArgumentException("address is 0");
        }
        if (hook == 0) {
            throw new IllegalArgumentException("hook is 0");
        }
        // 68 [hook]  push address:hook
        // c3        ret
        // maybe this will make the intel shadow stack unhappy
        // but let's ignore it for now
        byte[] stub = new byte[6];
        stub[0] = 0x68;
        ByteArrayUtils.writeInt32(stub, 1, (int) hook);
        stub[5] = (byte) 0xc3;
        writeByteArrayToTextSection(stub, address);
    }

}
