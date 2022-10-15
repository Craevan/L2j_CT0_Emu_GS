package com.l2j.emu.commons.mmocore;

import java.nio.BufferOverflowException;

public final class NioNetStringBuffer {

    private final char[] buffer;
    private final int size;
    private int length;

    public NioNetStringBuffer(final int size) {
        buffer = new char[size];
        this.size = size;
        this.length = 0;
    }

    public void clear() {
        this.length = 0;
    }

    public void apend(final char ch) {
        if (length < size) {
            buffer[length++] = ch;
        } else {
            throw new BufferOverflowException();
        }
    }

    @Override
    public String toString() {
        return new String(buffer, 0, length);
    }
}
