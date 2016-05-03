package org.apache.commons.io.monitor;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.comparator.NameFileComparator;

public class FileAlterationObserver implements Serializable {

    private final List listeners;
    private final FileEntry rootEntry;
    private final FileFilter fileFilter;
    private final Comparator comparator;

    public FileAlterationObserver(String s) {
        this(new File(s));
    }

    public FileAlterationObserver(String s, FileFilter filefilter) {
        this(new File(s), filefilter);
    }

    public FileAlterationObserver(String s, FileFilter filefilter, IOCase iocase) {
        this(new File(s), filefilter, iocase);
    }

    public FileAlterationObserver(File file) {
        this(file, (FileFilter) null);
    }

    public FileAlterationObserver(File file, FileFilter filefilter) {
        this(file, filefilter, (IOCase) null);
    }

    public FileAlterationObserver(File file, FileFilter filefilter, IOCase iocase) {
        this(new FileEntry(file), filefilter, iocase);
    }

    protected FileAlterationObserver(FileEntry fileentry, FileFilter filefilter, IOCase iocase) {
        this.listeners = new CopyOnWriteArrayList();
        if (fileentry == null) {
            throw new IllegalArgumentException("Root entry is missing");
        } else if (fileentry.getFile() == null) {
            throw new IllegalArgumentException("Root directory is missing");
        } else {
            this.rootEntry = fileentry;
            this.fileFilter = filefilter;
            if (iocase != null && !iocase.equals(IOCase.SYSTEM)) {
                if (iocase.equals(IOCase.INSENSITIVE)) {
                    this.comparator = NameFileComparator.NAME_INSENSITIVE_COMPARATOR;
                } else {
                    this.comparator = NameFileComparator.NAME_COMPARATOR;
                }
            } else {
                this.comparator = NameFileComparator.NAME_SYSTEM_COMPARATOR;
            }

        }
    }

    public File getDirectory() {
        return this.rootEntry.getFile();
    }

    public FileFilter getFileFilter() {
        return this.fileFilter;
    }

    public void addListener(FileAlterationListener filealterationlistener) {
        if (filealterationlistener != null) {
            this.listeners.add(filealterationlistener);
        }

    }

    public void removeListener(FileAlterationListener filealterationlistener) {
        if (filealterationlistener != null) {
            while (true) {
                if (this.listeners.remove(filealterationlistener)) {
                    continue;
                }
            }
        }

    }

    public Iterable getListeners() {
        return this.listeners;
    }

    public void initialize() throws Exception {
        this.rootEntry.refresh(this.rootEntry.getFile());
        File[] afile = this.listFiles(this.rootEntry.getFile());
        FileEntry[] afileentry = afile.length > 0 ? new FileEntry[afile.length] : FileEntry.EMPTY_ENTRIES;

        for (int i = 0; i < afile.length; ++i) {
            afileentry[i] = this.createFileEntry(this.rootEntry, afile[i]);
        }

        this.rootEntry.setChildren(afileentry);
    }

    public void destroy() throws Exception {}

    public void checkAndNotify() {
        Iterator iterator = this.listeners.iterator();

        while (iterator.hasNext()) {
            FileAlterationListener filealterationlistener = (FileAlterationListener) iterator.next();

            filealterationlistener.onStart(this);
        }

        File file = this.rootEntry.getFile();

        if (file.exists()) {
            this.checkAndNotify(this.rootEntry, this.rootEntry.getChildren(), this.listFiles(file));
        } else if (this.rootEntry.isExists()) {
            this.checkAndNotify(this.rootEntry, this.rootEntry.getChildren(), FileUtils.EMPTY_FILE_ARRAY);
        }

        Iterator iterator1 = this.listeners.iterator();

        while (iterator1.hasNext()) {
            FileAlterationListener filealterationlistener1 = (FileAlterationListener) iterator1.next();

            filealterationlistener1.onStop(this);
        }

    }

    private void checkAndNotify(FileEntry fileentry, FileEntry[] afileentry, File[] afile) {
        int i = 0;
        FileEntry[] afileentry1 = afile.length > 0 ? new FileEntry[afile.length] : FileEntry.EMPTY_ENTRIES;
        FileEntry[] afileentry2 = afileentry;
        int j = afileentry.length;

        for (int k = 0; k < j; ++k) {
            FileEntry fileentry1;

            for (fileentry1 = afileentry2[k]; i < afile.length && this.comparator.compare(fileentry1.getFile(), afile[i]) > 0; ++i) {
                afileentry1[i] = this.createFileEntry(fileentry, afile[i]);
                this.doCreate(afileentry1[i]);
            }

            if (i < afile.length && this.comparator.compare(fileentry1.getFile(), afile[i]) == 0) {
                this.doMatch(fileentry1, afile[i]);
                this.checkAndNotify(fileentry1, fileentry1.getChildren(), this.listFiles(afile[i]));
                afileentry1[i] = fileentry1;
                ++i;
            } else {
                this.checkAndNotify(fileentry1, fileentry1.getChildren(), FileUtils.EMPTY_FILE_ARRAY);
                this.doDelete(fileentry1);
            }
        }

        while (i < afile.length) {
            afileentry1[i] = this.createFileEntry(fileentry, afile[i]);
            this.doCreate(afileentry1[i]);
            ++i;
        }

        fileentry.setChildren(afileentry1);
    }

    private FileEntry createFileEntry(FileEntry fileentry, File file) {
        FileEntry fileentry1 = fileentry.newChildInstance(file);

        fileentry1.refresh(file);
        File[] afile = this.listFiles(file);
        FileEntry[] afileentry = afile.length > 0 ? new FileEntry[afile.length] : FileEntry.EMPTY_ENTRIES;

        for (int i = 0; i < afile.length; ++i) {
            afileentry[i] = this.createFileEntry(fileentry1, afile[i]);
        }

        fileentry1.setChildren(afileentry);
        return fileentry1;
    }

    private void doCreate(FileEntry fileentry) {
        Iterator iterator = this.listeners.iterator();

        while (iterator.hasNext()) {
            FileAlterationListener filealterationlistener = (FileAlterationListener) iterator.next();

            if (fileentry.isDirectory()) {
                filealterationlistener.onDirectoryCreate(fileentry.getFile());
            } else {
                filealterationlistener.onFileCreate(fileentry.getFile());
            }
        }

        FileEntry[] afileentry = fileentry.getChildren();
        FileEntry[] afileentry1 = afileentry;
        int i = afileentry.length;

        for (int j = 0; j < i; ++j) {
            FileEntry fileentry1 = afileentry1[j];

            this.doCreate(fileentry1);
        }

    }

    private void doMatch(FileEntry fileentry, File file) {
        if (fileentry.refresh(file)) {
            Iterator iterator = this.listeners.iterator();

            while (iterator.hasNext()) {
                FileAlterationListener filealterationlistener = (FileAlterationListener) iterator.next();

                if (fileentry.isDirectory()) {
                    filealterationlistener.onDirectoryChange(file);
                } else {
                    filealterationlistener.onFileChange(file);
                }
            }
        }

    }

    private void doDelete(FileEntry fileentry) {
        Iterator iterator = this.listeners.iterator();

        while (iterator.hasNext()) {
            FileAlterationListener filealterationlistener = (FileAlterationListener) iterator.next();

            if (fileentry.isDirectory()) {
                filealterationlistener.onDirectoryDelete(fileentry.getFile());
            } else {
                filealterationlistener.onFileDelete(fileentry.getFile());
            }
        }

    }

    private File[] listFiles(File file) {
        File[] afile = null;

        if (file.isDirectory()) {
            afile = this.fileFilter == null ? file.listFiles() : file.listFiles(this.fileFilter);
        }

        if (afile == null) {
            afile = FileUtils.EMPTY_FILE_ARRAY;
        }

        if (this.comparator != null && afile.length > 1) {
            Arrays.sort(afile, this.comparator);
        }

        return afile;
    }

    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(this.getClass().getSimpleName());
        stringbuilder.append("[file=\'");
        stringbuilder.append(this.getDirectory().getPath());
        stringbuilder.append('\'');
        if (this.fileFilter != null) {
            stringbuilder.append(", ");
            stringbuilder.append(this.fileFilter.toString());
        }

        stringbuilder.append(", listeners=");
        stringbuilder.append(this.listeners.size());
        stringbuilder.append("]");
        return stringbuilder.toString();
    }
}
