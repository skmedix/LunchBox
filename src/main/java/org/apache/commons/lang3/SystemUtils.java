package org.apache.commons.lang3;

import java.io.File;

public class SystemUtils {

    private static final String OS_NAME_WINDOWS_PREFIX = "Windows";
    private static final String USER_HOME_KEY = "user.home";
    private static final String USER_DIR_KEY = "user.dir";
    private static final String JAVA_IO_TMPDIR_KEY = "java.io.tmpdir";
    private static final String JAVA_HOME_KEY = "java.home";
    public static final String AWT_TOOLKIT = getSystemProperty("awt.toolkit");
    public static final String FILE_ENCODING = getSystemProperty("file.encoding");
    public static final String FILE_SEPARATOR = getSystemProperty("file.separator");
    public static final String JAVA_AWT_FONTS = getSystemProperty("java.awt.fonts");
    public static final String JAVA_AWT_GRAPHICSENV = getSystemProperty("java.awt.graphicsenv");
    public static final String JAVA_AWT_HEADLESS = getSystemProperty("java.awt.headless");
    public static final String JAVA_AWT_PRINTERJOB = getSystemProperty("java.awt.printerjob");
    public static final String JAVA_CLASS_PATH = getSystemProperty("java.class.path");
    public static final String JAVA_CLASS_VERSION = getSystemProperty("java.class.version");
    public static final String JAVA_COMPILER = getSystemProperty("java.compiler");
    public static final String JAVA_ENDORSED_DIRS = getSystemProperty("java.endorsed.dirs");
    public static final String JAVA_EXT_DIRS = getSystemProperty("java.ext.dirs");
    public static final String JAVA_HOME = getSystemProperty("java.home");
    public static final String JAVA_IO_TMPDIR = getSystemProperty("java.io.tmpdir");
    public static final String JAVA_LIBRARY_PATH = getSystemProperty("java.library.path");
    public static final String JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name");
    public static final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version");
    public static final String JAVA_SPECIFICATION_NAME = getSystemProperty("java.specification.name");
    public static final String JAVA_SPECIFICATION_VENDOR = getSystemProperty("java.specification.vendor");
    public static final String JAVA_SPECIFICATION_VERSION = getSystemProperty("java.specification.version");
    private static final JavaVersion JAVA_SPECIFICATION_VERSION_AS_ENUM = JavaVersion.get(SystemUtils.JAVA_SPECIFICATION_VERSION);
    public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY = getSystemProperty("java.util.prefs.PreferencesFactory");
    public static final String JAVA_VENDOR = getSystemProperty("java.vendor");
    public static final String JAVA_VENDOR_URL = getSystemProperty("java.vendor.url");
    public static final String JAVA_VERSION = getSystemProperty("java.version");
    public static final String JAVA_VM_INFO = getSystemProperty("java.vm.info");
    public static final String JAVA_VM_NAME = getSystemProperty("java.vm.name");
    public static final String JAVA_VM_SPECIFICATION_NAME = getSystemProperty("java.vm.specification.name");
    public static final String JAVA_VM_SPECIFICATION_VENDOR = getSystemProperty("java.vm.specification.vendor");
    public static final String JAVA_VM_SPECIFICATION_VERSION = getSystemProperty("java.vm.specification.version");
    public static final String JAVA_VM_VENDOR = getSystemProperty("java.vm.vendor");
    public static final String JAVA_VM_VERSION = getSystemProperty("java.vm.version");
    public static final String LINE_SEPARATOR = getSystemProperty("line.separator");
    public static final String OS_ARCH = getSystemProperty("os.arch");
    public static final String OS_NAME = getSystemProperty("os.name");
    public static final String OS_VERSION = getSystemProperty("os.version");
    public static final String PATH_SEPARATOR = getSystemProperty("path.separator");
    public static final String USER_COUNTRY = getSystemProperty("user.country") == null ? getSystemProperty("user.region") : getSystemProperty("user.country");
    public static final String USER_DIR = getSystemProperty("user.dir");
    public static final String USER_HOME = getSystemProperty("user.home");
    public static final String USER_LANGUAGE = getSystemProperty("user.language");
    public static final String USER_NAME = getSystemProperty("user.name");
    public static final String USER_TIMEZONE = getSystemProperty("user.timezone");
    public static final boolean IS_JAVA_1_1 = getJavaVersionMatches("1.1");
    public static final boolean IS_JAVA_1_2 = getJavaVersionMatches("1.2");
    public static final boolean IS_JAVA_1_3 = getJavaVersionMatches("1.3");
    public static final boolean IS_JAVA_1_4 = getJavaVersionMatches("1.4");
    public static final boolean IS_JAVA_1_5 = getJavaVersionMatches("1.5");
    public static final boolean IS_JAVA_1_6 = getJavaVersionMatches("1.6");
    public static final boolean IS_JAVA_1_7 = getJavaVersionMatches("1.7");
    public static final boolean IS_JAVA_1_8 = getJavaVersionMatches("1.8");
    public static final boolean IS_OS_AIX = getOSMatchesName("AIX");
    public static final boolean IS_OS_HP_UX = getOSMatchesName("HP-UX");
    public static final boolean IS_OS_400 = getOSMatchesName("OS/400");
    public static final boolean IS_OS_IRIX = getOSMatchesName("Irix");
    public static final boolean IS_OS_LINUX = getOSMatchesName("Linux") || getOSMatchesName("LINUX");
    public static final boolean IS_OS_MAC = getOSMatchesName("Mac");
    public static final boolean IS_OS_MAC_OSX = getOSMatchesName("Mac OS X");
    public static final boolean IS_OS_FREE_BSD = getOSMatchesName("FreeBSD");
    public static final boolean IS_OS_OPEN_BSD = getOSMatchesName("OpenBSD");
    public static final boolean IS_OS_NET_BSD = getOSMatchesName("NetBSD");
    public static final boolean IS_OS_OS2 = getOSMatchesName("OS/2");
    public static final boolean IS_OS_SOLARIS = getOSMatchesName("Solaris");
    public static final boolean IS_OS_SUN_OS = getOSMatchesName("SunOS");
    public static final boolean IS_OS_UNIX = SystemUtils.IS_OS_AIX || SystemUtils.IS_OS_HP_UX || SystemUtils.IS_OS_IRIX || SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC_OSX || SystemUtils.IS_OS_SOLARIS || SystemUtils.IS_OS_SUN_OS || SystemUtils.IS_OS_FREE_BSD || SystemUtils.IS_OS_OPEN_BSD || SystemUtils.IS_OS_NET_BSD;
    public static final boolean IS_OS_WINDOWS = getOSMatchesName("Windows");
    public static final boolean IS_OS_WINDOWS_2000 = getOSMatches("Windows", "5.0");
    public static final boolean IS_OS_WINDOWS_2003 = getOSMatches("Windows", "5.2");
    public static final boolean IS_OS_WINDOWS_2008 = getOSMatches("Windows Server 2008", "6.1");
    public static final boolean IS_OS_WINDOWS_95 = getOSMatches("Windows 9", "4.0");
    public static final boolean IS_OS_WINDOWS_98 = getOSMatches("Windows 9", "4.1");
    public static final boolean IS_OS_WINDOWS_ME = getOSMatches("Windows", "4.9");
    public static final boolean IS_OS_WINDOWS_NT = getOSMatchesName("Windows NT");
    public static final boolean IS_OS_WINDOWS_XP = getOSMatches("Windows", "5.1");
    public static final boolean IS_OS_WINDOWS_VISTA = getOSMatches("Windows", "6.0");
    public static final boolean IS_OS_WINDOWS_7 = getOSMatches("Windows", "6.1");
    public static final boolean IS_OS_WINDOWS_8 = getOSMatches("Windows", "6.2");

    public static File getJavaHome() {
        return new File(System.getProperty("java.home"));
    }

    public static File getJavaIoTmpDir() {
        return new File(System.getProperty("java.io.tmpdir"));
    }

    private static boolean getJavaVersionMatches(String s) {
        return isJavaVersionMatch(SystemUtils.JAVA_SPECIFICATION_VERSION, s);
    }

    private static boolean getOSMatches(String s, String s1) {
        return isOSMatch(SystemUtils.OS_NAME, SystemUtils.OS_VERSION, s, s1);
    }

    private static boolean getOSMatchesName(String s) {
        return isOSNameMatch(SystemUtils.OS_NAME, s);
    }

    private static String getSystemProperty(String s) {
        try {
            return System.getProperty(s);
        } catch (SecurityException securityexception) {
            System.err.println("Caught a SecurityException reading the system property \'" + s + "\'; the SystemUtils property value will default to null.");
            return null;
        }
    }

    public static File getUserDir() {
        return new File(System.getProperty("user.dir"));
    }

    public static File getUserHome() {
        return new File(System.getProperty("user.home"));
    }

    public static boolean isJavaAwtHeadless() {
        return SystemUtils.JAVA_AWT_HEADLESS != null ? SystemUtils.JAVA_AWT_HEADLESS.equals(Boolean.TRUE.toString()) : false;
    }

    public static boolean isJavaVersionAtLeast(JavaVersion javaversion) {
        return SystemUtils.JAVA_SPECIFICATION_VERSION_AS_ENUM.atLeast(javaversion);
    }

    static boolean isJavaVersionMatch(String s, String s1) {
        return s == null ? false : s.startsWith(s1);
    }

    static boolean isOSMatch(String s, String s1, String s2, String s3) {
        return s != null && s1 != null ? s.startsWith(s2) && s1.startsWith(s3) : false;
    }

    static boolean isOSNameMatch(String s, String s1) {
        return s == null ? false : s.startsWith(s1);
    }
}
