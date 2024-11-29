package com.example.test.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import dev.tmpfs.libcoresyscall.core.impl.ReflectHelper;

public class DebugUtils {

    private DebugUtils() {
        throw new AssertionError();
    }

    public static boolean isNativeDebuggerAttached() throws IOException {
        File file = new File("/proc/self/status");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("TracerPid:")) {
                    int pid = Integer.parseInt(line.substring(10).trim());
                    return pid != 0;
                }
            }
            throw new IOException("TracerPid not found");
        }
    }

    public static void waitForNativeDebugger() {
        try {
            while (!isNativeDebuggerAttached()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        } catch (IOException e) {
            throw ReflectHelper.unsafeThrow(e);
        }
    }

}
