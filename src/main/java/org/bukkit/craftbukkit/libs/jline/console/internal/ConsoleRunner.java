package org.bukkit.craftbukkit.libs.jline.console.internal;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.craftbukkit.libs.jline.console.completer.ArgumentCompleter;
import org.bukkit.craftbukkit.libs.jline.console.completer.Completer;
import org.bukkit.craftbukkit.libs.jline.console.history.FileHistory;
import org.bukkit.craftbukkit.libs.jline.internal.Configuration;

public class ConsoleRunner {

    public static final String property = "org.bukkit.craftbukkit.libs.jline.history";

    public static void main(String[] args) throws Exception {
        ArrayList argList = new ArrayList(Arrays.asList(args));

        if (argList.size() == 0) {
            usage();
        } else {
            String historyFileName = System.getProperty("org.bukkit.craftbukkit.libs.jline.history", (String) null);
            String mainClass = (String) argList.remove(0);
            ConsoleReader reader = new ConsoleReader();

            if (historyFileName != null) {
                reader.setHistory(new FileHistory(new File(Configuration.getUserHome(), String.format(".org.bukkit.craftbukkit.libs.jline-%s.%s.history", new Object[] { mainClass, historyFileName}))));
            } else {
                reader.setHistory(new FileHistory(new File(Configuration.getUserHome(), String.format(".org.bukkit.craftbukkit.libs.jline-%s.history", new Object[] { mainClass}))));
            }

            String completors = System.getProperty(ConsoleRunner.class.getName() + ".completers", "");
            ArrayList completorList = new ArrayList();
            StringTokenizer type = new StringTokenizer(completors, ",");

            while (type.hasMoreTokens()) {
                Object method = Class.forName(type.nextToken()).newInstance();

                completorList.add((Completer) method);
            }

            if (completorList.size() > 0) {
                reader.addCompleter(new ArgumentCompleter(completorList));
            }

            ConsoleReaderInputStream.setIn(reader);

            try {
                Class type1 = Class.forName(mainClass);
                Method method1 = type1.getMethod("main", new Class[] { String[].class});

                method1.invoke((Object) null, new Object[0]);
            } finally {
                ConsoleReaderInputStream.restoreIn();
            }

        }
    }

    private static void usage() {
        System.out.println("Usage: \n   java [-Djline.history=\'name\'] " + ConsoleRunner.class.getName() + " <target class name> [args]" + "\n\nThe -Djline.history option will avoid history" + "\nmangling when running ConsoleRunner on the same application." + "\n\nargs will be passed directly to the target class name.");
    }
}
