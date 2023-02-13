package com.l2j.emu.gameserver.model.location;

import java.util.Objects;

public class Point2D {
    protected int x;
    protected int y;

    public Point2D(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(final int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(final int y) {
        this.y = y;
    }

    public void set(final int x, final int y) {
        setX(x);
        setY(y);
    }

    public boolean equals(final int x, final int y) {
        return this.x == x && this.y == y;
    }

    public void clean() {
        this.x = 0;
        this.y = 0;
    }

    public void setFleeing(final Point2D referenceLoc, final int distance) {
        final int differenceByX = referenceLoc.getX() - x;
        final int differenceByY = referenceLoc.getY() - y;
        final int division = Math.abs(differenceByX / differenceByY);
        final int newY = distance / (division + 1);
        final int newX = y * division;
        x += (differenceByX < 0 ? newX : (-newX));
        y += (differenceByY < 0 ? newY : (-newY));
    }

    public double distance2D(final Point2D point) {
        return distance2D(point.getX(), point.getY());
    }

    public double distance2D(final int x, final int y) {
        final double dx = (double) x - x;
        final double dy = (double) y - y;
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    public boolean isIn2DRadius(final Point2D point, final int radius) {
        return isIn2DRadius(point.getX(), point.getY(), radius);
    }

    public boolean isIn2DRadius(final int x, final int y, final int radius) {
        return distance2D(x, y) < radius;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Point2D point2D = (Point2D) obj;
        return x == point2D.x && y == point2D.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        return new Point2D(x, y);
    }

    @Override
    public String toString() {
        return x + " , " + y;
    }
}
