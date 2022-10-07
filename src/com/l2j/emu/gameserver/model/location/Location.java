package com.l2j.emu.gameserver.model.location;

//TODO STUB
public class Location extends Point2D {

    public static final Location DUMMY_LOC = new Location(0, 0, 0);

    protected int z;

    public Location(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    public int getZ() {
        return z;
    }
}
