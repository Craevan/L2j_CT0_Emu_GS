package com.l2j.emu.gameserver.network;

import com.l2j.emu.commons.random.Rnd;

public class BlowFishKeygen {

    private static final int CRYPT_KEYS_SIZE = 20;
    private static final byte[][] CRYPT_KEYS = new byte[CRYPT_KEYS_SIZE][16];

    static {
        // инициализация ключей шифрования GameServer при загрузке класса

        for (int i = 0; i < CRYPT_KEYS_SIZE; i++) {
            // рандомизировать 8 первых байтов
            for (int j = 0; j < CRYPT_KEYS[i].length; j++) {
                CRYPT_KEYS[i][j] = (byte) Rnd.get(255);
            }

            // последние 8 байт являются статическими
            CRYPT_KEYS[i][8] = (byte) 0xc8;
            CRYPT_KEYS[i][9] = (byte) 0x27;
            CRYPT_KEYS[i][10] = (byte) 0x93;
            CRYPT_KEYS[i][11] = (byte) 0x01;
            CRYPT_KEYS[i][12] = (byte) 0xa1;
            CRYPT_KEYS[i][13] = (byte) 0x6c;
            CRYPT_KEYS[i][14] = (byte) 0x31;
            CRYPT_KEYS[i][15] = (byte) 0x97;
        }
    }

    // блокировка возможности создания объекта
    private BlowFishKeygen() {
    }

    public static byte[] getRandomKey() {
        return Rnd.get(CRYPT_KEYS);
    }
}
