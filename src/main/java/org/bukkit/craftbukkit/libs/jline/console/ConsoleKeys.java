package org.bukkit.craftbukkit.libs.jline.console;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.craftbukkit.libs.jline.internal.Log;

public class ConsoleKeys {

    private KeyMap keys;
    private Map keyMaps = KeyMap.keyMaps();
    private Map variables = new HashMap();

    public ConsoleKeys(String appName, URL inputrcUrl) {
        this.loadKeys(appName, inputrcUrl);
    }

    protected boolean isViEditMode() {
        return this.keys.isViKeyMap();
    }

    protected boolean setKeyMap(String name) {
        KeyMap map = (KeyMap) this.keyMaps.get(name);

        if (map == null) {
            return false;
        } else {
            this.keys = map;
            return true;
        }
    }

    protected Map getKeyMaps() {
        return this.keyMaps;
    }

    protected KeyMap getKeys() {
        return this.keys;
    }

    protected void setKeys(KeyMap keys) {
        this.keys = keys;
    }

    protected boolean getViEditMode() {
        return this.keys.isViKeyMap();
    }

    protected void loadKeys(String appName, URL inputrcUrl) {
        this.keys = (KeyMap) this.keyMaps.get("emacs");

        try {
            InputStream e = inputrcUrl.openStream();

            try {
                this.loadKeys(e, appName);
                Log.debug(new Object[] { "Loaded user configuration: ", inputrcUrl});
            } finally {
                try {
                    e.close();
                } catch (IOException ioexception) {
                    ;
                }

            }
        } catch (IOException ioexception1) {
            if (inputrcUrl.getProtocol().equals("file")) {
                File file = new File(inputrcUrl.getPath());

                if (file.exists()) {
                    Log.warn(new Object[] { "Unable to read user configuration: ", inputrcUrl, ioexception1});
                }
            } else {
                Log.warn(new Object[] { "Unable to read user configuration: ", inputrcUrl, ioexception1});
            }
        }

    }

    private void loadKeys(InputStream input, String appName) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        boolean parsing = true;
        ArrayList ifsStack = new ArrayList();

        String line;

        while ((line = reader.readLine()) != null) {
            try {
                line = line.trim();
                if (line.length() != 0 && line.charAt(0) != 35) {
                    byte e = 0;
                    String keySeq;
                    int i;
                    int j;

                    if (line.charAt(e) == 36) {
                        for (i = e + 1; i < line.length() && (line.charAt(i) == 32 || line.charAt(i) == 9); ++i) {
                            ;
                        }

                        for (j = i; i < line.length() && line.charAt(i) != 32 && line.charAt(i) != 9; ++i) {
                            ;
                        }

                        String s;

                        for (s = line.substring(j, i); i < line.length() && (line.charAt(i) == 32 || line.charAt(i) == 9); ++i) {
                            ;
                        }

                        for (j = i; i < line.length() && line.charAt(i) != 32 && line.charAt(i) != 9; ++i) {
                            ;
                        }

                        keySeq = line.substring(j, i);
                        if ("if".equalsIgnoreCase(s)) {
                            ifsStack.add(Boolean.valueOf(parsing));
                            if (parsing && !keySeq.startsWith("term=")) {
                                if (keySeq.startsWith("mode=")) {
                                    if (keySeq.equalsIgnoreCase("mode=vi")) {
                                        parsing = this.isViEditMode();
                                    } else if (keySeq.equals("mode=emacs")) {
                                        parsing = !this.isViEditMode();
                                    } else {
                                        parsing = false;
                                    }
                                } else {
                                    parsing = keySeq.equalsIgnoreCase(appName);
                                }
                            }
                        } else if (!"else".equalsIgnoreCase(s)) {
                            if ("endif".equalsIgnoreCase(s)) {
                                if (ifsStack.isEmpty()) {
                                    throw new IllegalArgumentException("endif found without matching $if");
                                }

                                parsing = ((Boolean) ifsStack.remove(ifsStack.size() - 1)).booleanValue();
                            } else if ("include".equalsIgnoreCase(s)) {
                                ;
                            }
                        } else {
                            if (ifsStack.isEmpty()) {
                                throw new IllegalArgumentException("$else found without matching $if");
                            }

                            boolean flag = true;
                            Iterator iterator = ifsStack.iterator();

                            while (iterator.hasNext()) {
                                boolean flag1 = ((Boolean) iterator.next()).booleanValue();

                                if (!flag1) {
                                    flag = false;
                                    break;
                                }
                            }

                            if (flag) {
                                parsing = !parsing;
                            }
                        }
                    } else if (parsing) {
                        keySeq = "";
                        i = e + 1;
                        if (line.charAt(e) == 34) {
                            boolean start = false;

                            while (true) {
                                if (i >= line.length()) {
                                    throw new IllegalArgumentException("Missing closing quote on line \'" + line + "\'");
                                }

                                if (start) {
                                    start = false;
                                } else if (line.charAt(i) == 92) {
                                    start = true;
                                } else if (line.charAt(i) == 34) {
                                    break;
                                }

                                ++i;
                            }
                        }

                        while (i < line.length() && line.charAt(i) != 58 && line.charAt(i) != 32 && line.charAt(i) != 9) {
                            ++i;
                        }

                        keySeq = line.substring(0, i);
                        boolean equivalency = i + 1 < line.length() && line.charAt(i) == 58 && line.charAt(i + 1) == 61;

                        ++i;
                        if (equivalency) {
                            ++i;
                        }

                        String s1;

                        if (keySeq.equalsIgnoreCase("set")) {
                            while (i < line.length() && (line.charAt(i) == 32 || line.charAt(i) == 9)) {
                                ++i;
                            }

                            int k;

                            for (k = i; i < line.length() && line.charAt(i) != 32 && line.charAt(i) != 9; ++i) {
                                ;
                            }

                            String s2;

                            for (s2 = line.substring(k, i); i < line.length() && (line.charAt(i) == 32 || line.charAt(i) == 9); ++i) {
                                ;
                            }

                            for (k = i; i < line.length() && line.charAt(i) != 32 && line.charAt(i) != 9; ++i) {
                                ;
                            }

                            s1 = line.substring(k, i);
                            this.setVar(s2, s1);
                        } else {
                            while (i < line.length() && (line.charAt(i) == 32 || line.charAt(i) == 9)) {
                                ++i;
                            }

                            j = i;
                            if (i < line.length() && (line.charAt(i) == 39 || line.charAt(i) == 34)) {
                                char val = line.charAt(i++);

                                for (boolean operationName = false; i < line.length(); ++i) {
                                    if (operationName) {
                                        operationName = false;
                                    } else if (line.charAt(i) == 92) {
                                        operationName = true;
                                    } else if (line.charAt(i) == val) {
                                        break;
                                    }
                                }
                            }

                            while (i < line.length() && line.charAt(i) != 32 && line.charAt(i) != 9) {
                                ++i;
                            }

                            s1 = line.substring(Math.min(j, line.length()), Math.min(i, line.length()));
                            String s3;

                            if (keySeq.charAt(0) == 34) {
                                keySeq = this.translateQuoted(keySeq);
                            } else {
                                s3 = keySeq.lastIndexOf(45) > 0 ? keySeq.substring(keySeq.lastIndexOf(45) + 1) : keySeq;
                                char e1 = this.getKeyFromName(s3);

                                s3 = keySeq.toLowerCase();
                                keySeq = "";
                                if (s3.contains("meta-") || s3.contains("m-")) {
                                    keySeq = keySeq + "\u001b";
                                }

                                if (s3.contains("control-") || s3.contains("c-") || s3.contains("ctrl-")) {
                                    e1 = (char) (Character.toUpperCase(e1) & 31);
                                }

                                keySeq = keySeq + e1;
                            }

                            if (s1.length() > 0 && (s1.charAt(0) == 39 || s1.charAt(0) == 34)) {
                                this.keys.bind(keySeq, this.translateQuoted(s1));
                            } else {
                                s3 = s1.replace('-', '_').toUpperCase();

                                try {
                                    this.keys.bind(keySeq, Operation.valueOf(s3));
                                } catch (IllegalArgumentException illegalargumentexception) {
                                    Log.info(new Object[] { "Unable to bind key for unsupported operation: ", s1});
                                }
                            }
                        }
                    }
                }
            } catch (IllegalArgumentException illegalargumentexception1) {
                Log.warn(new Object[] { "Unable to parse user configuration: ", illegalargumentexception1});
            }
        }

    }

    private String translateQuoted(String keySeq) {
        String str = keySeq.substring(1, keySeq.length() - 1);

        keySeq = "";

        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);

            if (c == 92) {
                boolean ctrl = str.regionMatches(i, "\\C-", 0, 3) || str.regionMatches(i, "\\M-\\C-", 0, 6);
                boolean meta = str.regionMatches(i, "\\M-", 0, 3) || str.regionMatches(i, "\\C-\\M-", 0, 6);

                i += (meta ? 3 : 0) + (ctrl ? 3 : 0) + (!meta && !ctrl ? 1 : 0);
                if (i >= str.length()) {
                    break;
                }

                c = str.charAt(i);
                if (meta) {
                    keySeq = keySeq + "\u001b";
                }

                if (ctrl) {
                    c = c == 63 ? 127 : (char) (Character.toUpperCase(c) & 31);
                }

                if (!meta && !ctrl) {
                    int j;
                    int k;

                    label112:
                    switch (c) {
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                        c = 0;

                        for (j = 0; j < 3 && i < str.length(); ++i) {
                            k = Character.digit(str.charAt(i), 8);
                            if (k < 0) {
                                break;
                            }

                            c = (char) (c * 8 + k);
                            ++j;
                        }

                        c = (char) (c & 255);

                    case '8':
                    case '9':
                    case ':':
                    case ';':
                    case '<':
                    case '=':
                    case '>':
                    case '?':
                    case '@':
                    case 'A':
                    case 'B':
                    case 'C':
                    case 'D':
                    case 'E':
                    case 'F':
                    case 'G':
                    case 'H':
                    case 'I':
                    case 'J':
                    case 'K':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'O':
                    case 'P':
                    case 'Q':
                    case 'R':
                    case 'S':
                    case 'T':
                    case 'U':
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                    case 'Z':
                    case '[':
                    case ']':
                    case '^':
                    case '_':
                    case '`':
                    case 'c':
                    case 'g':
                    case 'h':
                    case 'i':
                    case 'j':
                    case 'k':
                    case 'l':
                    case 'm':
                    case 'o':
                    case 'p':
                    case 'q':
                    case 's':
                    case 'w':
                    default:
                        break;

                    case '\\':
                        c = 92;
                        break;

                    case 'a':
                        c = 7;
                        break;

                    case 'b':
                        c = 8;
                        break;

                    case 'd':
                        c = 127;
                        break;

                    case 'e':
                        c = 27;
                        break;

                    case 'f':
                        c = 12;
                        break;

                    case 'n':
                        c = 10;
                        break;

                    case 'r':
                        c = 13;
                        break;

                    case 't':
                        c = 9;
                        break;

                    case 'u':
                        ++i;
                        c = 0;
                        j = 0;

                        while (true) {
                            if (j >= 4 || i >= str.length()) {
                                break label112;
                            }

                            k = Character.digit(str.charAt(i), 16);
                            if (k < 0) {
                                break label112;
                            }

                            c = (char) (c * 16 + k);
                            ++j;
                            ++i;
                        }

                    case 'v':
                        c = 11;
                        break;

                    case 'x':
                        ++i;
                        c = 0;

                        for (j = 0; j < 2 && i < str.length(); ++i) {
                            k = Character.digit(str.charAt(i), 16);
                            if (k < 0) {
                                break;
                            }

                            c = (char) (c * 16 + k);
                            ++j;
                        }

                        c = (char) (c & 255);
                    }
                }

                keySeq = keySeq + c;
            } else {
                keySeq = keySeq + c;
            }
        }

        return keySeq;
    }

    private char getKeyFromName(String name) {
        return !"DEL".equalsIgnoreCase(name) && !"Rubout".equalsIgnoreCase(name) ? (!"ESC".equalsIgnoreCase(name) && !"Escape".equalsIgnoreCase(name) ? (!"LFD".equalsIgnoreCase(name) && !"NewLine".equalsIgnoreCase(name) ? (!"RET".equalsIgnoreCase(name) && !"Return".equalsIgnoreCase(name) ? (!"SPC".equalsIgnoreCase(name) && !"Space".equalsIgnoreCase(name) ? ("Tab".equalsIgnoreCase(name) ? '\t' : name.charAt(0)) : ' ') : '\r') : '\n') : '\u001b') : '\u007f';
    }

    private void setVar(String key, String val) {
        if ("keymap".equalsIgnoreCase(key)) {
            if (this.keyMaps.containsKey(val)) {
                this.keys = (KeyMap) this.keyMaps.get(val);
            }
        } else if ("editing-mode".equals(key)) {
            if ("vi".equalsIgnoreCase(val)) {
                this.keys = (KeyMap) this.keyMaps.get("vi-insert");
            } else if ("emacs".equalsIgnoreCase(key)) {
                this.keys = (KeyMap) this.keyMaps.get("emacs");
            }
        } else if ("blink-matching-paren".equals(key)) {
            if ("on".equalsIgnoreCase(val)) {
                this.keys.setBlinkMatchingParen(true);
            } else if ("off".equalsIgnoreCase(val)) {
                this.keys.setBlinkMatchingParen(false);
            }
        }

        this.variables.put(key, val);
    }

    public String getVariable(String s) {
        return (String) this.variables.get(s);
    }
}
