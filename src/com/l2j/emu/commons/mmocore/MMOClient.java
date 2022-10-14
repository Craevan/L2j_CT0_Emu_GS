package com.l2j.emu.commons.mmocore;

import java.nio.ByteBuffer;

public abstract class MMOClient<T extends MMOConnection<?>> {

    private final T connection;

    public MMOClient(final T connection) {
        this.connection = connection;
    }

    public T getConnection() {
        return connection;
    }

    public abstract boolean decrypt(final ByteBuffer buf, final int size);

    public abstract boolean encrypt(final ByteBuffer buf, final int size);

    protected abstract void onDisconnection();

    protected abstract void onForcedDisconnection();
}
