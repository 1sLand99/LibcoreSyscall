package com.example.test.app;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import dev.tmpfs.libcoresyscall.core.impl.ReflectHelper;

public class StandaloneTest {

    // /system/bin/app_process -Xzygote -Djava.class.path=$(pm path com.example.test.app.syscalldemo) / com.example.test.app.StandaloneTest
    // /system/bin/app_process -Xusejit:false -Djava.class.path=$(pm path com.example.test.app.syscalldemo) / com.example.test.app.StandaloneTest

    // avc: denied { execmem } for scontext=u:r:system_server:s0 tcontext=u:r:system_server:s0 tclass=process permissive=0
    // magiskpolicy --live "allow system_server system_server process execmem"

    private StandaloneTest() {
        throw new AssertionError("no instances");
    }

    private static Object theZygoteHooksObject;

    private static void callPreFork() {
        try {
            Class<?> kZygoteHooks = Class.forName("dalvik.system.ZygoteHooks");
            Method preFork = kZygoteHooks.getDeclaredMethod("preFork");
            if (!Modifier.isStatic(preFork.getModifiers())) {
                if (theZygoteHooksObject == null) {
                    theZygoteHooksObject = kZygoteHooks.newInstance();
                }
            }
            preFork.invoke(theZygoteHooksObject);
        } catch (Exception e) {
            throw ReflectHelper.unsafeThrowForIteCause(e);
        }
    }

    private static void callPostForkCommon() {
        try {
            Class<?> kZygoteHooks = Class.forName("dalvik.system.ZygoteHooks");
            Method postForkCommon = kZygoteHooks.getDeclaredMethod("postForkCommon");
            postForkCommon.invoke(theZygoteHooksObject);
        } catch (Exception e) {
            throw ReflectHelper.unsafeThrowForIteCause(e);
        }
    }

    private static void setupSystemServerSeLabel() {
        String current = SELinuxUtils.getCurrentProcessLabel();
        if (!"u:r:su:s0".equals(current) && !"u:r:magisk:s0".equals(current)) {
            throw new IllegalStateException("Not running as u:r:su:s0 or u:r:magisk:s0, but has -Xzygote, current: " + current);
        }
        byte[] target = "u:r:system_server:s0".getBytes();
        int pid = android.os.Process.myPid();
        callPreFork();
        try (FileOutputStream fos = new FileOutputStream("/proc/" + pid + "/attr/current")) {
            fos.write(target);
        } catch (Exception e) {
            throw ReflectHelper.unsafeThrow(e);
        }
        callPostForkCommon();
    }

    private static void setupDefaultUncaughtExceptionHandler() {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.err.println("Uncaught exception in thread: " + t.getName());
            e.printStackTrace(System.err);
            System.exit(1);
        });
    }

    private static String[] getCmdline() {
        // read all cmdline arguments
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (FileInputStream fis = new FileInputStream("/proc/self/cmdline")) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        } catch (Exception e) {
            throw ReflectHelper.unsafeThrow(e);
        }
        ArrayList<String> args = new ArrayList<>();
        // find 0 and split
        byte[] data = baos.toByteArray();
        int start = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {
                args.add(new String(data, start, i - start));
                start = i + 1;
            }
        }
        return args.toArray(new String[0]);
    }

    public static void main(String[] args) {
        setupDefaultUncaughtExceptionHandler();
        String[] cmdline = getCmdline();
        // check is there -Xzygote
        boolean isZygoteMode = false;
        for (String arg : cmdline) {
            if ("-Xzygote".equals(arg)) {
                isZygoteMode = true;
                break;
            }
        }
        if (isZygoteMode) {
            int uid = android.os.Process.myUid();
            String selabel = SELinuxUtils.getCurrentProcessLabel();
            if (uid != 0 || (!"u:r:su:s0".equals(selabel) && !"u:r:magisk:s0".equals(selabel))) {
                throw new IllegalStateException("Not running as root/u:r:su:s0, but has -Xzygote, uid: "
                        + uid + " selabel: " + selabel);
            }
            setupSystemServerSeLabel();
        }
        int pid = android.os.Process.myPid();
        int uid = android.os.Process.myUid();
        String selabel = SELinuxUtils.getCurrentProcessLabel();
        System.out.println("PID: " + pid + " UID: " + uid + " SELinux Label: " + selabel);
        boolean hasExecMem = SELinuxUtils.hasExecMem();
        System.out.println("Has execmem: " + hasExecMem);
        try {
            boolean canMapAshmemExecutable = SELinuxUtils.canMapAshmemAsExec();
            System.out.println("Can map ashmem as executable: " + canMapAshmemExecutable);
        } catch (Exception e) {
            System.out.println("Can map ashmem as executable: " + e);
        }
        try {
            boolean canMapMemfdExecutable = SELinuxUtils.canMapMemfdAsExec();
            System.out.println("Can map memfd as executable: " + canMapMemfdExecutable);
        } catch (Exception e) {
            System.out.println("Can map memfd as executable: " + e);
        }
        long handle = TestNativeLoader.load("libmmkv.so");
        System.out.println("Handle: " + handle);
    }

}
