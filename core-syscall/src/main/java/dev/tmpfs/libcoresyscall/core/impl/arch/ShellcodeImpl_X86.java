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
        //05f0 l     O .text  0018 get_hook_info.sHookInfo
        //0000 g     F .text  000b NativeBridge_breakpoint
        //0010 g     F .text  000a __clear_cache
        //0020 g     F .text  0045 syscall_ext
        //0070 g     F .text  003e NativeBridge_nativeSyscall
        //00b0 g     F .text  000a NativeBridge_nativeClearCache
        //00c0 g     F .text  0021 NativeBridge_nativeCallPointerFunction0
        //00f0 g     F .text  0027 NativeBridge_nativeCallPointerFunction1
        //0120 g     F .text  002d NativeBridge_nativeCallPointerFunction2
        //0150 g     F .text  0030 NativeBridge_nativeCallPointerFunction3
        //0180 g     F .text  0030 NativeBridge_nativeCallPointerFunction4
        //01b0 g     F .text  004a NativeBridge_nativeGetJavaVM
        //0200 g     F .text  001c get_hook_info
        //0220 g     F .text  0040 lsw_pread64
        //0260 g     F .text  0038 lsw_mprotect
        //02a0 g     F .text  00c0 fake_fstat64
        //0360 g     F .text  0243 fake_mmap64
        //05b0 g     F .text  003f fake_mmap
        String b64 =
                "VYnlg+T8zInsXcOQkJCQkFWJ5YPk/InsXcOQkJCQkJBVieVXVoPk/IPsEItNEItVFIt1GIt9HItF\n" +
                        "DIlEJASLRSCJRCQIi0UIiUQkDI1EJARVU4toBIsYi0AIzYBbXY1l+F5fXcOQkJCQkJCQkJCQkFWJ\n" +
                        "5VOD5PCD7BDoAAAAAFuBw9EVAACD7AT/dTz/dTT/dSz/dST/dRz/dRT/dRDoff///4PEIDHSjWX8\n" +
                        "W13DkJBVieWD5PyJ7F3DkJCQkJCQVYnlU4Pk8IPsEOgAAAAAW4HDgRUAAP9VEDHSjWX8W13DkJCQ\n" +
                        "kJCQkJCQkJCQkJCQVYnlU4Pk8IPsEOgAAAAAW4HDURUAAItFGIkEJP9VEDHSjWX8W13DkJCQkJCQ\n" +
                        "kJCQVYnlU4Pk8IPsEOgAAAAAW4HDIRUAAIPsCP91IP91GP9VEIPEEDHSjWX8W13DkJCQVYnlU4Pk\n" +
                        "8IPsEOgAAAAAW4HD8RQAAIPsBP91KP91IP91GP9VEIPEEDHSjWX8W13DVYnlU4Pk8IPsEOgAAAAA\n" +
                        "W4HDwRQAAP91MP91KP91IP91GP9VEIPEEDHSjWX8W13DVYnlU4Pk8IPsEOgAAAAAW4HDkRQAAItF\n" +
                        "CMcEJAAAAACLCIPsCI1UJAhSUP+RbAMAAIPEEInBuAAAAACFyXUDiwQkMdKNZfxbXcOQkJCQkJBV\n" +
                        "ieWD5PzoAAAAAFiBwEUUAACNgKDv//+J7F3DkJCQkFWJ5VOD5PCD7DDoAAAAAFuBwyEUAAAPKEUI\n" +
                        "i0UYiUQkFA8RRCQEx0QkGAAAAADHBCS0AAAA6Mb9//+NZfxbXcNVieVTg+Twg+wQ6AAAAABbgcPh\n" +
                        "EwAAg+wEagBqAGoA/3UQ/3UM/3UIan3okf3//4PEII1l/Ftdw5CQkJCQkJCQVYnlU1dWg+Twg+wg\n" +
                        "6AAAAABbgcOfEwAAi30Mi0UID1fADxFEJAyJfCQIiUQkBMcEJMUAAADoRv3//z0B8P//c2aLi6Dv\n" +
                        "//+Lk6Tv//+LdwQx1os/Mc8xwAn3dVwJ0XRYi3UMi04sC04wdU0PKIPwzv//DxFEJAiLRQiJRCQE\n" +
                        "x0QkGAAAAADHBCQ2AAAA6O78//89APD//3cKiUYsx0YwAAAAADHA6xGJxv+TqO////feiTC4////\n" +
                        "/41l9F5fW13DVYnlU1dWg+TwgeyQAAAA6AAAAABbgcPcEgAAi0UUi3UQMcm/AAAAAMdEJBD/////\n" +
                        "g30YAHhpg+AiugAAAADHRCQIAAAAAIP4Ag+FugAAAA9XwA8pRCRwDylEJGAPKUQkUA8pRCRADylE\n" +
                        "JDAPKUQkIIPsCI1EJChQ/3UY6MD+//+DxBAx/8dEJBD/////hcB0HTHJMdLHRCQIAAAAAOtqugAA\n" +
                        "AADHRCQIAAAAAOtbi4Og7///i4uk7///i1QkJDHKi3QkIDHGCdZ1LgnIi3UQdBmDzgOLfQyLVRyL\n" +
                        "RSCJRCQIi0UYiUQkEOsdMf8x0sdEJAgAAAAA6w8x/zHSx0QkCAAAAACLdRAxyYlUJBSJTCQY/5Oo\n" +
                        "7///i00c98H/DwAAdROB+QHw//+LVSCJ0YHZ/w8AAHITxwAWAAAAuP////+NZfReX1tdw4lEJAyL\n" +
                        "RRwPpMIUg+wEUv91GP91FFb/dQz/dQhowAAAAOhS+///g8QgPQHw//8Pg7oAAACDfCQQ/3S9iUQk\n" +
                        "HIn4i0wkGAnIi1QkFHR2i0QkHIlEJAzrIZApx4tMJBiD2QCLVCQUAcKDVCQIAAHGiXQkDIn4Cch0\n" +
                        "S4lUJBSJTCQYgf8AEAAAuAAQAAAPQseFyb4AEAAAD0XGg+wM/3QkFFJQi3QkJFb/dCQs6Mn8//+D\n" +
                        "xCCFwH+ig/j8i0wkGItUJBR0r4uDsO///4tNDAHBSffYIciD7AT/dRBQi3QkKFbo1Pz//4nwg8QQ\n" +
                        "6Qr////32ItMJAyJAen4/v//kJCQkJCQkJCQkJCQkFWJ5VOD5PCD7DDoAAAAAFuBw5EQAAAPKEUI\n" +
                        "i0UYi00ciUwkFIlEJBAPEQQkx0QkGAAAAADod/3//41l/Ftdw8zvvq/eAAAAABRFEQAAAAAAAAAA\n" +
                        "AAAAAAA=";
        byte[] bytes = android.util.Base64.decode(b64, android.util.Base64.DEFAULT);
        int hookInfoOffset = 0x05f0;
        fillInHookInfo(bytes, hookInfoOffset);
        return bytes;
    }

    @Override
    public int getNativeDebugBreakOffset() {
        return 0x0000;
    }

    @Override
    public int getNativeClearCacheOffset() {
        return 0x00b0;
    }

    @Override
    public int getNativeSyscallOffset() {
        return 0x0070;
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
        return 0x02a0;
    }

    @Override
    public int getFakeMmap64Offset() {
        return 0x0360;
    }

    @Override
    public int getFakeMmapOffset() {
        return 0x05b0;
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
