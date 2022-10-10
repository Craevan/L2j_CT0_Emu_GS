package com.l2j.emu.gameserver.network;

import com.l2j.emu.Config;

public class GameCrypt {
    private final byte[] inKey = new byte[16];
    private final byte[] outKey = new byte[16];
    private boolean isEnabled;

    public void setKey(byte[] key) {
        System.arraycopy(key, 0, inKey, 0, 16);
        System.arraycopy(key, 0, outKey, 0, 16);
    }

    public void decrypt(byte[] raw, final int offset, final int size) {
        if (!Config.USE_BLOWFISH_CIPHER || !isEnabled) {
            return;
        }

        int temp = 0;
        for (int i = 0; i < size; i++) {
            int temp2 = raw[offset + i] & 0xFF;
            raw[offset + i] = (byte) (temp2 ^ inKey[i & 15] ^ temp);
            temp = temp2;
        }

        crypt(size, inKey);
    }

    public void encrypt(byte[] raw, final int offset, final int size) {
        if (!isEnabled) {
            isEnabled = Config.USE_BLOWFISH_CIPHER;
            return;
        }

        int temp = 0;
        for (int i = 0; i < size; i++) {
            int temp2 = raw[offset + i] & 0xFF;
            temp = temp2 ^ outKey[i & 15] ^ temp;
            raw[offset + i] = (byte) temp;
        }

        crypt(size, outKey);
    }

    private void crypt(int size, byte[] key) {
        int old = key[8] & 0xff;
        old |= key[9] << 8 & 0xff00;
        old |= key[10] << 0x10 & 0xff0000;
        old |= key[11] << 0x18 & 0xff000000;

        old += size;

        key[8] = (byte) (old & 0xff);
        key[9] = (byte) (old >> 0x08 & 0xff);
        key[10] = (byte) (old >> 0x10 & 0xff);
        key[11] = (byte) (old >> 0x18 & 0xff);
    }
}
