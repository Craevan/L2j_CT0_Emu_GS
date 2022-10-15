package com.l2j.emu.commons.mmocore;

public final class NioNetStackList<E> {
    private final NioNetStackNode startNode = new NioNetStackNode();

    private final NioNetStackNodeBuffer nioNetStackNodeBuf = new NioNetStackNodeBuffer();

    private NioNetStackNode endNode = new NioNetStackNode();

    public NioNetStackList() {
        clear();
    }

    public void addLast(final E elem) {
        final NioNetStackNode newEndNode = nioNetStackNodeBuf.removeFirst();
        endNode.value = elem;
        endNode.nextNode = newEndNode;
        endNode = newEndNode;
    }

    public E removeFirst() {
        final NioNetStackNode old = startNode.nextNode;
        final E value = old.value;
        startNode.nextNode = old.nextNode;
        nioNetStackNodeBuf.addLast(old);
        return value;
    }

    public boolean isEmpty() {
        return startNode.nextNode == endNode;
    }

    public void clear() {
        startNode.nextNode = endNode;
    }

    private final class NioNetStackNode {
        private NioNetStackNode nextNode;

        private E value;

        private NioNetStackNode() {
        }
    }

    private final class NioNetStackNodeBuffer {
        private final NioNetStackNode startNode = new NioNetStackNode();

        private NioNetStackNode endNode = new NioNetStackNode();

        NioNetStackNodeBuffer() {
            startNode.nextNode = endNode;
        }

        void addLast(final NioNetStackNode node) {
            node.nextNode = null;
            node.value = null;
            endNode.nextNode = node;
            endNode = node;
        }

        NioNetStackNode removeFirst() {
            if (startNode.nextNode == endNode)
                return new NioNetStackNode();

            final NioNetStackNode old = startNode.nextNode;
            startNode.nextNode = old.nextNode;
            return old;
        }
    }
}
