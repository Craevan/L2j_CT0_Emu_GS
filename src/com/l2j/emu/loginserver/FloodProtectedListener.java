package com.l2j.emu.loginserver;

import com.l2j.emu.Config;
import com.l2j.emu.commons.logging.CLogger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class FloodProtectedListener extends Thread {

    private static final CLogger logger = new CLogger(FloodProtectedListener.class.getName());

    private final Map<String, ForeignConnection> flooders = new ConcurrentHashMap<>();
    private final ServerSocket serverSocket;

    public FloodProtectedListener(String ip, int port) throws IOException {
        if ("*".equals(ip)) {
            serverSocket = new ServerSocket(port);
        } else {
            serverSocket = new ServerSocket(port, 50, InetAddress.getByName(ip));
        }
    }

    public void removeFloodProtection(String ip) {
        if (!Config.FLOOD_PROTECTION) {
            return;
        }
        final ForeignConnection foreignConnection = flooders.get(ip);
        if (foreignConnection != null) {
            foreignConnection.attempts -= 1;
            if (foreignConnection.attempts == 0) {
                flooders.remove(ip);
            }
        }
    }

    public abstract void addClient(Socket socket);

    @Override
    public void run() {
        Socket connection = null;
        while (true) {
            try {
                connection = serverSocket.accept();
                if (Config.FLOOD_PROTECTION) {
                    final String address = connection.getInetAddress().getHostAddress();
                    final long currentTime = System.currentTimeMillis();
                    final ForeignConnection foreignConnection = flooders.get(address);
                    if (foreignConnection != null) {
                        foreignConnection.attempts += 1;
                        if ((foreignConnection.attempts > Config.FAST_CONNECTION_LIMIT &&
                                (currentTime - foreignConnection.lastConnection) < Config.NORMAL_CONNECTION_TIME) ||
                                (currentTime - foreignConnection.lastConnection) < Config.FAST_CONNECTION_TIME ||
                                foreignConnection.attempts > Config.MAX_CONNECTION_PER_IP) {
                            foreignConnection.lastConnection = currentTime;
                            foreignConnection.attempts -= 1;
                            connection.close();
                            if (!foreignConnection.isFlooding) {
                                logger.info("Flood detected from {}.", address);
                            }
                            foreignConnection.isFlooding = true;
                            continue;
                        }
                        if (foreignConnection.isFlooding) {
                            foreignConnection.isFlooding = false;
                            logger.info("{} isn't considered as flooding anymore.", address);
                        }
                        foreignConnection.lastConnection = currentTime;
                    } else {
                        flooders.put(address, new ForeignConnection(currentTime));
                    }
                }
                addClient(connection);
            } catch (IOException ioe) {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (isInterrupted()) {
                        try {
                            serverSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            logger.error(e);
                        }
                        break;
                    }
                }
            }
        }
    }

    protected static class ForeignConnection {
        public int attempts;
        public long lastConnection;
        public boolean isFlooding = false;

        public ForeignConnection(long lastConnection) {
            this.lastConnection = lastConnection;
            attempts = 1;
        }
    }
}
