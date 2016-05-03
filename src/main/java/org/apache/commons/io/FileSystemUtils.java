package org.apache.commons.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class FileSystemUtils {

    private static final FileSystemUtils INSTANCE = new FileSystemUtils();
    private static final int INIT_PROBLEM = -1;
    private static final int OTHER = 0;
    private static final int WINDOWS = 1;
    private static final int UNIX = 2;
    private static final int POSIX_UNIX = 3;
    private static final int OS;
    private static final String DF;

    /** @deprecated */
    @Deprecated
    public static long freeSpace(String s) throws IOException {
        return FileSystemUtils.INSTANCE.freeSpaceOS(s, FileSystemUtils.OS, false, -1L);
    }

    public static long freeSpaceKb(String s) throws IOException {
        return freeSpaceKb(s, -1L);
    }

    public static long freeSpaceKb(String s, long i) throws IOException {
        return FileSystemUtils.INSTANCE.freeSpaceOS(s, FileSystemUtils.OS, true, i);
    }

    public static long freeSpaceKb() throws IOException {
        return freeSpaceKb(-1L);
    }

    public static long freeSpaceKb(long i) throws IOException {
        return freeSpaceKb((new File(".")).getAbsolutePath(), i);
    }

    long freeSpaceOS(String s, int i, boolean flag, long j) throws IOException {
        if (s == null) {
            throw new IllegalArgumentException("Path must not be empty");
        } else {
            switch (i) {
            case 0:
                throw new IllegalStateException("Unsupported operating system");

            case 1:
                return flag ? this.freeSpaceWindows(s, j) / 1024L : this.freeSpaceWindows(s, j);

            case 2:
                return this.freeSpaceUnix(s, flag, false, j);

            case 3:
                return this.freeSpaceUnix(s, flag, true, j);

            default:
                throw new IllegalStateException("Exception caught when determining operating system");
            }
        }
    }

    long freeSpaceWindows(String s, long i) throws IOException {
        s = FilenameUtils.normalize(s, false);
        if (s.length() > 0 && s.charAt(0) != 34) {
            s = "\"" + s + "\"";
        }

        String[] astring = new String[] { "cmd.exe", "/C", "dir /a /-c " + s};
        List list = this.performCommand(astring, Integer.MAX_VALUE, i);

        for (int j = list.size() - 1; j >= 0; --j) {
            String s1 = (String) list.get(j);

            if (s1.length() > 0) {
                return this.parseDir(s1, s);
            }
        }

        throw new IOException("Command line \'dir /-c\' did not return any info for path \'" + s + "\'");
    }

    long parseDir(String s, String s1) throws IOException {
        int i = 0;
        int j = 0;

        int k;
        char c0;

        for (k = s.length() - 1; k >= 0; --k) {
            c0 = s.charAt(k);
            if (Character.isDigit(c0)) {
                j = k + 1;
                break;
            }
        }

        while (k >= 0) {
            c0 = s.charAt(k);
            if (!Character.isDigit(c0) && c0 != 44 && c0 != 46) {
                i = k + 1;
                break;
            }

            --k;
        }

        if (k < 0) {
            throw new IOException("Command line \'dir /-c\' did not return valid info for path \'" + s1 + "\'");
        } else {
            StringBuilder stringbuilder = new StringBuilder(s.substring(i, j));

            for (int l = 0; l < stringbuilder.length(); ++l) {
                if (stringbuilder.charAt(l) == 44 || stringbuilder.charAt(l) == 46) {
                    stringbuilder.deleteCharAt(l--);
                }
            }

            return this.parseBytes(stringbuilder.toString(), s1);
        }
    }

    long freeSpaceUnix(String s, boolean flag, boolean flag1, long i) throws IOException {
        if (s.length() == 0) {
            throw new IllegalArgumentException("Path must not be empty");
        } else {
            String s1 = "-";

            if (flag) {
                s1 = s1 + "k";
            }

            if (flag1) {
                s1 = s1 + "P";
            }

            String[] astring = s1.length() > 1 ? new String[] { FileSystemUtils.DF, s1, s} : new String[] { FileSystemUtils.DF, s};
            List list = this.performCommand(astring, 3, i);

            if (list.size() < 2) {
                throw new IOException("Command line \'" + FileSystemUtils.DF + "\' did not return info as expected " + "for path \'" + s + "\'- response was " + list);
            } else {
                String s2 = (String) list.get(1);
                StringTokenizer stringtokenizer = new StringTokenizer(s2, " ");
                String s3;

                if (stringtokenizer.countTokens() < 4) {
                    if (stringtokenizer.countTokens() != 1 || list.size() < 3) {
                        throw new IOException("Command line \'" + FileSystemUtils.DF + "\' did not return data as expected " + "for path \'" + s + "\'- check path is valid");
                    }

                    s3 = (String) list.get(2);
                    stringtokenizer = new StringTokenizer(s3, " ");
                } else {
                    stringtokenizer.nextToken();
                }

                stringtokenizer.nextToken();
                stringtokenizer.nextToken();
                s3 = stringtokenizer.nextToken();
                return this.parseBytes(s3, s);
            }
        }
    }

    long parseBytes(String s, String s1) throws IOException {
        try {
            long i = Long.parseLong(s);

            if (i < 0L) {
                throw new IOException("Command line \'" + FileSystemUtils.DF + "\' did not find free space in response " + "for path \'" + s1 + "\'- check path is valid");
            } else {
                return i;
            }
        } catch (NumberFormatException numberformatexception) {
            throw new IOExceptionWithCause("Command line \'" + FileSystemUtils.DF + "\' did not return numeric data as expected " + "for path \'" + s1 + "\'- check path is valid", numberformatexception);
        }
    }

    List performCommand(String[] astring, int i, long j) throws IOException {
        ArrayList arraylist = new ArrayList(20);
        Process process = null;
        InputStream inputstream = null;
        OutputStream outputstream = null;
        InputStream inputstream1 = null;
        BufferedReader bufferedreader = null;

        ArrayList arraylist1;

        try {
            Thread thread = ThreadMonitor.start(j);

            process = this.openProcess(astring);
            inputstream = process.getInputStream();
            outputstream = process.getOutputStream();
            inputstream1 = process.getErrorStream();
            bufferedreader = new BufferedReader(new InputStreamReader(inputstream));

            for (String s = bufferedreader.readLine(); s != null && arraylist.size() < i; s = bufferedreader.readLine()) {
                s = s.toLowerCase(Locale.ENGLISH).trim();
                arraylist.add(s);
            }

            process.waitFor();
            ThreadMonitor.stop(thread);
            if (process.exitValue() != 0) {
                throw new IOException("Command line returned OS error code \'" + process.exitValue() + "\' for command " + Arrays.asList(astring));
            }

            if (arraylist.isEmpty()) {
                throw new IOException("Command line did not return any info for command " + Arrays.asList(astring));
            }

            arraylist1 = arraylist;
        } catch (InterruptedException interruptedexception) {
            throw new IOExceptionWithCause("Command line threw an InterruptedException for command " + Arrays.asList(astring) + " timeout=" + j, interruptedexception);
        } finally {
            IOUtils.closeQuietly(inputstream);
            IOUtils.closeQuietly(outputstream);
            IOUtils.closeQuietly(inputstream1);
            IOUtils.closeQuietly((Reader) bufferedreader);
            if (process != null) {
                process.destroy();
            }

        }

        return arraylist1;
    }

    Process openProcess(String[] astring) throws IOException {
        return Runtime.getRuntime().exec(astring);
    }

    static {
        boolean flag = false;
        String s = "df";

        byte b0;

        try {
            String s1 = System.getProperty("os.name");

            if (s1 == null) {
                throw new IOException("os.name not found");
            }

            s1 = s1.toLowerCase(Locale.ENGLISH);
            if (s1.indexOf("windows") != -1) {
                b0 = 1;
            } else if (s1.indexOf("linux") == -1 && s1.indexOf("mpe/ix") == -1 && s1.indexOf("freebsd") == -1 && s1.indexOf("irix") == -1 && s1.indexOf("digital unix") == -1 && s1.indexOf("unix") == -1 && s1.indexOf("mac os x") == -1) {
                if (s1.indexOf("sun os") == -1 && s1.indexOf("sunos") == -1 && s1.indexOf("solaris") == -1) {
                    if (s1.indexOf("hp-ux") == -1 && s1.indexOf("aix") == -1) {
                        b0 = 0;
                    } else {
                        b0 = 3;
                    }
                } else {
                    b0 = 3;
                    s = "/usr/xpg4/bin/df";
                }
            } else {
                b0 = 2;
            }
        } catch (Exception exception) {
            b0 = -1;
        }

        OS = b0;
        DF = s;
    }
}
