package com.l2j.emu.commons.mmocore;

@FunctionalInterface
public interface IMMOExecutor<T extends MMOClient<?>> {
    void execute(ReceivablePacket<T> packet);
}
