package org.bukkit.craftbukkit.libs.jline.console;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Stack;
import org.bukkit.craftbukkit.libs.jline.Terminal;
import org.bukkit.craftbukkit.libs.jline.TerminalFactory;
import org.bukkit.craftbukkit.libs.jline.UnixTerminal;
import org.bukkit.craftbukkit.libs.jline.console.completer.CandidateListCompletionHandler;
import org.bukkit.craftbukkit.libs.jline.console.completer.Completer;
import org.bukkit.craftbukkit.libs.jline.console.completer.CompletionHandler;
import org.bukkit.craftbukkit.libs.jline.console.history.History;
import org.bukkit.craftbukkit.libs.jline.console.history.MemoryHistory;
import org.bukkit.craftbukkit.libs.jline.internal.Configuration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.craftbukkit.libs.jline.internal.Log;
import org.bukkit.craftbukkit.libs.jline.internal.NonBlockingInputStream;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.craftbukkit.libs.jline.internal.Preconditions;
import org.bukkit.craftbukkit.libs.jline.internal.Urls;
import org.fusesource.jansi.AnsiOutputStream;

public class ConsoleReader {

    public static final String JLINE_NOBELL = "org.bukkit.craftbukkit.libs.jline.nobell";
    public static final String JLINE_ESC_TIMEOUT = "org.bukkit.craftbukkit.libs.jline.esc.timeout";
    public static final String JLINE_INPUTRC = "org.bukkit.craftbukkit.libs.jline.inputrc";
    public static final String INPUT_RC = ".inputrc";
    public static final String DEFAULT_INPUT_RC = "/etc/inputrc";
    public static final char BACKSPACE = '\b';
    public static final char RESET_LINE = '\r';
    public static final char KEYBOARD_BELL = '\u0007';
    public static final char NULL_MASK = '\u0000';
    public static final int TAB_WIDTH = 4;
    private static final ResourceBundle resources = ResourceBundle.getBundle(CandidateListCompletionHandler.class.getName());
    private final Terminal terminal;
    private final Writer out;
    private final CursorBuffer buf;
    private String prompt;
    private int promptLen;
    private boolean expandEvents;
    private boolean bellEnabled;
    private boolean handleUserInterrupt;
    private Character mask;
    private Character echoCharacter;
    private StringBuffer searchTerm;
    private String previousSearchTerm;
    private int searchIndex;
    private int parenBlinkTimeout;
    private NonBlockingInputStream in;
    private long escapeTimeout;
    private Reader reader;
    private boolean isUnitTestInput;
    private char charSearchChar;
    private char charSearchLastInvokeChar;
    private char charSearchFirstInvokeChar;
    private String yankBuffer;
    private KillRing killRing;
    private String encoding;
    private boolean recording;
    private String macro;
    private String appName;
    private URL inputrcUrl;
    private ConsoleKeys consoleKeys;
    private String commentBegin;
    private boolean skipLF;
    private boolean copyPasteDetection;
    private ConsoleReader.State state;
    public static final String JLINE_COMPLETION_THRESHOLD = "org.bukkit.craftbukkit.libs.jline.completion.threshold";
    private final List completers;
    private CompletionHandler completionHandler;
    private int autoprintThreshold;
    private boolean paginationEnabled;
    private History history;
    private boolean historyEnabled;
    public static final String CR = Configuration.getLineSeparator();
    private final Map triggeredActions;
    private Thread maskThread;

    public ConsoleReader() throws IOException {
        this((String) null, new FileInputStream(FileDescriptor.in), System.out, (Terminal) null);
    }

    public ConsoleReader(InputStream in, OutputStream out) throws IOException {
        this((String) null, in, out, (Terminal) null);
    }

    public ConsoleReader(InputStream in, OutputStream out, Terminal term) throws IOException {
        this((String) null, in, out, term);
    }

    public ConsoleReader(@Nullable String appName, InputStream in, OutputStream out, @Nullable Terminal term) throws IOException {
        this(appName, in, out, term, (String) null);
    }

    public ConsoleReader(@Nullable String appName, InputStream in, OutputStream out, @Nullable Terminal term, @Nullable String encoding) throws IOException {
        this.buf = new CursorBuffer();
        this.expandEvents = true;
        this.bellEnabled = !Configuration.getBoolean("org.bukkit.craftbukkit.libs.jline.nobell", true);
        this.handleUserInterrupt = false;
        this.searchTerm = null;
        this.previousSearchTerm = "";
        this.searchIndex = -1;
        this.parenBlinkTimeout = 500;
        this.charSearchChar = 0;
        this.charSearchLastInvokeChar = 0;
        this.charSearchFirstInvokeChar = 0;
        this.yankBuffer = "";
        this.killRing = new KillRing();
        this.macro = "";
        this.commentBegin = null;
        this.skipLF = false;
        this.copyPasteDetection = false;
        this.state = ConsoleReader.State.NORMAL;
        this.completers = new LinkedList();
        this.completionHandler = new CandidateListCompletionHandler();
        this.autoprintThreshold = Configuration.getInteger("org.bukkit.craftbukkit.libs.jline.completion.threshold", 100);
        this.history = new MemoryHistory();
        this.historyEnabled = true;
        this.triggeredActions = new HashMap();
        this.appName = appName != null ? appName : "JLine";
        this.encoding = encoding != null ? encoding : Configuration.getEncoding();
        this.terminal = term != null ? term : TerminalFactory.get();
        String outEncoding = this.terminal.getOutputEncoding() != null ? this.terminal.getOutputEncoding() : this.encoding;

        this.out = new OutputStreamWriter(this.terminal.wrapOutIfNeeded(out), outEncoding);
        this.setInput(in);
        this.inputrcUrl = this.getInputRc();
        this.consoleKeys = new ConsoleKeys(this.appName, this.inputrcUrl);
    }

    private URL getInputRc() throws IOException {
        String path = Configuration.getString("org.bukkit.craftbukkit.libs.jline.inputrc");

        if (path == null) {
            File f = new File(Configuration.getUserHome(), ".inputrc");

            if (!f.exists()) {
                f = new File("/etc/inputrc");
            }

            return f.toURI().toURL();
        } else {
            return Urls.create(path);
        }
    }

    public KeyMap getKeys() {
        return this.consoleKeys.getKeys();
    }

    void setInput(InputStream in) throws IOException {
        this.escapeTimeout = Configuration.getLong("org.bukkit.craftbukkit.libs.jline.esc.timeout", 100L);
        this.isUnitTestInput = in instanceof ByteArrayInputStream;
        boolean nonBlockingEnabled = this.escapeTimeout > 0L && this.terminal.isSupported() && in != null;

        if (this.in != null) {
            this.in.shutdown();
        }

        InputStream wrapped = this.terminal.wrapInIfNeeded(in);

        this.in = new NonBlockingInputStream(wrapped, nonBlockingEnabled);
        this.reader = new InputStreamReader(this.in, this.encoding);
    }

    public void shutdown() {
        if (this.in != null) {
            this.in.shutdown();
        }

    }

    protected void finalize() throws Throwable {
        try {
            this.shutdown();
        } finally {
            super.finalize();
        }

    }

    public InputStream getInput() {
        return this.in;
    }

    public Writer getOutput() {
        return this.out;
    }

    public Terminal getTerminal() {
        return this.terminal;
    }

    public CursorBuffer getCursorBuffer() {
        return this.buf;
    }

    public void setExpandEvents(boolean expand) {
        this.expandEvents = expand;
    }

    public boolean getExpandEvents() {
        return this.expandEvents;
    }

    public void setCopyPasteDetection(boolean onoff) {
        this.copyPasteDetection = onoff;
    }

    public boolean isCopyPasteDetectionEnabled() {
        return this.copyPasteDetection;
    }

    public void setBellEnabled(boolean enabled) {
        this.bellEnabled = enabled;
    }

    public boolean getBellEnabled() {
        return this.bellEnabled;
    }

    public void setHandleUserInterrupt(boolean enabled) {
        this.handleUserInterrupt = enabled;
    }

    public boolean getHandleUserInterrupt() {
        return this.handleUserInterrupt;
    }

    public void setCommentBegin(String commentBegin) {
        this.commentBegin = commentBegin;
    }

    public String getCommentBegin() {
        String str = this.commentBegin;

        if (str == null) {
            str = this.consoleKeys.getVariable("comment-begin");
            if (str == null) {
                str = "#";
            }
        }

        return str;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
        this.promptLen = prompt == null ? 0 : this.stripAnsi(this.lastLine(prompt)).length();
    }

    public String getPrompt() {
        return this.prompt;
    }

    public void setEchoCharacter(Character c) {
        this.echoCharacter = c;
    }

    public Character getEchoCharacter() {
        return this.echoCharacter;
    }

    protected final boolean resetLine() throws IOException {
        if (this.buf.cursor == 0) {
            return false;
        } else {
            StringBuilder killed = new StringBuilder();

            while (this.buf.cursor > 0) {
                char copy = this.buf.current();

                if (copy == 0) {
                    break;
                }

                killed.append(copy);
                this.backspace();
            }

            String copy1 = killed.reverse().toString();

            this.killRing.addBackwards(copy1);
            return true;
        }
    }

    int getCursorPosition() {
        return this.promptLen + this.buf.cursor;
    }

    private String lastLine(String str) {
        if (str == null) {
            return "";
        } else {
            int last = str.lastIndexOf("\n");

            return last >= 0 ? str.substring(last + 1, str.length()) : str;
        }
    }

    private String stripAnsi(String str) {
        if (str == null) {
            return "";
        } else {
            try {
                ByteArrayOutputStream e = new ByteArrayOutputStream();
                AnsiOutputStream aos = new AnsiOutputStream(e);

                aos.write(str.getBytes());
                aos.flush();
                return e.toString();
            } catch (IOException ioexception) {
                return str;
            }
        }
    }

    public final boolean setCursorPosition(int position) throws IOException {
        return position == this.buf.cursor ? true : this.moveCursor(position - this.buf.cursor) != 0;
    }

    private void setBuffer(String buffer) throws IOException {
        if (!buffer.equals(this.buf.buffer.toString())) {
            int sameIndex = 0;
            int diff = 0;
            int l1 = buffer.length();

            for (int l2 = this.buf.buffer.length(); diff < l1 && diff < l2 && buffer.charAt(diff) == this.buf.buffer.charAt(diff); ++diff) {
                ++sameIndex;
            }

            diff = this.buf.cursor - sameIndex;
            if (diff < 0) {
                this.moveToEnd();
                diff = this.buf.buffer.length() - sameIndex;
            }

            this.backspace(diff);
            this.killLine();
            this.buf.buffer.setLength(sameIndex);
            this.putString(buffer.substring(sameIndex));
        }
    }

    private void setBuffer(CharSequence buffer) throws IOException {
        this.setBuffer(String.valueOf(buffer));
    }

    private void setBufferKeepPos(String buffer) throws IOException {
        int pos = this.buf.cursor;

        this.setBuffer(buffer);
        this.setCursorPosition(pos);
    }

    private void setBufferKeepPos(CharSequence buffer) throws IOException {
        this.setBufferKeepPos(String.valueOf(buffer));
    }

    public final void drawLine() throws IOException {
        String prompt = this.getPrompt();

        if (prompt != null) {
            this.print((CharSequence) prompt);
        }

        this.print((CharSequence) this.buf.buffer.toString());
        if (this.buf.length() != this.buf.cursor) {
            this.back(this.buf.length() - this.buf.cursor - 1);
        }

        this.drawBuffer();
    }

    public final void redrawLine() throws IOException {
        this.print(13);
        this.drawLine();
    }

    final String finishBuffer() throws IOException {
        String str = this.buf.buffer.toString();
        String historyLine = str;

        if (this.expandEvents) {
            try {
                str = this.expandEvents(str);
                historyLine = str.replace("!", "\\!");
                historyLine = historyLine.replaceAll("^\\^", "\\\\^");
            } catch (IllegalArgumentException illegalargumentexception) {
                Log.error(new Object[] { "Could not expand event", illegalargumentexception});
                this.beep();
                this.buf.clear();
                str = "";
            }
        }

        if (str.length() > 0) {
            if (this.mask == null && this.isHistoryEnabled()) {
                this.history.add(historyLine);
            } else {
                this.mask = null;
            }
        }

        this.history.moveToEnd();
        this.buf.buffer.setLength(0);
        this.buf.cursor = 0;
        return str;
    }

    protected String expandEvents(String str) throws IOException {
        StringBuilder sb = new StringBuilder();

        for (int result = 0; result < str.length(); ++result) {
            char c = str.charAt(result);
            String s;

            switch (c) {
            case '!':
                if (result + 1 < str.length()) {
                    ++result;
                    c = str.charAt(result);
                    boolean flag = false;
                    String s = null;
                    int i;
                    int j;

                    switch (c) {
                    case '\t':
                    case ' ':
                        sb.append('!');
                        sb.append(c);
                        break;

                    case '\n':
                    case '\u000b':
                    case '\f':
                    case '\r':
                    case '\u000e':
                    case '\u000f':
                    case '\u0010':
                    case '\u0011':
                    case '\u0012':
                    case '\u0013':
                    case '\u0014':
                    case '\u0015':
                    case '\u0016':
                    case '\u0017':
                    case '\u0018':
                    case '\u0019':
                    case '\u001a':
                    case '\u001b':
                    case '\u001c':
                    case '\u001d':
                    case '\u001e':
                    case '\u001f':
                    case '\"':
                    case '%':
                    case '&':
                    case '\'':
                    case '(':
                    case ')':
                    case '*':
                    case '+':
                    case ',':
                    case '.':
                    case '/':
                    case ':':
                    case ';':
                    case '<':
                    case '=':
                    case '>':
                    default:
                        String ss = str.substring(result);

                        result = str.length();
                        j = this.searchBackwards(ss, this.history.index(), true);
                        if (j < 0) {
                            throw new IllegalArgumentException("!" + ss + ": event not found");
                        }

                        s = this.history.get(j).toString();
                        break;

                    case '!':
                        if (this.history.size() == 0) {
                            throw new IllegalArgumentException("!!: event not found");
                        }

                        s = this.history.get(this.history.index() - 1).toString();
                        break;

                    case '#':
                        sb.append(sb.toString());
                        break;

                    case '$':
                        if (this.history.size() == 0) {
                            throw new IllegalArgumentException("!$: event not found");
                        }

                        String previous = this.history.get(this.history.index() - 1).toString().trim();
                        int lastSpace = previous.lastIndexOf(32);

                        if (lastSpace != -1) {
                            s = previous.substring(lastSpace + 1);
                        } else {
                            s = previous;
                        }
                        break;

                    case '-':
                        flag = true;
                        ++result;

                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                    case '8':
                    case '9':
                        for (i = result; result < str.length(); ++result) {
                            c = str.charAt(result);
                            if (c < 48 || c > 57) {
                                break;
                            }
                        }

                        boolean flag1 = false;

                        try {
                            j = Integer.parseInt(str.substring(i, result));
                        } catch (NumberFormatException numberformatexception) {
                            throw new IllegalArgumentException((flag ? "!-" : "!") + str.substring(i, result) + ": event not found");
                        }

                        if (flag) {
                            if (j <= 0 || j > this.history.size()) {
                                throw new IllegalArgumentException((flag ? "!-" : "!") + str.substring(i, result) + ": event not found");
                            }

                            s = this.history.get(this.history.index() - j).toString();
                        } else {
                            if (j <= this.history.index() - this.history.size() || j > this.history.index()) {
                                throw new IllegalArgumentException((flag ? "!-" : "!") + str.substring(i, result) + ": event not found");
                            }

                            s = this.history.get(j - 1).toString();
                        }
                        break;

                    case '?':
                        i = str.indexOf(63, result + 1);
                        if (i < 0) {
                            i = str.length();
                        }

                        s = str.substring(result + 1, i);
                        result = i;
                        j = this.searchBackwards(s);
                        if (j < 0) {
                            throw new IllegalArgumentException("!?" + s + ": event not found");
                        }

                        s = this.history.get(j).toString();
                    }

                    if (s != null) {
                        sb.append(s);
                    }
                } else {
                    sb.append(c);
                }
                break;

            case '\\':
                if (result + 1 < str.length()) {
                    char c0 = str.charAt(result + 1);

                    if (c0 == 33 || c0 == 94 && result == 0) {
                        c = c0;
                        ++result;
                    }
                }

                sb.append(c);
                break;

            case '^':
                if (result == 0) {
                    int i1 = str.indexOf(94, result + 1);
                    int i2 = str.indexOf(94, i1 + 1);

                    if (i2 < 0) {
                        i2 = str.length();
                    }

                    if (i1 > 0 && i2 > 0) {
                        String s1 = str.substring(result + 1, i1);
                        String s2 = str.substring(i1 + 1, i2);

                        s = this.history.get(this.history.index() - 1).toString().replace(s1, s2);
                        sb.append(s);
                        result = i2 + 1;
                        break;
                    }
                }

                sb.append(c);
                break;

            default:
                sb.append(c);
            }
        }

        String s1 = sb.toString();

        if (!str.equals(s1)) {
            this.print((CharSequence) s1);
            this.println();
            this.flush();
        }

        return s1;
    }

    public final void putString(CharSequence str) throws IOException {
        this.buf.write(str);
        if (this.mask == null) {
            this.print(str);
        } else if (this.mask.charValue() != 0) {
            this.print(this.mask.charValue(), str.length());
        }

        this.drawBuffer();
    }

    private void drawBuffer(int clear) throws IOException {
        if (this.buf.cursor != this.buf.length() || clear != 0) {
            char[] width = this.buf.buffer.substring(this.buf.cursor).toCharArray();

            if (this.mask != null) {
                Arrays.fill(width, this.mask.charValue());
            }

            if (this.terminal.hasWeirdWrap()) {
                int width1 = this.terminal.getWidth();
                int pos = this.getCursorPosition();

                for (int i = 0; i < width.length; ++i) {
                    this.print(width[i]);
                    if ((pos + i + 1) % width1 == 0) {
                        this.print(32);
                        this.print(13);
                    }
                }
            } else {
                this.print(width);
            }

            this.clearAhead(clear, width.length);
            if (this.terminal.isAnsiSupported()) {
                if (width.length > 0) {
                    this.back(width.length);
                }
            } else {
                this.back(width.length);
            }
        }

        if (this.terminal.hasWeirdWrap()) {
            int i = this.terminal.getWidth();

            if (this.getCursorPosition() > 0 && this.getCursorPosition() % i == 0 && this.buf.cursor == this.buf.length() && clear == 0) {
                this.print(32);
                this.print(13);
            }
        }

    }

    private void drawBuffer() throws IOException {
        this.drawBuffer(0);
    }

    private void clearAhead(int num, int delta) throws IOException {
        if (num != 0) {
            if (!this.terminal.isAnsiSupported()) {
                this.print(' ', num);
                this.back(num);
            } else {
                int width = this.terminal.getWidth();
                int screenCursorCol = this.getCursorPosition() + delta;

                this.printAnsiSequence("K");
                int curCol = screenCursorCol % width;
                int endCol = (screenCursorCol + num - 1) % width;
                int lines = num / width;

                if (endCol < curCol) {
                    ++lines;
                }

                int i;

                for (i = 0; i < lines; ++i) {
                    this.printAnsiSequence("B");
                    this.printAnsiSequence("2K");
                }

                for (i = 0; i < lines; ++i) {
                    this.printAnsiSequence("A");
                }

            }
        }
    }

    protected void back(int num) throws IOException {
        if (num != 0) {
            if (this.terminal.isAnsiSupported()) {
                int width = this.getTerminal().getWidth();
                int cursor = this.getCursorPosition();
                int realCursor = cursor + num;
                int realCol = realCursor % width;
                int newCol = cursor % width;
                int moveup = num / width;
                int delta = realCol - newCol;

                if (delta < 0) {
                    ++moveup;
                }

                if (moveup > 0) {
                    this.printAnsiSequence(moveup + "A");
                }

                this.printAnsiSequence(1 + newCol + "G");
            } else {
                this.print('\b', num);
            }
        }
    }

    public void flush() throws IOException {
        this.out.flush();
    }

    private int backspaceAll() throws IOException {
        return this.backspace(Integer.MAX_VALUE);
    }

    private int backspace(int num) throws IOException {
        if (this.buf.cursor == 0) {
            return 0;
        } else {
            boolean count = false;
            int termwidth = this.getTerminal().getWidth();
            int lines = this.getCursorPosition() / termwidth;
            int count1 = this.moveCursor(-1 * num) * -1;

            this.buf.buffer.delete(this.buf.cursor, this.buf.cursor + count1);
            if (this.getCursorPosition() / termwidth != lines && this.terminal.isAnsiSupported()) {
                this.printAnsiSequence("K");
            }

            this.drawBuffer(count1);
            return count1;
        }
    }

    public boolean backspace() throws IOException {
        return this.backspace(1) == 1;
    }

    protected boolean moveToEnd() throws IOException {
        return this.buf.cursor == this.buf.length() ? true : this.moveCursor(this.buf.length() - this.buf.cursor) > 0;
    }

    private boolean deleteCurrentCharacter() throws IOException {
        if (this.buf.length() != 0 && this.buf.cursor != this.buf.length()) {
            this.buf.buffer.deleteCharAt(this.buf.cursor);
            this.drawBuffer(1);
            return true;
        } else {
            return false;
        }
    }

    private Operation viDeleteChangeYankToRemap(Operation op) {
        switch (ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[op.ordinal()]) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
        case 12:
        case 13:
        case 14:
        case 15:
        case 16:
        case 17:
        case 18:
            return op;

        default:
            return Operation.VI_MOVEMENT_MODE;
        }
    }

    private boolean viRubout(int count) throws IOException {
        boolean ok = true;

        for (int i = 0; ok && i < count; ++i) {
            ok = this.backspace();
        }

        return ok;
    }

    private boolean viDelete(int count) throws IOException {
        boolean ok = true;

        for (int i = 0; ok && i < count; ++i) {
            ok = this.deleteCurrentCharacter();
        }

        return ok;
    }

    private boolean viChangeCase(int count) throws IOException {
        boolean ok = true;

        for (int i = 0; ok && i < count; ++i) {
            ok = this.buf.cursor < this.buf.buffer.length();
            if (ok) {
                char ch = this.buf.buffer.charAt(this.buf.cursor);

                if (Character.isUpperCase(ch)) {
                    ch = Character.toLowerCase(ch);
                } else if (Character.isLowerCase(ch)) {
                    ch = Character.toUpperCase(ch);
                }

                this.buf.buffer.setCharAt(this.buf.cursor, ch);
                this.drawBuffer(1);
                this.moveCursor(1);
            }
        }

        return ok;
    }

    private boolean viChangeChar(int count, int c) throws IOException {
        if (c >= 0 && c != 27 && c != 3) {
            boolean ok = true;

            for (int i = 0; ok && i < count; ++i) {
                ok = this.buf.cursor < this.buf.buffer.length();
                if (ok) {
                    this.buf.buffer.setCharAt(this.buf.cursor, (char) c);
                    this.drawBuffer(1);
                    if (i < count - 1) {
                        this.moveCursor(1);
                    }
                }
            }

            return ok;
        } else {
            return true;
        }
    }

    private boolean viPreviousWord(int count) throws IOException {
        boolean ok = true;

        if (this.buf.cursor == 0) {
            return false;
        } else {
            int pos = this.buf.cursor - 1;

            for (int i = 0; pos > 0 && i < count; ++i) {
                while (pos > 0 && this.isWhitespace(this.buf.buffer.charAt(pos))) {
                    --pos;
                }

                while (pos > 0 && !this.isDelimiter(this.buf.buffer.charAt(pos - 1))) {
                    --pos;
                }

                if (pos > 0 && i < count - 1) {
                    --pos;
                }
            }

            this.setCursorPosition(pos);
            return ok;
        }
    }

    private boolean viDeleteTo(int startPos, int endPos, boolean isChange) throws IOException {
        if (startPos == endPos) {
            return true;
        } else {
            if (endPos < startPos) {
                int tmp = endPos;

                endPos = startPos;
                startPos = tmp;
            }

            this.setCursorPosition(startPos);
            this.buf.cursor = startPos;
            this.buf.buffer.delete(startPos, endPos);
            this.drawBuffer(endPos - startPos);
            if (!isChange && startPos > 0 && startPos == this.buf.length()) {
                this.moveCursor(-1);
            }

            return true;
        }
    }

    private boolean viYankTo(int startPos, int endPos) throws IOException {
        int cursorPos = startPos;

        if (endPos < startPos) {
            int tmp = endPos;

            endPos = startPos;
            startPos = tmp;
        }

        if (startPos == endPos) {
            this.yankBuffer = "";
            return true;
        } else {
            this.yankBuffer = this.buf.buffer.substring(startPos, endPos);
            this.setCursorPosition(cursorPos);
            return true;
        }
    }

    private boolean viPut(int count) throws IOException {
        if (this.yankBuffer.length() == 0) {
            return true;
        } else {
            if (this.buf.cursor < this.buf.buffer.length()) {
                this.moveCursor(1);
            }

            for (int i = 0; i < count; ++i) {
                this.putString(this.yankBuffer);
            }

            this.moveCursor(-1);
            return true;
        }
    }

    private boolean viCharSearch(int count, int invokeChar, int ch) throws IOException {
        if (ch >= 0 && invokeChar >= 0) {
            char searchChar = (char) ch;

            if (invokeChar != 59 && invokeChar != 44) {
                this.charSearchChar = searchChar;
                this.charSearchFirstInvokeChar = (char) invokeChar;
            } else {
                if (this.charSearchChar == 0) {
                    return false;
                }

                if (this.charSearchLastInvokeChar != 59 && this.charSearchLastInvokeChar != 44) {
                    if (invokeChar == 44) {
                        this.charSearchFirstInvokeChar = this.switchCase(this.charSearchFirstInvokeChar);
                    }
                } else if (this.charSearchLastInvokeChar != invokeChar) {
                    this.charSearchFirstInvokeChar = this.switchCase(this.charSearchFirstInvokeChar);
                }

                searchChar = this.charSearchChar;
            }

            this.charSearchLastInvokeChar = (char) invokeChar;
            boolean isForward = Character.isLowerCase(this.charSearchFirstInvokeChar);
            boolean stopBefore = Character.toLowerCase(this.charSearchFirstInvokeChar) == 116;
            boolean ok = false;
            int pos;

            if (isForward) {
                while (count-- > 0) {
                    for (pos = this.buf.cursor + 1; pos < this.buf.buffer.length(); ++pos) {
                        if (this.buf.buffer.charAt(pos) == searchChar) {
                            this.setCursorPosition(pos);
                            ok = true;
                            break;
                        }
                    }
                }

                if (ok) {
                    if (stopBefore) {
                        this.moveCursor(-1);
                    }

                    if (this.isInViMoveOperationState()) {
                        this.moveCursor(1);
                    }
                }
            } else {
                while (count-- > 0) {
                    for (pos = this.buf.cursor - 1; pos >= 0; --pos) {
                        if (this.buf.buffer.charAt(pos) == searchChar) {
                            this.setCursorPosition(pos);
                            ok = true;
                            break;
                        }
                    }
                }

                if (ok && stopBefore) {
                    this.moveCursor(1);
                }
            }

            return ok;
        } else {
            return false;
        }
    }

    private char switchCase(char ch) {
        return Character.isUpperCase(ch) ? Character.toLowerCase(ch) : Character.toUpperCase(ch);
    }

    private final boolean isInViMoveOperationState() {
        return this.state == ConsoleReader.State.VI_CHANGE_TO || this.state == ConsoleReader.State.VI_DELETE_TO || this.state == ConsoleReader.State.VI_YANK_TO;
    }

    private boolean viNextWord(int count) throws IOException {
        int pos = this.buf.cursor;
        int end = this.buf.buffer.length();

        for (int i = 0; pos < end && i < count; ++i) {
            while (pos < end && !this.isDelimiter(this.buf.buffer.charAt(pos))) {
                ++pos;
            }

            if (i < count - 1 || this.state != ConsoleReader.State.VI_CHANGE_TO) {
                while (pos < end && this.isDelimiter(this.buf.buffer.charAt(pos))) {
                    ++pos;
                }
            }
        }

        this.setCursorPosition(pos);
        return true;
    }

    private boolean viEndWord(int count) throws IOException {
        int pos = this.buf.cursor;
        int end = this.buf.buffer.length();

        for (int i = 0; pos < end && i < count; ++i) {
            if (pos < end - 1 && !this.isDelimiter(this.buf.buffer.charAt(pos)) && this.isDelimiter(this.buf.buffer.charAt(pos + 1))) {
                ++pos;
            }

            while (pos < end && this.isDelimiter(this.buf.buffer.charAt(pos))) {
                ++pos;
            }

            while (pos < end - 1 && !this.isDelimiter(this.buf.buffer.charAt(pos + 1))) {
                ++pos;
            }
        }

        this.setCursorPosition(pos);
        return true;
    }

    private boolean previousWord() throws IOException {
        while (this.isDelimiter(this.buf.current()) && this.moveCursor(-1) != 0) {
            ;
        }

        while (!this.isDelimiter(this.buf.current()) && this.moveCursor(-1) != 0) {
            ;
        }

        return true;
    }

    private boolean nextWord() throws IOException {
        while (this.isDelimiter(this.buf.nextChar()) && this.moveCursor(1) != 0) {
            ;
        }

        while (!this.isDelimiter(this.buf.nextChar()) && this.moveCursor(1) != 0) {
            ;
        }

        return true;
    }

    private boolean unixWordRubout(int count) throws IOException {
        boolean success = true;

        StringBuilder killed;

        for (killed = new StringBuilder(); count > 0; --count) {
            if (this.buf.cursor == 0) {
                success = false;
                break;
            }

            char copy;

            while (this.isWhitespace(this.buf.current())) {
                copy = this.buf.current();
                if (copy == 0) {
                    break;
                }

                killed.append(copy);
                this.backspace();
            }

            while (!this.isWhitespace(this.buf.current())) {
                copy = this.buf.current();
                if (copy == 0) {
                    break;
                }

                killed.append(copy);
                this.backspace();
            }
        }

        String s = killed.reverse().toString();

        this.killRing.addBackwards(s);
        return success;
    }

    private String insertComment(boolean isViMode) throws IOException {
        String comment = this.getCommentBegin();

        this.setCursorPosition(0);
        this.putString(comment);
        if (isViMode) {
            this.consoleKeys.setKeyMap("vi-insert");
        }

        return this.accept();
    }

    private boolean insert(int count, CharSequence str) throws IOException {
        for (int i = 0; i < count; ++i) {
            this.buf.write(str);
            if (this.mask == null) {
                this.print(str);
            } else if (this.mask.charValue() != 0) {
                this.print(this.mask.charValue(), str.length());
            }
        }

        this.drawBuffer();
        return true;
    }

    private int viSearch(char searchChar) throws IOException {
        boolean isForward = searchChar == 47;
        CursorBuffer origBuffer = this.buf.copy();

        this.setCursorPosition(0);
        this.killLine();
        this.putString(Character.toString(searchChar));
        this.flush();
        boolean isAborted = false;
        boolean isComplete = false;

        int ch;

        for (ch = -1; !isAborted && !isComplete && (ch = this.readCharacter()) != -1; this.flush()) {
            switch (ch) {
            case 8:
            case 127:
                this.backspace();
                if (this.buf.cursor == 0) {
                    isAborted = true;
                }
                break;

            case 10:
            case 13:
                isComplete = true;
                break;

            case 27:
                isAborted = true;
                break;

            default:
                this.putString(Character.toString((char) ch));
            }
        }

        if (ch != -1 && !isAborted) {
            String searchTerm = this.buf.buffer.substring(1);
            int idx = -1;
            int end = this.history.index();
            int start = end <= this.history.size() ? 0 : end - this.history.size();
            int forward;

            if (isForward) {
                for (forward = start; forward < end; ++forward) {
                    if (this.history.get(forward).toString().contains(searchTerm)) {
                        idx = forward;
                        break;
                    }
                }
            } else {
                for (forward = end - 1; forward >= start; --forward) {
                    if (this.history.get(forward).toString().contains(searchTerm)) {
                        idx = forward;
                        break;
                    }
                }
            }

            if (idx == -1) {
                this.setCursorPosition(0);
                this.killLine();
                this.putString(origBuffer.buffer);
                this.setCursorPosition(0);
                return -1;
            } else {
                this.setCursorPosition(0);
                this.killLine();
                this.putString(this.history.get(idx));
                this.setCursorPosition(0);
                this.flush();

                for (isComplete = false; !isComplete && (ch = this.readCharacter()) != -1; this.flush()) {
                    boolean flag = isForward;

                    switch (ch) {
                    case 80:
                    case 112:
                        flag = !isForward;

                    case 78:
                    case 110:
                        boolean isMatch = false;
                        int i;

                        if (flag) {
                            for (i = idx + 1; !isMatch && i < end; ++i) {
                                if (this.history.get(i).toString().contains(searchTerm)) {
                                    idx = i;
                                    isMatch = true;
                                }
                            }
                        } else {
                            for (i = idx - 1; !isMatch && i >= start; --i) {
                                if (this.history.get(i).toString().contains(searchTerm)) {
                                    idx = i;
                                    isMatch = true;
                                }
                            }
                        }

                        if (isMatch) {
                            this.setCursorPosition(0);
                            this.killLine();
                            this.putString(this.history.get(idx));
                            this.setCursorPosition(0);
                        }
                        break;

                    default:
                        isComplete = true;
                    }
                }

                return ch;
            }
        } else {
            this.setCursorPosition(0);
            this.killLine();
            this.putString(origBuffer.buffer);
            this.setCursorPosition(origBuffer.cursor);
            return -1;
        }
    }

    public void setParenBlinkTimeout(int timeout) {
        this.parenBlinkTimeout = timeout;
    }

    private void insertClose(String s) throws IOException {
        this.putString(s);
        int closePosition = this.buf.cursor;

        this.moveCursor(-1);
        this.viMatch();
        if (this.in.isNonBlockingEnabled()) {
            this.in.peek((long) this.parenBlinkTimeout);
        }

        this.setCursorPosition(closePosition);
    }

    private boolean viMatch() throws IOException {
        int pos = this.buf.cursor;

        if (pos == this.buf.length()) {
            return false;
        } else {
            int type = this.getBracketType(this.buf.buffer.charAt(pos));
            int move = type < 0 ? -1 : 1;
            int count = 1;

            if (type == 0) {
                return false;
            } else {
                while (count > 0) {
                    pos += move;
                    if (pos < 0 || pos >= this.buf.buffer.length()) {
                        return false;
                    }

                    int curType = this.getBracketType(this.buf.buffer.charAt(pos));

                    if (curType == type) {
                        ++count;
                    } else if (curType == -type) {
                        --count;
                    }
                }

                if (move > 0 && this.isInViMoveOperationState()) {
                    ++pos;
                }

                this.setCursorPosition(pos);
                return true;
            }
        }
    }

    private int getBracketType(char ch) {
        switch (ch) {
        case '(':
            return 3;

        case ')':
            return -3;

        case '[':
            return 1;

        case ']':
            return -1;

        case '{':
            return 2;

        case '}':
            return -2;

        default:
            return 0;
        }
    }

    private boolean deletePreviousWord() throws IOException {
        StringBuilder killed = new StringBuilder();

        char c;

        while (this.isDelimiter(c = this.buf.current()) && c != 0) {
            killed.append(c);
            this.backspace();
        }

        while (!this.isDelimiter(c = this.buf.current()) && c != 0) {
            killed.append(c);
            this.backspace();
        }

        String copy = killed.reverse().toString();

        this.killRing.addBackwards(copy);
        return true;
    }

    private boolean deleteNextWord() throws IOException {
        StringBuilder killed = new StringBuilder();

        char c;

        while (this.isDelimiter(c = this.buf.nextChar()) && c != 0) {
            killed.append(c);
            this.delete();
        }

        while (!this.isDelimiter(c = this.buf.nextChar()) && c != 0) {
            killed.append(c);
            this.delete();
        }

        String copy = killed.toString();

        this.killRing.add(copy);
        return true;
    }

    private boolean capitalizeWord() throws IOException {
        boolean first = true;

        int i;
        char c;

        for (i = 1; this.buf.cursor + i - 1 < this.buf.length() && !this.isDelimiter(c = this.buf.buffer.charAt(this.buf.cursor + i - 1)); ++i) {
            this.buf.buffer.setCharAt(this.buf.cursor + i - 1, first ? Character.toUpperCase(c) : Character.toLowerCase(c));
            first = false;
        }

        this.drawBuffer();
        this.moveCursor(i - 1);
        return true;
    }

    private boolean upCaseWord() throws IOException {
        int i;
        char c;

        for (i = 1; this.buf.cursor + i - 1 < this.buf.length() && !this.isDelimiter(c = this.buf.buffer.charAt(this.buf.cursor + i - 1)); ++i) {
            this.buf.buffer.setCharAt(this.buf.cursor + i - 1, Character.toUpperCase(c));
        }

        this.drawBuffer();
        this.moveCursor(i - 1);
        return true;
    }

    private boolean downCaseWord() throws IOException {
        int i;
        char c;

        for (i = 1; this.buf.cursor + i - 1 < this.buf.length() && !this.isDelimiter(c = this.buf.buffer.charAt(this.buf.cursor + i - 1)); ++i) {
            this.buf.buffer.setCharAt(this.buf.cursor + i - 1, Character.toLowerCase(c));
        }

        this.drawBuffer();
        this.moveCursor(i - 1);
        return true;
    }

    private boolean transposeChars(int count) throws IOException {
        while (true) {
            if (count > 0) {
                if (this.buf.cursor != 0 && this.buf.cursor != this.buf.buffer.length()) {
                    int first = this.buf.cursor - 1;
                    int second = this.buf.cursor;
                    char tmp = this.buf.buffer.charAt(first);

                    this.buf.buffer.setCharAt(first, this.buf.buffer.charAt(second));
                    this.buf.buffer.setCharAt(second, tmp);
                    this.moveInternal(-1);
                    this.drawBuffer();
                    this.moveInternal(2);
                    --count;
                    continue;
                }

                return false;
            }

            return true;
        }
    }

    public boolean isKeyMap(String name) {
        KeyMap map = this.consoleKeys.getKeys();
        KeyMap mapByName = (KeyMap) this.consoleKeys.getKeyMaps().get(name);

        return mapByName == null ? false : map == mapByName;
    }

    public String accept() throws IOException {
        this.moveToEnd();
        this.println();
        this.flush();
        return this.finishBuffer();
    }

    private void abort() throws IOException {
        this.beep();
        this.buf.clear();
        this.println();
        this.redrawLine();
    }

    public int moveCursor(int num) throws IOException {
        int where = num;

        if (this.buf.cursor == 0 && num <= 0) {
            return 0;
        } else if (this.buf.cursor == this.buf.buffer.length() && num >= 0) {
            return 0;
        } else {
            if (this.buf.cursor + num < 0) {
                where = -this.buf.cursor;
            } else if (this.buf.cursor + num > this.buf.buffer.length()) {
                where = this.buf.buffer.length() - this.buf.cursor;
            }

            this.moveInternal(where);
            return where;
        }
    }

    private void moveInternal(int where) throws IOException {
        this.buf.cursor += where;
        int len;
        int chars;

        if (this.terminal.isAnsiSupported()) {
            if (where < 0) {
                this.back(Math.abs(where));
            } else {
                int i = this.getTerminal().getWidth();

                len = this.getCursorPosition();
                chars = (len - where) / i;
                int newLine = len / i;

                if (newLine > chars) {
                    this.printAnsiSequence(newLine - chars + "B");
                }

                this.printAnsiSequence(1 + len % i + "G");
            }

        } else if (where < 0) {
            len = 0;

            for (chars = this.buf.cursor; chars < this.buf.cursor - where; ++chars) {
                if (this.buf.buffer.charAt(chars) == 9) {
                    len += 4;
                } else {
                    ++len;
                }
            }

            char[] achar = new char[len];

            Arrays.fill(achar, '\b');
            this.out.write(achar);
        } else if (this.buf.cursor != 0) {
            if (this.mask != null) {
                char c = this.mask.charValue();

                if (this.mask.charValue() != 0) {
                    this.print(c, Math.abs(where));
                }
            } else {
                this.print(this.buf.buffer.substring(this.buf.cursor - where, this.buf.cursor).toCharArray());
            }
        }
    }

    public final boolean replace(int num, String replacement) {
        this.buf.buffer.replace(this.buf.cursor - num, this.buf.cursor, replacement);

        try {
            this.moveCursor(-num);
            this.drawBuffer(Math.max(0, num - replacement.length()));
            this.moveCursor(replacement.length());
            return true;
        } catch (IOException ioexception) {
            ioexception.printStackTrace();
            return false;
        }
    }

    public final int readCharacter() throws IOException {
        int c = this.reader.read();

        if (c >= 0) {
            Log.trace(new Object[] { "Keystroke: ", Integer.valueOf(c)});
            if (this.terminal.isSupported()) {
                this.clearEcho(c);
            }
        }

        return c;
    }

    private int clearEcho(int c) throws IOException {
        if (!this.terminal.isEchoEnabled()) {
            return 0;
        } else {
            int num = this.countEchoCharacters(c);

            this.back(num);
            this.drawBuffer(num);
            return num;
        }
    }

    private int countEchoCharacters(int c) {
        if (c == 9) {
            byte tabStop = 8;
            int position = this.getCursorPosition();

            return tabStop - position % tabStop;
        } else {
            return this.getPrintableCharacters(c).length();
        }
    }

    private StringBuilder getPrintableCharacters(int ch) {
        StringBuilder sbuff = new StringBuilder();

        if (ch >= 32) {
            if (ch < 127) {
                sbuff.append(ch);
            } else if (ch == 127) {
                sbuff.append('^');
                sbuff.append('?');
            } else {
                sbuff.append('M');
                sbuff.append('-');
                if (ch >= 160) {
                    if (ch < 255) {
                        sbuff.append((char) (ch - 128));
                    } else {
                        sbuff.append('^');
                        sbuff.append('?');
                    }
                } else {
                    sbuff.append('^');
                    sbuff.append((char) (ch - 128 + 64));
                }
            }
        } else {
            sbuff.append('^');
            sbuff.append((char) (ch + 64));
        }

        return sbuff;
    }

    public final int readCharacter(char... allowed) throws IOException {
        Arrays.sort(allowed);

        char c;

        while (Arrays.binarySearch(allowed, c = (char) this.readCharacter()) < 0) {
            ;
        }

        return c;
    }

    public String readLine() throws IOException {
        return this.readLine((String) null);
    }

    public String readLine(Character mask) throws IOException {
        return this.readLine((String) null, mask);
    }

    public String readLine(String prompt) throws IOException {
        return this.readLine(prompt, (Character) null);
    }

    public boolean setKeyMap(String name) {
        return this.consoleKeys.setKeyMap(name);
    }

    public String getKeyMap() {
        return this.consoleKeys.getKeys().getName();
    }

    public String readLine(String prompt, Character mask) throws IOException {
        int repeatCount = 0;

        this.mask = mask;
        if (prompt != null) {
            this.setPrompt(prompt);
        } else {
            prompt = this.getPrompt();
        }

        try {
            if (!this.terminal.isSupported()) {
                this.beforeReadLine(prompt, mask);
            }

            if (prompt != null && prompt.length() > 0) {
                this.out.write(prompt);
                this.out.flush();
            }

            String originalPrompt;

            if (!this.terminal.isSupported()) {
                originalPrompt = this.readLineSimple();
                return originalPrompt;
            } else {
                if (this.handleUserInterrupt && this.terminal instanceof UnixTerminal) {
                    ((UnixTerminal) this.terminal).disableInterruptCharacter();
                }

                originalPrompt = this.prompt;
                this.state = ConsoleReader.State.NORMAL;
                boolean success = true;
                StringBuilder sb = new StringBuilder();
                Stack pushBackChar = new Stack();

                while (true) {
                    int c = pushBackChar.isEmpty() ? this.readCharacter() : ((Character) pushBackChar.pop()).charValue();
                    Object o;

                    if (c == -1) {
                        o = null;
                        return (String) o;
                    }

                    sb.appendCodePoint(c);
                    if (this.recording) {
                        this.macro = this.macro + new String(new int[] { c}, 0, 1);
                    }

                    o = this.getKeys().getBound(sb);
                    if (!this.recording && !(o instanceof KeyMap)) {
                        if (o != Operation.YANK_POP && o != Operation.YANK) {
                            this.killRing.resetLastYank();
                        }

                        if (o != Operation.KILL_LINE && o != Operation.KILL_WHOLE_LINE && o != Operation.BACKWARD_KILL_WORD && o != Operation.KILL_WORD && o != Operation.UNIX_LINE_DISCARD && o != Operation.UNIX_WORD_RUBOUT) {
                            this.killRing.resetLastKill();
                        }
                    }

                    if (o == Operation.DO_LOWERCASE_VERSION) {
                        sb.setLength(sb.length() - 1);
                        sb.append(Character.toLowerCase((char) c));
                        o = this.getKeys().getBound(sb);
                    }

                    if (o instanceof KeyMap) {
                        if (c != 27 || !pushBackChar.isEmpty() || !this.in.isNonBlockingEnabled() || this.in.peek(this.escapeTimeout) != -2) {
                            continue;
                        }

                        o = ((KeyMap) o).getAnotherKey();
                        if (o == null || o instanceof KeyMap) {
                            continue;
                        }

                        sb.setLength(0);
                    }

                    while (o == null && sb.length() > 0) {
                        c = sb.charAt(sb.length() - 1);
                        sb.setLength(sb.length() - 1);
                        Object isArgDigit = this.getKeys().getBound(sb);

                        if (isArgDigit instanceof KeyMap) {
                            o = ((KeyMap) isArgDigit).getAnotherKey();
                            if (o != null) {
                                pushBackChar.push(Character.valueOf((char) c));
                            }
                        }
                    }

                    if (o != null) {
                        Log.trace(new Object[] { "Binding: ", o});
                        int count;

                        if (o instanceof String) {
                            String s = (String) o;

                            for (count = 0; count < s.length(); ++count) {
                                pushBackChar.push(Character.valueOf(s.charAt(s.length() - 1 - count)));
                            }

                            sb.setLength(0);
                        } else if (o instanceof ActionListener) {
                            ((ActionListener) o).actionPerformed((ActionEvent) null);
                            sb.setLength(0);
                        } else {
                            if (this.state == ConsoleReader.State.SEARCH || this.state == ConsoleReader.State.FORWARD_SEARCH) {
                                int i = -1;

                                switch (ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[((Operation) o).ordinal()]) {
                                case 2:
                                    this.state = ConsoleReader.State.NORMAL;
                                    this.buf.clear();
                                    this.buf.buffer.append(this.searchTerm);
                                    break;

                                case 19:
                                    this.state = ConsoleReader.State.SEARCH;
                                    if (this.searchTerm.length() == 0) {
                                        this.searchTerm.append(this.previousSearchTerm);
                                    }

                                    if (this.searchIndex > 0) {
                                        this.searchIndex = this.searchBackwards(this.searchTerm.toString(), this.searchIndex);
                                    }
                                    break;

                                case 20:
                                    this.state = ConsoleReader.State.FORWARD_SEARCH;
                                    if (this.searchTerm.length() == 0) {
                                        this.searchTerm.append(this.previousSearchTerm);
                                    }

                                    if (this.searchIndex > -1 && this.searchIndex < this.history.size() - 1) {
                                        this.searchIndex = this.searchForwards(this.searchTerm.toString(), this.searchIndex);
                                    }
                                    break;

                                case 21:
                                    if (this.searchTerm.length() > 0) {
                                        this.searchTerm.deleteCharAt(this.searchTerm.length() - 1);
                                        if (this.state == ConsoleReader.State.SEARCH) {
                                            this.searchIndex = this.searchBackwards(this.searchTerm.toString());
                                        } else {
                                            this.searchIndex = this.searchForwards(this.searchTerm.toString());
                                        }
                                    }
                                    break;

                                case 22:
                                    this.searchTerm.appendCodePoint(c);
                                    if (this.state == ConsoleReader.State.SEARCH) {
                                        this.searchIndex = this.searchBackwards(this.searchTerm.toString());
                                    } else {
                                        this.searchIndex = this.searchForwards(this.searchTerm.toString());
                                    }
                                    break;

                                default:
                                    if (this.searchIndex != -1) {
                                        this.history.moveTo(this.searchIndex);
                                        i = this.history.current().toString().indexOf(this.searchTerm.toString());
                                    }

                                    this.state = ConsoleReader.State.NORMAL;
                                }

                                if (this.state != ConsoleReader.State.SEARCH && this.state != ConsoleReader.State.FORWARD_SEARCH) {
                                    this.restoreLine(originalPrompt, i);
                                } else if (this.searchTerm.length() == 0) {
                                    if (this.state == ConsoleReader.State.SEARCH) {
                                        this.printSearchStatus("", "");
                                    } else {
                                        this.printForwardSearchStatus("", "");
                                    }

                                    this.searchIndex = -1;
                                } else if (this.searchIndex == -1) {
                                    this.beep();
                                    this.printSearchStatus(this.searchTerm.toString(), "");
                                } else if (this.state == ConsoleReader.State.SEARCH) {
                                    this.printSearchStatus(this.searchTerm.toString(), this.history.get(this.searchIndex).toString());
                                } else {
                                    this.printForwardSearchStatus(this.searchTerm.toString(), this.history.get(this.searchIndex).toString());
                                }
                            }

                            if (this.state != ConsoleReader.State.SEARCH && this.state != ConsoleReader.State.FORWARD_SEARCH) {
                                boolean flag = false;

                                count = repeatCount == 0 ? 1 : repeatCount;
                                success = true;
                                if (o instanceof Operation) {
                                    Operation op = (Operation) o;
                                    int cursorStart = this.buf.cursor;
                                    ConsoleReader.State origState = this.state;

                                    if (this.state == ConsoleReader.State.VI_CHANGE_TO || this.state == ConsoleReader.State.VI_YANK_TO || this.state == ConsoleReader.State.VI_DELETE_TO) {
                                        op = this.viDeleteChangeYankToRemap(op);
                                    }

                                    int lastChar;
                                    String s1;
                                    String s2;

                                    switch (ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[op.ordinal()]) {
                                    case 1:
                                        if (this.buf.buffer.length() == 0) {
                                            s2 = null;
                                            return s2;
                                        }

                                        s2 = this.accept();
                                        return s2;

                                    case 2:
                                        if (this.searchTerm == null) {
                                            this.abort();
                                        }
                                        break;

                                    case 3:
                                        success = this.moveCursor(-count) != 0;
                                        break;

                                    case 4:
                                        success = this.moveCursor(count) != 0;
                                        break;

                                    case 5:
                                        success = this.moveToEnd();
                                        break;

                                    case 6:
                                        success = this.viMatch();
                                        break;

                                    case 7:
                                        if (repeatCount > 0) {
                                            repeatCount = repeatCount * 10 + sb.charAt(0) - 48;
                                            flag = true;
                                        } else {
                                            success = this.setCursorPosition(0);
                                        }
                                        break;

                                    case 8:
                                        repeatCount = repeatCount * 10 + sb.charAt(0) - 48;
                                        flag = true;
                                        break;

                                    case 9:
                                        success = this.viPreviousWord(count);
                                        break;

                                    case 10:
                                        success = this.viEndWord(count);
                                        break;

                                    case 11:
                                        int searchChar = c != 59 && c != 44 ? (pushBackChar.isEmpty() ? this.readCharacter() : ((Character) pushBackChar.pop()).charValue()) : 0;

                                        success = this.viCharSearch(count, c, searchChar);
                                        break;

                                    case 12:
                                        success = this.viNextWord(count);
                                        break;

                                    case 13:
                                        success = this.setCursorPosition(0) && this.viNextWord(1);

                                    case 14:
                                    case 15:
                                    default:
                                        break;

                                    case 16:
                                        if (this.state == ConsoleReader.State.VI_DELETE_TO) {
                                            success = this.setCursorPosition(0) && this.killLine();
                                            origState = ConsoleReader.State.NORMAL;
                                            this.state = ConsoleReader.State.NORMAL;
                                        } else {
                                            this.state = ConsoleReader.State.VI_DELETE_TO;
                                        }
                                        break;

                                    case 17:
                                        if (this.state == ConsoleReader.State.VI_YANK_TO) {
                                            this.yankBuffer = this.buf.buffer.toString();
                                            origState = ConsoleReader.State.NORMAL;
                                            this.state = ConsoleReader.State.NORMAL;
                                        } else {
                                            this.state = ConsoleReader.State.VI_YANK_TO;
                                        }
                                        break;

                                    case 18:
                                        if (this.state == ConsoleReader.State.VI_CHANGE_TO) {
                                            success = this.setCursorPosition(0) && this.killLine();
                                            origState = ConsoleReader.State.NORMAL;
                                            this.state = ConsoleReader.State.NORMAL;
                                            this.consoleKeys.setKeyMap("vi-insert");
                                        } else {
                                            this.state = ConsoleReader.State.VI_CHANGE_TO;
                                        }
                                        break;

                                    case 19:
                                        if (this.searchTerm != null) {
                                            this.previousSearchTerm = this.searchTerm.toString();
                                        }

                                        this.searchTerm = new StringBuffer(this.buf.buffer);
                                        this.state = ConsoleReader.State.SEARCH;
                                        if (this.searchTerm.length() > 0) {
                                            this.searchIndex = this.searchBackwards(this.searchTerm.toString());
                                            if (this.searchIndex == -1) {
                                                this.beep();
                                            }

                                            this.printSearchStatus(this.searchTerm.toString(), this.searchIndex > -1 ? this.history.get(this.searchIndex).toString() : "");
                                        } else {
                                            this.searchIndex = -1;
                                            this.printSearchStatus("", "");
                                        }
                                        break;

                                    case 20:
                                        if (this.searchTerm != null) {
                                            this.previousSearchTerm = this.searchTerm.toString();
                                        }

                                        this.searchTerm = new StringBuffer(this.buf.buffer);
                                        this.state = ConsoleReader.State.FORWARD_SEARCH;
                                        if (this.searchTerm.length() > 0) {
                                            this.searchIndex = this.searchForwards(this.searchTerm.toString());
                                            if (this.searchIndex == -1) {
                                                this.beep();
                                            }

                                            this.printForwardSearchStatus(this.searchTerm.toString(), this.searchIndex > -1 ? this.history.get(this.searchIndex).toString() : "");
                                        } else {
                                            this.searchIndex = -1;
                                            this.printForwardSearchStatus("", "");
                                        }
                                        break;

                                    case 21:
                                        success = this.backspace();
                                        break;

                                    case 22:
                                        this.putString(sb);
                                        break;

                                    case 23:
                                        boolean isTabLiteral = false;

                                        if (this.copyPasteDetection && c == 9 && (!pushBackChar.isEmpty() || this.in.isNonBlockingEnabled() && this.in.peek(this.escapeTimeout) != -2)) {
                                            isTabLiteral = true;
                                        }

                                        if (!isTabLiteral) {
                                            success = this.complete();
                                        } else {
                                            this.putString(sb);
                                        }
                                        break;

                                    case 24:
                                        this.printCompletionCandidates();
                                        break;

                                    case 25:
                                        success = this.setCursorPosition(0);
                                        break;

                                    case 26:
                                        success = this.yank();
                                        break;

                                    case 27:
                                        success = this.yankPop();
                                        break;

                                    case 28:
                                        success = this.killLine();
                                        break;

                                    case 29:
                                        success = this.setCursorPosition(0) && this.killLine();
                                        break;

                                    case 30:
                                        success = this.clearScreen();
                                        this.redrawLine();
                                        break;

                                    case 31:
                                        this.buf.setOverTyping(!this.buf.isOverTyping());
                                        break;

                                    case 32:
                                        s1 = this.accept();
                                        return s1;

                                    case 33:
                                        if (this.handleUserInterrupt) {
                                            this.println();
                                            this.flush();
                                            s1 = this.buf.buffer.toString();
                                            this.buf.clear();
                                            this.history.moveToEnd();
                                            throw new UserInterruptException(s1);
                                        }
                                        break;

                                    case 34:
                                        this.consoleKeys.setKeyMap("vi-insert");
                                        s1 = this.accept();
                                        return s1;

                                    case 35:
                                        success = this.previousWord();
                                        break;

                                    case 36:
                                        success = this.nextWord();
                                        break;

                                    case 37:
                                        success = this.moveHistory(false);
                                        break;

                                    case 38:
                                        success = this.moveHistory(false, count) && this.setCursorPosition(0);
                                        break;

                                    case 39:
                                        success = this.moveHistory(true);
                                        break;

                                    case 40:
                                        success = this.moveHistory(true, count) && this.setCursorPosition(0);
                                        break;

                                    case 41:
                                        if (this.buf.buffer.length() == 0) {
                                            s1 = null;
                                            return s1;
                                        }

                                        success = this.deleteCurrentCharacter();
                                        break;

                                    case 42:
                                        success = this.deleteCurrentCharacter();
                                        break;

                                    case 43:
                                        success = this.resetLine();
                                        break;

                                    case 44:
                                        success = this.unixWordRubout(count);
                                        break;

                                    case 45:
                                        success = this.deletePreviousWord();
                                        break;

                                    case 46:
                                        success = this.deleteNextWord();
                                        break;

                                    case 47:
                                        success = this.history.moveToFirst();
                                        if (success) {
                                            this.setBuffer(this.history.current());
                                        }
                                        break;

                                    case 48:
                                        success = this.history.moveToLast();
                                        if (success) {
                                            this.setBuffer(this.history.current());
                                        }
                                        break;

                                    case 49:
                                        this.searchTerm = new StringBuffer(this.buf.upToCursor());
                                        this.searchIndex = this.searchBackwards(this.searchTerm.toString(), this.history.index(), true);
                                        if (this.searchIndex == -1) {
                                            this.beep();
                                        } else {
                                            success = this.history.moveTo(this.searchIndex);
                                            if (success) {
                                                this.setBufferKeepPos(this.history.current());
                                            }
                                        }
                                        break;

                                    case 50:
                                        this.searchTerm = new StringBuffer(this.buf.upToCursor());
                                        int index = this.history.index() + 1;

                                        if (index == this.history.size()) {
                                            this.history.moveToEnd();
                                            this.setBufferKeepPos(this.searchTerm.toString());
                                        } else if (index < this.history.size()) {
                                            this.searchIndex = this.searchForwards(this.searchTerm.toString(), index, true);
                                            if (this.searchIndex == -1) {
                                                this.beep();
                                            } else {
                                                success = this.history.moveTo(this.searchIndex);
                                                if (success) {
                                                    this.setBufferKeepPos(this.history.current());
                                                }
                                            }
                                        }
                                        break;

                                    case 51:
                                        success = this.capitalizeWord();
                                        break;

                                    case 52:
                                        success = this.upCaseWord();
                                        break;

                                    case 53:
                                        success = this.downCaseWord();
                                        break;

                                    case 54:
                                        this.putString("\t");
                                        break;

                                    case 55:
                                        this.consoleKeys.loadKeys(this.appName, this.inputrcUrl);
                                        break;

                                    case 56:
                                        this.recording = true;
                                        break;

                                    case 57:
                                        this.recording = false;
                                        this.macro = this.macro.substring(0, this.macro.length() - sb.length());
                                        break;

                                    case 58:
                                        for (lastChar = 0; lastChar < this.macro.length(); ++lastChar) {
                                            pushBackChar.push(Character.valueOf(this.macro.charAt(this.macro.length() - 1 - lastChar)));
                                        }

                                        sb.setLength(0);
                                        break;

                                    case 59:
                                        this.consoleKeys.setKeyMap("vi-insert");
                                        break;

                                    case 60:
                                        ConsoleReader.State consolereader_state = this.state;

                                        if (this.state == ConsoleReader.State.NORMAL) {
                                            this.moveCursor(-1);
                                        }

                                        this.consoleKeys.setKeyMap("vi-move");
                                        break;

                                    case 61:
                                        this.consoleKeys.setKeyMap("vi-insert");
                                        break;

                                    case 62:
                                        this.moveCursor(1);
                                        this.consoleKeys.setKeyMap("vi-insert");
                                        break;

                                    case 63:
                                        success = this.moveToEnd();
                                        this.consoleKeys.setKeyMap("vi-insert");
                                        break;

                                    case 64:
                                        success = this.transposeChars(count);
                                        break;

                                    case 65:
                                        s2 = this.insertComment(false);
                                        return s2;

                                    case 66:
                                        this.insertClose("}");
                                        break;

                                    case 67:
                                        this.insertClose(")");
                                        break;

                                    case 68:
                                        this.insertClose("]");
                                        break;

                                    case 69:
                                        s2 = this.insertComment(true);
                                        return s2;

                                    case 70:
                                        lastChar = this.viSearch(sb.charAt(0));
                                        if (lastChar != -1) {
                                            pushBackChar.push(Character.valueOf((char) lastChar));
                                        }
                                        break;

                                    case 71:
                                        success = this.setCursorPosition(0);
                                        this.consoleKeys.setKeyMap("vi-insert");
                                        break;

                                    case 72:
                                        success = this.viRubout(count);
                                        break;

                                    case 73:
                                        success = this.viDelete(count);
                                        break;

                                    case 74:
                                        success = this.setCursorPosition(0) && this.killLine();
                                        this.consoleKeys.setKeyMap("vi-insert");
                                        break;

                                    case 75:
                                        success = this.viPut(count);
                                        break;

                                    case 76:
                                        success = this.viChangeCase(count);
                                        break;

                                    case 77:
                                        success = this.viChangeChar(count, pushBackChar.isEmpty() ? this.readCharacter() : ((Character) pushBackChar.pop()).charValue());
                                        break;

                                    case 78:
                                        success = this.viDeleteTo(this.buf.cursor, this.buf.buffer.length(), false);
                                        break;

                                    case 79:
                                        success = this.viDeleteTo(this.buf.cursor, this.buf.buffer.length(), true);
                                        this.consoleKeys.setKeyMap("vi-insert");
                                        break;

                                    case 80:
                                        this.consoleKeys.setKeyMap("emacs");
                                    }

                                    if (origState != ConsoleReader.State.NORMAL) {
                                        if (origState == ConsoleReader.State.VI_DELETE_TO) {
                                            success = this.viDeleteTo(cursorStart, this.buf.cursor, false);
                                        } else if (origState == ConsoleReader.State.VI_CHANGE_TO) {
                                            success = this.viDeleteTo(cursorStart, this.buf.cursor, true);
                                            this.consoleKeys.setKeyMap("vi-insert");
                                        } else if (origState == ConsoleReader.State.VI_YANK_TO) {
                                            success = this.viYankTo(cursorStart, this.buf.cursor);
                                        }

                                        this.state = ConsoleReader.State.NORMAL;
                                    }

                                    if (this.state == ConsoleReader.State.NORMAL && !flag) {
                                        repeatCount = 0;
                                    }

                                    if (this.state != ConsoleReader.State.SEARCH && this.state != ConsoleReader.State.FORWARD_SEARCH) {
                                        this.previousSearchTerm = "";
                                        this.searchTerm = null;
                                        this.searchIndex = -1;
                                    }
                                }
                            }

                            if (!success) {
                                this.beep();
                            }

                            sb.setLength(0);
                            this.flush();
                        }
                    }
                }
            }
        } finally {
            if (!this.terminal.isSupported()) {
                this.afterReadLine();
            }

            if (this.handleUserInterrupt && this.terminal instanceof UnixTerminal) {
                ((UnixTerminal) this.terminal).enableInterruptCharacter();
            }

        }
    }

    private String readLineSimple() throws IOException {
        StringBuilder buff = new StringBuilder();
        int i;

        if (this.skipLF) {
            this.skipLF = false;
            i = this.readCharacter();
            if (i == -1 || i == 13) {
                return buff.toString();
            }

            if (i != 10) {
                buff.append((char) i);
            }
        }

        while (true) {
            i = this.readCharacter();
            if (i == -1 && buff.length() == 0) {
                return null;
            }

            if (i == -1 || i == 10) {
                return buff.toString();
            }

            if (i == 13) {
                this.skipLF = true;
                return buff.toString();
            }

            buff.append((char) i);
        }
    }

    public boolean addCompleter(Completer completer) {
        return this.completers.add(completer);
    }

    public boolean removeCompleter(Completer completer) {
        return this.completers.remove(completer);
    }

    public Collection getCompleters() {
        return Collections.unmodifiableList(this.completers);
    }

    public void setCompletionHandler(CompletionHandler handler) {
        this.completionHandler = (CompletionHandler) Preconditions.checkNotNull(handler);
    }

    public CompletionHandler getCompletionHandler() {
        return this.completionHandler;
    }

    protected boolean complete() throws IOException {
        if (this.completers.size() == 0) {
            return false;
        } else {
            LinkedList candidates = new LinkedList();
            String bufstr = this.buf.buffer.toString();
            int cursor = this.buf.cursor;
            int position = -1;
            Iterator i$ = this.completers.iterator();

            while (i$.hasNext()) {
                Completer comp = (Completer) i$.next();

                if ((position = comp.complete(bufstr, cursor, candidates)) != -1) {
                    break;
                }
            }

            return candidates.size() != 0 && this.getCompletionHandler().complete(this, candidates, position);
        }
    }

    protected void printCompletionCandidates() throws IOException {
        if (this.completers.size() != 0) {
            LinkedList candidates = new LinkedList();
            String bufstr = this.buf.buffer.toString();
            int cursor = this.buf.cursor;
            Iterator i$ = this.completers.iterator();

            while (i$.hasNext()) {
                Completer comp = (Completer) i$.next();

                if (comp.complete(bufstr, cursor, candidates) != -1) {
                    break;
                }
            }

            CandidateListCompletionHandler.printCandidates(this, candidates);
            this.drawLine();
        }
    }

    public void setAutoprintThreshold(int threshold) {
        this.autoprintThreshold = threshold;
    }

    public int getAutoprintThreshold() {
        return this.autoprintThreshold;
    }

    public void setPaginationEnabled(boolean enabled) {
        this.paginationEnabled = enabled;
    }

    public boolean isPaginationEnabled() {
        return this.paginationEnabled;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    public History getHistory() {
        return this.history;
    }

    public void setHistoryEnabled(boolean enabled) {
        this.historyEnabled = enabled;
    }

    public boolean isHistoryEnabled() {
        return this.historyEnabled;
    }

    private boolean moveHistory(boolean next, int count) throws IOException {
        boolean ok = true;

        for (int i = 0; i < count && (ok = this.moveHistory(next)); ++i) {
            ;
        }

        return ok;
    }

    private boolean moveHistory(boolean next) throws IOException {
        if (next && !this.history.next()) {
            return false;
        } else if (!next && !this.history.previous()) {
            return false;
        } else {
            this.setBuffer(this.history.current());
            return true;
        }
    }

    private void print(int c) throws IOException {
        if (c == 9) {
            char[] chars = new char[4];

            Arrays.fill(chars, ' ');
            this.out.write(chars);
        } else {
            this.out.write(c);
        }
    }

    private void print(char... buff) throws IOException {
        int len = 0;
        char[] chars = buff;
        int pos = buff.length;

        for (int arr$ = 0; arr$ < pos; ++arr$) {
            char len$ = chars[arr$];

            if (len$ == 9) {
                len += 4;
            } else {
                ++len;
            }
        }

        if (len == buff.length) {
            chars = buff;
        } else {
            chars = new char[len];
            pos = 0;
            char[] achar = buff;
            int i = buff.length;

            for (int i$ = 0; i$ < i; ++i$) {
                char c = achar[i$];

                if (c == 9) {
                    Arrays.fill(chars, pos, pos + 4, ' ');
                    pos += 4;
                } else {
                    chars[pos] = c;
                    ++pos;
                }
            }
        }

        this.out.write(chars);
    }

    private void print(char c, int num) throws IOException {
        if (num == 1) {
            this.print(c);
        } else {
            char[] chars = new char[num];

            Arrays.fill(chars, c);
            this.print(chars);
        }

    }

    public final void print(CharSequence s) throws IOException {
        this.print(((CharSequence) Preconditions.checkNotNull(s)).toString().toCharArray());
    }

    public final void println(CharSequence s) throws IOException {
        this.print(((CharSequence) Preconditions.checkNotNull(s)).toString().toCharArray());
        this.println();
    }

    public final void println() throws IOException {
        this.print((CharSequence) ConsoleReader.CR);
    }

    public final boolean delete() throws IOException {
        if (this.buf.cursor == this.buf.buffer.length()) {
            return false;
        } else {
            this.buf.buffer.delete(this.buf.cursor, this.buf.cursor + 1);
            this.drawBuffer(1);
            return true;
        }
    }

    public boolean killLine() throws IOException {
        int cp = this.buf.cursor;
        int len = this.buf.buffer.length();

        if (cp >= len) {
            return false;
        } else {
            int num = len - cp;

            this.clearAhead(num, 0);
            char[] killed = new char[num];

            this.buf.buffer.getChars(cp, cp + num, killed, 0);
            this.buf.buffer.delete(cp, cp + num);
            String copy = new String(killed);

            this.killRing.add(copy);
            return true;
        }
    }

    public boolean yank() throws IOException {
        String yanked = this.killRing.yank();

        if (yanked == null) {
            return false;
        } else {
            this.putString(yanked);
            return true;
        }
    }

    public boolean yankPop() throws IOException {
        if (!this.killRing.lastYank()) {
            return false;
        } else {
            String current = this.killRing.yank();

            if (current == null) {
                return false;
            } else {
                this.backspace(current.length());
                String yanked = this.killRing.yankPop();

                if (yanked == null) {
                    return false;
                } else {
                    this.putString(yanked);
                    return true;
                }
            }
        }
    }

    public boolean clearScreen() throws IOException {
        if (!this.terminal.isAnsiSupported()) {
            return false;
        } else {
            this.printAnsiSequence("2J");
            this.printAnsiSequence("1;1H");
            return true;
        }
    }

    public void beep() throws IOException {
        if (this.bellEnabled) {
            this.print(7);
            this.flush();
        }

    }

    public boolean paste() throws IOException {
        Clipboard clipboard;

        try {
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        } catch (Exception exception) {
            return false;
        }

        if (clipboard == null) {
            return false;
        } else {
            Transferable transferable = clipboard.getContents((Object) null);

            if (transferable == null) {
                return false;
            } else {
                try {
                    Object e = transferable.getTransferData(DataFlavor.plainTextFlavor);

                    if (e == null) {
                        try {
                            e = (new DataFlavor()).getReaderForText(transferable);
                        } catch (Exception exception1) {
                            ;
                        }
                    }

                    if (e == null) {
                        return false;
                    } else {
                        String value;

                        if (e instanceof Reader) {
                            value = "";

                            String line;

                            for (BufferedReader read = new BufferedReader((Reader) e); (line = read.readLine()) != null; value = value + line) {
                                if (value.length() > 0) {
                                    value = value + "\n";
                                }
                            }
                        } else {
                            value = e.toString();
                        }

                        if (value == null) {
                            return true;
                        } else {
                            this.putString(value);
                            return true;
                        }
                    }
                } catch (UnsupportedFlavorException unsupportedflavorexception) {
                    Log.error(new Object[] { "Paste failed: ", unsupportedflavorexception});
                    return false;
                }
            }
        }
    }

    public void addTriggeredAction(char c, ActionListener listener) {
        this.triggeredActions.put(Character.valueOf(c), listener);
    }

    public void printColumns(Collection items) throws IOException {
        if (items != null && !items.isEmpty()) {
            int width = this.getTerminal().getWidth();
            int height = this.getTerminal().getHeight();
            int maxWidth = 0;

            CharSequence buff;

            for (Iterator showLines = items.iterator(); showLines.hasNext(); maxWidth = Math.max(maxWidth, buff.length())) {
                buff = (CharSequence) showLines.next();
            }

            maxWidth += 3;
            Log.debug(new Object[] { "Max width: ", Integer.valueOf(maxWidth)});
            int i;

            if (this.isPaginationEnabled()) {
                i = height - 1;
            } else {
                i = Integer.MAX_VALUE;
            }

            StringBuilder stringbuilder = new StringBuilder();
            Iterator i$ = items.iterator();

            while (i$.hasNext()) {
                CharSequence item = (CharSequence) i$.next();
                int i;

                if (stringbuilder.length() + maxWidth > width) {
                    this.println(stringbuilder);
                    stringbuilder.setLength(0);
                    --i;
                    if (i == 0) {
                        this.print((CharSequence) ConsoleReader.resources.getString("DISPLAY_MORE"));
                        this.flush();
                        i = this.readCharacter();
                        if (i != 13 && i != 10) {
                            if (i != 113) {
                                i = height - 1;
                            }
                        } else {
                            i = 1;
                        }

                        this.back(ConsoleReader.resources.getString("DISPLAY_MORE").length());
                        if (i == 113) {
                            break;
                        }
                    }
                }

                stringbuilder.append(item.toString());

                for (i = 0; i < maxWidth - item.length(); ++i) {
                    stringbuilder.append(' ');
                }
            }

            if (stringbuilder.length() > 0) {
                this.println(stringbuilder);
            }

        }
    }

    private void beforeReadLine(String prompt, Character mask) {
        if (mask != null && this.maskThread == null) {
            final String fullPrompt = "\r" + prompt + "                 " + "                 " + "                 " + "\r" + prompt;

            this.maskThread = new Thread() {
                public void run() {
                    while (!interrupted()) {
                        try {
                            Writer e = ConsoleReader.this.getOutput();

                            e.write(fullPrompt);
                            e.flush();
                            sleep(3L);
                        } catch (IOException ioexception) {
                            return;
                        } catch (InterruptedException interruptedexception) {
                            return;
                        }
                    }

                }
            };
            this.maskThread.setPriority(10);
            this.maskThread.setDaemon(true);
            this.maskThread.start();
        }

    }

    private void afterReadLine() {
        if (this.maskThread != null && this.maskThread.isAlive()) {
            this.maskThread.interrupt();
        }

        this.maskThread = null;
    }

    public void resetPromptLine(String prompt, String buffer, int cursorDest) throws IOException {
        this.moveToEnd();
        this.buf.buffer.append(this.prompt);
        int promptLength = 0;

        if (this.prompt != null) {
            promptLength = this.prompt.length();
        }

        this.buf.cursor += promptLength;
        this.setPrompt("");
        this.backspaceAll();
        this.setPrompt(prompt);
        this.redrawLine();
        this.setBuffer(buffer);
        if (cursorDest < 0) {
            cursorDest = buffer.length();
        }

        this.setCursorPosition(cursorDest);
        this.flush();
    }

    public void printSearchStatus(String searchTerm, String match) throws IOException {
        this.printSearchStatus(searchTerm, match, "(reverse-i-search)`");
    }

    public void printForwardSearchStatus(String searchTerm, String match) throws IOException {
        this.printSearchStatus(searchTerm, match, "(i-search)`");
    }

    private void printSearchStatus(String searchTerm, String match, String searchLabel) throws IOException {
        String prompt = searchLabel + searchTerm + "\': ";
        int cursorDest = match.indexOf(searchTerm);

        this.resetPromptLine(prompt, match, cursorDest);
    }

    public void restoreLine(String originalPrompt, int cursorDest) throws IOException {
        String prompt = this.lastLine(originalPrompt);
        String buffer = this.buf.buffer.toString();

        this.resetPromptLine(prompt, buffer, cursorDest);
    }

    public int searchBackwards(String searchTerm, int startIndex) {
        return this.searchBackwards(searchTerm, startIndex, false);
    }

    public int searchBackwards(String searchTerm) {
        return this.searchBackwards(searchTerm, this.history.index());
    }

    public int searchBackwards(String searchTerm, int startIndex, boolean startsWith) {
        ListIterator it = this.history.entries(startIndex);

        while (it.hasPrevious()) {
            History.Entry e = (History.Entry) it.previous();

            if (startsWith) {
                if (e.value().toString().startsWith(searchTerm)) {
                    return e.index();
                }
            } else if (e.value().toString().contains(searchTerm)) {
                return e.index();
            }
        }

        return -1;
    }

    public int searchForwards(String searchTerm, int startIndex) {
        return this.searchForwards(searchTerm, startIndex, false);
    }

    public int searchForwards(String searchTerm) {
        return this.searchForwards(searchTerm, this.history.index());
    }

    public int searchForwards(String searchTerm, int startIndex, boolean startsWith) {
        if (startIndex >= this.history.size()) {
            startIndex = this.history.size() - 1;
        }

        ListIterator it = this.history.entries(startIndex);

        if (this.searchIndex != -1 && it.hasNext()) {
            it.next();
        }

        while (it.hasNext()) {
            History.Entry e = (History.Entry) it.next();

            if (startsWith) {
                if (e.value().toString().startsWith(searchTerm)) {
                    return e.index();
                }
            } else if (e.value().toString().contains(searchTerm)) {
                return e.index();
            }
        }

        return -1;
    }

    private boolean isDelimiter(char c) {
        return !Character.isLetterOrDigit(c);
    }

    private boolean isWhitespace(char c) {
        return Character.isWhitespace(c);
    }

    private void printAnsiSequence(String sequence) throws IOException {
        this.print(27);
        this.print(91);
        this.print((CharSequence) sequence);
        this.flush();
    }

    static class SyntheticClass_1 {

        static final int[] $SwitchMap$jline$console$Operation = new int[Operation.values().length];

        static {
            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_EOF_MAYBE.ordinal()] = 1;
            } catch (NoSuchFieldError nosuchfielderror) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.ABORT.ordinal()] = 2;
            } catch (NoSuchFieldError nosuchfielderror1) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.BACKWARD_CHAR.ordinal()] = 3;
            } catch (NoSuchFieldError nosuchfielderror2) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.FORWARD_CHAR.ordinal()] = 4;
            } catch (NoSuchFieldError nosuchfielderror3) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.END_OF_LINE.ordinal()] = 5;
            } catch (NoSuchFieldError nosuchfielderror4) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_MATCH.ordinal()] = 6;
            } catch (NoSuchFieldError nosuchfielderror5) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_BEGNNING_OF_LINE_OR_ARG_DIGIT.ordinal()] = 7;
            } catch (NoSuchFieldError nosuchfielderror6) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_ARG_DIGIT.ordinal()] = 8;
            } catch (NoSuchFieldError nosuchfielderror7) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_PREV_WORD.ordinal()] = 9;
            } catch (NoSuchFieldError nosuchfielderror8) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_END_WORD.ordinal()] = 10;
            } catch (NoSuchFieldError nosuchfielderror9) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_CHAR_SEARCH.ordinal()] = 11;
            } catch (NoSuchFieldError nosuchfielderror10) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_NEXT_WORD.ordinal()] = 12;
            } catch (NoSuchFieldError nosuchfielderror11) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_FIRST_PRINT.ordinal()] = 13;
            } catch (NoSuchFieldError nosuchfielderror12) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_GOTO_MARK.ordinal()] = 14;
            } catch (NoSuchFieldError nosuchfielderror13) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_COLUMN.ordinal()] = 15;
            } catch (NoSuchFieldError nosuchfielderror14) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_DELETE_TO.ordinal()] = 16;
            } catch (NoSuchFieldError nosuchfielderror15) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_YANK_TO.ordinal()] = 17;
            } catch (NoSuchFieldError nosuchfielderror16) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_CHANGE_TO.ordinal()] = 18;
            } catch (NoSuchFieldError nosuchfielderror17) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.REVERSE_SEARCH_HISTORY.ordinal()] = 19;
            } catch (NoSuchFieldError nosuchfielderror18) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.FORWARD_SEARCH_HISTORY.ordinal()] = 20;
            } catch (NoSuchFieldError nosuchfielderror19) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.BACKWARD_DELETE_CHAR.ordinal()] = 21;
            } catch (NoSuchFieldError nosuchfielderror20) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.SELF_INSERT.ordinal()] = 22;
            } catch (NoSuchFieldError nosuchfielderror21) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.COMPLETE.ordinal()] = 23;
            } catch (NoSuchFieldError nosuchfielderror22) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.POSSIBLE_COMPLETIONS.ordinal()] = 24;
            } catch (NoSuchFieldError nosuchfielderror23) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.BEGINNING_OF_LINE.ordinal()] = 25;
            } catch (NoSuchFieldError nosuchfielderror24) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.YANK.ordinal()] = 26;
            } catch (NoSuchFieldError nosuchfielderror25) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.YANK_POP.ordinal()] = 27;
            } catch (NoSuchFieldError nosuchfielderror26) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.KILL_LINE.ordinal()] = 28;
            } catch (NoSuchFieldError nosuchfielderror27) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.KILL_WHOLE_LINE.ordinal()] = 29;
            } catch (NoSuchFieldError nosuchfielderror28) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.CLEAR_SCREEN.ordinal()] = 30;
            } catch (NoSuchFieldError nosuchfielderror29) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.OVERWRITE_MODE.ordinal()] = 31;
            } catch (NoSuchFieldError nosuchfielderror30) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.ACCEPT_LINE.ordinal()] = 32;
            } catch (NoSuchFieldError nosuchfielderror31) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.INTERRUPT.ordinal()] = 33;
            } catch (NoSuchFieldError nosuchfielderror32) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_MOVE_ACCEPT_LINE.ordinal()] = 34;
            } catch (NoSuchFieldError nosuchfielderror33) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.BACKWARD_WORD.ordinal()] = 35;
            } catch (NoSuchFieldError nosuchfielderror34) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.FORWARD_WORD.ordinal()] = 36;
            } catch (NoSuchFieldError nosuchfielderror35) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.PREVIOUS_HISTORY.ordinal()] = 37;
            } catch (NoSuchFieldError nosuchfielderror36) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_PREVIOUS_HISTORY.ordinal()] = 38;
            } catch (NoSuchFieldError nosuchfielderror37) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.NEXT_HISTORY.ordinal()] = 39;
            } catch (NoSuchFieldError nosuchfielderror38) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_NEXT_HISTORY.ordinal()] = 40;
            } catch (NoSuchFieldError nosuchfielderror39) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.EXIT_OR_DELETE_CHAR.ordinal()] = 41;
            } catch (NoSuchFieldError nosuchfielderror40) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.DELETE_CHAR.ordinal()] = 42;
            } catch (NoSuchFieldError nosuchfielderror41) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.UNIX_LINE_DISCARD.ordinal()] = 43;
            } catch (NoSuchFieldError nosuchfielderror42) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.UNIX_WORD_RUBOUT.ordinal()] = 44;
            } catch (NoSuchFieldError nosuchfielderror43) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.BACKWARD_KILL_WORD.ordinal()] = 45;
            } catch (NoSuchFieldError nosuchfielderror44) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.KILL_WORD.ordinal()] = 46;
            } catch (NoSuchFieldError nosuchfielderror45) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.BEGINNING_OF_HISTORY.ordinal()] = 47;
            } catch (NoSuchFieldError nosuchfielderror46) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.END_OF_HISTORY.ordinal()] = 48;
            } catch (NoSuchFieldError nosuchfielderror47) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.HISTORY_SEARCH_BACKWARD.ordinal()] = 49;
            } catch (NoSuchFieldError nosuchfielderror48) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.HISTORY_SEARCH_FORWARD.ordinal()] = 50;
            } catch (NoSuchFieldError nosuchfielderror49) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.CAPITALIZE_WORD.ordinal()] = 51;
            } catch (NoSuchFieldError nosuchfielderror50) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.UPCASE_WORD.ordinal()] = 52;
            } catch (NoSuchFieldError nosuchfielderror51) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.DOWNCASE_WORD.ordinal()] = 53;
            } catch (NoSuchFieldError nosuchfielderror52) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.TAB_INSERT.ordinal()] = 54;
            } catch (NoSuchFieldError nosuchfielderror53) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.RE_READ_INIT_FILE.ordinal()] = 55;
            } catch (NoSuchFieldError nosuchfielderror54) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.START_KBD_MACRO.ordinal()] = 56;
            } catch (NoSuchFieldError nosuchfielderror55) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.END_KBD_MACRO.ordinal()] = 57;
            } catch (NoSuchFieldError nosuchfielderror56) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.CALL_LAST_KBD_MACRO.ordinal()] = 58;
            } catch (NoSuchFieldError nosuchfielderror57) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_EDITING_MODE.ordinal()] = 59;
            } catch (NoSuchFieldError nosuchfielderror58) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_MOVEMENT_MODE.ordinal()] = 60;
            } catch (NoSuchFieldError nosuchfielderror59) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_INSERTION_MODE.ordinal()] = 61;
            } catch (NoSuchFieldError nosuchfielderror60) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_APPEND_MODE.ordinal()] = 62;
            } catch (NoSuchFieldError nosuchfielderror61) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_APPEND_EOL.ordinal()] = 63;
            } catch (NoSuchFieldError nosuchfielderror62) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.TRANSPOSE_CHARS.ordinal()] = 64;
            } catch (NoSuchFieldError nosuchfielderror63) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.INSERT_COMMENT.ordinal()] = 65;
            } catch (NoSuchFieldError nosuchfielderror64) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.INSERT_CLOSE_CURLY.ordinal()] = 66;
            } catch (NoSuchFieldError nosuchfielderror65) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.INSERT_CLOSE_PAREN.ordinal()] = 67;
            } catch (NoSuchFieldError nosuchfielderror66) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.INSERT_CLOSE_SQUARE.ordinal()] = 68;
            } catch (NoSuchFieldError nosuchfielderror67) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_INSERT_COMMENT.ordinal()] = 69;
            } catch (NoSuchFieldError nosuchfielderror68) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_SEARCH.ordinal()] = 70;
            } catch (NoSuchFieldError nosuchfielderror69) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_INSERT_BEG.ordinal()] = 71;
            } catch (NoSuchFieldError nosuchfielderror70) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_RUBOUT.ordinal()] = 72;
            } catch (NoSuchFieldError nosuchfielderror71) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_DELETE.ordinal()] = 73;
            } catch (NoSuchFieldError nosuchfielderror72) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_KILL_WHOLE_LINE.ordinal()] = 74;
            } catch (NoSuchFieldError nosuchfielderror73) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_PUT.ordinal()] = 75;
            } catch (NoSuchFieldError nosuchfielderror74) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_CHANGE_CASE.ordinal()] = 76;
            } catch (NoSuchFieldError nosuchfielderror75) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_CHANGE_CHAR.ordinal()] = 77;
            } catch (NoSuchFieldError nosuchfielderror76) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_DELETE_TO_EOL.ordinal()] = 78;
            } catch (NoSuchFieldError nosuchfielderror77) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.VI_CHANGE_TO_EOL.ordinal()] = 79;
            } catch (NoSuchFieldError nosuchfielderror78) {
                ;
            }

            try {
                ConsoleReader.SyntheticClass_1.$SwitchMap$jline$console$Operation[Operation.EMACS_EDITING_MODE.ordinal()] = 80;
            } catch (NoSuchFieldError nosuchfielderror79) {
                ;
            }

        }
    }

    private static enum State {

        NORMAL, SEARCH, FORWARD_SEARCH, VI_YANK_TO, VI_DELETE_TO, VI_CHANGE_TO;
    }
}
