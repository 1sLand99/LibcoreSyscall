package dev.tmpfs.libcoresyscall.core.impl.trampoline;

import java.util.HashMap;

import dev.tmpfs.libcoresyscall.core.impl.NativeHelper;
import dev.tmpfs.libcoresyscall.core.impl.arch.TrampolineCreator_ARM64;
import dev.tmpfs.libcoresyscall.core.impl.arch.TrampolineCreator_X86_64;

public class TrampolineCreatorFactory {

    private TrampolineCreatorFactory() {
        throw new AssertionError("no instances");
    }

    private static final HashMap<Integer, ITrampolineCreator> CREATOR_MAP = new HashMap<>(6);

    static {
        // add all supported ISAs
        CREATOR_MAP.put(NativeHelper.ISA_ARM64, TrampolineCreator_ARM64.INSTANCE);
        CREATOR_MAP.put(NativeHelper.ISA_X86_64, TrampolineCreator_X86_64.INSTANCE);
    }

    public static ITrampolineCreator create() {
        return create(NativeHelper.getCurrentRuntimeIsa());
    }

    public static ITrampolineCreator create(int isa) {
        ITrampolineCreator creator = CREATOR_MAP.get(isa);
        if (creator == null) {
            throw new UnsupportedOperationException("Unsupported ISA: " + NativeHelper.getIsaName(isa));
        }
        return creator;
    }

}
