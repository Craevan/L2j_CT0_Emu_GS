package com.l2j.emu.gameserver.enums;

public enum GeoType {
    L2J("%d_%d.l2j"),
    L2OFF("%d_%d_conv.dat");

    private final String filename;

    GeoType(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return this.filename;
    }
}
