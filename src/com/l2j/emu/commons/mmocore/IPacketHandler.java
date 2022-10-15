package com.l2j.emu.commons.mmocore;

import java.nio.ByteBuffer;

@FunctionalInterface
public interface IPacketHandler<T extends MMOClient<?>> {
    ReceivablePacket<T> handlePacket(ByteBuffer buf, T client);
}
