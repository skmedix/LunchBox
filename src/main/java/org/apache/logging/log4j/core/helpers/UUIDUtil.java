package org.apache.logging.log4j.core.helpers;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.util.PropertiesUtil;

public final class UUIDUtil {

    public static final String UUID_SEQUENCE = "org.apache.logging.log4j.uuidSequence";
    private static final String ASSIGNED_SEQUENCES = "org.apache.logging.log4j.assignedSequences";
    private static AtomicInteger count = new AtomicInteger(0);
    private static final long TYPE1 = 4096L;
    private static final byte VARIANT = -128;
    private static final int SEQUENCE_MASK = 16383;
    private static final long NUM_100NS_INTERVALS_SINCE_UUID_EPOCH = 122192928000000000L;
    private static long uuidSequence = PropertiesUtil.getProperties().getLongProperty("org.apache.logging.log4j.uuidSequence", 0L);
    private static long least;
    private static final long LOW_MASK = 4294967295L;
    private static final long MID_MASK = 281470681743360L;
    private static final long HIGH_MASK = 1152640029630136320L;
    private static final int NODE_SIZE = 8;
    private static final int SHIFT_2 = 16;
    private static final int SHIFT_4 = 32;
    private static final int SHIFT_6 = 48;
    private static final int HUNDRED_NANOS_PER_MILLI = 10000;

    public static UUID getTimeBasedUUID() {
        long i = System.currentTimeMillis() * 10000L + 122192928000000000L + (long) (UUIDUtil.count.incrementAndGet() % 10000);
        long j = (i & 4294967295L) << 32;
        long k = (i & 281470681743360L) >> 16;
        long l = (i & 1152640029630136320L) >> 48;
        long i1 = j | k | 4096L | l;

        return new UUID(i1, UUIDUtil.least);
    }

    static {
        byte[] abyte = null;

        try {
            InetAddress inetaddress = InetAddress.getLocalHost();

            try {
                NetworkInterface networkinterface = NetworkInterface.getByInetAddress(inetaddress);

                if (networkinterface != null && !networkinterface.isLoopback() && networkinterface.isUp()) {
                    Method method = networkinterface.getClass().getMethod("getHardwareAddress", new Class[0]);

                    if (method != null) {
                        abyte = (byte[]) ((byte[]) method.invoke(networkinterface, new Object[0]));
                    }
                }

                if (abyte == null) {
                    Enumeration enumeration = NetworkInterface.getNetworkInterfaces();

                    while (enumeration.hasMoreElements() && abyte == null) {
                        networkinterface = (NetworkInterface) enumeration.nextElement();
                        if (networkinterface != null && networkinterface.isUp() && !networkinterface.isLoopback()) {
                            Method method1 = networkinterface.getClass().getMethod("getHardwareAddress", new Class[0]);

                            if (method1 != null) {
                                abyte = (byte[]) ((byte[]) method1.invoke(networkinterface, new Object[0]));
                            }
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            if (abyte == null || abyte.length == 0) {
                abyte = inetaddress.getAddress();
            }
        } catch (UnknownHostException unknownhostexception) {
            ;
        }

        SecureRandom securerandom = new SecureRandom();

        if (abyte == null || abyte.length == 0) {
            abyte = new byte[6];
            securerandom.nextBytes(abyte);
        }

        int i = abyte.length >= 6 ? 6 : abyte.length;
        int j = abyte.length >= 6 ? abyte.length - 6 : 0;
        byte[] abyte1 = new byte[8];

        abyte1[0] = -128;
        abyte1[1] = 0;

        for (int k = 2; k < 8; ++k) {
            abyte1[k] = 0;
        }

        System.arraycopy(abyte, j, abyte1, j + 2, i);
        ByteBuffer bytebuffer = ByteBuffer.wrap(abyte1);
        long l = UUIDUtil.uuidSequence;
        Runtime runtime = Runtime.getRuntime();

        synchronized (runtime) {
            String s = PropertiesUtil.getProperties().getStringProperty("org.apache.logging.log4j.assignedSequences");
            long[] along;
            int i1;

            if (s == null) {
                along = new long[0];
            } else {
                String[] astring = s.split(",");

                along = new long[astring.length];
                int j1 = 0;
                String[] astring1 = astring;

                i1 = astring.length;

                for (int k1 = 0; k1 < i1; ++k1) {
                    String s1 = astring1[k1];

                    along[j1] = Long.parseLong(s1);
                    ++j1;
                }
            }

            if (l == 0L) {
                l = securerandom.nextLong();
            }

            l &= 16383L;

            while (true) {
                boolean flag = false;
                long[] along1 = along;
                int l1 = along.length;

                for (i1 = 0; i1 < l1; ++i1) {
                    long i2 = along1[i1];

                    if (i2 == l) {
                        flag = true;
                        break;
                    }
                }

                if (flag) {
                    l = l + 1L & 16383L;
                }

                if (!flag) {
                    s = s == null ? Long.toString(l) : s + "," + Long.toString(l);
                    System.setProperty("org.apache.logging.log4j.assignedSequences", s);
                    break;
                }
            }
        }

        UUIDUtil.least = bytebuffer.getLong() | l << 48;
    }
}
