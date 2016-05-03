package org.apache.logging.log4j.core.config;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class FileConfigurationMonitor implements ConfigurationMonitor {

    private static final int MASK = 15;
    private static final int MIN_INTERVAL = 5;
    private static final int MILLIS_PER_SECOND = 1000;
    private final File file;
    private long lastModified;
    private final List listeners;
    private final int interval;
    private long nextCheck;
    private volatile int counter = 0;
    private final Reconfigurable reconfigurable;

    public FileConfigurationMonitor(Reconfigurable reconfigurable, File file, List list, int i) {
        this.reconfigurable = reconfigurable;
        this.file = file;
        this.lastModified = file.lastModified();
        this.listeners = list;
        this.interval = (i < 5 ? 5 : i) * 1000;
        this.nextCheck = System.currentTimeMillis() + (long) i;
    }

    public void checkConfiguration() {
        if ((++this.counter & 15) == 0) {
            synchronized (this) {
                long i = System.currentTimeMillis();

                if (i >= this.nextCheck) {
                    this.nextCheck = i + (long) this.interval;
                    if (this.file.lastModified() > this.lastModified) {
                        this.lastModified = this.file.lastModified();
                        Iterator iterator = this.listeners.iterator();

                        while (iterator.hasNext()) {
                            ConfigurationListener configurationlistener = (ConfigurationListener) iterator.next();

                            configurationlistener.onChange(this.reconfigurable);
                        }
                    }
                }
            }
        }

    }
}
