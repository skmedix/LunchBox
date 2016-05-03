package org.bukkit.craftbukkit.libs.jline.console.history;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.Iterator;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.craftbukkit.libs.jline.internal.Preconditions;

public class FileHistory extends MemoryHistory implements PersistentHistory, Flushable {

    private final File file;

    public FileHistory(File file) throws IOException {
        this.file = (File) Preconditions.checkNotNull(file);
        this.load(file);
    }

    public File getFile() {
        return this.file;
    }

    public void load(File file) throws IOException {
        Preconditions.checkNotNull(file);
        if (file.exists()) {
            Log.trace(new Object[] { "Loading history from: ", file});
            this.load((Reader) (new FileReader(file)));
        }

    }

    public void load(InputStream input) throws IOException {
        Preconditions.checkNotNull(input);
        this.load((Reader) (new InputStreamReader(input)));
    }

    public void load(Reader reader) throws IOException {
        Preconditions.checkNotNull(reader);
        BufferedReader input = new BufferedReader(reader);

        String item;

        while ((item = input.readLine()) != null) {
            this.internalAdd(item);
        }

    }

    public void flush() throws IOException {
        Log.trace(new Object[] { "Flushing history"});
        if (!this.file.exists()) {
            File out = this.file.getParentFile();

            if (!out.exists() && !out.mkdirs()) {
                Log.warn(new Object[] { "Failed to create directory: ", out});
            }

            if (!this.file.createNewFile()) {
                Log.warn(new Object[] { "Failed to create file: ", this.file});
            }
        }

        PrintStream out1 = new PrintStream(new BufferedOutputStream(new FileOutputStream(this.file)));

        try {
            Iterator i$ = this.iterator();

            while (i$.hasNext()) {
                History.Entry entry = (History.Entry) i$.next();

                out1.println(entry.value());
            }
        } finally {
            out1.close();
        }

    }

    public void purge() throws IOException {
        Log.trace(new Object[] { "Purging history"});
        this.clear();
        if (!this.file.delete()) {
            Log.warn(new Object[] { "Failed to delete history file: ", this.file});
        }

    }
}
