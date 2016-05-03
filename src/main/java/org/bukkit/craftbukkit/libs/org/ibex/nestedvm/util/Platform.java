package org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.text.DateFormatSymbols;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.TimeZone;

public abstract class Platform {

    private static final Platform p;
    static Class class$org$ibex$nestedvm$util$Platform;

    public static String getProperty(String s) {
        try {
            return System.getProperty(s);
        } catch (SecurityException securityexception) {
            return null;
        }
    }

    abstract boolean _atomicCreateFile(File file) throws IOException;

    public static boolean atomicCreateFile(File file) throws IOException {
        return Platform.p._atomicCreateFile(file);
    }

    abstract Seekable.Lock _lockFile(Seekable seekable, RandomAccessFile randomaccessfile, long i, long j, boolean flag) throws IOException;

    public static Seekable.Lock lockFile(Seekable seekable, RandomAccessFile randomaccessfile, long i, long j, boolean flag) throws IOException {
        return Platform.p._lockFile(seekable, randomaccessfile, i, j, flag);
    }

    abstract void _socketHalfClose(Socket socket, boolean flag) throws IOException;

    public static void socketHalfClose(Socket socket, boolean flag) throws IOException {
        Platform.p._socketHalfClose(socket, flag);
    }

    abstract void _socketSetKeepAlive(Socket socket, boolean flag) throws SocketException;

    public static void socketSetKeepAlive(Socket socket, boolean flag) throws SocketException {
        Platform.p._socketSetKeepAlive(socket, flag);
    }

    abstract InetAddress _inetAddressFromBytes(byte[] abyte) throws UnknownHostException;

    public static InetAddress inetAddressFromBytes(byte[] abyte) throws UnknownHostException {
        return Platform.p._inetAddressFromBytes(abyte);
    }

    abstract String _timeZoneGetDisplayName(TimeZone timezone, boolean flag, boolean flag1, Locale locale);

    public static String timeZoneGetDisplayName(TimeZone timezone, boolean flag, boolean flag1, Locale locale) {
        return Platform.p._timeZoneGetDisplayName(timezone, flag, flag1, locale);
    }

    public static String timeZoneGetDisplayName(TimeZone timezone, boolean flag, boolean flag1) {
        return timeZoneGetDisplayName(timezone, flag, flag1, Locale.getDefault());
    }

    abstract void _setFileLength(RandomAccessFile randomaccessfile, int i) throws IOException;

    public static void setFileLength(RandomAccessFile randomaccessfile, int i) throws IOException {
        Platform.p._setFileLength(randomaccessfile, i);
    }

    abstract File[] _listRoots();

    public static File[] listRoots() {
        return Platform.p._listRoots();
    }

    abstract File _getRoot(File file);

    public static File getRoot(File file) {
        return Platform.p._getRoot(file);
    }

    static Class class$(String s) {
        try {
            return Class.forName(s);
        } catch (ClassNotFoundException classnotfoundexception) {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    static {
        float f;

        try {
            if (getProperty("java.vm.name").equals("SableVM")) {
                f = 1.2F;
            } else {
                f = Float.valueOf(getProperty("java.specification.version")).floatValue();
            }
        } catch (Exception exception) {
            System.err.println("WARNING: " + exception + " while trying to find jvm version -  assuming 1.1");
            f = 1.1F;
        }

        String s;

        if (f >= 1.4F) {
            s = "Jdk14";
        } else if (f >= 1.3F) {
            s = "Jdk13";
        } else if (f >= 1.2F) {
            s = "Jdk12";
        } else {
            if (f < 1.1F) {
                throw new Error("JVM Specification version: " + f + " is too old. (see org.ibex.util.Platform to add support)");
            }

            s = "Jdk11";
        }

        try {
            p = (Platform) Class.forName((Platform.class$org$ibex$nestedvm$util$Platform == null ? (Platform.class$org$ibex$nestedvm$util$Platform = class$("org.bukkit.craftbukkit.libs.org.ibex.nestedvm.util.Platform")) : Platform.class$org$ibex$nestedvm$util$Platform).getName() + "$" + s).newInstance();
        } catch (Exception exception1) {
            exception1.printStackTrace();
            throw new Error("Error instansiating platform class");
        }
    }

    private static final class Jdk14FileLock extends Seekable.Lock {

        private final Seekable s;
        private final FileLock l;

        Jdk14FileLock(Seekable seekable, FileLock filelock) {
            this.s = seekable;
            this.l = filelock;
        }

        public Seekable seekable() {
            return this.s;
        }

        public boolean isShared() {
            return this.l.isShared();
        }

        public boolean isValid() {
            return this.l.isValid();
        }

        public void release() throws IOException {
            this.l.release();
        }

        public long position() {
            return this.l.position();
        }

        public long size() {
            return this.l.size();
        }

        public String toString() {
            return this.l.toString();
        }
    }

    static class Jdk14 extends Platform.Jdk13 {

        InetAddress _inetAddressFromBytes(byte[] abyte) throws UnknownHostException {
            return InetAddress.getByAddress(abyte);
        }

        Seekable.Lock _lockFile(Seekable seekable, RandomAccessFile randomaccessfile, long i, long j, boolean flag) throws IOException {
            FileLock filelock;

            try {
                filelock = i == 0L && j == 0L ? randomaccessfile.getChannel().lock() : randomaccessfile.getChannel().tryLock(i, j, flag);
            } catch (OverlappingFileLockException overlappingfilelockexception) {
                filelock = null;
            }

            return filelock == null ? null : new Platform.Jdk14FileLock(seekable, filelock);
        }
    }

    static class Jdk13 extends Platform.Jdk12 {

        void _socketHalfClose(Socket socket, boolean flag) throws IOException {
            if (flag) {
                socket.shutdownOutput();
            } else {
                socket.shutdownInput();
            }

        }

        void _socketSetKeepAlive(Socket socket, boolean flag) throws SocketException {
            socket.setKeepAlive(flag);
        }
    }

    static class Jdk12 extends Platform.Jdk11 {

        boolean _atomicCreateFile(File file) throws IOException {
            return file.createNewFile();
        }

        String _timeZoneGetDisplayName(TimeZone timezone, boolean flag, boolean flag1, Locale locale) {
            return timezone.getDisplayName(flag, flag1 ? 1 : 0, locale);
        }

        void _setFileLength(RandomAccessFile randomaccessfile, int i) throws IOException {
            randomaccessfile.setLength((long) i);
        }

        File[] _listRoots() {
            return File.listRoots();
        }
    }

    static class Jdk11 extends Platform {

        boolean _atomicCreateFile(File file) throws IOException {
            if (file.exists()) {
                return false;
            } else {
                (new FileOutputStream(file)).close();
                return true;
            }
        }

        Seekable.Lock _lockFile(Seekable seekable, RandomAccessFile randomaccessfile, long i, long j, boolean flag) throws IOException {
            throw new IOException("file locking requires jdk 1.4+");
        }

        void _socketHalfClose(Socket socket, boolean flag) throws IOException {
            throw new IOException("half closing sockets not supported");
        }

        InetAddress _inetAddressFromBytes(byte[] abyte) throws UnknownHostException {
            if (abyte.length != 4) {
                throw new UnknownHostException("only ipv4 addrs supported");
            } else {
                return InetAddress.getByName("" + (abyte[0] & 255) + "." + (abyte[1] & 255) + "." + (abyte[2] & 255) + "." + (abyte[3] & 255));
            }
        }

        void _socketSetKeepAlive(Socket socket, boolean flag) throws SocketException {
            if (flag) {
                throw new SocketException("keepalive not supported");
            }
        }

        String _timeZoneGetDisplayName(TimeZone timezone, boolean flag, boolean flag1, Locale locale) {
            String[][] astring = (new DateFormatSymbols(locale)).getZoneStrings();
            String s = timezone.getID();

            for (int i = 0; i < astring.length; ++i) {
                if (astring[i][0].equals(s)) {
                    return astring[i][flag ? (flag1 ? 3 : 4) : (flag1 ? 1 : 2)];
                }
            }

            StringBuffer stringbuffer = new StringBuffer("GMT");
            int j = timezone.getRawOffset() / 1000;

            if (j < 0) {
                stringbuffer.append("-");
                j = -j;
            } else {
                stringbuffer.append("+");
            }

            stringbuffer.append(j / 3600);
            j %= 3600;
            if (j > 0) {
                stringbuffer.append(":").append(j / 60);
            }

            j %= 60;
            if (j > 0) {
                stringbuffer.append(":").append(j);
            }

            return stringbuffer.toString();
        }

        void _setFileLength(RandomAccessFile randomaccessfile, int i) throws IOException {
            FileInputStream fileinputstream = new FileInputStream(randomaccessfile.getFD());
            FileOutputStream fileoutputstream = new FileOutputStream(randomaccessfile.getFD());

            byte[] abyte;
            int j;

            for (abyte = new byte[1024]; i > 0; i -= j) {
                j = fileinputstream.read(abyte, 0, Math.min(i, abyte.length));
                if (j == -1) {
                    break;
                }

                fileoutputstream.write(abyte, 0, j);
            }

            if (i != 0) {
                for (j = 0; j < abyte.length; ++j) {
                    abyte[j] = 0;
                }

                while (i > 0) {
                    fileoutputstream.write(abyte, 0, Math.min(i, abyte.length));
                    i -= abyte.length;
                }

            }
        }

        RandomAccessFile _truncatedRandomAccessFile(File file, String s) throws IOException {
            (new FileOutputStream(file)).close();
            return new RandomAccessFile(file, s);
        }

        File[] _listRoots() {
            String[] astring = new String[] { "java.home", "java.class.path", "java.library.path", "java.io.tmpdir", "java.ext.dirs", "user.home", "user.dir"};
            Hashtable hashtable = new Hashtable();

            for (int i = 0; i < astring.length; ++i) {
                String s = getProperty(astring[i]);
                int j;

                if (s != null) {
                    do {
                        String s1 = s;

                        if ((j = s.indexOf(File.pathSeparatorChar)) != -1) {
                            s1 = s.substring(0, j);
                            s = s.substring(j + 1);
                        }

                        File file = getRoot(new File(s1));

                        hashtable.put(file, Boolean.TRUE);
                    } while (j != -1);
                }
            }

            File[] afile = new File[hashtable.size()];
            int k = 0;

            for (Enumeration enumeration = hashtable.keys(); enumeration.hasMoreElements(); afile[k++] = (File) enumeration.nextElement()) {
                ;
            }

            return afile;
        }

        File _getRoot(File file) {
            if (!file.isAbsolute()) {
                file = new File(file.getAbsolutePath());
            }

            String s;

            while ((s = file.getParent()) != null) {
                file = new File(s);
            }

            if (file.getPath().length() == 0) {
                file = new File("/");
            }

            return file;
        }
    }
}
