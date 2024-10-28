package dev.tmpfs.libcoresyscall.core.impl;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Objects;

import libcore.io.Memory;
import sun.misc.Unsafe;

public class ArtMethodHelper {

    private ArtMethodHelper() {
        throw new AssertionError("no instances");
    }

    public static long getPointerSize() {
        return NativeHelper.isCurrentRuntime64Bit() ? 8 : 4;
    }

    private static Unsafe sUnsafe = null;

    private static Unsafe getUnsafe() {
        if (sUnsafe == null) {
            try {
                @SuppressLint("DiscouragedPrivateApi")
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                sUnsafe = (Unsafe) field.get(null);
            } catch (ReflectiveOperationException e) {
                throw ReflectHelper.unsafeThrow(e);
            }
        }
        return sUnsafe;
    }

    @RequiresApi(23)
    private static long getArtMethodFromReflectedMethodForApi23To25(@NonNull Member method) {
        try {
            Class<?> kAbstractMethod = Class.forName("java.lang.reflect.AbstractMethod");
            Field artMethod = kAbstractMethod.getDeclaredField("artMethod");
            artMethod.setAccessible(true);
            return (long) Objects.requireNonNull(artMethod.get(method));
        } catch (ReflectiveOperationException e) {
            throw ReflectHelper.unsafeThrow(e);
        }
    }

    @RequiresApi(26)
    private static long getArtMethodFromReflectedMethodAboveApi26(@NonNull Member method) {
        try {
            // Ljava/lang/reflect/Executable;->artMethod:J,unsupported
            //noinspection JavaReflectionMemberAccess
            Field artMethod = Executable.class.getDeclaredField("artMethod");
            artMethod.setAccessible(true);
            return (long) Objects.requireNonNull(artMethod.get(method));
        } catch (ReflectiveOperationException e) {
            throw ReflectHelper.unsafeThrow(e);
        }
    }

    /**
     * Get the ArtMethod from a reflected method or constructor.
     *
     * @param method method or constructor
     * @return the ArtMethod address
     */
    @RequiresApi(22)
    private static long getArtMethodFromReflectedMethod(@NonNull Member method) {
        if (Build.VERSION.SDK_INT >= 26) {
            return getArtMethodFromReflectedMethodAboveApi26(method);
        } else if (Build.VERSION.SDK_INT >= 23) {
            return getArtMethodFromReflectedMethodForApi23To25(method);
        } else {
            throw new UnsupportedOperationException("unsupported API: " + Build.VERSION.SDK_INT);
        }
    }

    @RequiresApi(22)
    private static Object getArtMethodObjectForSdk22(@NonNull Member method) {
        try {
            Class<?> kArtMethod = Class.forName("java.lang.reflect.ArtMethod");
            Class<?> kAbstractMethod = Class.forName("java.lang.reflect.AbstractMethod");
            Field artMethod = kAbstractMethod.getDeclaredField("artMethod");
            artMethod.setAccessible(true);
            return kArtMethod.cast(artMethod.get(method));
        } catch (ReflectiveOperationException e) {
            throw ReflectHelper.unsafeThrow(e);
        }
    }

    /**
     * Set the entry point of the ArtMethod from JNI for API 21.
     *
     * @param method     method or constructor
     * @param entryPoint entry point
     */
    private static void setArtMethodEntryPointFromJniForApi21(@NonNull Member method, long entryPoint) {
        try {
            Class<?> KArtMethod = Class.forName("java.lang.reflect.ArtMethod");
            Field entryPointFromJni = KArtMethod.getDeclaredField("entryPointFromJni");
            entryPointFromJni.setAccessible(true);
            Class<?> kAbstractMethod = Class.forName("java.lang.reflect.AbstractMethod");
            Field artMethod = kAbstractMethod.getDeclaredField("artMethod");
            artMethod.setAccessible(true);
            Object artMethodObj = artMethod.get(method);
            entryPointFromJni.set(artMethodObj, entryPoint);
        } catch (ReflectiveOperationException e) {
            throw ReflectHelper.unsafeThrow(e);
        }
    }

    private static long sArtMethodNativeEntryPointOffset = 0;

    @RequiresApi(22)
    public static long getArtMethodNativeEntryPointOffsetAboveApi22() {
        if (sArtMethodNativeEntryPointOffset != 0) {
            return sArtMethodNativeEntryPointOffset;
        }
        if (Build.VERSION.SDK_INT == 22) {
            // get ArtMethod.methodIndex offset
            try {
                Class<?> kArtMethod = Class.forName("java.lang.reflect.ArtMethod");
                Field methodIndex = kArtMethod.getDeclaredField("methodIndex");
                long methodIndexOffset = getUnsafe().objectFieldOffset(methodIndex);
                long entryPointFromInterpreterOffset = methodIndexOffset + 4;
                if (NativeHelper.isCurrentRuntime64Bit()) {
                    // align to 8 bytes
                    entryPointFromInterpreterOffset = (entryPointFromInterpreterOffset + 7) & ~7;
                }
                // next field is entryPointFromJni
                sArtMethodNativeEntryPointOffset = entryPointFromInterpreterOffset + getPointerSize();
            } catch (ReflectiveOperationException e) {
                throw ReflectHelper.unsafeThrow(e);
            }
        } else {
            // For Android 6.0+/SDK23+, ArtMethod is no longer a mirror object.
            // We need to calculate the offset of the art::ArtMethod::entry_point_from_jni_ field.
            // See https://github.com/canyie/pine/blob/master/core/src/main/cpp/art/art_method.h
            boolean is64Bit = NativeHelper.isCurrentRuntime64Bit();
            switch (Build.VERSION.SDK_INT) {
                case Build.VERSION_CODES.M:
                    sArtMethodNativeEntryPointOffset = is64Bit ? 40 : 32;
                    break;
                case Build.VERSION_CODES.N:
                case Build.VERSION_CODES.N_MR1:
                    sArtMethodNativeEntryPointOffset = is64Bit ? 40 : 28;
                    break;
                case Build.VERSION_CODES.O:
                case Build.VERSION_CODES.O_MR1:
                    sArtMethodNativeEntryPointOffset = is64Bit ? 32 : 24;
                    break;
                case Build.VERSION_CODES.P:
                case Build.VERSION_CODES.Q:
                case Build.VERSION_CODES.R:
                    sArtMethodNativeEntryPointOffset = is64Bit ? 24 : 20;
                    break;
                case Build.VERSION_CODES.S:
                case Build.VERSION_CODES.S_V2:
                case Build.VERSION_CODES.TIRAMISU:
                case Build.VERSION_CODES.UPSIDE_DOWN_CAKE:
                case 35:
                    sArtMethodNativeEntryPointOffset = 16;
                    break;
                default:
                    // use last/latest known offset
                    sArtMethodNativeEntryPointOffset = 16;
                    break;
            }
        }
        return sArtMethodNativeEntryPointOffset;
    }

    public static void registerNativeMethod(@NonNull Member method, long address) {
        if (!(method instanceof Method) && !(method instanceof Constructor)) {
            throw new IllegalArgumentException("method must be a method or constructor");
        }
        if (address == 0) {
            throw new IllegalArgumentException("address must not be 0");
        }
        int modifiers = method.getModifiers();
        if (!Modifier.isNative(modifiers)) {
            throw new IllegalArgumentException("method must be native: " + method);
        }
        if (!NativeHelper.isCurrentRuntime64Bit()) {
            // check overflow
            if (address != ((long) (int) address)) {
                throw new IllegalArgumentException("address overflow: " + address);
            }
        }
        try {
            Class<?> declaringClass = method.getDeclaringClass();
            // JNI specification says that the class needs to be initialized before the native method is registered
            Class.forName(declaringClass.getName(), true, declaringClass.getClassLoader());
        } catch (ClassNotFoundException e) {
            // should not happen
            throw ReflectHelper.unsafeThrow(e);
        }
        if (Build.VERSION.SDK_INT == 21) {
            setArtMethodEntryPointFromJniForApi21(method, address);
        } else if (Build.VERSION.SDK_INT == 22) {
            Object artMethod = getArtMethodObjectForSdk22(method);
            if (artMethod == null) {
                throw new IllegalArgumentException("unable to get ArtMethod from " + method);
            }
            long offset = getArtMethodNativeEntryPointOffsetAboveApi22();
            if (offset == 0) {
                throw new IllegalStateException("unable to get ArtMethod::entry_point_from_jni_ offset");
            }
            if (NativeHelper.isCurrentRuntime64Bit()) {
                getUnsafe().putLong(artMethod, offset, address);
            } else {
                getUnsafe().putInt(artMethod, offset, (int) address);
            }
        } else {
            // for API 23 and above
            long artMethod = getArtMethodFromReflectedMethod(method);
            if (artMethod == 0) {
                throw new IllegalArgumentException("unable to get ArtMethod from " + method);
            }
            long offset = getArtMethodNativeEntryPointOffsetAboveApi22();
            if (offset == 0) {
                throw new IllegalStateException("unable to get ArtMethod::entry_point_from_jni_ offset");
            }
            long addr = artMethod + offset;
            // actual native method registration
            if (NativeHelper.isCurrentRuntime64Bit()) {
                Memory.pokeLong(addr, address, false);
            } else {
                Memory.pokeInt(addr, (int) address, false);
            }
        }
    }

}
