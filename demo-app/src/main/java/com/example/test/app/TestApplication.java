package com.example.test.app;

import android.app.Application;

import dev.tmpfs.libcoresyscall.core.Syscall;
import dev.tmpfs.libcoresyscall.core.impl.ReflectHelper;

public class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // initialize your application here
        try {
            Class.forName(Syscall.class.getName(), true, Syscall.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw ReflectHelper.unsafeThrow(e);
        }
    }

}
