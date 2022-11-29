package com.l2j.emu.commons.data;

import java.io.Serial;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link ConcurrentHashMap} используется для хранения исключительно пар {@link String}
 */
public abstract class MemoSet extends ConcurrentHashMap<String, String> {

    @Serial
    private static final long serialVersionUID = 1L;

    public MemoSet() {
        super();
    }

    public MemoSet(final int size) {
        super(size);
    }

    protected abstract void onSet(String key, String value);

    protected abstract void onUnset(String key);

    public final void set(final String key, final String value) {
        onSet(key, value);
        put(key, value);
    }

    public void set(final String key, final boolean value) {
        put(key, String.valueOf(value));
    }

    public void set(final String key, final int value) {
        put(key, String.valueOf(value));
    }

    public void set(final String key, final long value) {
        put(key, String.valueOf(value));
    }

    public void set(final String key, final double value) {
        put(key, String.valueOf(value));
    }

    public void set(final String key, final Enum<?> value) {
        put(key, String.valueOf(value));
    }

    public final void unSet(final String key) {
        onUnset(key);
        remove(key);
    }

    public boolean getBoolean(final String key) {
        final String value = get(key);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        throw new IllegalArgumentException("MemoSet : Boolean value required, but found: " + null + " for key: " + key + ".");
    }

    public boolean getBool(final String key, final boolean defaultValue) {
        final String value = get(key);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }

        return defaultValue;
    }

    public int getInteger(final String key) {
        final String value = get(key);
        if (value != null) {
            return Integer.parseInt(value);
        }

        throw new IllegalArgumentException("MemoSet : Integer value required, but found: " + null + " for key: " + key + ".");
    }

    public int getInteger(final String key, final int defaultValue) {
        final String value = get(key);
        if (value != null) {
            return Integer.parseInt(value);
        }

        return defaultValue;
    }

    public long getLong(final String key) {
        final String value = get(key);
        if (value != null) {
            return Long.parseLong(value);
        }

        throw new IllegalArgumentException("MemoSet : Long value required, but found: " + null + " for key: " + key + ".");
    }

    public long getLong(final String key, final long defaultValue) {
        final String value = get(key);
        if (value != null) {
            return Long.parseLong(value);
        }

        return defaultValue;
    }

    public double getDouble(final String key) {
        final String value = get(key);
        if (value != null) {
            return Double.parseDouble(value);
        }

        throw new IllegalArgumentException("MemoSet : Double value required, but found: " + null + " for key: " + key + ".");
    }

    public double getDouble(final String key, final double defaultValue) {
        final String value = get(key);
        if (value != null) {
            return Double.parseDouble(value);
        }

        return defaultValue;
    }

    public <E extends Enum<E>> E getEnum(final String name, final Class<E> enumClass) {
        final String value = get(name);
        if (value != null) {
            return Enum.valueOf(enumClass, value);
        }

        throw new IllegalArgumentException("Enum value of type " + enumClass.getName() + " required, but found: " + null + ".");
    }

    public <E extends Enum<E>> E getEnum(final String name, final Class<E> enumClass, final E defaultValue) {
        final String value = get(name);
        if (value != null) {
            return Enum.valueOf(enumClass, value);
        }

        return defaultValue;
    }
}
