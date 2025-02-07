package com.example.test.app;

import android.content.Context;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;

import androidx.annotation.NonNull;

import com.tencent.mmkv.MMKV;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import dev.tmpfs.libcoresyscall.core.NativeAccess;
import dev.tmpfs.libcoresyscall.core.NativeHelper;
import dev.tmpfs.libcoresyscall.core.impl.ReflectHelper;
import dev.tmpfs.libcoresyscall.elfloader.DlExtLibraryLoader;

public class TestNativeLoader {

    private TestNativeLoader() {
    }

    private static long sHandle;

    public static String getNativeLibraryDirName(int isa) {
        switch (isa) {
            case NativeHelper.ISA_X86:
                return "x86";
            case NativeHelper.ISA_X86_64:
                return "x86_64";
            case NativeHelper.ISA_ARM:
                // we only support armeabi-v7a, not armeabi
                return "armeabi-v7a";
            case NativeHelper.ISA_ARM64:
                return "arm64-v8a";
            case NativeHelper.ISA_MIPS:
                // not sure, I have never seen a mips device
                return "mips";
            case NativeHelper.ISA_MIPS64:
                // not sure, I have never seen a mips64 device
                return "mips64";
            case NativeHelper.ISA_RISCV64:
                // not sure, I have never seen a riscv64 device
                return "riscv64";
            default:
                throw new IllegalArgumentException("Unsupported ISA: " + isa);
        }
    }

    public static byte[] getElfData(String soname) {
        String path = "/lib/" + getNativeLibraryDirName(NativeHelper.getCurrentRuntimeIsa()) + "/" + soname;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = TestNativeLoader.class.getResourceAsStream(path)) {
            byte[] buf = new byte[4096];
            int len;
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw ReflectHelper.unsafeThrow(e);
        }
    }

    private static FileDescriptor createTempReadOnlyFile(@NonNull Context ctx, @NonNull String name, @NonNull byte[] data) {
        File cacheDir = ctx.getCacheDir();
        File randomDir = new File(cacheDir, "tmpfs-" + System.nanoTime());
        if (!randomDir.mkdirs()) {
            throw new IllegalStateException("Cannot create directory: " + randomDir);
        }
        File file = new File(randomDir, name);
        // write data to file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data);
        } catch (IOException e) {
            throw ReflectHelper.unsafeThrow(e);
        }
        // make the file read-only
        if (!file.setWritable(false, false)) {
            throw new IllegalStateException("Cannot set file permissions: " + file);
        }
        // re-open the file in read-only mode
        FileDescriptor fd;
        try {
            fd = Os.open(file.getAbsolutePath(), OsConstants.O_RDONLY, 0);
        } catch (ErrnoException e) {
            throw ReflectHelper.unsafeThrow(e);
        }
        // delete the file
        if (!file.delete()) {
            throw new IllegalStateException("Cannot delete file: " + file);
        }
        // delete the directory, if empty
        randomDir.delete();
        return fd;
    }

    public static long initialize(@NonNull Context ctx) {
        load();
        if (sHandle != 0) {
            MMKV.initialize(ctx, libName -> {
                // no-op
            });
            return sHandle;
        }
        return 0;
    }

    public static long load() {
        if (sHandle != 0) {
            return sHandle;
        }
        String soname = "libmmkv.so";
        byte[] elfData = getElfData(soname);

        sHandle = DlExtLibraryLoader.dlopenExtFromMemory(elfData, soname, DlExtLibraryLoader.RTLD_NOW, 0, 0);

        if (sHandle != 0) {
            long jniOnLoad = DlExtLibraryLoader.dlsym(sHandle, "JNI_OnLoad");
            if (jniOnLoad != 0) {
                long javaVm = NativeAccess.getJavaVM();
                long ret = NativeAccess.callPointerFunction(jniOnLoad, javaVm, 0);
                if (ret < 0) {
                    throw new IllegalStateException("JNI_OnLoad failed: " + ret);
                }
            }
        }
        return sHandle;
    }

}
