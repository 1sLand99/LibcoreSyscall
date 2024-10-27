package dev.tmpfs.libcoresyscall.core.impl.trampoline;

public interface ITrampolineCreator {

    TrampolineInfo generateTrampoline(int pageSize);

    long mprotect(long address, long size, int prot);

}
