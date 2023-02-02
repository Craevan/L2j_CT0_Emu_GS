package com.l2j.emu.loginserver.model;

import com.l2j.emu.commons.network.ServerType;
import com.l2j.emu.loginserver.GameServerThread;

public class GameServerInfo {
    private final byte[] hexId;

    private boolean isAuthed;
    private int id;
    private GameServerThread gameServerThread;
    private ServerType serverType;
    private String hostName;
    private int port;
    private boolean isPvp;
    private boolean isTestServer;
    private boolean isClockShowing;
    private boolean isBracketsShowing;
    private int ageLimit;
    private int maxPlayers;

    public GameServerInfo(final int id, final byte[] hexId) {
        this(id, hexId, null);
    }

    public GameServerInfo(final int id, final byte[] hexId, final GameServerThread gameServerThread) {
        this.id = id;
        this.hexId = hexId;
        this.gameServerThread = gameServerThread;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public byte[] getHexId() {
        return hexId;
    }

    public boolean isAuthed() {
        return isAuthed;
    }

    public void setAuthed(final boolean authed) {
        isAuthed = authed;
    }

    public GameServerThread getGameServerThread() {
        return gameServerThread;
    }

    public void setGameServerThread(final GameServerThread gameServerThread) {
        this.gameServerThread = gameServerThread;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public void setServerType(final ServerType serverType) {
        this.serverType = serverType;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(final String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(final int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public boolean isPvp() {
        return isPvp;
    }

    public void setPvp(final boolean pvp) {
        isPvp = pvp;
    }

    public boolean isTestServer() {
        return isTestServer;
    }

    public void setTestServer(final boolean testServer) {
        isTestServer = testServer;
    }

    public boolean isClockShowing() {
        return isClockShowing;
    }

    public void setClockShowing(final boolean clockShowing) {
        isClockShowing = clockShowing;
    }

    public boolean isBracketsShowing() {
        return isBracketsShowing;
    }

    public void setBracketsShowing(final boolean bracketsShowing) {
        isBracketsShowing = bracketsShowing;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(final int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public void setDown() {
        setAuthed(false);
        setPort(0);
        setGameServerThread(null);
        setServerType(ServerType.DOWN);
    }

    public int getCurrentPlayerCount() {
        return (gameServerThread == null) ? 0 : gameServerThread.getPlayerCount();
    }
}
