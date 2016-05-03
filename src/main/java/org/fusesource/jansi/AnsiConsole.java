package org.fusesource.jansi;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.fusesource.jansi.internal.CLibrary;

public class AnsiConsole {

    public static final PrintStream system_out = System.out;
    public static final PrintStream out = new PrintStream(wrapOutputStream(AnsiConsole.system_out));
    public static final PrintStream system_err = System.err;
    public static final PrintStream err = new PrintStream(wrapOutputStream(AnsiConsole.system_err));
    private static int installed;

    public static OutputStream wrapOutputStream(final OutputStream stream) {
        if (Boolean.getBoolean("jansi.passthrough")) {
            return stream;
        } else if (Boolean.getBoolean("jansi.strip")) {
            return new AnsiOutputStream(stream);
        } else {
            String os = System.getProperty("os.name");

            if (os.startsWith("Windows")) {
                try {
                    return new WindowsAnsiOutputStream(stream);
                } catch (Throwable throwable) {
                    return new AnsiOutputStream(stream);
                }
            } else {
                try {
                    int ignore = CLibrary.isatty(CLibrary.STDOUT_FILENO);

                    if (ignore == 0) {
                        return new AnsiOutputStream(stream);
                    }
                } catch (NoClassDefFoundError noclassdeffounderror) {
                    ;
                } catch (UnsatisfiedLinkError unsatisfiedlinkerror) {
                    ;
                }

                return new FilterOutputStream(stream) {
                    public void close() throws IOException {
                        this.write(AnsiOutputStream.REST_CODE);
                        this.flush();
                        super.close();
                    }
                };
            }
        }
    }

    public static PrintStream out() {
        return AnsiConsole.out;
    }

    public static PrintStream err() {
        return AnsiConsole.err;
    }

    public static synchronized void systemInstall() {
        ++AnsiConsole.installed;
        if (AnsiConsole.installed == 1) {
            System.setOut(AnsiConsole.out);
            System.setErr(AnsiConsole.err);
        }

    }

    public static synchronized void systemUninstall() {
        --AnsiConsole.installed;
        if (AnsiConsole.installed == 0) {
            System.setOut(AnsiConsole.system_out);
            System.setErr(AnsiConsole.system_err);
        }

    }
}
