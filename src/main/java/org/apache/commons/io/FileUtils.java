package org.apache.commons.io;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.output.NullOutputStream;

public class FileUtils {

    public static final long ONE_KB = 1024L;
    public static final BigInteger ONE_KB_BI = BigInteger.valueOf(1024L);
    public static final long ONE_MB = 1048576L;
    public static final BigInteger ONE_MB_BI = FileUtils.ONE_KB_BI.multiply(FileUtils.ONE_KB_BI);
    private static final long FILE_COPY_BUFFER_SIZE = 31457280L;
    public static final long ONE_GB = 1073741824L;
    public static final BigInteger ONE_GB_BI = FileUtils.ONE_KB_BI.multiply(FileUtils.ONE_MB_BI);
    public static final long ONE_TB = 1099511627776L;
    public static final BigInteger ONE_TB_BI = FileUtils.ONE_KB_BI.multiply(FileUtils.ONE_GB_BI);
    public static final long ONE_PB = 1125899906842624L;
    public static final BigInteger ONE_PB_BI = FileUtils.ONE_KB_BI.multiply(FileUtils.ONE_TB_BI);
    public static final long ONE_EB = 1152921504606846976L;
    public static final BigInteger ONE_EB_BI = FileUtils.ONE_KB_BI.multiply(FileUtils.ONE_PB_BI);
    public static final BigInteger ONE_ZB = BigInteger.valueOf(1024L).multiply(BigInteger.valueOf(1152921504606846976L));
    public static final BigInteger ONE_YB = FileUtils.ONE_KB_BI.multiply(FileUtils.ONE_ZB);
    public static final File[] EMPTY_FILE_ARRAY = new File[0];
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public static File getFile(File file, String... astring) {
        if (file == null) {
            throw new NullPointerException("directorydirectory must not be null");
        } else if (astring == null) {
            throw new NullPointerException("names must not be null");
        } else {
            File file1 = file;
            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s = astring1[j];

                file1 = new File(file1, s);
            }

            return file1;
        }
    }

    public static File getFile(String... astring) {
        if (astring == null) {
            throw new NullPointerException("names must not be null");
        } else {
            File file = null;
            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s = astring1[j];

                if (file == null) {
                    file = new File(s);
                } else {
                    file = new File(file, s);
                }
            }

            return file;
        }
    }

    public static String getTempDirectoryPath() {
        return System.getProperty("java.io.tmpdir");
    }

    public static File getTempDirectory() {
        return new File(getTempDirectoryPath());
    }

    public static String getUserDirectoryPath() {
        return System.getProperty("user.home");
    }

    public static File getUserDirectory() {
        return new File(getUserDirectoryPath());
    }

    public static FileInputStream openInputStream(File file) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File \'" + file + "\' exists but is a directory");
            } else if (!file.canRead()) {
                throw new IOException("File \'" + file + "\' cannot be read");
            } else {
                return new FileInputStream(file);
            }
        } else {
            throw new FileNotFoundException("File \'" + file + "\' does not exist");
        }
    }

    public static FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }

    public static FileOutputStream openOutputStream(File file, boolean flag) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File \'" + file + "\' exists but is a directory");
            }

            if (!file.canWrite()) {
                throw new IOException("File \'" + file + "\' cannot be written to");
            }
        } else {
            File file1 = file.getParentFile();

            if (file1 != null && !file1.mkdirs() && !file1.isDirectory()) {
                throw new IOException("Directory \'" + file1 + "\' could not be created");
            }
        }

        return new FileOutputStream(file, flag);
    }

    public static String byteCountToDisplaySize(BigInteger biginteger) {
        String s;

        if (biginteger.divide(FileUtils.ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
            s = biginteger.divide(FileUtils.ONE_EB_BI) + " EB";
        } else if (biginteger.divide(FileUtils.ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
            s = biginteger.divide(FileUtils.ONE_PB_BI) + " PB";
        } else if (biginteger.divide(FileUtils.ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
            s = biginteger.divide(FileUtils.ONE_TB_BI) + " TB";
        } else if (biginteger.divide(FileUtils.ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
            s = biginteger.divide(FileUtils.ONE_GB_BI) + " GB";
        } else if (biginteger.divide(FileUtils.ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
            s = biginteger.divide(FileUtils.ONE_MB_BI) + " MB";
        } else if (biginteger.divide(FileUtils.ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
            s = biginteger.divide(FileUtils.ONE_KB_BI) + " KB";
        } else {
            s = biginteger + " bytes";
        }

        return s;
    }

    public static String byteCountToDisplaySize(long i) {
        return byteCountToDisplaySize(BigInteger.valueOf(i));
    }

    public static void touch(File file) throws IOException {
        if (!file.exists()) {
            FileOutputStream fileoutputstream = openOutputStream(file);

            IOUtils.closeQuietly((OutputStream) fileoutputstream);
        }

        boolean flag = file.setLastModified(System.currentTimeMillis());

        if (!flag) {
            throw new IOException("Unable to set the last modification time for " + file);
        }
    }

    public static File[] convertFileCollectionToFileArray(Collection collection) {
        return (File[]) collection.toArray(new File[collection.size()]);
    }

    private static void innerListFiles(Collection collection, File file, IOFileFilter iofilefilter, boolean flag) {
        File[] afile = file.listFiles(iofilefilter);

        if (afile != null) {
            File[] afile1 = afile;
            int i = afile.length;

            for (int j = 0; j < i; ++j) {
                File file1 = afile1[j];

                if (file1.isDirectory()) {
                    if (flag) {
                        collection.add(file1);
                    }

                    innerListFiles(collection, file1, iofilefilter, flag);
                } else {
                    collection.add(file1);
                }
            }
        }

    }

    public static Collection listFiles(File file, IOFileFilter iofilefilter, IOFileFilter iofilefilter1) {
        validateListFilesParameters(file, iofilefilter);
        IOFileFilter iofilefilter2 = setUpEffectiveFileFilter(iofilefilter);
        IOFileFilter iofilefilter3 = setUpEffectiveDirFilter(iofilefilter1);
        LinkedList linkedlist = new LinkedList();

        innerListFiles(linkedlist, file, FileFilterUtils.or(new IOFileFilter[] { iofilefilter2, iofilefilter3}), false);
        return linkedlist;
    }

    private static void validateListFilesParameters(File file, IOFileFilter iofilefilter) {
        if (!file.isDirectory()) {
            throw new IllegalArgumentException("Parameter \'directory\' is not a directory");
        } else if (iofilefilter == null) {
            throw new NullPointerException("Parameter \'fileFilter\' is null");
        }
    }

    private static IOFileFilter setUpEffectiveFileFilter(IOFileFilter iofilefilter) {
        return FileFilterUtils.and(new IOFileFilter[] { iofilefilter, FileFilterUtils.notFileFilter(DirectoryFileFilter.INSTANCE)});
    }

    private static IOFileFilter setUpEffectiveDirFilter(IOFileFilter iofilefilter) {
        return iofilefilter == null ? FalseFileFilter.INSTANCE : FileFilterUtils.and(new IOFileFilter[] { iofilefilter, DirectoryFileFilter.INSTANCE});
    }

    public static Collection listFilesAndDirs(File file, IOFileFilter iofilefilter, IOFileFilter iofilefilter1) {
        validateListFilesParameters(file, iofilefilter);
        IOFileFilter iofilefilter2 = setUpEffectiveFileFilter(iofilefilter);
        IOFileFilter iofilefilter3 = setUpEffectiveDirFilter(iofilefilter1);
        LinkedList linkedlist = new LinkedList();

        if (file.isDirectory()) {
            linkedlist.add(file);
        }

        innerListFiles(linkedlist, file, FileFilterUtils.or(new IOFileFilter[] { iofilefilter2, iofilefilter3}), true);
        return linkedlist;
    }

    public static Iterator iterateFiles(File file, IOFileFilter iofilefilter, IOFileFilter iofilefilter1) {
        return listFiles(file, iofilefilter, iofilefilter1).iterator();
    }

    public static Iterator iterateFilesAndDirs(File file, IOFileFilter iofilefilter, IOFileFilter iofilefilter1) {
        return listFilesAndDirs(file, iofilefilter, iofilefilter1).iterator();
    }

    private static String[] toSuffixes(String[] astring) {
        String[] astring1 = new String[astring.length];

        for (int i = 0; i < astring.length; ++i) {
            astring1[i] = "." + astring[i];
        }

        return astring1;
    }

    public static Collection listFiles(File file, String[] astring, boolean flag) {
        Object object;

        if (astring == null) {
            object = TrueFileFilter.INSTANCE;
        } else {
            String[] astring1 = toSuffixes(astring);

            object = new SuffixFileFilter(astring1);
        }

        return listFiles(file, (IOFileFilter) object, flag ? TrueFileFilter.INSTANCE : FalseFileFilter.INSTANCE);
    }

    public static Iterator iterateFiles(File file, String[] astring, boolean flag) {
        return listFiles(file, astring, flag).iterator();
    }

    public static boolean contentEquals(File file, File file1) throws IOException {
        boolean flag = file.exists();

        if (flag != file1.exists()) {
            return false;
        } else if (!flag) {
            return true;
        } else if (!file.isDirectory() && !file1.isDirectory()) {
            if (file.length() != file1.length()) {
                return false;
            } else if (file.getCanonicalFile().equals(file1.getCanonicalFile())) {
                return true;
            } else {
                FileInputStream fileinputstream = null;
                FileInputStream fileinputstream1 = null;

                boolean flag1;

                try {
                    fileinputstream = new FileInputStream(file);
                    fileinputstream1 = new FileInputStream(file1);
                    flag1 = IOUtils.contentEquals((InputStream) fileinputstream, (InputStream) fileinputstream1);
                } finally {
                    IOUtils.closeQuietly((InputStream) fileinputstream);
                    IOUtils.closeQuietly((InputStream) fileinputstream1);
                }

                return flag1;
            }
        } else {
            throw new IOException("Can\'t compare directories, only files");
        }
    }

    public static boolean contentEqualsIgnoreEOL(File file, File file1, String s) throws IOException {
        boolean flag = file.exists();

        if (flag != file1.exists()) {
            return false;
        } else if (!flag) {
            return true;
        } else if (!file.isDirectory() && !file1.isDirectory()) {
            if (file.getCanonicalFile().equals(file1.getCanonicalFile())) {
                return true;
            } else {
                InputStreamReader inputstreamreader = null;
                InputStreamReader inputstreamreader1 = null;

                boolean flag1;

                try {
                    if (s == null) {
                        inputstreamreader = new InputStreamReader(new FileInputStream(file));
                        inputstreamreader1 = new InputStreamReader(new FileInputStream(file1));
                    } else {
                        inputstreamreader = new InputStreamReader(new FileInputStream(file), s);
                        inputstreamreader1 = new InputStreamReader(new FileInputStream(file1), s);
                    }

                    flag1 = IOUtils.contentEqualsIgnoreEOL(inputstreamreader, inputstreamreader1);
                } finally {
                    IOUtils.closeQuietly((Reader) inputstreamreader);
                    IOUtils.closeQuietly((Reader) inputstreamreader1);
                }

                return flag1;
            }
        } else {
            throw new IOException("Can\'t compare directories, only files");
        }
    }

    public static File toFile(URL url) {
        if (url != null && "file".equalsIgnoreCase(url.getProtocol())) {
            String s = url.getFile().replace('/', File.separatorChar);

            s = decodeUrl(s);
            return new File(s);
        } else {
            return null;
        }
    }

    static String decodeUrl(String s) {
        String s1 = s;

        if (s != null && s.indexOf(37) >= 0) {
            int i = s.length();
            StringBuffer stringbuffer = new StringBuffer();
            ByteBuffer bytebuffer = ByteBuffer.allocate(i);
            int j = 0;

            label93:
            while (j < i) {
                if (s.charAt(j) == 37) {
                    try {
                        while (true) {
                            byte b0 = (byte) Integer.parseInt(s.substring(j + 1, j + 3), 16);

                            bytebuffer.put(b0);
                            j += 3;
                            if (j >= i || s.charAt(j) != 37) {
                                continue label93;
                            }
                        }
                    } catch (RuntimeException runtimeexception) {
                        ;
                    } finally {
                        if (bytebuffer.position() > 0) {
                            bytebuffer.flip();
                            stringbuffer.append(FileUtils.UTF8.decode(bytebuffer).toString());
                            bytebuffer.clear();
                        }

                    }
                }

                stringbuffer.append(s.charAt(j++));
            }

            s1 = stringbuffer.toString();
        }

        return s1;
    }

    public static File[] toFiles(URL[] aurl) {
        if (aurl != null && aurl.length != 0) {
            File[] afile = new File[aurl.length];

            for (int i = 0; i < aurl.length; ++i) {
                URL url = aurl[i];

                if (url != null) {
                    if (!url.getProtocol().equals("file")) {
                        throw new IllegalArgumentException("URL could not be converted to a File: " + url);
                    }

                    afile[i] = toFile(url);
                }
            }

            return afile;
        } else {
            return FileUtils.EMPTY_FILE_ARRAY;
        }
    }

    public static URL[] toURLs(File[] afile) throws IOException {
        URL[] aurl = new URL[afile.length];

        for (int i = 0; i < aurl.length; ++i) {
            aurl[i] = afile[i].toURI().toURL();
        }

        return aurl;
    }

    public static void copyFileToDirectory(File file, File file1) throws IOException {
        copyFileToDirectory(file, file1, true);
    }

    public static void copyFileToDirectory(File file, File file1, boolean flag) throws IOException {
        if (file1 == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (file1.exists() && !file1.isDirectory()) {
            throw new IllegalArgumentException("Destination \'" + file1 + "\' is not a directory");
        } else {
            File file2 = new File(file1, file.getName());

            copyFile(file, file2, flag);
        }
    }

    public static void copyFile(File file, File file1) throws IOException {
        copyFile(file, file1, true);
    }

    public static void copyFile(File file, File file1, boolean flag) throws IOException {
        if (file == null) {
            throw new NullPointerException("Source must not be null");
        } else if (file1 == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!file.exists()) {
            throw new FileNotFoundException("Source \'" + file + "\' does not exist");
        } else if (file.isDirectory()) {
            throw new IOException("Source \'" + file + "\' exists but is a directory");
        } else if (file.getCanonicalPath().equals(file1.getCanonicalPath())) {
            throw new IOException("Source \'" + file + "\' and destination \'" + file1 + "\' are the same");
        } else {
            File file2 = file1.getParentFile();

            if (file2 != null && !file2.mkdirs() && !file2.isDirectory()) {
                throw new IOException("Destination \'" + file2 + "\' directory cannot be created");
            } else if (file1.exists() && !file1.canWrite()) {
                throw new IOException("Destination \'" + file1 + "\' exists but is read-only");
            } else {
                doCopyFile(file, file1, flag);
            }
        }
    }

    public static long copyFile(File file, OutputStream outputstream) throws IOException {
        FileInputStream fileinputstream = new FileInputStream(file);

        long i;

        try {
            i = IOUtils.copyLarge((InputStream) fileinputstream, outputstream);
        } finally {
            fileinputstream.close();
        }

        return i;
    }

    private static void doCopyFile(File file, File file1, boolean flag) throws IOException {
        if (file1.exists() && file1.isDirectory()) {
            throw new IOException("Destination \'" + file1 + "\' exists but is a directory");
        } else {
            FileInputStream fileinputstream = null;
            FileOutputStream fileoutputstream = null;
            FileChannel filechannel = null;
            FileChannel filechannel1 = null;

            try {
                fileinputstream = new FileInputStream(file);
                fileoutputstream = new FileOutputStream(file1);
                filechannel = fileinputstream.getChannel();
                filechannel1 = fileoutputstream.getChannel();
                long i = filechannel.size();
                long j = 0L;

                for (long k = 0L; j < i; j += filechannel1.transferFrom(filechannel, j, k)) {
                    k = i - j > 31457280L ? 31457280L : i - j;
                }
            } finally {
                IOUtils.closeQuietly((Closeable) filechannel1);
                IOUtils.closeQuietly((OutputStream) fileoutputstream);
                IOUtils.closeQuietly((Closeable) filechannel);
                IOUtils.closeQuietly((InputStream) fileinputstream);
            }

            if (file.length() != file1.length()) {
                throw new IOException("Failed to copy full contents from \'" + file + "\' to \'" + file1 + "\'");
            } else {
                if (flag) {
                    file1.setLastModified(file.lastModified());
                }

            }
        }
    }

    public static void copyDirectoryToDirectory(File file, File file1) throws IOException {
        if (file == null) {
            throw new NullPointerException("Source must not be null");
        } else if (file.exists() && !file.isDirectory()) {
            throw new IllegalArgumentException("Source \'" + file1 + "\' is not a directory");
        } else if (file1 == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (file1.exists() && !file1.isDirectory()) {
            throw new IllegalArgumentException("Destination \'" + file1 + "\' is not a directory");
        } else {
            copyDirectory(file, new File(file1, file.getName()), true);
        }
    }

    public static void copyDirectory(File file, File file1) throws IOException {
        copyDirectory(file, file1, true);
    }

    public static void copyDirectory(File file, File file1, boolean flag) throws IOException {
        copyDirectory(file, file1, (FileFilter) null, flag);
    }

    public static void copyDirectory(File file, File file1, FileFilter filefilter) throws IOException {
        copyDirectory(file, file1, filefilter, true);
    }

    public static void copyDirectory(File file, File file1, FileFilter filefilter, boolean flag) throws IOException {
        if (file == null) {
            throw new NullPointerException("Source must not be null");
        } else if (file1 == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!file.exists()) {
            throw new FileNotFoundException("Source \'" + file + "\' does not exist");
        } else if (!file.isDirectory()) {
            throw new IOException("Source \'" + file + "\' exists but is not a directory");
        } else if (file.getCanonicalPath().equals(file1.getCanonicalPath())) {
            throw new IOException("Source \'" + file + "\' and destination \'" + file1 + "\' are the same");
        } else {
            ArrayList arraylist = null;

            if (file1.getCanonicalPath().startsWith(file.getCanonicalPath())) {
                File[] afile = filefilter == null ? file.listFiles() : file.listFiles(filefilter);

                if (afile != null && afile.length > 0) {
                    arraylist = new ArrayList(afile.length);
                    File[] afile1 = afile;
                    int i = afile.length;

                    for (int j = 0; j < i; ++j) {
                        File file2 = afile1[j];
                        File file3 = new File(file1, file2.getName());

                        arraylist.add(file3.getCanonicalPath());
                    }
                }
            }

            doCopyDirectory(file, file1, filefilter, flag, arraylist);
        }
    }

    private static void doCopyDirectory(File file, File file1, FileFilter filefilter, boolean flag, List list) throws IOException {
        File[] afile = filefilter == null ? file.listFiles() : file.listFiles(filefilter);

        if (afile == null) {
            throw new IOException("Failed to list contents of " + file);
        } else {
            if (file1.exists()) {
                if (!file1.isDirectory()) {
                    throw new IOException("Destination \'" + file1 + "\' exists but is not a directory");
                }
            } else if (!file1.mkdirs() && !file1.isDirectory()) {
                throw new IOException("Destination \'" + file1 + "\' directory cannot be created");
            }

            if (!file1.canWrite()) {
                throw new IOException("Destination \'" + file1 + "\' cannot be written to");
            } else {
                File[] afile1 = afile;
                int i = afile.length;

                for (int j = 0; j < i; ++j) {
                    File file2 = afile1[j];
                    File file3 = new File(file1, file2.getName());

                    if (list == null || !list.contains(file2.getCanonicalPath())) {
                        if (file2.isDirectory()) {
                            doCopyDirectory(file2, file3, filefilter, flag, list);
                        } else {
                            doCopyFile(file2, file3, flag);
                        }
                    }
                }

                if (flag) {
                    file1.setLastModified(file.lastModified());
                }

            }
        }
    }

    public static void copyURLToFile(URL url, File file) throws IOException {
        InputStream inputstream = url.openStream();

        copyInputStreamToFile(inputstream, file);
    }

    public static void copyURLToFile(URL url, File file, int i, int j) throws IOException {
        URLConnection urlconnection = url.openConnection();

        urlconnection.setConnectTimeout(i);
        urlconnection.setReadTimeout(j);
        InputStream inputstream = urlconnection.getInputStream();

        copyInputStreamToFile(inputstream, file);
    }

    public static void copyInputStreamToFile(InputStream inputstream, File file) throws IOException {
        try {
            FileOutputStream fileoutputstream = openOutputStream(file);

            try {
                IOUtils.copy(inputstream, (OutputStream) fileoutputstream);
                fileoutputstream.close();
            } finally {
                IOUtils.closeQuietly((OutputStream) fileoutputstream);
            }
        } finally {
            IOUtils.closeQuietly(inputstream);
        }

    }

    public static void deleteDirectory(File file) throws IOException {
        if (file.exists()) {
            if (!isSymlink(file)) {
                cleanDirectory(file);
            }

            if (!file.delete()) {
                String s = "Unable to delete directory " + file + ".";

                throw new IOException(s);
            }
        }
    }

    public static boolean deleteQuietly(File file) {
        if (file == null) {
            return false;
        } else {
            try {
                if (file.isDirectory()) {
                    cleanDirectory(file);
                }
            } catch (Exception exception) {
                ;
            }

            try {
                return file.delete();
            } catch (Exception exception1) {
                return false;
            }
        }
    }

    public static boolean directoryContains(File file, File file1) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("Directory must not be null");
        } else if (!file.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + file);
        } else if (file1 == null) {
            return false;
        } else if (file.exists() && file1.exists()) {
            String s = file.getCanonicalPath();
            String s1 = file1.getCanonicalPath();

            return FilenameUtils.directoryContains(s, s1);
        } else {
            return false;
        }
    }

    public static void cleanDirectory(File file) throws IOException {
        String s;

        if (!file.exists()) {
            s = file + " does not exist";
            throw new IllegalArgumentException(s);
        } else if (!file.isDirectory()) {
            s = file + " is not a directory";
            throw new IllegalArgumentException(s);
        } else {
            File[] afile = file.listFiles();

            if (afile == null) {
                throw new IOException("Failed to list contents of " + file);
            } else {
                IOException ioexception = null;
                File[] afile1 = afile;
                int i = afile.length;

                for (int j = 0; j < i; ++j) {
                    File file1 = afile1[j];

                    try {
                        forceDelete(file1);
                    } catch (IOException ioexception1) {
                        ioexception = ioexception1;
                    }
                }

                if (null != ioexception) {
                    throw ioexception;
                }
            }
        }
    }

    public static boolean waitFor(File file, int i) {
        int j = 0;
        int k = 0;

        while (!file.exists()) {
            if (k++ >= 10) {
                k = 0;
                if (j++ > i) {
                    return false;
                }
            }

            try {
                Thread.sleep(100L);
            } catch (InterruptedException interruptedexception) {
                ;
            } catch (Exception exception) {
                break;
            }
        }

        return true;
    }

    public static String readFileToString(File file, Charset charset) throws IOException {
        FileInputStream fileinputstream = null;

        String s;

        try {
            fileinputstream = openInputStream(file);
            s = IOUtils.toString((InputStream) fileinputstream, Charsets.toCharset(charset));
        } finally {
            IOUtils.closeQuietly((InputStream) fileinputstream);
        }

        return s;
    }

    public static String readFileToString(File file, String s) throws IOException {
        return readFileToString(file, Charsets.toCharset(s));
    }

    public static String readFileToString(File file) throws IOException {
        return readFileToString(file, Charset.defaultCharset());
    }

    public static byte[] readFileToByteArray(File file) throws IOException {
        FileInputStream fileinputstream = null;

        byte[] abyte;

        try {
            fileinputstream = openInputStream(file);
            abyte = IOUtils.toByteArray(fileinputstream, file.length());
        } finally {
            IOUtils.closeQuietly((InputStream) fileinputstream);
        }

        return abyte;
    }

    public static List readLines(File file, Charset charset) throws IOException {
        FileInputStream fileinputstream = null;

        List list;

        try {
            fileinputstream = openInputStream(file);
            list = IOUtils.readLines(fileinputstream, Charsets.toCharset(charset));
        } finally {
            IOUtils.closeQuietly((InputStream) fileinputstream);
        }

        return list;
    }

    public static List readLines(File file, String s) throws IOException {
        return readLines(file, Charsets.toCharset(s));
    }

    public static List readLines(File file) throws IOException {
        return readLines(file, Charset.defaultCharset());
    }

    public static LineIterator lineIterator(File file, String s) throws IOException {
        FileInputStream fileinputstream = null;

        try {
            fileinputstream = openInputStream(file);
            return IOUtils.lineIterator(fileinputstream, s);
        } catch (IOException ioexception) {
            IOUtils.closeQuietly((InputStream) fileinputstream);
            throw ioexception;
        } catch (RuntimeException runtimeexception) {
            IOUtils.closeQuietly((InputStream) fileinputstream);
            throw runtimeexception;
        }
    }

    public static LineIterator lineIterator(File file) throws IOException {
        return lineIterator(file, (String) null);
    }

    public static void writeStringToFile(File file, String s, Charset charset) throws IOException {
        writeStringToFile(file, s, charset, false);
    }

    public static void writeStringToFile(File file, String s, String s1) throws IOException {
        writeStringToFile(file, s, s1, false);
    }

    public static void writeStringToFile(File file, String s, Charset charset, boolean flag) throws IOException {
        FileOutputStream fileoutputstream = null;

        try {
            fileoutputstream = openOutputStream(file, flag);
            IOUtils.write(s, (OutputStream) fileoutputstream, charset);
            fileoutputstream.close();
        } finally {
            IOUtils.closeQuietly((OutputStream) fileoutputstream);
        }

    }

    public static void writeStringToFile(File file, String s, String s1, boolean flag) throws IOException {
        writeStringToFile(file, s, Charsets.toCharset(s1), flag);
    }

    public static void writeStringToFile(File file, String s) throws IOException {
        writeStringToFile(file, s, Charset.defaultCharset(), false);
    }

    public static void writeStringToFile(File file, String s, boolean flag) throws IOException {
        writeStringToFile(file, s, Charset.defaultCharset(), flag);
    }

    public static void write(File file, CharSequence charsequence) throws IOException {
        write(file, charsequence, Charset.defaultCharset(), false);
    }

    public static void write(File file, CharSequence charsequence, boolean flag) throws IOException {
        write(file, charsequence, Charset.defaultCharset(), flag);
    }

    public static void write(File file, CharSequence charsequence, Charset charset) throws IOException {
        write(file, charsequence, charset, false);
    }

    public static void write(File file, CharSequence charsequence, String s) throws IOException {
        write(file, charsequence, s, false);
    }

    public static void write(File file, CharSequence charsequence, Charset charset, boolean flag) throws IOException {
        String s = charsequence == null ? null : charsequence.toString();

        writeStringToFile(file, s, charset, flag);
    }

    public static void write(File file, CharSequence charsequence, String s, boolean flag) throws IOException {
        write(file, charsequence, Charsets.toCharset(s), flag);
    }

    public static void writeByteArrayToFile(File file, byte[] abyte) throws IOException {
        writeByteArrayToFile(file, abyte, false);
    }

    public static void writeByteArrayToFile(File file, byte[] abyte, boolean flag) throws IOException {
        FileOutputStream fileoutputstream = null;

        try {
            fileoutputstream = openOutputStream(file, flag);
            fileoutputstream.write(abyte);
            fileoutputstream.close();
        } finally {
            IOUtils.closeQuietly((OutputStream) fileoutputstream);
        }

    }

    public static void writeLines(File file, String s, Collection collection) throws IOException {
        writeLines(file, s, collection, (String) null, false);
    }

    public static void writeLines(File file, String s, Collection collection, boolean flag) throws IOException {
        writeLines(file, s, collection, (String) null, flag);
    }

    public static void writeLines(File file, Collection collection) throws IOException {
        writeLines(file, (String) null, collection, (String) null, false);
    }

    public static void writeLines(File file, Collection collection, boolean flag) throws IOException {
        writeLines(file, (String) null, collection, (String) null, flag);
    }

    public static void writeLines(File file, String s, Collection collection, String s1) throws IOException {
        writeLines(file, s, collection, s1, false);
    }

    public static void writeLines(File file, String s, Collection collection, String s1, boolean flag) throws IOException {
        FileOutputStream fileoutputstream = null;

        try {
            fileoutputstream = openOutputStream(file, flag);
            BufferedOutputStream bufferedoutputstream = new BufferedOutputStream(fileoutputstream);

            IOUtils.writeLines(collection, s1, bufferedoutputstream, s);
            bufferedoutputstream.flush();
            fileoutputstream.close();
        } finally {
            IOUtils.closeQuietly((OutputStream) fileoutputstream);
        }

    }

    public static void writeLines(File file, Collection collection, String s) throws IOException {
        writeLines(file, (String) null, collection, s, false);
    }

    public static void writeLines(File file, Collection collection, String s, boolean flag) throws IOException {
        writeLines(file, (String) null, collection, s, flag);
    }

    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean flag = file.exists();

            if (!file.delete()) {
                if (!flag) {
                    throw new FileNotFoundException("File does not exist: " + file);
                }

                String s = "Unable to delete file: " + file;

                throw new IOException(s);
            }
        }

    }

    public static void forceDeleteOnExit(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectoryOnExit(file);
        } else {
            file.deleteOnExit();
        }

    }

    private static void deleteDirectoryOnExit(File file) throws IOException {
        if (file.exists()) {
            file.deleteOnExit();
            if (!isSymlink(file)) {
                cleanDirectoryOnExit(file);
            }

        }
    }

    private static void cleanDirectoryOnExit(File file) throws IOException {
        String s;

        if (!file.exists()) {
            s = file + " does not exist";
            throw new IllegalArgumentException(s);
        } else if (!file.isDirectory()) {
            s = file + " is not a directory";
            throw new IllegalArgumentException(s);
        } else {
            File[] afile = file.listFiles();

            if (afile == null) {
                throw new IOException("Failed to list contents of " + file);
            } else {
                IOException ioexception = null;
                File[] afile1 = afile;
                int i = afile.length;

                for (int j = 0; j < i; ++j) {
                    File file1 = afile1[j];

                    try {
                        forceDeleteOnExit(file1);
                    } catch (IOException ioexception1) {
                        ioexception = ioexception1;
                    }
                }

                if (null != ioexception) {
                    throw ioexception;
                }
            }
        }
    }

    public static void forceMkdir(File file) throws IOException {
        String s;

        if (file.exists()) {
            if (!file.isDirectory()) {
                s = "File " + file + " exists and is " + "not a directory. Unable to create directory.";
                throw new IOException(s);
            }
        } else if (!file.mkdirs() && !file.isDirectory()) {
            s = "Unable to create directory " + file;
            throw new IOException(s);
        }

    }

    public static long sizeOf(File file) {
        if (!file.exists()) {
            String s = file + " does not exist";

            throw new IllegalArgumentException(s);
        } else {
            return file.isDirectory() ? sizeOfDirectory(file) : file.length();
        }
    }

    public static BigInteger sizeOfAsBigInteger(File file) {
        if (!file.exists()) {
            String s = file + " does not exist";

            throw new IllegalArgumentException(s);
        } else {
            return file.isDirectory() ? sizeOfDirectoryAsBigInteger(file) : BigInteger.valueOf(file.length());
        }
    }

    public static long sizeOfDirectory(File file) {
        checkDirectory(file);
        File[] afile = file.listFiles();

        if (afile == null) {
            return 0L;
        } else {
            long i = 0L;
            File[] afile1 = afile;
            int j = afile.length;

            for (int k = 0; k < j; ++k) {
                File file1 = afile1[k];

                try {
                    if (!isSymlink(file1)) {
                        i += sizeOf(file1);
                        if (i < 0L) {
                            break;
                        }
                    }
                } catch (IOException ioexception) {
                    ;
                }
            }

            return i;
        }
    }

    public static BigInteger sizeOfDirectoryAsBigInteger(File file) {
        checkDirectory(file);
        File[] afile = file.listFiles();

        if (afile == null) {
            return BigInteger.ZERO;
        } else {
            BigInteger biginteger = BigInteger.ZERO;
            File[] afile1 = afile;
            int i = afile.length;

            for (int j = 0; j < i; ++j) {
                File file1 = afile1[j];

                try {
                    if (!isSymlink(file1)) {
                        biginteger = biginteger.add(BigInteger.valueOf(sizeOf(file1)));
                    }
                } catch (IOException ioexception) {
                    ;
                }
            }

            return biginteger;
        }
    }

    private static void checkDirectory(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException(file + " does not exist");
        } else if (!file.isDirectory()) {
            throw new IllegalArgumentException(file + " is not a directory");
        }
    }

    public static boolean isFileNewer(File file, File file1) {
        if (file1 == null) {
            throw new IllegalArgumentException("No specified reference file");
        } else if (!file1.exists()) {
            throw new IllegalArgumentException("The reference file \'" + file1 + "\' doesn\'t exist");
        } else {
            return isFileNewer(file, file1.lastModified());
        }
    }

    public static boolean isFileNewer(File file, Date date) {
        if (date == null) {
            throw new IllegalArgumentException("No specified date");
        } else {
            return isFileNewer(file, date.getTime());
        }
    }

    public static boolean isFileNewer(File file, long i) {
        if (file == null) {
            throw new IllegalArgumentException("No specified file");
        } else {
            return !file.exists() ? false : file.lastModified() > i;
        }
    }

    public static boolean isFileOlder(File file, File file1) {
        if (file1 == null) {
            throw new IllegalArgumentException("No specified reference file");
        } else if (!file1.exists()) {
            throw new IllegalArgumentException("The reference file \'" + file1 + "\' doesn\'t exist");
        } else {
            return isFileOlder(file, file1.lastModified());
        }
    }

    public static boolean isFileOlder(File file, Date date) {
        if (date == null) {
            throw new IllegalArgumentException("No specified date");
        } else {
            return isFileOlder(file, date.getTime());
        }
    }

    public static boolean isFileOlder(File file, long i) {
        if (file == null) {
            throw new IllegalArgumentException("No specified file");
        } else {
            return !file.exists() ? false : file.lastModified() < i;
        }
    }

    public static long checksumCRC32(File file) throws IOException {
        CRC32 crc32 = new CRC32();

        checksum(file, crc32);
        return crc32.getValue();
    }

    public static Checksum checksum(File file, Checksum checksum) throws IOException {
        if (file.isDirectory()) {
            throw new IllegalArgumentException("Checksums can\'t be computed on directories");
        } else {
            CheckedInputStream checkedinputstream = null;

            try {
                checkedinputstream = new CheckedInputStream(new FileInputStream(file), checksum);
                IOUtils.copy((InputStream) checkedinputstream, (OutputStream) (new NullOutputStream()));
            } finally {
                IOUtils.closeQuietly((InputStream) checkedinputstream);
            }

            return checksum;
        }
    }

    public static void moveDirectory(File file, File file1) throws IOException {
        if (file == null) {
            throw new NullPointerException("Source must not be null");
        } else if (file1 == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!file.exists()) {
            throw new FileNotFoundException("Source \'" + file + "\' does not exist");
        } else if (!file.isDirectory()) {
            throw new IOException("Source \'" + file + "\' is not a directory");
        } else if (file1.exists()) {
            throw new FileExistsException("Destination \'" + file1 + "\' already exists");
        } else {
            boolean flag = file.renameTo(file1);

            if (!flag) {
                if (file1.getCanonicalPath().startsWith(file.getCanonicalPath())) {
                    throw new IOException("Cannot move directory: " + file + " to a subdirectory of itself: " + file1);
                }

                copyDirectory(file, file1);
                deleteDirectory(file);
                if (file.exists()) {
                    throw new IOException("Failed to delete original directory \'" + file + "\' after copy to \'" + file1 + "\'");
                }
            }

        }
    }

    public static void moveDirectoryToDirectory(File file, File file1, boolean flag) throws IOException {
        if (file == null) {
            throw new NullPointerException("Source must not be null");
        } else if (file1 == null) {
            throw new NullPointerException("Destination directory must not be null");
        } else {
            if (!file1.exists() && flag) {
                file1.mkdirs();
            }

            if (!file1.exists()) {
                throw new FileNotFoundException("Destination directory \'" + file1 + "\' does not exist [createDestDir=" + flag + "]");
            } else if (!file1.isDirectory()) {
                throw new IOException("Destination \'" + file1 + "\' is not a directory");
            } else {
                moveDirectory(file, new File(file1, file.getName()));
            }
        }
    }

    public static void moveFile(File file, File file1) throws IOException {
        if (file == null) {
            throw new NullPointerException("Source must not be null");
        } else if (file1 == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!file.exists()) {
            throw new FileNotFoundException("Source \'" + file + "\' does not exist");
        } else if (file.isDirectory()) {
            throw new IOException("Source \'" + file + "\' is a directory");
        } else if (file1.exists()) {
            throw new FileExistsException("Destination \'" + file1 + "\' already exists");
        } else if (file1.isDirectory()) {
            throw new IOException("Destination \'" + file1 + "\' is a directory");
        } else {
            boolean flag = file.renameTo(file1);

            if (!flag) {
                copyFile(file, file1);
                if (!file.delete()) {
                    deleteQuietly(file1);
                    throw new IOException("Failed to delete original file \'" + file + "\' after copy to \'" + file1 + "\'");
                }
            }

        }
    }

    public static void moveFileToDirectory(File file, File file1, boolean flag) throws IOException {
        if (file == null) {
            throw new NullPointerException("Source must not be null");
        } else if (file1 == null) {
            throw new NullPointerException("Destination directory must not be null");
        } else {
            if (!file1.exists() && flag) {
                file1.mkdirs();
            }

            if (!file1.exists()) {
                throw new FileNotFoundException("Destination directory \'" + file1 + "\' does not exist [createDestDir=" + flag + "]");
            } else if (!file1.isDirectory()) {
                throw new IOException("Destination \'" + file1 + "\' is not a directory");
            } else {
                moveFile(file, new File(file1, file.getName()));
            }
        }
    }

    public static void moveToDirectory(File file, File file1, boolean flag) throws IOException {
        if (file == null) {
            throw new NullPointerException("Source must not be null");
        } else if (file1 == null) {
            throw new NullPointerException("Destination must not be null");
        } else if (!file.exists()) {
            throw new FileNotFoundException("Source \'" + file + "\' does not exist");
        } else {
            if (file.isDirectory()) {
                moveDirectoryToDirectory(file, file1, flag);
            } else {
                moveFileToDirectory(file, file1, flag);
            }

        }
    }

    public static boolean isSymlink(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        } else if (FilenameUtils.isSystemWindows()) {
            return false;
        } else {
            File file1 = null;

            if (file.getParent() == null) {
                file1 = file;
            } else {
                File file2 = file.getParentFile().getCanonicalFile();

                file1 = new File(file2, file.getName());
            }

            return !file1.getCanonicalFile().equals(file1.getAbsoluteFile());
        }
    }
}
