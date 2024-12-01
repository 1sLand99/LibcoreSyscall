package com.example.test.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tencent.mmkv.MMKV;

import java.io.FileInputStream;

import dev.tmpfs.libcoresyscall.core.IAllocatedMemory;
import dev.tmpfs.libcoresyscall.core.MemoryAccess;
import dev.tmpfs.libcoresyscall.core.MemoryAllocator;
import dev.tmpfs.libcoresyscall.core.NativeHelper;
import dev.tmpfs.libcoresyscall.core.Syscall;
import dev.tmpfs.libcoresyscall.core.impl.ReflectHelper;
import dev.tmpfs.libcoresyscall.core.impl.trampoline.CommonSyscallNumberTables;

public class TestMainActivity extends Activity {

    private TextView mTestTextView;

    private static final boolean WAIT_FOR_JAVA_DEBUGGER = false;
    private static final boolean WAIT_FOR_NATIVE_DEBUGGER = false;

    private int testCount = 0;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTestTextView = new TextView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.addView(mTestTextView);
        float dp8 = getResources().getDisplayMetrics().density * 8;
        linearLayout.setPadding((int) dp8, (int) dp8, (int) dp8, (int) dp8);
        ScrollView scrollView = new ScrollView(this);
        scrollView.setFitsSystemWindows(true);
        scrollView.addView(linearLayout);
        setContentView(scrollView);
        mTestTextView.setTextIsSelectable(true);
        mTestTextView.setFocusable(true);
        mTestTextView.setMovementMethod(new LinkMovementMethod());
        new Thread(() -> {
            // wait every thing is ready
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            // enable debug info
            // DlExtLibraryLoader.setLdDebugVerbosity(3);
            performTestsAsync();
        }).start();
    }

    @SuppressLint("SetTextI18n")
    private void performTestsAsync() {
        new Thread(() -> {
            if (WAIT_FOR_NATIVE_DEBUGGER) {
                runOnUiThread(() -> mTestTextView.setText("Waiting for native debugger...\nPID = " + Os.getpid()));
                DebugUtils.waitForNativeDebugger();
            }
            if (WAIT_FOR_JAVA_DEBUGGER) {
                runOnUiThread(() -> mTestTextView.setText("Waiting for Java debugger..."));
                Debug.waitForDebugger();
            }
            String result;
            try {
                result = runTests();
            } catch (Throwable e) {
                result = Log.getStackTraceString(e);
            }
            updateStatus(result);
        }).start();
    }

    private void updateStatus(String status) {
        SpannableStringBuilder builder = new SpannableStringBuilder(status);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                performTestsAsync();
            }
        };
        builder.append("\n\n");
        builder.append("Click here to run tests again", clickableSpan, 0);
        runOnUiThread(() -> {
            mTestTextView.setText(builder);
        });
    }

    private String runTests() {
        StringBuilder sb = new StringBuilder();
        sb.append("Test ").append(++testCount);
        sb.append("\n");
        sb.append("ISA = ").append(NativeHelper.getIsaName(NativeHelper.getCurrentRuntimeIsa()));
        sb.append("\n");
        sb.append("SDK_INT = ");
        if ("REL".equals(Build.VERSION.CODENAME)) {
            sb.append(Build.VERSION.SDK_INT);
        } else {
            // dev preview
            sb.append(Build.VERSION.CODENAME).append(" ").append(Build.VERSION.SDK_INT);
        }
        sb.append("\n");
        sb.append("Page size = ").append(Os.sysconf(OsConstants._SC_PAGESIZE));
        sb.append("\n");
        sb.append("PID = ").append(Os.getpid()).append(", PPID = ").append(Os.getppid()).append(", UID = ").append(Os.getuid());
        sb.append("\n");
        int __NR_uname;
        switch (NativeHelper.getCurrentRuntimeIsa()) {
            case NativeHelper.ISA_X86_64:
                __NR_uname = 63;
                break;
            case NativeHelper.ISA_ARM64:
            case NativeHelper.ISA_RISCV64:
                __NR_uname = 160;
                break;
            case NativeHelper.ISA_X86:
            case NativeHelper.ISA_ARM:
                __NR_uname = 122;
                break;
            default:
                // just for demo purpose. I don't want to search for the correct value for other ISAs.
                throw new IllegalStateException("Unexpected value: " + NativeHelper.getCurrentRuntimeIsa());
        }
        ///** The maximum length of any field in `struct utsname`. */
        //#define SYS_NMLN 65
        ///** The information returned by uname(). */
        //struct utsname {
        //  /** The OS name. "Linux" on Android. */
        //  char sysname[SYS_NMLN];
        //  /** The name on the network. Typically "localhost" on Android. */
        //  char nodename[SYS_NMLN];
        //  /** The OS release. Typically something like "4.4.115-g442ad7fba0d" on Android. */
        //  char release[SYS_NMLN];
        //  /** The OS version. Typically something like "#1 SMP PREEMPT" on Android. */
        //  char version[SYS_NMLN];
        //  /** The hardware architecture. Typically "aarch64" on Android. */
        //  char machine[SYS_NMLN];
        //  /** The domain name set by setdomainname(). Typically "localdomain" on Android. */
        //  char domainname[SYS_NMLN];
        //};
        int sysnameOffset = 0;
        int nodenameOffset = 65;
        int releaseOffset = 65 * 2;
        int versionOffset = 65 * 3;
        int machineOffset = 65 * 4;
        int domainnameOffset = 65 * 5;
        int utsSize = 65 * 6;
        try (IAllocatedMemory uts = MemoryAllocator.allocate(utsSize, true)) {
            long utsAddress = uts.getAddress();
            Syscall.syscall(__NR_uname, utsAddress);
            sb.append("sysname = ").append(MemoryAccess.peekCString(utsAddress + sysnameOffset));
            sb.append("\n");
            sb.append("nodename = ").append(MemoryAccess.peekCString(utsAddress + nodenameOffset));
            sb.append("\n");
            sb.append("release = ").append(MemoryAccess.peekCString(utsAddress + releaseOffset));
            sb.append("\n");
            sb.append("version = ").append(MemoryAccess.peekCString(utsAddress + versionOffset));
            sb.append("\n");
            sb.append("machine = ").append(MemoryAccess.peekCString(utsAddress + machineOffset));
            sb.append("\n");
            sb.append("domainname = ").append(MemoryAccess.peekCString(utsAddress + domainnameOffset));
            sb.append("\n");
            sb.append("Native load test: \n");
            sb.append("handle = ").append(TestNativeLoader.load(this));
            sb.append("\n");
            sb.append("MMKV.version = ").append(MMKV.version());
        } catch (Exception | LinkageError | AssertionError e) {
            sb.append('\n').append("FAIL: \n").append(Log.getStackTraceString(e));
            Log.e("TestMainActivity", "runTests", e);
        }
        sb.append("\n");
        sb.append("cat /proc/self/maps | grep libmmkv.so\n");
        sb.append(catProcSelfMapsAndGrep("libmmkv.so"));
        return sb.toString();
    }

    private static void testSyscallConsistency() {
        int SIGABRT = 6;
        int NR_tgkill = CommonSyscallNumberTables.get().__NR_tgkill();
        int tid = Os.gettid();
        int pid = Os.getpid();
        String msg;
        long[] args = new long[]{pid, tid, SIGABRT, 0x33333333, 0x44444444, 0x55555555};
        if (NativeHelper.isCurrentRuntime64Bit()) {
            msg = String.format("a0 = %016x, a1 = %016x, a2 = %016x, \na3 = %016x, a4 = %016x, a5 = %016x",
                    args[0], args[1], args[2], args[3], args[4], args[5]);
        } else {
            msg = String.format("a0 = %08x, a1 = %08x, a2 = %08x, \na3 = %08x, a4 = %08x, a5 = %08x",
                    args[0], args[1], args[2], args[3], args[4], args[5]);
        }
        Log.e("TestMainActivity", "testSyscallConsistency: \n" + msg);
        try {
            // check whether syscall arguments are filled in the correct registers
            Syscall.syscall(NR_tgkill, args);
            // go to logcat and check the crashdump for the registers
        } catch (ErrnoException e) {
            throw ReflectHelper.unsafeThrow(e);
        }
        throw new AssertionError("tgkill should not return");
    }

    private static String catProcSelfMapsAndGrep(String grep) {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = new FileInputStream("/proc/self/maps");
             java.util.Scanner scanner = new java.util.Scanner(fis)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(grep)) {
                    sb.append(line).append('\n');
                }
            }
        } catch (Exception e) {
            sb.append(Log.getStackTraceString(e));
        }
        return sb.toString();
    }

}
