package dev.tmpfs.libcoresyscall.core;

import java.io.Closeable;

public interface IAllocatedMemory extends Closeable {

    /**
     * Get the address of the memory block. If the memory block is already freed, the behavior is undefined.
     *
     * @return the address of the memory block.
     */
    long getAddress();

    /**
     * Get the size of the memory block.
     *
     * @return the size of the memory block in bytes.
     */
    long getSize();

    /**
     * Free the memory block. It is safe to call this method multiple times.
     * After the memory block is freed, the behavior of calling {@link #getAddress()} is undefined.
     */
    void free();

    /**
     * Same as {@link #free()}.
     */
    @Override
    void close();

    /**
     * Check if the memory block is already freed.
     */
    boolean isFreed();

}
