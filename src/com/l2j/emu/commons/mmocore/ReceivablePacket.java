package com.l2j.emu.commons.mmocore;

import java.nio.ByteBuffer;

public abstract class ReceivablePacket<T extends MMOClient<?>> extends AbstractPacket<T> implements Runnable {

    NioNetStringBuffer nioNetStringBuffer;

    protected ReceivablePacket() {
    }

    public void setBuffers(ByteBuffer data, T client, NioNetStringBuffer nioNetStringBuffer) {
        this.buffer = data;
        this.client = client;
        this.nioNetStringBuffer = nioNetStringBuffer;
    }

    protected abstract boolean read();

    protected final void readB(final byte[] dst) {
        buffer.get(dst);
    }

    protected final void readB(final byte[] dst, final int offset, final int len) {
        buffer.get(dst, offset, len);
    }

    protected final int readC() {
        return buffer.get() & 0xFF;
    }

    protected final int readH() {
        return buffer.getShort() & 0xFFFF;
    }

    protected final int readD() {
        return buffer.getInt();
    }

    protected final long readQ() {
        return buffer.getLong();
    }

    protected final double readF() {
        return buffer.getDouble();
    }

    protected final String readS() {
        nioNetStringBuffer.clear();
        char ch;
        while ((ch = buffer.getChar()) != 0) {
            nioNetStringBuffer.append(ch);
        }
        return nioNetStringBuffer.toString();
    }
}
