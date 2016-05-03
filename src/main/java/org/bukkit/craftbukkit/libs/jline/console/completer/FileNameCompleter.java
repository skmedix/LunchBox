package org.bukkit.craftbukkit.libs.jline.console.completer;

import java.io.File;
import java.util.List;
import org.bukkit.craftbukkit.libs.jline.internal.Configuration;
import org.bukkit.craftbukkit.libs.jline.internal.Preconditions;

public class FileNameCompleter implements Completer {

    private static final boolean OS_IS_WINDOWS;

    public int complete(String buffer, int cursor, List candidates) {
        Preconditions.checkNotNull(candidates);
        if (buffer == null) {
            buffer = "";
        }

        if (FileNameCompleter.OS_IS_WINDOWS) {
            buffer = buffer.replace('/', '\\');
        }

        String translated = buffer;
        File homeDir = this.getUserHome();

        if (buffer.startsWith("~" + this.separator())) {
            translated = homeDir.getPath() + buffer.substring(1);
        } else if (buffer.startsWith("~")) {
            translated = homeDir.getParentFile().getAbsolutePath();
        } else if (!(new File(buffer)).isAbsolute()) {
            String file = this.getUserDir().getAbsolutePath();

            translated = file + this.separator() + buffer;
        }

        File file1 = new File(translated);
        File dir;

        if (translated.endsWith(this.separator())) {
            dir = file1;
        } else {
            dir = file1.getParentFile();
        }

        File[] entries = dir == null ? new File[0] : dir.listFiles();

        return this.matchFiles(buffer, translated, entries, candidates);
    }

    protected String separator() {
        return File.separator;
    }

    protected File getUserHome() {
        return Configuration.getUserHome();
    }

    protected File getUserDir() {
        return new File(".");
    }

    protected int matchFiles(String buffer, String translated, File[] files, List candidates) {
        if (files == null) {
            return -1;
        } else {
            int matches = 0;
            File[] index = files;
            int len$ = files.length;

            int i$;
            File file;

            for (i$ = 0; i$ < len$; ++i$) {
                file = index[i$];
                if (file.getAbsolutePath().startsWith(translated)) {
                    ++matches;
                }
            }

            index = files;
            len$ = files.length;

            for (i$ = 0; i$ < len$; ++i$) {
                file = index[i$];
                if (file.getAbsolutePath().startsWith(translated)) {
                    String name = file.getName() + (matches == 1 && file.isDirectory() ? this.separator() : " ");

                    candidates.add(this.render(file, name).toString());
                }
            }

            int i = buffer.lastIndexOf(this.separator());

            return i + this.separator().length();
        }
    }

    protected CharSequence render(File file, CharSequence name) {
        return name;
    }

    static {
        String os = Configuration.getOsName();

        OS_IS_WINDOWS = os.contains("windows");
    }
}
