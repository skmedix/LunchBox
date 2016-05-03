package org.apache.commons.lang3.math;

public class IEEE754rUtils {

    public static double min(double[] adouble) {
        if (adouble == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (adouble.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            double d0 = adouble[0];

            for (int i = 1; i < adouble.length; ++i) {
                d0 = min(adouble[i], d0);
            }

            return d0;
        }
    }

    public static float min(float[] afloat) {
        if (afloat == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (afloat.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            float f = afloat[0];

            for (int i = 1; i < afloat.length; ++i) {
                f = min(afloat[i], f);
            }

            return f;
        }
    }

    public static double min(double d0, double d1, double d2) {
        return min(min(d0, d1), d2);
    }

    public static double min(double d0, double d1) {
        return Double.isNaN(d0) ? d1 : (Double.isNaN(d1) ? d0 : Math.min(d0, d1));
    }

    public static float min(float f, float f1, float f2) {
        return min(min(f, f1), f2);
    }

    public static float min(float f, float f1) {
        return Float.isNaN(f) ? f1 : (Float.isNaN(f1) ? f : Math.min(f, f1));
    }

    public static double max(double[] adouble) {
        if (adouble == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (adouble.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            double d0 = adouble[0];

            for (int i = 1; i < adouble.length; ++i) {
                d0 = max(adouble[i], d0);
            }

            return d0;
        }
    }

    public static float max(float[] afloat) {
        if (afloat == null) {
            throw new IllegalArgumentException("The Array must not be null");
        } else if (afloat.length == 0) {
            throw new IllegalArgumentException("Array cannot be empty.");
        } else {
            float f = afloat[0];

            for (int i = 1; i < afloat.length; ++i) {
                f = max(afloat[i], f);
            }

            return f;
        }
    }

    public static double max(double d0, double d1, double d2) {
        return max(max(d0, d1), d2);
    }

    public static double max(double d0, double d1) {
        return Double.isNaN(d0) ? d1 : (Double.isNaN(d1) ? d0 : Math.max(d0, d1));
    }

    public static float max(float f, float f1, float f2) {
        return max(max(f, f1), f2);
    }

    public static float max(float f, float f1) {
        return Float.isNaN(f) ? f1 : (Float.isNaN(f1) ? f : Math.max(f, f1));
    }
}
