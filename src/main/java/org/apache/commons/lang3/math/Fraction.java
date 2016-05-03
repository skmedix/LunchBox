package org.apache.commons.lang3.math;

import java.math.BigInteger;

public final class Fraction extends Number implements Comparable {

    private static final long serialVersionUID = 65382027393090L;
    public static final Fraction ZERO = new Fraction(0, 1);
    public static final Fraction ONE = new Fraction(1, 1);
    public static final Fraction ONE_HALF = new Fraction(1, 2);
    public static final Fraction ONE_THIRD = new Fraction(1, 3);
    public static final Fraction TWO_THIRDS = new Fraction(2, 3);
    public static final Fraction ONE_QUARTER = new Fraction(1, 4);
    public static final Fraction TWO_QUARTERS = new Fraction(2, 4);
    public static final Fraction THREE_QUARTERS = new Fraction(3, 4);
    public static final Fraction ONE_FIFTH = new Fraction(1, 5);
    public static final Fraction TWO_FIFTHS = new Fraction(2, 5);
    public static final Fraction THREE_FIFTHS = new Fraction(3, 5);
    public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);
    private final int numerator;
    private final int denominator;
    private transient int hashCode = 0;
    private transient String toString = null;
    private transient String toProperString = null;

    private Fraction(int i, int j) {
        this.numerator = i;
        this.denominator = j;
    }

    public static Fraction getFraction(int i, int j) {
        if (j == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        } else {
            if (j < 0) {
                if (i == Integer.MIN_VALUE || j == Integer.MIN_VALUE) {
                    throw new ArithmeticException("overflow: can\'t negate");
                }

                i = -i;
                j = -j;
            }

            return new Fraction(i, j);
        }
    }

    public static Fraction getFraction(int i, int j, int k) {
        if (k == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        } else if (k < 0) {
            throw new ArithmeticException("The denominator must not be negative");
        } else if (j < 0) {
            throw new ArithmeticException("The numerator must not be negative");
        } else {
            long l;

            if (i < 0) {
                l = (long) i * (long) k - (long) j;
            } else {
                l = (long) i * (long) k + (long) j;
            }

            if (l >= -2147483648L && l <= 2147483647L) {
                return new Fraction((int) l, k);
            } else {
                throw new ArithmeticException("Numerator too large to represent as an Integer.");
            }
        }
    }

    public static Fraction getReducedFraction(int i, int j) {
        if (j == 0) {
            throw new ArithmeticException("The denominator must not be zero");
        } else if (i == 0) {
            return Fraction.ZERO;
        } else {
            if (j == Integer.MIN_VALUE && (i & 1) == 0) {
                i /= 2;
                j /= 2;
            }

            if (j < 0) {
                if (i == Integer.MIN_VALUE || j == Integer.MIN_VALUE) {
                    throw new ArithmeticException("overflow: can\'t negate");
                }

                i = -i;
                j = -j;
            }

            int k = greatestCommonDivisor(i, j);

            i /= k;
            j /= k;
            return new Fraction(i, j);
        }
    }

    public static Fraction getFraction(double d0) {
        int i = d0 < 0.0D ? -1 : 1;

        d0 = Math.abs(d0);
        if (d0 <= 2.147483647E9D && !Double.isNaN(d0)) {
            int j = (int) d0;

            d0 -= (double) j;
            int k = 0;
            int l = 1;
            int i1 = 1;
            int j1 = 0;
            boolean flag = false;
            boolean flag1 = false;
            int k1 = (int) d0;
            boolean flag2 = false;
            double d1 = 1.0D;
            double d2 = 0.0D;
            double d3 = d0 - (double) k1;
            double d4 = 0.0D;
            double d5 = Double.MAX_VALUE;
            int l1 = 1;

            double d6;
            int i2;

            do {
                d6 = d5;
                int j2 = (int) (d1 / d3);

                d4 = d1 - (double) j2 * d3;
                int k2 = k1 * i1 + k;

                i2 = k1 * j1 + l;
                double d7 = (double) k2 / (double) i2;

                d5 = Math.abs(d0 - d7);
                k1 = j2;
                d1 = d3;
                d3 = d4;
                k = i1;
                l = j1;
                i1 = k2;
                j1 = i2;
                ++l1;
            } while (d6 > d5 && i2 <= 10000 && i2 > 0 && l1 < 25);

            if (l1 == 25) {
                throw new ArithmeticException("Unable to convert double to fraction");
            } else {
                return getReducedFraction((k + j * l) * i, l);
            }
        } else {
            throw new ArithmeticException("The value must not be greater than Integer.MAX_VALUE or NaN");
        }
    }

    public static Fraction getFraction(String s) {
        if (s == null) {
            throw new IllegalArgumentException("The string must not be null");
        } else {
            int i = s.indexOf(46);

            if (i >= 0) {
                return getFraction(Double.parseDouble(s));
            } else {
                i = s.indexOf(32);
                int j;
                int k;

                if (i > 0) {
                    j = Integer.parseInt(s.substring(0, i));
                    s = s.substring(i + 1);
                    i = s.indexOf(47);
                    if (i < 0) {
                        throw new NumberFormatException("The fraction could not be parsed as the format X Y/Z");
                    } else {
                        k = Integer.parseInt(s.substring(0, i));
                        int l = Integer.parseInt(s.substring(i + 1));

                        return getFraction(j, k, l);
                    }
                } else {
                    i = s.indexOf(47);
                    if (i < 0) {
                        return getFraction(Integer.parseInt(s), 1);
                    } else {
                        j = Integer.parseInt(s.substring(0, i));
                        k = Integer.parseInt(s.substring(i + 1));
                        return getFraction(j, k);
                    }
                }
            }
        }
    }

    public int getNumerator() {
        return this.numerator;
    }

    public int getDenominator() {
        return this.denominator;
    }

    public int getProperNumerator() {
        return Math.abs(this.numerator % this.denominator);
    }

    public int getProperWhole() {
        return this.numerator / this.denominator;
    }

    public int intValue() {
        return this.numerator / this.denominator;
    }

    public long longValue() {
        return (long) this.numerator / (long) this.denominator;
    }

    public float floatValue() {
        return (float) this.numerator / (float) this.denominator;
    }

    public double doubleValue() {
        return (double) this.numerator / (double) this.denominator;
    }

    public Fraction reduce() {
        if (this.numerator == 0) {
            return this.equals(Fraction.ZERO) ? this : Fraction.ZERO;
        } else {
            int i = greatestCommonDivisor(Math.abs(this.numerator), this.denominator);

            return i == 1 ? this : getFraction(this.numerator / i, this.denominator / i);
        }
    }

    public Fraction invert() {
        if (this.numerator == 0) {
            throw new ArithmeticException("Unable to invert zero.");
        } else if (this.numerator == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow: can\'t negate numerator");
        } else {
            return this.numerator < 0 ? new Fraction(-this.denominator, -this.numerator) : new Fraction(this.denominator, this.numerator);
        }
    }

    public Fraction negate() {
        if (this.numerator == Integer.MIN_VALUE) {
            throw new ArithmeticException("overflow: too large to negate");
        } else {
            return new Fraction(-this.numerator, this.denominator);
        }
    }

    public Fraction abs() {
        return this.numerator >= 0 ? this : this.negate();
    }

    public Fraction pow(int i) {
        if (i == 1) {
            return this;
        } else if (i == 0) {
            return Fraction.ONE;
        } else if (i < 0) {
            return i == Integer.MIN_VALUE ? this.invert().pow(2).pow(-(i / 2)) : this.invert().pow(-i);
        } else {
            Fraction fraction = this.multiplyBy(this);

            return i % 2 == 0 ? fraction.pow(i / 2) : fraction.pow(i / 2).multiplyBy(this);
        }
    }

    private static int greatestCommonDivisor(int i, int j) {
        if (i != 0 && j != 0) {
            if (Math.abs(i) != 1 && Math.abs(j) != 1) {
                if (i > 0) {
                    i = -i;
                }

                if (j > 0) {
                    j = -j;
                }

                int k;

                for (k = 0; (i & 1) == 0 && (j & 1) == 0 && k < 31; ++k) {
                    i /= 2;
                    j /= 2;
                }

                if (k == 31) {
                    throw new ArithmeticException("overflow: gcd is 2^31");
                } else {
                    int l = (i & 1) == 1 ? j : -(i / 2);

                    while (true) {
                        while ((l & 1) != 0) {
                            if (l > 0) {
                                i = -l;
                            } else {
                                j = l;
                            }

                            l = (j - i) / 2;
                            if (l == 0) {
                                return -i * (1 << k);
                            }
                        }

                        l /= 2;
                    }
                }
            } else {
                return 1;
            }
        } else if (i != Integer.MIN_VALUE && j != Integer.MIN_VALUE) {
            return Math.abs(i) + Math.abs(j);
        } else {
            throw new ArithmeticException("overflow: gcd is 2^31");
        }
    }

    private static int mulAndCheck(int i, int j) {
        long k = (long) i * (long) j;

        if (k >= -2147483648L && k <= 2147483647L) {
            return (int) k;
        } else {
            throw new ArithmeticException("overflow: mul");
        }
    }

    private static int mulPosAndCheck(int i, int j) {
        long k = (long) i * (long) j;

        if (k > 2147483647L) {
            throw new ArithmeticException("overflow: mulPos");
        } else {
            return (int) k;
        }
    }

    private static int addAndCheck(int i, int j) {
        long k = (long) i + (long) j;

        if (k >= -2147483648L && k <= 2147483647L) {
            return (int) k;
        } else {
            throw new ArithmeticException("overflow: add");
        }
    }

    private static int subAndCheck(int i, int j) {
        long k = (long) i - (long) j;

        if (k >= -2147483648L && k <= 2147483647L) {
            return (int) k;
        } else {
            throw new ArithmeticException("overflow: add");
        }
    }

    public Fraction add(Fraction fraction) {
        return this.addSub(fraction, true);
    }

    public Fraction subtract(Fraction fraction) {
        return this.addSub(fraction, false);
    }

    private Fraction addSub(Fraction fraction, boolean flag) {
        if (fraction == null) {
            throw new IllegalArgumentException("The fraction must not be null");
        } else if (this.numerator == 0) {
            return flag ? fraction : fraction.negate();
        } else if (fraction.numerator == 0) {
            return this;
        } else {
            int i = greatestCommonDivisor(this.denominator, fraction.denominator);

            if (i == 1) {
                int j = mulAndCheck(this.numerator, fraction.denominator);
                int k = mulAndCheck(fraction.numerator, this.denominator);

                return new Fraction(flag ? addAndCheck(j, k) : subAndCheck(j, k), mulPosAndCheck(this.denominator, fraction.denominator));
            } else {
                BigInteger biginteger = BigInteger.valueOf((long) this.numerator).multiply(BigInteger.valueOf((long) (fraction.denominator / i)));
                BigInteger biginteger1 = BigInteger.valueOf((long) fraction.numerator).multiply(BigInteger.valueOf((long) (this.denominator / i)));
                BigInteger biginteger2 = flag ? biginteger.add(biginteger1) : biginteger.subtract(biginteger1);
                int l = biginteger2.mod(BigInteger.valueOf((long) i)).intValue();
                int i1 = l == 0 ? i : greatestCommonDivisor(l, i);
                BigInteger biginteger3 = biginteger2.divide(BigInteger.valueOf((long) i1));

                if (biginteger3.bitLength() > 31) {
                    throw new ArithmeticException("overflow: numerator too large after multiply");
                } else {
                    return new Fraction(biginteger3.intValue(), mulPosAndCheck(this.denominator / i, fraction.denominator / i1));
                }
            }
        }
    }

    public Fraction multiplyBy(Fraction fraction) {
        if (fraction == null) {
            throw new IllegalArgumentException("The fraction must not be null");
        } else if (this.numerator != 0 && fraction.numerator != 0) {
            int i = greatestCommonDivisor(this.numerator, fraction.denominator);
            int j = greatestCommonDivisor(fraction.numerator, this.denominator);

            return getReducedFraction(mulAndCheck(this.numerator / i, fraction.numerator / j), mulPosAndCheck(this.denominator / j, fraction.denominator / i));
        } else {
            return Fraction.ZERO;
        }
    }

    public Fraction divideBy(Fraction fraction) {
        if (fraction == null) {
            throw new IllegalArgumentException("The fraction must not be null");
        } else if (fraction.numerator == 0) {
            throw new ArithmeticException("The fraction to divide by must not be zero");
        } else {
            return this.multiplyBy(fraction.invert());
        }
    }

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        } else if (!(object instanceof Fraction)) {
            return false;
        } else {
            Fraction fraction = (Fraction) object;

            return this.getNumerator() == fraction.getNumerator() && this.getDenominator() == fraction.getDenominator();
        }
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = 37 * (629 + this.getNumerator()) + this.getDenominator();
        }

        return this.hashCode;
    }

    public int compareTo(Fraction fraction) {
        if (this == fraction) {
            return 0;
        } else if (this.numerator == fraction.numerator && this.denominator == fraction.denominator) {
            return 0;
        } else {
            long i = (long) this.numerator * (long) fraction.denominator;
            long j = (long) fraction.numerator * (long) this.denominator;

            return i == j ? 0 : (i < j ? -1 : 1);
        }
    }

    public String toString() {
        if (this.toString == null) {
            this.toString = (new StringBuilder(32)).append(this.getNumerator()).append('/').append(this.getDenominator()).toString();
        }

        return this.toString;
    }

    public String toProperString() {
        if (this.toProperString == null) {
            if (this.numerator == 0) {
                this.toProperString = "0";
            } else if (this.numerator == this.denominator) {
                this.toProperString = "1";
            } else if (this.numerator == -1 * this.denominator) {
                this.toProperString = "-1";
            } else if ((this.numerator > 0 ? -this.numerator : this.numerator) < -this.denominator) {
                int i = this.getProperNumerator();

                if (i == 0) {
                    this.toProperString = Integer.toString(this.getProperWhole());
                } else {
                    this.toProperString = (new StringBuilder(32)).append(this.getProperWhole()).append(' ').append(i).append('/').append(this.getDenominator()).toString();
                }
            } else {
                this.toProperString = (new StringBuilder(32)).append(this.getNumerator()).append('/').append(this.getDenominator()).toString();
            }
        }

        return this.toProperString;
    }
}
