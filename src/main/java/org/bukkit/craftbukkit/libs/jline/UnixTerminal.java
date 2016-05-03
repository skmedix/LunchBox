package org.bukkit.craftbukkit.libs.jline;

import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.craftbukkit.libs.jline.internal.TerminalLineSettings;

public class UnixTerminal extends TerminalSupport {

    private final TerminalLineSettings settings = new TerminalLineSettings();

    public UnixTerminal() throws Exception {
        super(true);
    }

    protected TerminalLineSettings getSettings() {
        return this.settings;
    }

    public void init() throws Exception {
        super.init();
        this.setAnsiSupported(true);
        this.settings.set("-icanon min 1 -icrnl -inlcr -ixon");
        this.settings.set("dsusp undef");
        this.setEchoEnabled(false);
    }

    public void restore() throws Exception {
        this.settings.restore();
        super.restore();
    }

    public int getWidth() {
        int w = this.settings.getProperty("columns");

        return w < 1 ? 80 : w;
    }

    public int getHeight() {
        int h = this.settings.getProperty("rows");

        return h < 1 ? 24 : h;
    }

    public synchronized void setEchoEnabled(boolean enabled) {
        try {
            if (enabled) {
                this.settings.set("echo");
            } else {
                this.settings.set("-echo");
            }

            super.setEchoEnabled(enabled);
        } catch (Exception exception) {
            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }

            Log.error(new Object[] { "Failed to ", enabled ? "enable" : "disable", " echo", exception});
        }

    }

    public void disableInterruptCharacter() {
        try {
            this.settings.set("intr undef");
        } catch (Exception exception) {
            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }

            Log.error(new Object[] { "Failed to disable interrupt character", exception});
        }

    }

    public void enableInterruptCharacter() {
        try {
            this.settings.set("intr ^C");
        } catch (Exception exception) {
            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }

            Log.error(new Object[] { "Failed to enable interrupt character", exception});
        }

    }
}
