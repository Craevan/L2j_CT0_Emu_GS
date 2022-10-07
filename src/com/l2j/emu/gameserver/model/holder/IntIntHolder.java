package com.l2j.emu.gameserver.model.holder;

/**
 * Общий контейнер int/int
 */
public class IntIntHolder {
    private int id;
    private int value;

    public IntIntHolder(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "IntIntHolder [id=" + this.id + " value=" + this.value + "]";
    }
}
