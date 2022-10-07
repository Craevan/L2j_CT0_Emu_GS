package com.l2j.emu.commons.math;

import com.l2j.emu.gameserver.model.WorldObject;
import com.l2j.emu.gameserver.model.actor.Creature;
import com.l2j.emu.gameserver.model.location.Location;
import com.l2j.emu.gameserver.model.location.Point2D;

public class MathUtil {
    public static final int[][] MATRIX_3X3_LINES =
            {
                    {
                            1,
                            2,
                            3
                    }, // line 1
                    {
                            4,
                            5,
                            6
                    }, // line 2
                    {
                            7,
                            8,
                            9
                    }, // line 3
                    {
                            1,
                            4,
                            7
                    }, // column 1
                    {
                            2,
                            5,
                            8
                    }, // column 2
                    {
                            3,
                            6,
                            9
                    }, // column 3
                    {
                            1,
                            5,
                            9
                    }, // diagonal 1
                    {
                            3,
                            5,
                            7
                    }, // diagonal 2
            };

    /**
     * @param objectsSize : Общий размер элементов.
     * @param pageSize    : Количество элементов на странице.
     * @return Количество страниц, основанное на количестве элементов и количестве элементов, которые мы хотим разместить на одной странице.
     */
    public static int countPagesNumber(int objectsSize, int pageSize) {
        return objectsSize / pageSize + (objectsSize % pageSize == 0 ? 0 : 1);
    }

    /**
     * @param numToTest : Номер для тестирования.
     * @param min       : Минимальный лимит.
     * @param max       : Максимальный лимит.
     * @return число или один из пределов (минимум / максимум).
     */
    public static int limit(int numToTest, int min, int max) {
        return (numToTest > max) ? max : (Math.max(numToTest, min));
    }

    public static double calculateAngleFrom(WorldObject obj1, WorldObject obj2) {
        return calculateAngleFrom(obj1.getX(), obj1.getY(), obj2.getX(), obj2.getY());
    }

    public static double calculateAngleFrom(int obj1X, int obj1Y, int obj2X, int obj2Y) {
        double angleTarget = Math.toDegrees(Math.atan2(obj2Y - obj1Y, obj2X - obj1X));
        if (angleTarget < 0)
            angleTarget = 360 + angleTarget;

        return angleTarget;
    }

    public static double convertHeadingToDegree(int clientHeading) {
        return clientHeading / 182.04444444444444444444444444444;
    }

    public static Point2D getNewLocationByDistanceAndHeading(int x, int y, int heading, int distance) {
        return getNewLocationByDistanceAndDegree(x, y, MathUtil.convertHeadingToDegree(heading), distance);
    }

    public static Point2D getNewLocationByDistanceAndDegree(int x, int y, double degree, int distance) {
        final double radians = Math.toRadians(degree);
        final int deltaX = (int) (distance * Math.cos(radians));
        final int deltaY = (int) (distance * Math.sin(radians));

        return new Point2D(x + deltaX, y + deltaY);
    }

    public static int calculateHeadingFrom(int obj1X, int obj1Y, int obj2X, int obj2Y) {
        double angleTarget = Math.toDegrees(Math.atan2(obj2Y - obj1Y, obj2X - obj1X));
        if (angleTarget < 0)
            angleTarget = 360 + angleTarget;

        return (int) (angleTarget * 182.04444444444444444444444444444);
    }

    /**
     * Эта проверка включает радиус столкновения обоих персонажей.<br>
     * Используется для проведения точных проверок (skill casts, known list, etc).
     *
     * @param range        Диапазон для использования в качестве проверки.
     * @param obj1         Позиция 1 для проверки.
     * @param obj2         Позиция 2 для проверки.
     * @param includeZAxis Включать или нет проверку по оси Z.
     * @return true, если оба объекта находятся в заданном радиусе.
     */
    public static boolean checkIfInRange(int range, WorldObject obj1, WorldObject obj2, boolean includeZAxis) {
        if (obj1 == null || obj2 == null)
            return false;

        if (range == -1) {
            return true; // not limited
        }

        double rad = 0;
        if (obj1 instanceof Creature)
            rad += ((Creature) obj1).getCollisionRadius();

        return check(range, obj1, obj2, includeZAxis, rad, obj2.getX(), obj2.getY(), obj2.getZ());
    }

    public static boolean checkIfInRange(int range, WorldObject obj, Location loc, boolean includeZAxis) {
        if (obj == null || loc.equals(Location.DUMMY_LOC)) {
            return false;
        }

        if (range == -1) {
            return true; // not limited
        }

        double rad = 0;
        return check(range, obj, obj, includeZAxis, rad, loc.getX(), loc.getY(), loc.getZ());
    }

    /**
     * Возвращает округленное значение val до указанного количества цифр после десятичной точки.<br>
     * (На основе функции round() в PHP)
     *
     * @param val       значение для округления.
     * @param numPlaces количество знаков после запятой.
     * @return округленное значение типа float.
     */
    public static float roundTo(float val, int numPlaces) {
        if (numPlaces <= 1) {
            return Math.round(val);
        }
        float exponent = (float) Math.pow(10, numPlaces);
        return (Math.round(val * exponent) / exponent);
    }

    private static boolean check(int range, WorldObject obj1, WorldObject obj2, boolean includeZAxis, double rad, int x, int y, double z) {
        if (obj2 instanceof Creature) {
            rad += ((Creature) obj2).getCollisionRadius();
        }

        double dx = obj1.getX() - x;
        double dy = obj1.getY() - y;

        double v = range * range + 2 * range * rad + rad * rad;
        if (includeZAxis) {
            double dz = obj1.getZ() - z;
            double d = dx * dx + dy * dy + dz * dz;

            return d <= v;
        }

        double d = dx * dx + dy * dy;
        return d <= v;
    }
}
