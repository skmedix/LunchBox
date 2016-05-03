package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOCase;

public class FileFilterUtils {

    private static final IOFileFilter cvsFilter = notFileFilter(and(new IOFileFilter[] { directoryFileFilter(), nameFileFilter("CVS")}));
    private static final IOFileFilter svnFilter = notFileFilter(and(new IOFileFilter[] { directoryFileFilter(), nameFileFilter(".svn")}));

    public static File[] filter(IOFileFilter iofilefilter, File... afile) {
        if (iofilefilter == null) {
            throw new IllegalArgumentException("file filter is null");
        } else if (afile == null) {
            return new File[0];
        } else {
            ArrayList arraylist = new ArrayList();
            File[] afile1 = afile;
            int i = afile.length;

            for (int j = 0; j < i; ++j) {
                File file = afile1[j];

                if (file == null) {
                    throw new IllegalArgumentException("file array contains null");
                }

                if (iofilefilter.accept(file)) {
                    arraylist.add(file);
                }
            }

            return (File[]) arraylist.toArray(new File[arraylist.size()]);
        }
    }

    public static File[] filter(IOFileFilter iofilefilter, Iterable iterable) {
        List list = filterList(iofilefilter, iterable);

        return (File[]) list.toArray(new File[list.size()]);
    }

    public static List filterList(IOFileFilter iofilefilter, Iterable iterable) {
        return (List) filter(iofilefilter, iterable, new ArrayList());
    }

    public static List filterList(IOFileFilter iofilefilter, File... afile) {
        File[] afile1 = filter(iofilefilter, afile);

        return Arrays.asList(afile1);
    }

    public static Set filterSet(IOFileFilter iofilefilter, File... afile) {
        File[] afile1 = filter(iofilefilter, afile);

        return new HashSet(Arrays.asList(afile1));
    }

    public static Set filterSet(IOFileFilter iofilefilter, Iterable iterable) {
        return (Set) filter(iofilefilter, iterable, new HashSet());
    }

    private static Collection filter(IOFileFilter iofilefilter, Iterable iterable, Collection collection) {
        if (iofilefilter == null) {
            throw new IllegalArgumentException("file filter is null");
        } else {
            if (iterable != null) {
                Iterator iterator = iterable.iterator();

                while (iterator.hasNext()) {
                    File file = (File) iterator.next();

                    if (file == null) {
                        throw new IllegalArgumentException("file collection contains null");
                    }

                    if (iofilefilter.accept(file)) {
                        collection.add(file);
                    }
                }
            }

            return collection;
        }
    }

    public static IOFileFilter prefixFileFilter(String s) {
        return new PrefixFileFilter(s);
    }

    public static IOFileFilter prefixFileFilter(String s, IOCase iocase) {
        return new PrefixFileFilter(s, iocase);
    }

    public static IOFileFilter suffixFileFilter(String s) {
        return new SuffixFileFilter(s);
    }

    public static IOFileFilter suffixFileFilter(String s, IOCase iocase) {
        return new SuffixFileFilter(s, iocase);
    }

    public static IOFileFilter nameFileFilter(String s) {
        return new NameFileFilter(s);
    }

    public static IOFileFilter nameFileFilter(String s, IOCase iocase) {
        return new NameFileFilter(s, iocase);
    }

    public static IOFileFilter directoryFileFilter() {
        return DirectoryFileFilter.DIRECTORY;
    }

    public static IOFileFilter fileFileFilter() {
        return FileFileFilter.FILE;
    }

    /** @deprecated */
    @Deprecated
    public static IOFileFilter andFileFilter(IOFileFilter iofilefilter, IOFileFilter iofilefilter1) {
        return new AndFileFilter(iofilefilter, iofilefilter1);
    }

    /** @deprecated */
    @Deprecated
    public static IOFileFilter orFileFilter(IOFileFilter iofilefilter, IOFileFilter iofilefilter1) {
        return new OrFileFilter(iofilefilter, iofilefilter1);
    }

    public static IOFileFilter and(IOFileFilter... aiofilefilter) {
        return new AndFileFilter(toList(aiofilefilter));
    }

    public static IOFileFilter or(IOFileFilter... aiofilefilter) {
        return new OrFileFilter(toList(aiofilefilter));
    }

    public static List toList(IOFileFilter... aiofilefilter) {
        if (aiofilefilter == null) {
            throw new IllegalArgumentException("The filters must not be null");
        } else {
            ArrayList arraylist = new ArrayList(aiofilefilter.length);

            for (int i = 0; i < aiofilefilter.length; ++i) {
                if (aiofilefilter[i] == null) {
                    throw new IllegalArgumentException("The filter[" + i + "] is null");
                }

                arraylist.add(aiofilefilter[i]);
            }

            return arraylist;
        }
    }

    public static IOFileFilter notFileFilter(IOFileFilter iofilefilter) {
        return new NotFileFilter(iofilefilter);
    }

    public static IOFileFilter trueFileFilter() {
        return TrueFileFilter.TRUE;
    }

    public static IOFileFilter falseFileFilter() {
        return FalseFileFilter.FALSE;
    }

    public static IOFileFilter asFileFilter(FileFilter filefilter) {
        return new DelegateFileFilter(filefilter);
    }

    public static IOFileFilter asFileFilter(FilenameFilter filenamefilter) {
        return new DelegateFileFilter(filenamefilter);
    }

    public static IOFileFilter ageFileFilter(long i) {
        return new AgeFileFilter(i);
    }

    public static IOFileFilter ageFileFilter(long i, boolean flag) {
        return new AgeFileFilter(i, flag);
    }

    public static IOFileFilter ageFileFilter(Date date) {
        return new AgeFileFilter(date);
    }

    public static IOFileFilter ageFileFilter(Date date, boolean flag) {
        return new AgeFileFilter(date, flag);
    }

    public static IOFileFilter ageFileFilter(File file) {
        return new AgeFileFilter(file);
    }

    public static IOFileFilter ageFileFilter(File file, boolean flag) {
        return new AgeFileFilter(file, flag);
    }

    public static IOFileFilter sizeFileFilter(long i) {
        return new SizeFileFilter(i);
    }

    public static IOFileFilter sizeFileFilter(long i, boolean flag) {
        return new SizeFileFilter(i, flag);
    }

    public static IOFileFilter sizeRangeFileFilter(long i, long j) {
        SizeFileFilter sizefilefilter = new SizeFileFilter(i, true);
        SizeFileFilter sizefilefilter1 = new SizeFileFilter(j + 1L, false);

        return new AndFileFilter(sizefilefilter, sizefilefilter1);
    }

    public static IOFileFilter magicNumberFileFilter(String s) {
        return new MagicNumberFileFilter(s);
    }

    public static IOFileFilter magicNumberFileFilter(String s, long i) {
        return new MagicNumberFileFilter(s, i);
    }

    public static IOFileFilter magicNumberFileFilter(byte[] abyte) {
        return new MagicNumberFileFilter(abyte);
    }

    public static IOFileFilter magicNumberFileFilter(byte[] abyte, long i) {
        return new MagicNumberFileFilter(abyte, i);
    }

    public static IOFileFilter makeCVSAware(IOFileFilter iofilefilter) {
        return iofilefilter == null ? FileFilterUtils.cvsFilter : and(new IOFileFilter[] { iofilefilter, FileFilterUtils.cvsFilter});
    }

    public static IOFileFilter makeSVNAware(IOFileFilter iofilefilter) {
        return iofilefilter == null ? FileFilterUtils.svnFilter : and(new IOFileFilter[] { iofilefilter, FileFilterUtils.svnFilter});
    }

    public static IOFileFilter makeDirectoryOnly(IOFileFilter iofilefilter) {
        return (IOFileFilter) (iofilefilter == null ? DirectoryFileFilter.DIRECTORY : new AndFileFilter(DirectoryFileFilter.DIRECTORY, iofilefilter));
    }

    public static IOFileFilter makeFileOnly(IOFileFilter iofilefilter) {
        return (IOFileFilter) (iofilefilter == null ? FileFileFilter.FILE : new AndFileFilter(FileFileFilter.FILE, iofilefilter));
    }
}
