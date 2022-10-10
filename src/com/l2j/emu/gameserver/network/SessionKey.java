package com.l2j.emu.gameserver.network;

public class SessionKey {

    private final int playOkID1;
    private final int playOkID2;
    private final int loginOkID1;
    private final int loginOkID2;

    public SessionKey(int loginOK1, int loginOK2, int playOK1, int playOK2) {
        playOkID1 = playOK1;
        playOkID2 = playOK2;
        loginOkID1 = loginOK1;
        loginOkID2 = loginOK2;
    }

    @Override
    public String toString() {
        return "PlayOk: " + playOkID1 + " " + playOkID2 + " LoginOk:" + loginOkID1 + " " + loginOkID2;
    }
}
