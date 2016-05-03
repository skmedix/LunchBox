package org.apache.commons.io;

import java.io.File;

/** @deprecated */
@Deprecated
public class FileCleaner {

    static final FileCleaningTracker theInstance = new FileCleaningTracker();

    /** @deprecated */
    @Deprecated
    public static void track(File file, Object object) {
        FileCleaner.theInstance.track(file, object);
    }

    /** @deprecated */
    @Deprecated
    public static void track(File file, Object object, FileDeleteStrategy filedeletestrategy) {
        FileCleaner.theInstance.track(file, object, filedeletestrategy);
    }

    /** @deprecated */
    @Deprecated
    public static void track(String s, Object object) {
        FileCleaner.theInstance.track(s, object);
    }

    /** @deprecated */
    @Deprecated
    public static void track(String s, Object object, FileDeleteStrategy filedeletestrategy) {
        FileCleaner.theInstance.track(s, object, filedeletestrategy);
    }

    /** @deprecated */
    @Deprecated
    public static int getTrackCount() {
        return FileCleaner.theInstance.getTrackCount();
    }

    /** @deprecated */
    @Deprecated
    public static synchronized void exitWhenFinished() {
        FileCleaner.theInstance.exitWhenFinished();
    }

    public static FileCleaningTracker getInstance() {
        return FileCleaner.theInstance;
    }
}
