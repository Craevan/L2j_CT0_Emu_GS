package com.l2j.emu.commons.mmocore;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.WritableByteChannel;
import java.util.Objects;

public class MMOConnection<T extends MMOClient<?>> {

    private final SelectorThread<T> selectorThread;
    private final Socket socket;
    private final InetAddress inetAddress;
    private final ReadableByteChannel readableByteChannel;
    private final WritableByteChannel writableByteChannel;
    private final int port;
    private final NioNetStackList<SendablePacket<T>> sendQueue;
    private final SelectionKey selectionKey;

    private ByteBuffer readBuffer;
    private ByteBuffer primaryWriteBuffer;
    private ByteBuffer secondaryWriteBuffer;

    private volatile boolean pendingClose;

    private T client;

    public MMOConnection(final SelectorThread<T> selectorThread,
                         final Socket socket,
                         final SelectionKey key,
                         boolean tcpNoDelay) {
        this.selectorThread = selectorThread;
        this.socket = socket;
        this.inetAddress = socket.getInetAddress();
        this.readableByteChannel = socket.getChannel();
        this.writableByteChannel = socket.getChannel();
        this.port = socket.getPort();
        this.selectionKey = key;
        this.sendQueue = new NioNetStackList<>();
        try {
            socket.setTcpNoDelay(tcpNoDelay);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    final void setClient(final T client) {
        this.client = client;
    }

    public final T getClient() {
        return client;
    }

    public final void sendPacket(final SendablePacket<T> sp) {
        sp.client = this.client;
        if (pendingClose) {
            return;
        }
        synchronized (getSendQueue()) {
            sendQueue.addLast(sp);
        }

        if (!sendQueue.isEmpty()) {
            try {
                selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
            } catch (CancelledKeyException ignore) {
                //ignore
            }
        }
    }

    final SelectionKey getSelectionKey() {
        return selectionKey;
    }

    public final InetAddress getInetAddress() {
        return inetAddress;
    }

    public final int getPort() {
        return port;
    }

    final void close() throws IOException {
        socket.close();
    }

    final int read(final ByteBuffer byteBuffer) throws IOException {
        return readableByteChannel.read(byteBuffer);
    }

    final int write(final ByteBuffer byteBuffer) throws IOException {
        return writableByteChannel.write(byteBuffer);
    }

    final void createWriteBuffer(final ByteBuffer byteBuffer) {
        if (primaryWriteBuffer == null) {
            primaryWriteBuffer = selectorThread.getPooledBuffer();
            Objects.requireNonNull(primaryWriteBuffer).put(byteBuffer);
        } else {
            final ByteBuffer temp = selectorThread.getPooledBuffer();
            Objects.requireNonNull(temp).put(byteBuffer);

            final int remaining = temp.remaining();
            primaryWriteBuffer.flip();
            final int limit = primaryWriteBuffer.limit();

            if (remaining >= primaryWriteBuffer.remaining()) {
                temp.put(primaryWriteBuffer);
                selectorThread.recycleBuffer(primaryWriteBuffer);
            } else {
                primaryWriteBuffer.limit(remaining);
                temp.put(primaryWriteBuffer);
                primaryWriteBuffer.limit(limit);
                primaryWriteBuffer.compact();
                secondaryWriteBuffer = primaryWriteBuffer;
            }
            primaryWriteBuffer = temp;
        }
    }

    final boolean hasPendingWriteBuffer() {
        return primaryWriteBuffer != null;
    }

    final void movePendingWriteBufferTo(final ByteBuffer byteBuffer) {
        primaryWriteBuffer.flip();
        byteBuffer.put(primaryWriteBuffer);
        selectorThread.recycleBuffer(primaryWriteBuffer);
        primaryWriteBuffer = secondaryWriteBuffer;
        secondaryWriteBuffer = null;
    }

    final void setReadBuffer(final ByteBuffer byteBuffer) {
        this.readBuffer = byteBuffer;
    }

    final ByteBuffer getReadBuffer() {
        return readBuffer;
    }

    public boolean isClose() {
        return pendingClose;
    }

    final NioNetStackList<SendablePacket<T>> getSendQueue() {
        return sendQueue;
    }

    public final void close(final SendablePacket<T> sp) {
        if (pendingClose) {
            return;
        }
        synchronized (getSendQueue()) {
            if (!pendingClose) {
                pendingClose = true;
                sendQueue.clear();
                sendQueue.addLast(sp);
            }
        }
        try {
            selectionKey.interestOps(selectionKey.interestOps() & ~SelectionKey.OP_WRITE);
        } catch (CancelledKeyException ignore) {
            //ignore
        }
        selectorThread.closeConnection(this);
    }

    final void releaseBuffers() {
        if (primaryWriteBuffer != null) {
            selectorThread.recycleBuffer(primaryWriteBuffer);
            primaryWriteBuffer = null;

            if (secondaryWriteBuffer != null) {
                selectorThread.recycleBuffer(secondaryWriteBuffer);
                secondaryWriteBuffer = null;
            }
        }
        if (readBuffer != null) {
            selectorThread.recycleBuffer(readBuffer);
            readBuffer = null;
        }
    }
}
