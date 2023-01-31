package com.l2j.emu.loginserver;

import com.l2j.emu.Config;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServerListener extends FloodProtectedListener {

    private static final List<GameServerThread> gameServers = new ArrayList<>();

    public GameServerListener() throws IOException {
        super(Config.GAMESERVER_LOGIN_HOSTNAME, Config.GAMESERVER_LOGIN_PORT);
    }

    public void removeGameServer(GameServerThread gst) {
        gameServers.remove(gst);
    }

    @Override
    public void addClient(Socket socket) {
        gameServers.add(new GameServerThread(socket));
    }
}
