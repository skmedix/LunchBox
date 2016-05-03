package org.apache.logging.log4j.core.appender.rolling;

import java.io.File;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.appender.rolling.helper.Action;
import org.apache.logging.log4j.core.appender.rolling.helper.FileRenameAction;
import org.apache.logging.log4j.core.appender.rolling.helper.GZCompressAction;
import org.apache.logging.log4j.core.appender.rolling.helper.ZipCompressAction;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Integers;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
    name = "DefaultRolloverStrategy",
    category = "Core",
    printObject = true
)
public class DefaultRolloverStrategy implements RolloverStrategy {

    protected static final Logger LOGGER = StatusLogger.getLogger();
    private static final int MIN_WINDOW_SIZE = 1;
    private static final int DEFAULT_WINDOW_SIZE = 7;
    private final int maxIndex;
    private final int minIndex;
    private final boolean useMax;
    private final StrSubstitutor subst;
    private final int compressionLevel;

    protected DefaultRolloverStrategy(int i, int j, boolean flag, int k, StrSubstitutor strsubstitutor) {
        this.minIndex = i;
        this.maxIndex = j;
        this.useMax = flag;
        this.compressionLevel = k;
        this.subst = strsubstitutor;
    }

    public RolloverDescription rollover(RollingFileManager rollingfilemanager) throws SecurityException {
        if (this.maxIndex >= 0) {
            int i;

            if ((i = this.purge(this.minIndex, this.maxIndex, rollingfilemanager)) < 0) {
                return null;
            } else {
                StringBuilder stringbuilder = new StringBuilder();

                rollingfilemanager.getPatternProcessor().formatFileName(this.subst, stringbuilder, Integer.valueOf(i));
                String s = rollingfilemanager.getFileName();
                String s1 = stringbuilder.toString();
                String s2 = s1;
                Object object = null;

                if (s1.endsWith(".gz")) {
                    s1 = s1.substring(0, s1.length() - 3);
                    object = new GZCompressAction(new File(s1), new File(s2), true);
                } else if (s1.endsWith(".zip")) {
                    s1 = s1.substring(0, s1.length() - 4);
                    object = new ZipCompressAction(new File(s1), new File(s2), true, this.compressionLevel);
                }

                FileRenameAction filerenameaction = new FileRenameAction(new File(s), new File(s1), false);

                return new RolloverDescriptionImpl(s, false, filerenameaction, (Action) object);
            }
        } else {
            return null;
        }
    }

    private int purge(int i, int j, RollingFileManager rollingfilemanager) {
        return this.useMax ? this.purgeAscending(i, j, rollingfilemanager) : this.purgeDescending(i, j, rollingfilemanager);
    }

    private int purgeDescending(int i, int j, RollingFileManager rollingfilemanager) {
        byte b0 = 0;
        ArrayList arraylist = new ArrayList();
        StringBuilder stringbuilder = new StringBuilder();

        rollingfilemanager.getPatternProcessor().formatFileName(stringbuilder, (Object) Integer.valueOf(i));
        String s = this.subst.replace(stringbuilder);

        if (s.endsWith(".gz")) {
            b0 = 3;
        } else if (s.endsWith(".zip")) {
            b0 = 4;
        }

        int k;

        for (k = i; k <= j; ++k) {
            File file = new File(s);
            boolean flag = false;

            if (b0 > 0) {
                File file1 = new File(s.substring(0, s.length() - b0));

                if (file.exists()) {
                    if (file1.exists()) {
                        file1.delete();
                    }
                } else {
                    file = file1;
                    flag = true;
                }
            }

            if (!file.exists()) {
                break;
            }

            if (k == j) {
                if (!file.delete()) {
                    return -1;
                }
                break;
            }

            stringbuilder.setLength(0);
            rollingfilemanager.getPatternProcessor().formatFileName(stringbuilder, (Object) Integer.valueOf(k + 1));
            String s1 = this.subst.replace(stringbuilder);
            String s2 = s1;

            if (flag) {
                s2 = s1.substring(0, s1.length() - b0);
            }

            arraylist.add(new FileRenameAction(file, new File(s2), true));
            s = s1;
        }

        for (k = arraylist.size() - 1; k >= 0; --k) {
            Action action = (Action) arraylist.get(k);

            try {
                if (!action.execute()) {
                    return -1;
                }
            } catch (Exception exception) {
                DefaultRolloverStrategy.LOGGER.warn("Exception during purge in RollingFileAppender", (Throwable) exception);
                return -1;
            }
        }

        return i;
    }

    private int purgeAscending(int i, int j, RollingFileManager rollingfilemanager) {
        byte b0 = 0;
        ArrayList arraylist = new ArrayList();
        StringBuilder stringbuilder = new StringBuilder();

        rollingfilemanager.getPatternProcessor().formatFileName(stringbuilder, (Object) Integer.valueOf(j));
        String s = this.subst.replace(stringbuilder);

        if (s.endsWith(".gz")) {
            b0 = 3;
        } else if (s.endsWith(".zip")) {
            b0 = 4;
        }

        int k = 0;

        int l;

        for (l = j; l >= i; --l) {
            File file = new File(s);

            if (l == j && file.exists()) {
                k = j;
            } else if (k == 0 && file.exists()) {
                k = l + 1;
                break;
            }

            boolean flag = false;

            if (b0 > 0) {
                File file1 = new File(s.substring(0, s.length() - b0));

                if (file.exists()) {
                    if (file1.exists()) {
                        file1.delete();
                    }
                } else {
                    file = file1;
                    flag = true;
                }
            }

            if (file.exists()) {
                if (l == i) {
                    if (!file.delete()) {
                        return -1;
                    }
                    break;
                }

                stringbuilder.setLength(0);
                rollingfilemanager.getPatternProcessor().formatFileName(stringbuilder, (Object) Integer.valueOf(l - 1));
                String s1 = this.subst.replace(stringbuilder);
                String s2 = s1;

                if (flag) {
                    s2 = s1.substring(0, s1.length() - b0);
                }

                arraylist.add(new FileRenameAction(file, new File(s2), true));
                s = s1;
            } else {
                stringbuilder.setLength(0);
                rollingfilemanager.getPatternProcessor().formatFileName(stringbuilder, (Object) Integer.valueOf(l - 1));
                s = this.subst.replace(stringbuilder);
            }
        }

        if (k == 0) {
            k = i;
        }

        for (l = arraylist.size() - 1; l >= 0; --l) {
            Action action = (Action) arraylist.get(l);

            try {
                if (!action.execute()) {
                    return -1;
                }
            } catch (Exception exception) {
                DefaultRolloverStrategy.LOGGER.warn("Exception during purge in RollingFileAppender", (Throwable) exception);
                return -1;
            }
        }

        return k;
    }

    public String toString() {
        return "DefaultRolloverStrategy(min=" + this.minIndex + ", max=" + this.maxIndex + ")";
    }

    @PluginFactory
    public static DefaultRolloverStrategy createStrategy(@PluginAttribute("max") String s, @PluginAttribute("min") String s1, @PluginAttribute("fileIndex") String s2, @PluginAttribute("compressionLevel") String s3, @PluginConfiguration Configuration configuration) {
        boolean flag = s2 == null ? true : s2.equalsIgnoreCase("max");
        int i;

        if (s1 != null) {
            i = Integer.parseInt(s1);
            if (i < 1) {
                DefaultRolloverStrategy.LOGGER.error("Minimum window size too small. Limited to 1");
                i = 1;
            }
        } else {
            i = 1;
        }

        int j;

        if (s != null) {
            j = Integer.parseInt(s);
            if (j < i) {
                j = i < 7 ? 7 : i;
                DefaultRolloverStrategy.LOGGER.error("Maximum window size must be greater than the minimum windows size. Set to " + j);
            }
        } else {
            j = 7;
        }

        int k = Integers.parseInt(s3, -1);

        return new DefaultRolloverStrategy(i, j, flag, k, configuration.getStrSubstitutor());
    }
}
