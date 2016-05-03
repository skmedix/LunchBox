package org.bukkit.util;

public class EulerAngle {

    public static final EulerAngle ZERO = new EulerAngle(0.0D, 0.0D, 0.0D);
    private final double x;
    private final double y;
    private final double z;

    public EulerAngle(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }

    public EulerAngle setX(double x) {
        return new EulerAngle(x, this.y, this.z);
    }

    public EulerAngle setY(double y) {
        return new EulerAngle(this.x, y, this.z);
    }

    public EulerAngle setZ(double z) {
        return new EulerAngle(this.x, this.y, z);
    }

    public EulerAngle add(double x, double y, double z) {
        return new EulerAngle(this.x + x, this.y + y, this.z + z);
    }

    public EulerAngle subtract(double x, double y, double z) {
        return this.add(-x, -y, -z);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            EulerAngle that = (EulerAngle) o;

            return Double.compare(that.x, this.x) == 0 && Double.compare(that.y, this.y) == 0 && Double.compare(that.z, this.z) == 0;
        } else {
            return false;
        }
    }

    public int hashCode() {
        long temp = Double.doubleToLongBits(this.x);
        int result = (int) (temp ^ temp >>> 32);

        temp = Double.doubleToLongBits(this.y);
        result = 31 * result + (int) (temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.z);
        result = 31 * result + (int) (temp ^ temp >>> 32);
        return result;
    }
}
