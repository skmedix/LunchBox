package org.bukkit.craftbukkit.v1_8_R3.util;

import com.mojang.util.QueueLogAppender;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.craftbukkit.Main;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;

public class TerminalConsoleWriterThread implements Runnable {

    private final ConsoleReader reader;
    private final OutputStream output;

    public TerminalConsoleWriterThread(OutputStream output, ConsoleReader reader) {
        this.output = output;
        this.reader = reader;
    }

    public void run() {
        while (true) {
            String message = QueueLogAppender.getNextLogEvent("TerminalConsole");

            if (message != null) {
                try {
                    if (Main.useJline) {
                        this.reader.print((CharSequence) "\r");
                        this.reader.flush();
                        this.output.write(message.getBytes());
                        this.output.flush();

                        try {
                            this.reader.drawLine();
                        } catch (Throwable throwable) {
                            this.reader.getCursorBuffer().clear();
                        }

                        this.reader.flush();
                    } else {
                        this.output.write(message.getBytes());
                        this.output.flush();
                    }
                } catch (IOException ioexception) {
                    Logger.getLogger(TerminalConsoleWriterThread.class.getName()).log(Level.SEVERE, (String) null, ioexception);
                }
            }
        }
    }
}
