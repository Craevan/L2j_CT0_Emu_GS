package com.l2j.emu.commons.mmocore;

import java.nio.ByteBuffer;

public abstract class AbstractPacket<T extends MMOClient<?>> {

    protected ByteBuffer buffer;

    T client;

    public final T getClient() {
        return client;
    }
}
