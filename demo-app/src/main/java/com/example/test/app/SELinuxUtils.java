package com.example.test.app;

import android.os.Build;
import android.os.MemoryFile;
import android.os.SharedMemory;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;

import androidx.annotation.Nullable;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.lang.reflect.Method;

import dev.tmpfs.libcoresyscall.core.Syscall;
import dev.tmpfs.libcoresyscall.core.impl.FileDescriptorHelper;
import dev.tmpfs.libcoresyscall.core.impl.NativeBridge;
import dev.tmpfs.libcoresyscall.core.impl.ReflectHelper;

public class SELinuxUtils {

    private SELinuxUtils() {
        throw new AssertionError("no instances");
    }

    @Nullable
    public static String getCurrentProcessLabel() {
        try (FileInputStream fis = new FileInputStream("/proc/self/attr/current")) {
            byte[] buffer = new byte[256];
            int len = fis.read(buffer);
            // remove trailing '0' if present
            if (len > 0 && buffer[len - 1] == 0) {
                len--;
            }
            return new String(buffer, 0, len).intern();
        } catch (Exception e) {
            return null;
        }
    }

    private static Boolean sHasExecMem = null;

    public static boolean hasExecMem() {
        if (sHasExecMem == null) {
            sHasExecMem = testExecMemInternal();
        }
        return sHasExecMem;
    }

    private static boolean testExecMemInternal() {
        try {
            final int MAP_ANONYMOUS = 0x20;
            final int pageSize = (int) Os.sysconf(OsConstants._SC_PAGESIZE);
            long addr = Os.mmap(0, pageSize, OsConstants.PROT_READ | OsConstants.PROT_WRITE | OsConstants.PROT_EXEC,
                    OsConstants.MAP_PRIVATE | MAP_ANONYMOUS, null, 0);
            Os.munmap(addr, pageSize);
            return true;
        } catch (ErrnoException e) {
            if (e.errno == OsConstants.EACCES) {
                // execmem is not allowed
                return false;
            } else {
                // unexpected error
                throw ReflectHelper.unsafeThrow(e);
            }
        }
    }

    private static Boolean sCanMapAshmemAsExec = null;

    public static boolean canMapAshmemAsExec() {
        if (sCanMapAshmemAsExec == null) {
            sCanMapAshmemAsExec = testMapAshmemAsExecInternal();
        }
        return sCanMapAshmemAsExec;
    }

    private static boolean testMapAshmemAsExecInternal() {
        final int pageSize = (int) NativeBridge.getPageSize();
        // we need an ashmem fd, the implementation is really dirty
        // however, it's not part of the library, and it's only used for testing
        Method native_open;
        try {
            //noinspection JavaReflectionMemberAccess
            native_open = MemoryFile.class.getDeclaredMethod("native_open", String.class, int.class);
            native_open.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw ReflectHelper.unsafeThrow(e);
        }
        FileDescriptor theFdObj;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            try {
                SharedMemory ashmem = SharedMemory.create("ashemm-exec-test", pageSize);
                int peekFd;
                //noinspection JavaReflectionMemberAccess
                Method getFd = SharedMemory.class.getDeclaredMethod("getFd");
                getFd.setAccessible(true);
                //noinspection DataFlowIssue
                peekFd = (int) getFd.invoke(ashmem);
                theFdObj = FileDescriptorHelper.wrap(peekFd);
            } catch (ReflectiveOperationException | ErrnoException e) {
                throw ReflectHelper.unsafeThrow(e);
            }
        } else {
            try {
                theFdObj = (FileDescriptor) native_open.invoke(null, "ashemm-exec-test", pageSize);
            } catch (ReflectiveOperationException e) {
                throw ReflectHelper.unsafeThrow(e);
            }

        }
        if (theFdObj == null) {
            throw new IllegalStateException("native_open failed with null FileDescriptor without exception");
        }
        // check if the ashmem is a real ashmem
        int fdInt = FileDescriptorHelper.getInt(theFdObj);
        String realPath;
        try {
            realPath = Os.readlink("/proc/self/fd/" + fdInt);
        } catch (ErrnoException e) {
            throw ReflectHelper.unsafeThrow(e);
        }
        if (!realPath.startsWith("/dev/ashmem")) {
            throw new IllegalStateException("ashmem fd is not a real ashmem: " + realPath);
        }
        boolean canMapAsExecShared;
        boolean canMapAsExecPrivate;
        // now we can mmap it as exec, test map shared and private respectively
        try {
            long addr = Os.mmap(0, pageSize, OsConstants.PROT_READ | OsConstants.PROT_WRITE | OsConstants.PROT_EXEC,
                    OsConstants.MAP_SHARED, theFdObj, 0);
            canMapAsExecShared = true;
            Os.munmap(addr, pageSize);
        } catch (ErrnoException e) {
            if (e.errno == OsConstants.EACCES) {
                canMapAsExecShared = false;
            } else {
                throw ReflectHelper.unsafeThrow(e);
            }
        }
        try {
            long addr = Os.mmap(0, pageSize, OsConstants.PROT_READ | OsConstants.PROT_WRITE | OsConstants.PROT_EXEC,
                    OsConstants.MAP_PRIVATE, theFdObj, 0);
            canMapAsExecPrivate = true;
            Os.munmap(addr, pageSize);
        } catch (ErrnoException e) {
            if (e.errno == OsConstants.EACCES) {
                canMapAsExecPrivate = false;
            } else {
                throw ReflectHelper.unsafeThrow(e);
            }
        }
        if (canMapAsExecShared != canMapAsExecPrivate) {
            throw new IllegalStateException("canMapAsExecShared != canMapAsExecPrivate, shared: " + canMapAsExecShared + ", private: " + canMapAsExecPrivate);
        }
        return canMapAsExecShared;
    }

    private static Boolean sCanMapMemfdAsExec = null;

    public static boolean canMapMemfdAsExec() {
        if (sCanMapMemfdAsExec == null) {
            sCanMapMemfdAsExec = testMapMemfdAsExecInternal();
        }
        return sCanMapMemfdAsExec;
    }

    private static boolean testMapMemfdAsExecInternal() {
        final int pageSize = (int) NativeBridge.getPageSize();
        // we need a memfd
        int memfd;
        try {
            memfd = Syscall.memfd_create("memfd-exec-test", 0);
        } catch (ErrnoException e) {
            throw ReflectHelper.unsafeThrow(e);
        }
        FileDescriptor fdObj = FileDescriptorHelper.wrap(memfd);
        boolean canMapAsExec;
        // now we can mmap it as exec
        try {
            long addr = Os.mmap(0, pageSize, OsConstants.PROT_READ | OsConstants.PROT_WRITE | OsConstants.PROT_EXEC,
                    OsConstants.MAP_PRIVATE, fdObj, 0);
            canMapAsExec = true;
            Os.munmap(addr, pageSize);
        } catch (ErrnoException e) {
            if (e.errno == OsConstants.EACCES) {
                canMapAsExec = false;
            } else {
                throw ReflectHelper.unsafeThrow(e);
            }
        }
        return canMapAsExec;
    }

}
