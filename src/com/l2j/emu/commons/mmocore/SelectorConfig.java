package com.l2j.emu.commons.mmocore;

public class SelectorConfig {

    public int READ_BUFFER_SIZE = 64 * 1024;
    public int WRITE_BUFFER_SIZE = 64 * 1024;

    public int HELPER_BUFFER_COUNT = 20;
    public int HELPER_BUFFER_SIZE = 64 * 1024;

    /**
     * Сервер попытается отправить MAX_SEND_PER_PASS пакетов за один вызов записи в сокет,
     * однако он может отправить меньше, если буфер записи был заполнен до достижения этого значения.
     */
    public int MAX_SEND_PER_PASS = 10;

    /**
     * Сервер будет пытаться прочитать MAX_READ_PER_PASS пакетов за один вызов чтения сокета,
     * однако он может прочитать меньше, если буфер чтения был пуст до достижения этого значения.
     */
    public int MAX_READ_PER_PASS = 10;

    /**
     * Определяет, сколько времени (в миллисекундах) селектор должен спать,
     * более высокое значение увеличивает пропускную способность,
     * но также увеличивает задержку (до максимума самого значения sleep).<BR>
     * Также очень высокое значение (обычно > 100) снизит пропускную способность из-за того,
     * что сервер не будет делать достаточно отправлений в секунду
     * (зависит от максимального количества отправлений за проход).<BR>
     * Рекомендуемые значения:<BR>
     * 1 для минимальной задержки.<BR>
     * 10-30 для компромисса между задержкой и пропускной способностью в зависимости от ваших потребностей.<BR>
     */
    public int SLEEP_TIME = 10;

    /**
     * Используется для включения/выключения TCP_NODELAY, который отключает/включает Nagle's algorithm<BR>
     * <BR>
     * Nagle's algorithm пытается сохранить пропускную способность, минимизируя количество отправляемых сегментов.
     * Когда приложения хотят уменьшить задержку в сети и увеличить производительность, они могут отключить
     * Nagle's algorithm (то есть включить TCP_NODELAY).<BR>
     * Данные будут отправляться раньше, ценой увеличения потребления полосы пропускания.<BR>
     * Nagle's algorithm описан в RFC 896.<BR>
     * <BR>
     * Резюме: данные будут отправляться раньше, что снизит ping,
     * ценой небольшого увеличения потребления полосы пропускания.
     */
    public boolean TCP_NODELAY = true;
}
