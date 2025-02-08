package dev.tmpfs.libcoresyscall.elfloader;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import dev.tmpfs.libcoresyscall.core.NativeAccess;

public class NativeRegistrationHelper {

    private NativeRegistrationHelper() {
        throw new AssertionError("no instances");
    }

    public static class RegistrationSummary {
        // The methods that were successfully registered by this invocation.
        public final ArrayList<Method> registeredMethods = new ArrayList<>();
        // The methods that were not found in the native library.
        public final ArrayList<Method> missedMethods = new ArrayList<>();
        // The methods that were already registered.
        public final ArrayList<Method> skippedMethods = new ArrayList<>();
    }

    public interface NativeLibrarySymbolResolver {
        /**
         * Resolves a symbol in the native library.
         *
         * @param symbol the symbol to resolve
         * @return the address of the symbol, or 0 if the symbol could not be found
         */
        long resolveSymbol(String symbol);
    }

    private static final int JNI_NATIVE_REGISTRATION_SUCCESS = 1;
    private static final int JNI_NATIVE_REGISTRATION_ALREADY_REGISTERED = 2;
    private static final int JNI_NATIVE_REGISTRATION_SYM_NOT_FOUND = 3;

    private static int findAndRegisterNativeMethodInternal(@NonNull NativeLibrarySymbolResolver resolver, @NonNull Method method) {
        // check if the method is already registered
        if (NativeAccess.getRegisteredNativeMethod(method) != 0) {
            return JNI_NATIVE_REGISTRATION_ALREADY_REGISTERED;
        }
        // ART: Try the short name then the long name...
        long address = resolver.resolveSymbol(getJniShortName(method));
        if (address == 0) {
            address = resolver.resolveSymbol(getJniLongName(method));
        }
        if (address == 0) {
            return JNI_NATIVE_REGISTRATION_SYM_NOT_FOUND;
        }
        NativeAccess.registerNativeMethod(method, address);
        return JNI_NATIVE_REGISTRATION_SUCCESS;
    }

    public static RegistrationSummary findAndRegisterNativeMethods(long handle, @NonNull Class<?>[] klasses) {
        if (handle == 0) {
            throw new IllegalArgumentException("library handle is null");
        }
        return findAndRegisterNativeMethods(new DefaultNativeLibraryPublicSymbolResolver(handle), klasses);
    }

    public static RegistrationSummary findAndRegisterNativeMethods(@NonNull NativeLibrarySymbolResolver resolver, @NonNull Class<?>[] klasses) {
        RegistrationSummary summary = new RegistrationSummary();
        for (Class<?> klass : klasses) {
            // enumerate all declared methods in the class
            for (Method method : klass.getDeclaredMethods()) {
                if (Modifier.isNative(method.getModifiers())) {
                    int result = findAndRegisterNativeMethodInternal(resolver, method);
                    switch (result) {
                        case JNI_NATIVE_REGISTRATION_SUCCESS:
                            summary.registeredMethods.add(method);
                            break;
                        case JNI_NATIVE_REGISTRATION_ALREADY_REGISTERED:
                            summary.skippedMethods.add(method);
                            break;
                        case JNI_NATIVE_REGISTRATION_SYM_NOT_FOUND:
                            summary.missedMethods.add(method);
                            break;
                    }
                }
            }
        }
        return summary;
    }

    private static class DefaultNativeLibraryPublicSymbolResolver implements NativeLibrarySymbolResolver {

        private final long mHandle;

        public DefaultNativeLibraryPublicSymbolResolver(long handle) {
            mHandle = handle;
        }

        @Override
        public long resolveSymbol(String symbol) {
            if (TextUtils.isEmpty(symbol)) {
                return 0;
            }
            return DlExtLibraryLoader.dlsym(mHandle, symbol);
        }

    }

    @NonNull
    public static String getJniShortName(@NonNull Method method) {
        return getJniShortName(method.getDeclaringClass().getName(), method.getName());
    }

    @NonNull
    public static String getJniLongName(@NonNull Method method) {
        return getJniLongName(method.getDeclaringClass().getName(), method.getName(), method.getParameterTypes());
    }

    @NonNull
    public static String getJniShortName(@NonNull String className, @NonNull String methodName) {
        return "Java_" + mangleForJni(className) + "_" + mangleForJni(methodName);
    }

    @NonNull
    public static String getJniLongName(@NonNull String className, @NonNull String methodName, @NonNull Class<?>[] argTypes) {
        StringBuilder sb = new StringBuilder();
        sb.append("Java_");
        sb.append(mangleForJni(className));
        sb.append("_");
        sb.append(mangleForJni(methodName));
        sb.append("__");
        for (Class<?> argType : argTypes) {
            sb.append(mangleForJni(getTypeSignature(argType)));
        }
        return sb.toString();
    }

    @NonNull
    public static String getTypeSignature(@NonNull Class<?> type) {
        if (type == Void.TYPE) {
            return "V";
        } else if (type == Boolean.TYPE) {
            return "Z";
        } else if (type == Byte.TYPE) {
            return "B";
        } else if (type == Short.TYPE) {
            return "S";
        } else if (type == Character.TYPE) {
            return "C";
        } else if (type == Integer.TYPE) {
            return "I";
        } else if (type == Long.TYPE) {
            return "J";
        } else if (type == Float.TYPE) {
            return "F";
        } else if (type == Double.TYPE) {
            return "D";
        } else if (type.isArray()) {
            Class<?> c = type.getComponentType();
            assert c != null;
            return "[" + getTypeSignature(c);
        } else {
            return "L" + type.getName().replace('.', '/') + ";";
        }
    }

    // See http://java.sun.com/j2se/1.5.0/docs/guide/jni/spec/design.html#wp615 for the full rules.
    @NonNull
    private static String mangleForJni(@NonNull String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9')) {
                result.append(ch);
            } else if (ch == '.' || ch == '/') {
                result.append("_");
            } else if (ch == '_') {
                result.append("_1");
            } else if (ch == ';') {
                result.append("_2");
            } else if (ch == '[') {
                result.append("_3");
            } else {
                result.append("_0").append(String.format("%04x", (int) ch));
            }
        }
        return result.toString();
    }

}
