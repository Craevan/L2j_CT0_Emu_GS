package com.l2j.emu.commons.mmocore;

@FunctionalInterface
public interface IClientFactory<T extends MMOClient<?>> {
    T create(final MMOConnection<T> connection);
}
