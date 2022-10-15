package com.l2j.emu.commons.mmocore;

import java.net.Socket;

@FunctionalInterface
public interface IAcceptFilter {
    boolean accept(Socket socket);
}
