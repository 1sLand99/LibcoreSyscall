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
        //0000 g       .text  0000 ___text_dummy
        //0000 g     F .text  0014 NativeBridge_breakpoint
        //0020 g     F .text  0013 __clear_cache
        //0040 g     F .text  007a syscall_ext
        //00c0 g     F .text  0110 NativeBridge_nativeSyscall
        //01d0 g     F .text  005c NativeBridge_nativeClearCache
        //0230 g     F .text  0035 NativeBridge_nativeCallPointerFunction0
        //0270 g     F .text  0044 NativeBridge_nativeCallPointerFunction1
        //02c0 g     F .text  0052 NativeBridge_nativeCallPointerFunction2
        //0320 g     F .text  0060 NativeBridge_nativeCallPointerFunction3
        //0380 g     F .text  0074 NativeBridge_nativeCallPointerFunction4
        //0400 g     F .text  007b NativeBridge_nativeGetJavaVM
        //0480 g     F .text  001c get_hook_info
        //04a0 g     F .text  006c ashmem_dev_get_size_region
        //0510 g     F .text  0092 lsw_pread64
        //05b0 g     F .text  006a lsw_mprotect
        //0620 g     F .text  01b7 fake_fstat64
        //07e0 g     F .text  04b5 fake_mmap64
        //0d80  w    F .text  0155 memset
        //0d00 g     F .text  0079 fake_mmap
        //0ee0 l     O .text  0018 get_hook_info.sHookInfo
        //0ca0 l     F .text  0027 align_up
        //0cd0 l     F .text  0021 align_down
        String b64 =
                "VYnlg+T8g+wIi0UMi0UIzInsXcOQkJCQkJCQkJCQkJBVieWD5PyD7AiLRQyLRQiJ7F3DkJCQkJCQ\n" +
                        "kJCQkJCQkFWJ5VdWg+T8g+wgi0Ugi0Uci0UYi0UUi0UQi0UMi0UIi0UIiUQkHItEJByJRCQYi0UM\n" +
                        "iUQkCItFIIlEJAyLRCQYiUQkEI1EJAiLTRCLVRSLdRiLfRxVU4toBIsYi0AIzYBbXYlEJBSLRCQU\n" +
                        "iUQkBItEJASNZfheX13DkJCQkJCQVYnlU1dWg+TwgeyAAAAA6AAAAABbgcNsHgAAi0U8iUQkOItN\n" +
                        "QItVNIt1OIt9LItFMIlEJDSLRSSJRCQwi0UoiUQkLItFHIlEJCiLRSCJRCQki0UUiUQkHItFGIlE\n" +
                        "JCCLRRCLRQyLRQiLRCQciUQkcItEJCCJRCR0i0QkJIlEJGyLRCQoiUQkaItEJCyJRCRki0QkMIlE\n" +
                        "JGCLRCQ0iUQkXItEJDiJfCRYiXQkVIlUJFCJTCRMiUQkSItFEIlEJESLVCRwi3QkaIt8JGCLRCRY\n" +
                        "iUQkQItEJFCJRCQ8i0wkSIngiUgYi0wkPIlIFItMJECJSBCLTCREiXgMiXAIiVAEiQjoev7//zHS\n" +
                        "jWX0Xl9bXcNVieVTg+Twg+wg6AAAAABbgcNhHQAAi0UYi0Uci0UQi0UUi0UMi0UIi0UQiUQkEItF\n" +
                        "GIlEJAyLRCQQA0QkDIlEJAiLTCQQi0QkCIkMJIlEJATo+v3//41l/Ftdw5CQkJBVieVTg+Twg+wQ\n" +
                        "6AAAAABbgcMBHQAAi0UQi0UUi0UMi0UIi0UQiQQkiwQk/9Ax0o1l/Ftdw5CQkJCQkJCQkJCQVYnl\n" +
                        "U4Pk8IPsIOgAAAAAW4HDwRwAAItFGItFHItFEItFFItFDItFCItFEIlEJBCLRCQQi1UYieGJEf/Q\n" +
                        "MdKNZfxbXcOQkJCQkJCQkJCQkJBVieVTVoPk8IPsEOgAAAAAW4HDcBwAAItFIItFJItFGItFHItF\n" +
                        "EItFFItFDItFCItFEIlEJAyLRCQMi1UYi3UgieGJcQSJEf/QMdKNZfheW13DkJCQkJCQkJCQkJCQ\n" +
                        "kJBVieVTV1aD5PCD7CDoAAAAAFuBww8cAACLRSiLRSyLRSCLRSSLRRiLRRyLRRCLRRSLRQyLRQiL\n" +
                        "RRCJRCQYi0QkGItVGIt1IIt9KInhiXkIiXEEiRH/0DHSjWX0Xl9bXcNVieVTV1aD5PCD7CDoAAAA\n" +
                        "AFuBw68bAACLRTCLRTSLRSiLRSyLRSCLRSSLRRiLRRyLRRCLRRSLRQyLRQiLRRCJRCQYi0QkGIlE\n" +
                        "JBSLVRiLdSCLfSiLRTCJ4YlBDItEJBSJeQiJcQSJEf/QMdKNZfReX1tdw5CQkJCQkJCQkJCQkFWJ\n" +
                        "5VOD5PCD7CDoAAAAAFuBwzEbAACLRQyLRQjHRCQIAAAAAItFCIsAi4BsAwAAi1UIjUwkCIkUJIlM\n" +
                        "JAT/0IP4AA+FFQAAAItEJAiJRCQMx0QkEAAAAADpEAAAAMdEJBAAAAAAx0QkDAAAAACLRCQMi1Qk\n" +
                        "EI1l/Ftdw5CQkJCQVYnlg+T86AAAAABYgcC1GgAAjYCg7///iexdw5CQkJBVieVTg+Twg+ww6AAA\n" +
                        "AABbgcORGgAAi0UIx0QkIDYAAACLTCQgi0UIMdKJDCSJRCQEx0QkCAR3AADHRCQMAAAAAMdEJBAA\n" +
                        "AAAAx0QkFAAAAADHRCQYAAAAAOhC+///iUQkHItEJByNZfxbXcOQkJCQVYnlU1dWg+Twg+xA6AAA\n" +
                        "AABbgcMfGgAAi00Ui0UYi1UQi1UMi1UIiUwkMIlEJDTHRCQstAAAAItEJDSJRCQoi0QkMIlEJCSL\n" +
                        "fQiLdQyLVRCLTCQki0QkKIlEJCAxwItEJCDHBCS0AAAAiXwkBIl0JAiJVCQMiUwkEIlEJBTHRCQY\n" +
                        "AAAAAOim+v//jWX0Xl9bXcOQkJCQkJCQkJCQkJCQkFWJ5VNWg+Twg+wg6AAAAABbgcOAGQAAi0UQ\n" +
                        "i0UMi0UIx0QkHH0AAACLVQiLTQyLRRAx9scEJH0AAACJVCQEiUwkCIlEJAzHRCQQAAAAAMdEJBQA\n" +
                        "AAAAx0QkGAAAAADoLfr//41l+F5bXcOQkJCQkJBVieVTVoPk8IPsYOgAAAAAW4HDEBkAAIlcJCyL\n" +
                        "RQyLRQjoOv7//4tcJCyJRCRIi0UMiUQkRMdEJEDFAAAAi00Ii0QkRDHSxwQkxQAAAIlMJASJRCQI\n" +
                        "x0QkDAAAAADHRCQQAAAAAMdEJBQAAAAAx0QkGAAAAADopvn//4lEJDyLRCQ8iUQkXItEJFyJRCRY\n" +
                        "McCDfCRYAIhEJDMPjQ8AAACBfCRYAfD//w+dwIhEJDOKRCQzJAEPtsCD+AAPhCgAAACLXCQsi0Qk\n" +
                        "SP9QCIlEJDgxyStMJDyLRCQ4iQjHRCRM/////+nEAAAAi0wkRIsBi0kEi3QkSIsWi3YEMfEx0AnI\n" +
                        "D4WeAAAA6QAAAACLTCRIiwGLSQQJyA+EiAAAAOkAAAAAi0wkRItBLItJMAnID4VxAAAA6QAAAACL\n" +
                        "XCQsi0UIiQQk6Dn9//+JRCQ0i0QkNIlEJFSLRCRUiUQkUDHAg3wkUACIRCQrD40PAAAAgXwkUAHw\n" +
                        "//8PncCIRCQrikQkKyQBD7bAg/gAD4USAAAAi0wkNItEJESJSCzHQDAAAAAA6QAAAADHRCRMAAAA\n" +
                        "AItEJEyNZfheW13DkJCQkJCQkJCQVYnlU1dWg+TwgewAAQAA6AAAAABbgcNMFwAAiVwkOItNHItF\n" +
                        "IItVGItVFItVEItVDItVCImMJOgAAACJhCTsAAAA6Fn8//+JhCTkAAAAx4Qk4AAAAP/////HhCTc\n" +
                        "AAAAAAAAAMeEJNgAAAAAAAAAx4Qk1AAAAAAAAADHhCTQAAAAAAAAAItFEImEJMwAAACDfRgAD4zi\n" +
                        "AAAAi0UUg+ACg/gAD4TTAAAAi0UUg+Agg/gAD4XEAAAAi1wkOI1EJGwxyYkEJMdEJAQAAAAAx0Qk\n" +
                        "CGAAAADoxwQAAItcJDiLTRiNRCRsiQwkiUQkBOhQ/f//g/gAD4V9AAAAi0QkbItMJHCLtCTkAAAA\n" +
                        "ixaLdgQx8THQCcgPhVgAAADpAAAAAIuMJOQAAACLAYtJBAnID4Q/AAAA6QAAAACLRRiJhCTgAAAA\n" +
                        "8g8QhCToAAAA8g8RhCTYAAAAi0UMiYQk0AAAAMeEJNQAAAAAAAAAi0UQg8gDiUUQ6QAAAADpAAAA\n" +
                        "AItcJDiLhCTkAAAAi0AI/9CJRCRoD7eEJOgAAACp/w8AAA+EIAAAAOkAAAAAi0QkaMcAFgAAALj/\n" +
                        "////iYQk8AAAAOnkAgAAi4Qk7AAAAKkA8P//D4QgAAAA6QAAAACLRCRoxwAWAAAAuP////+JhCTw\n" +
                        "AAAA6bICAACLXQiLfQyLdRCLVRSLRRiJRCQwi4wk6AAAAIuEJOwAAAAPpMgUi0wkMMcEJMAAAACJ\n" +
                        "XCQEi1wkOIl8JAiJdCQMiVQkEIlMJBSJRCQY6Bv2//+JRCRki0QkZImEJPgAAACLhCT4AAAAiYQk\n" +
                        "9AAAADHAg7wk9AAAAACIRCQ3D40SAAAAgbwk9AAAAAHw//8PncCIRCQ3ikQkNyQBD7bAg/gAD4Qd\n" +
                        "AAAAMckrTCRki0QkaIkIuP////+JhCTwAAAA6e8BAACDvCTgAAAA/w+E0QEAAPIPEIQk2AAAAPIP\n" +
                        "EUQkWPIPEIQk0AAAAPIPEUQkUItEJGSJRCRMi0QkUItMJFQJyA+E3gAAAOkAAAAAi0wkUItEJFSB\n" +
                        "6QEQAACD2AAPghMAAADpAAAAALgAEAAAiUQkLOkIAAAAi0QkUIlEJCyLRCQsiUQkSIuMJOAAAACL\n" +
                        "VCRMi3QkSIt8JFiLXCRcieCJWBCLXCQ4iXgMiXAIiVAEiQjoxvn//4lEJESDfCREAA+PFQAAAIN8\n" +
                        "JET8D4UFAAAA6WL////pSwAAAIt0JESJ8sH6H4tMJFCLRCRUKfEZ0IlMJFCJRCRUi3QkRInywfof\n" +
                        "i0wkWItEJFwB8RHQiUwkWIlEJFyLRCRMA0QkRIlEJEzpEv///4tcJDiLRCRkiUQkKItNDIuEJOQA\n" +
                        "AACLQBCJDCSJRCQE6MIAAACLXCQ4i1QkKInBi4QkzAAAAIkUJIlMJASJRCQI6LH5//+LhCTMAAAA\n" +
                        "g+AEg/gAD4RfAAAAi1wkOItMJGSLhCTkAAAAi0AQiQwkiUQkBOigAAAAi1wkOIlEJECLTCRkA00M\n" +
                        "i4Qk5AAAAItAEIkMJIlEJAToSwAAAItcJDiJRCQ8i0wkQItEJDyJDCSJRCQE6K/z///pAAAAAOkA\n" +
                        "AAAAi0QkZImEJPAAAACLhCTwAAAAjWX0Xl9bXcOQkJCQkJCQkJCQkFWJ5YPk/IPsCItFDItFCItF\n" +
                        "CANFDIPoAYtNDIPpAYPx/yHIiexdw5CQkJCQkJCQkFWJ5YPk/IPsCItFDItFCItFCItNDIPpAYPx\n" +
                        "/yHIiexdw5CQkJCQkJCQkJCQkJCQkFWJ5VNXVoPk8IPsMOgAAAAAW4HDLxIAAItFHItFGItFFItF\n" +
                        "EItFDItFCItFCIlEJCiLVQyLdRCLfRSLRRiJRCQki0UciUQkIDHJieCJSBiLTCQgiUgUi0wkJIlI\n" +
                        "EItMJCiJeAyJcAiJUASJCOhv+v//jWX0Xl9bXcOQkJCQkJCQVYnlg+T8g+wYi0UQi0UMi0UIi0UI\n" +
                        "iUQkEIN9EAAPhQwAAACLRQiJRCQU6R4BAACLRQyIwYtEJBCICItFDIjCi0QkEItNEIPpAYgUCIN9\n" +
                        "EAIPhwwAAACLRQiJRCQU6esAAACLRQyIwYtEJBCISAGLRQyIwYtEJBCISAKLRQyIwotEJBCLTRCD\n" +
                        "6QKIFAiLRQyIwotEJBCLTRCD6QOIFAiDfRAGD4cMAAAAi0UIiUQkFOmZAAAAi0UMiMGLRCQQiEgD\n" +
                        "i0UMiMKLRCQQi00Qg+kEiBQIg30QCA+HDAAAAItFCIlEJBTpZQAAAItMJBAxwCnIg+ADiUQkDItE\n" +
                        "JAwDRCQQiUQkEItMJAyLRRApyIlFEItFEIPg/IlFEIN9EAAPhCQAAACLRQyIwYtEJBCICItFEIPA\n" +
                        "/4lFEItEJBCDwAGJRCQQ6dL///+LRQiJRCQUi0QkFInsXcPMzMzMzMzMzMzMzO++r94AAAAAFEUR\n" +
                        "AAAAAAAAEAAAAAAAAA==";
        byte[] bytes = android.util.Base64.decode(b64, android.util.Base64.DEFAULT);
        int hookInfoOffset = 0x0ee0;
        fillInHookInfo(bytes, hookInfoOffset);
        return bytes;
    }

    @Override
    public int getNativeDebugBreakOffset() {
        return 0x0000;
    }

    @Override
    public int getNativeClearCacheOffset() {
        return 0x01d0;
    }

    @Override
    public int getNativeSyscallOffset() {
        return 0x00c0;
    }

    @Override
    public int getNativeCallPointerFunction0Offset() {
        return 0x0230;
    }

    @Override
    public int getNativeCallPointerFunction1Offset() {
        return 0x0270;
    }

    @Override
    public int getNativeCallPointerFunction2Offset() {
        return 0x02c0;
    }

    @Override
    public int getNativeCallPointerFunction3Offset() {
        return 0x0320;
    }

    @Override
    public int getNativeCallPointerFunction4Offset() {
        return 0x0380;
    }

    @Override
    public int getNativeGetJavaVmOffset() {
        return 0x0400;
    }

    @Override
    public int getFakeStat64Offset() {
        return 0x0620;
    }

    @Override
    public int getFakeMmap64Offset() {
        return 0x07e0;
    }

    @Override
    public int getFakeMmapOffset() {
        return 0x0d00;
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
