package com.l2j.emu.loginserver.crypt;

import java.io.IOException;

public class NewCrypt {
    private final BlowfishEngine encrypt;
    private final BlowfishEngine decrypt;

    public NewCrypt(byte[] blowfishKey) {
        encrypt = new BlowfishEngine();
        encrypt.init(true, blowfishKey);
        decrypt = new BlowfishEngine();
        decrypt.init(false, blowfishKey);
    }

    public NewCrypt(final String key) {
        this(key.getBytes());
    }

    public static boolean verifyChecksum(final byte[] raw) {
        return NewCrypt.verifyChecksum(raw, 0, raw.length);
    }

    public static boolean verifyChecksum(final byte[] raw, final int offset, final int size) {
        if ((size & 3) != 0 || size <= 4) {
            return false;
        }
        long checkSum = 0;
        int count = size - 4;
        long check;
        int i;

        for (i = offset; i < count; i += 4) {
            check = raw[i] & 0xff;
            check |= raw[i + 1] << 8 & 0xff00;
            check |= raw[i + 2] << 0x10 & 0xff0000;
            check |= raw[i + 3] << 0x18 & 0xff000000;

            checkSum ^= check;
        }
        check = raw[i] & 0xff;
        check |= raw[i + 1] << 8 & 0xff00;
        check |= raw[i + 2] << 0x10 & 0xff0000;
        check |= raw[i + 3] << 0x18 & 0xff000000;

        return check == checkSum;
    }

    public static void appendChecksum(final byte[] raw) {
        NewCrypt.appendChecksum(raw, 0, raw.length);
    }

    public static void appendChecksum(final byte[] raw, final int offset, final int size) {
        long checkSum = 0;
        long ecx;
        int count = size - 4;
        int i;

        for (i = offset; i < count; i += 4) {
            ecx = raw[i] & 0xff;
            ecx |= raw[i + 1] << 8 & 0xff00;
            ecx |= raw[i + 2] << 0x10 & 0xff0000;
            ecx |= raw[i + 3] << 0x18 & 0xff000000;
            checkSum ^= ecx;
        }
        ecx = raw[i] & 0xff;
        ecx |= raw[i + 1] << 8 & 0xff00;
        ecx |= raw[i + 2] << 0x10 & 0xff0000;
        ecx |= raw[i + 3] << 0x18 & 0xff000000;

        raw[i] = (byte) (checkSum & 0xff);
        raw[i + 1] = (byte) (checkSum >> 0x08 & 0xff);
        raw[i + 2] = (byte) (checkSum >> 0x10 & 0xff);
        raw[i + 3] = (byte) (checkSum >> 0x18 & 0xff);
    }

    public static void encXORPass(byte[] raw, int key) {
        NewCrypt.encXORPass(raw, 0, raw.length, key);
    }

    public static void encXORPass(byte[] raw, final int offset, final int size, int key) {
        int stop = size - 8;
        int pos = 4 + offset;
        int edx;
        int ecx = key;

        while (pos < stop) {
            edx = raw[pos] & 0xFF;
            edx |= (raw[pos + 1] & 0xFF) << 8;
            edx |= (raw[pos + 2] & 0xFF) << 16;
            edx |= (raw[pos + 3] & 0xFF) << 24;

            ecx += edx;
            edx ^= ecx;

            raw[pos++] = (byte) (edx & 0xFF);
            raw[pos++] = (byte) (edx >> 8 & 0xFF);
            raw[pos++] = (byte) (edx >> 16 & 0xFF);
            raw[pos++] = (byte) (edx >> 24 & 0xFF);
        }
        raw[pos++] = (byte) (ecx & 0xFF);
        raw[pos++] = (byte) (ecx >> 8 & 0xFF);
        raw[pos++] = (byte) (ecx >> 16 & 0xFF);
        raw[pos] = (byte) (ecx >> 24 & 0xFF);
    }

    public byte[] decrypt(byte[] raw) throws IOException {
        byte[] result = new byte[raw.length];
        int count = raw.length / 8;
        for (int i = 0; i < count; i++) {
            decrypt.processBlock(raw, i * 8, result, i * 8);
        }
        return result;
    }

    public void decrypt(byte[] raw, final int offset, final int size) throws IOException {
        byte[] result = new byte[size];
        int count = size / 8;
        for (int i = 0; i < count; i++) {
            decrypt.processBlock(raw, offset + i * 8, result, i * 8);
        }
        System.arraycopy(result, 0, raw, offset, size);
    }

    public byte[] crypt(byte[] raw) throws IOException {
        int count = raw.length / 8;
        byte[] result = new byte[raw.length];
        for (int i = 0; i < count; i++) {
            encrypt.processBlock(raw, i * 8, result, i * 8);
        }
        return result;
    }

    public void crypt(byte[] raw, final int offset, final int size) throws IOException {
        int count = size / 8;
        byte[] result = new byte[size];
        for (int i = 0; i < count; i++) {
            encrypt.processBlock(raw, offset + i * 8, result, i * 8);
        }
        System.arraycopy(result, 0, raw, offset, size);
    }
}
