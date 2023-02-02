package com.l2j.emu.commons.network;

public enum ServerType {
    AUTO(0, "AUTO"),
    GOOD(1, "GOOD"),
    NORMAL(2, "NORMAL"),
    FULL(3, "FULL"),
    DOWN(4, "DOWN"),
    GM_ONLY(5, "GM_ONLY");

    private final int id;
    private final String name;

    ServerType(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static final ServerType[] VALUES = values();
}
