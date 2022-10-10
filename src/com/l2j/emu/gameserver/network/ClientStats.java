package com.l2j.emu.gameserver.network;

import com.l2j.emu.Config;

public class ClientStats {
    public int processedPackets = 0;
    public int droppedPackets = 0;
    public int unknownPackets = 0;
    public int totalQueueSize = 0;
    public int maxQueueSize = 0;
    public int totalBursts = 0;
    public int maxBurstSize = 0;
    public int shortFloods = 0;
    public int longFloods = 0;
    public int totalQueueOverflows = 0;
    public int totalUnderflowExceptions = 0;

    private final int[] packetsInSecond;
    private long packetCountStartTick = 0;
    private int head;
    private int totalCount = 0;

    private int floodsInMin = 0;
    private long floodStartTick = 0;
    private int unknownPacketsInMin = 0;
    private long unknownPacketStartTick = 0;
    private int overflowsInMin = 0;
    private long overflowStartTick = 0;
    private int underflowReadsInMin = 0;
    private long underflowReadStartTick = 0;

    private volatile boolean floodDetected = false;
    private volatile boolean queueOverflowDetected = false;

    private final int bufferSize;

    public ClientStats() {
        bufferSize = Config.CLIENT_PACKET_QUEUE_MEASURE_INTERVAL;
        packetsInSecond = new int[bufferSize];
        head = bufferSize - 1;
    }

    /**
     * @return true если входящий пакет необходимо отбросить.
     */
    protected final boolean dropPacket() {
        final boolean result = floodDetected || queueOverflowDetected;
        if (result)
            droppedPackets++;
        return result;
    }

    /**
     * @param queueSize размер очереди
     * @return True если сначала обнаружен flood и необходимо отправить пакет ActionFailed.
     * Позже во время flood возвращает true (и отправляет ActionFailed) один раз в секунду.
     */
    protected final boolean countPacket(int queueSize) {
        processedPackets++;
        totalQueueSize += queueSize;
        if (maxQueueSize < queueSize)
            maxQueueSize = queueSize;
        if (queueOverflowDetected && queueSize < 2)
            queueOverflowDetected = false;

        return countPacket();
    }

    /**
     * Подсчет неизвестных пакетов.
     *
     * @return true если порог достигнут.
     */
    protected final boolean countUnknownPacket() {
        unknownPackets++;

        final long tick = System.currentTimeMillis();
        if (tick - unknownPacketStartTick > 60000) {
            unknownPacketStartTick = tick;
            unknownPacketsInMin = 1;
            return false;
        }

        unknownPacketsInMin++;
        return unknownPacketsInMin > Config.CLIENT_PACKET_QUEUE_MAX_UNKNOWN_PER_MIN;
    }

    /**
     * Подсчитывает длительность серии.
     *
     * @param count - текущее количество обработанных пакетов в очереди
     * @return true если выполнение очереди должно быть прервано.
     */
    protected final boolean countBurst(int count) {
        if (count > maxBurstSize) {
            maxBurstSize = count;
        }

        if (count < Config.CLIENT_PACKET_QUEUE_MAX_BURST_SIZE) {
            return false;
        }

        totalBursts++;
        return true;
    }

    /**
     * Подсчитывает переполнения очереди.
     *
     * @return true если порог достигнут.
     */
    protected final boolean countQueueOverflow() {
        queueOverflowDetected = true;
        totalQueueOverflows++;

        final long tick = System.currentTimeMillis();
        if (tick - overflowStartTick > 60000) {
            overflowStartTick = tick;
            overflowsInMin = 1;
            return false;
        }

        overflowsInMin++;
        return overflowsInMin > Config.CLIENT_PACKET_QUEUE_MAX_OVERFLOWS_PER_MIN;
    }

    /**
     * Подсчитывает исключения при переполнении.
     *
     * @return true если порог достигнут.
     */
    protected final boolean countUnderflowException() {
        totalUnderflowExceptions++;

        final long tick = System.currentTimeMillis();
        if (tick - underflowReadStartTick > 60000) {
            underflowReadStartTick = tick;
            underflowReadsInMin = 1;
            return false;
        }

        underflowReadsInMin++;
        return underflowReadsInMin > Config.CLIENT_PACKET_QUEUE_MAX_UNDERFLOWS_PER_MIN;
    }

    /**
     * @return true если достигнуто максимальное количество flood в минуту.
     */
    protected final boolean countFloods() {
        return floodsInMin > Config.CLIENT_PACKET_QUEUE_MAX_FLOODS_PER_MIN;
    }

    private final boolean longFloodDetected() {
        return (totalCount / bufferSize) > Config.CLIENT_PACKET_QUEUE_MAX_AVERAGE_PACKETS_PER_SECOND;
    }

    /**
     * @return True если первым обнаружен флуд и необходимо отправить пакет ActionFailed.
     * Позже во время flood возвращает true (и отправляет ActionFailed) один раз в секунду
     */
    private final synchronized boolean countPacket() {
        totalCount++;
        final long tick = System.currentTimeMillis();
        if (tick - packetCountStartTick > 1000) {
            packetCountStartTick = tick;

            if (floodDetected && !longFloodDetected() && packetsInSecond[head] < Config.CLIENT_PACKET_QUEUE_MAX_PACKETS_PER_SECOND / 2) {
                floodDetected = false;
            }

            if (head <= 0) {
                head = bufferSize;
            }
            head--;

            totalCount -= packetsInSecond[head];
            packetsInSecond[head] = 1;
            return floodDetected;
        }

        final int count = ++packetsInSecond[head];
        if (!floodDetected) {
            if (count > Config.CLIENT_PACKET_QUEUE_MAX_PACKETS_PER_SECOND) {
                shortFloods++;
            } else if (longFloodDetected()) {
                longFloods++;
            } else {
                return false;
            }

            floodDetected = true;
            if (tick - floodStartTick > 60000) {
                floodStartTick = tick;
                floodsInMin = 1;
            } else {
                floodsInMin++;
            }

            return true; // Возвращает true только в начале flood
        }
        return false;
    }
}
