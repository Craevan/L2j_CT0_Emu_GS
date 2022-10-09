package com.l2j.emu.commons.random;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Основной класс для генерации случайных значений
 * В настоящее время все методы делегируются {@link ThreadLocalRandom}.
 */
public final class Rnd {

    public static double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public static int nextInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public static int nextInt(int n) {
        return ThreadLocalRandom.current().nextInt(n);
    }

    public static int get(int n) {
        return nextInt(n);
    }

    public static int get(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max == Integer.MAX_VALUE ? max : max + 1);
    }

    public static long nextLong() {
        return ThreadLocalRandom.current().nextLong();
    }

    public static long nextLong(long n) {
        return ThreadLocalRandom.current().nextLong(n);
    }

    public static long get(long n) {
        return nextLong(n);
    }

    public static long get(long min, long max) {
        return ThreadLocalRandom.current().nextLong(min, max == Long.MAX_VALUE ? max : max + 1);
    }

    public static boolean calcChance(double applicableUnits, int totalUnits) {
        return applicableUnits > nextInt(totalUnits);
    }

    public static double nextGaussian() {
        return ThreadLocalRandom.current().nextGaussian();
    }

    public static boolean nextBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public static byte[] nextBytes(byte[] array) {
        ThreadLocalRandom.current().nextBytes(array);
        return array;
    }

    public static byte[] nextBytes(int count) {
        return nextBytes(new byte[count]);
    }

    /**
     * Возвращает случайно выбранный элемент из заданного списка.
     *
     * @param <T>  тип элементов списка.
     * @param list список.
     * @return случайно выбранный элемент.
     */
    public static <T> T get(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(get(list.size()));
    }

    /**
     * Возвращает случайно выбранный элемент из заданного массива.
     *
     * @param array массив.
     * @return случайно выбранный элемент.
     */
    public static int get(int[] array) {
        return array[get(array.length)];
    }

    /**
     * Возвращает случайно выбранный элемент из заданного массива.
     *
     * @param <T>   тип элементов массива.
     * @param array массив.
     * @return случайно выбранный элемент.
     */
    public static <T> T get(T[] array) {
        return array[get(array.length)];
    }
}
