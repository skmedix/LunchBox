package org.apache.logging.log4j.core.config.plugins;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.helpers.Charsets;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.status.StatusLogger;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

public class ResolverUtil {

    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final String VFSZIP = "vfszip";
    private static final String BUNDLE_RESOURCE = "bundleresource";
    private final Set classMatches = new HashSet();
    private final Set resourceMatches = new HashSet();
    private ClassLoader classloader;

    public Set getClasses() {
        return this.classMatches;
    }

    public Set getResources() {
        return this.resourceMatches;
    }

    public ClassLoader getClassLoader() {
        return this.classloader != null ? this.classloader : (this.classloader = Loader.getClassLoader(ResolverUtil.class, (Class) null));
    }

    public void setClassLoader(ClassLoader classloader) {
        this.classloader = classloader;
    }

    public void findImplementations(Class oclass, String... astring) {
        if (astring != null) {
            ResolverUtil.IsA resolverutil_isa = new ResolverUtil.IsA(oclass);
            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s = astring1[j];

                this.findInPackage(resolverutil_isa, s);
            }

        }
    }

    public void findSuffix(String s, String... astring) {
        if (astring != null) {
            ResolverUtil.NameEndsWith resolverutil_nameendswith = new ResolverUtil.NameEndsWith(s);
            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s1 = astring1[j];

                this.findInPackage(resolverutil_nameendswith, s1);
            }

        }
    }

    public void findAnnotated(Class oclass, String... astring) {
        if (astring != null) {
            ResolverUtil.AnnotatedWith resolverutil_annotatedwith = new ResolverUtil.AnnotatedWith(oclass);
            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s = astring1[j];

                this.findInPackage(resolverutil_annotatedwith, s);
            }

        }
    }

    public void findNamedResource(String s, String... astring) {
        if (astring != null) {
            ResolverUtil.NameIs resolverutil_nameis = new ResolverUtil.NameIs(s);
            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s1 = astring1[j];

                this.findInPackage(resolverutil_nameis, s1);
            }

        }
    }

    public void find(ResolverUtil.Test resolverutil_test, String... astring) {
        if (astring != null) {
            String[] astring1 = astring;
            int i = astring.length;

            for (int j = 0; j < i; ++j) {
                String s = astring1[j];

                this.findInPackage(resolverutil_test, s);
            }

        }
    }

    public void findInPackage(ResolverUtil.Test resolverutil_test, String s) {
        s = s.replace('.', '/');
        ClassLoader classloader = this.getClassLoader();

        Enumeration enumeration;

        try {
            enumeration = classloader.getResources(s);
        } catch (IOException ioexception) {
            ResolverUtil.LOGGER.warn("Could not read package: " + s, (Throwable) ioexception);
            return;
        }

        while (enumeration.hasMoreElements()) {
            try {
                URL url = (URL) enumeration.nextElement();
                String s1 = url.getFile();

                s1 = URLDecoder.decode(s1, Charsets.UTF_8.name());
                if (s1.startsWith("file:")) {
                    s1 = s1.substring(5);
                }

                if (s1.indexOf(33) > 0) {
                    s1 = s1.substring(0, s1.indexOf(33));
                }

                ResolverUtil.LOGGER.info("Scanning for classes in [" + s1 + "] matching criteria: " + resolverutil_test);
                if ("vfszip".equals(url.getProtocol())) {
                    String s2 = s1.substring(0, s1.length() - s.length() - 2);
                    URL url1 = new URL(url.getProtocol(), url.getHost(), s2);
                    JarInputStream jarinputstream = new JarInputStream(url1.openStream());

                    try {
                        this.loadImplementationsInJar(resolverutil_test, s, s2, jarinputstream);
                    } finally {
                        this.close(jarinputstream, url1);
                    }
                } else if ("bundleresource".equals(url.getProtocol())) {
                    this.loadImplementationsInBundle(resolverutil_test, s);
                } else {
                    File file = new File(s1);

                    if (file.isDirectory()) {
                        this.loadImplementationsInDirectory(resolverutil_test, s, file);
                    } else {
                        this.loadImplementationsInJar(resolverutil_test, s, file);
                    }
                }
            } catch (IOException ioexception1) {
                ResolverUtil.LOGGER.warn("could not read entries", (Throwable) ioexception1);
            }
        }

    }

    private void loadImplementationsInBundle(ResolverUtil.Test resolverutil_test, String s) {
        BundleWiring bundlewiring = (BundleWiring) FrameworkUtil.getBundle(ResolverUtil.class).adapt(BundleWiring.class);
        Collection collection = bundlewiring.listResources(s, "*.class", 1);
        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            String s1 = (String) iterator.next();

            this.addIfMatching(resolverutil_test, s1);
        }

    }

    private void loadImplementationsInDirectory(ResolverUtil.Test resolverutil_test, String s, File file) {
        File[] afile = file.listFiles();

        if (afile != null) {
            File[] afile1 = afile;
            int i = afile.length;

            for (int j = 0; j < i; ++j) {
                File file1 = afile1[j];
                StringBuilder stringbuilder = new StringBuilder();

                stringbuilder.append(s).append("/").append(file1.getName());
                String s1 = s == null ? file1.getName() : stringbuilder.toString();

                if (file1.isDirectory()) {
                    this.loadImplementationsInDirectory(resolverutil_test, s1, file1);
                } else if (this.isTestApplicable(resolverutil_test, file1.getName())) {
                    this.addIfMatching(resolverutil_test, s1);
                }
            }

        }
    }

    private boolean isTestApplicable(ResolverUtil.Test resolverutil_test, String s) {
        return resolverutil_test.doesMatchResource() || s.endsWith(".class") && resolverutil_test.doesMatchClass();
    }

    private void loadImplementationsInJar(ResolverUtil.Test resolverutil_test, String s, File file) {
        JarInputStream jarinputstream = null;

        try {
            jarinputstream = new JarInputStream(new FileInputStream(file));
            this.loadImplementationsInJar(resolverutil_test, s, file.getPath(), jarinputstream);
        } catch (FileNotFoundException filenotfoundexception) {
            ResolverUtil.LOGGER.error("Could not search jar file \'" + file + "\' for classes matching criteria: " + resolverutil_test + " file not found");
        } catch (IOException ioexception) {
            ResolverUtil.LOGGER.error("Could not search jar file \'" + file + "\' for classes matching criteria: " + resolverutil_test + " due to an IOException", (Throwable) ioexception);
        } finally {
            this.close(jarinputstream, file);
        }

    }

    private void close(JarInputStream jarinputstream, Object object) {
        if (jarinputstream != null) {
            try {
                jarinputstream.close();
            } catch (IOException ioexception) {
                ResolverUtil.LOGGER.error("Error closing JAR file stream for {}", new Object[] { object, ioexception});
            }
        }

    }

    private void loadImplementationsInJar(ResolverUtil.Test resolverutil_test, String s, String s1, JarInputStream jarinputstream) {
        while (true) {
            try {
                JarEntry jarentry;

                if ((jarentry = jarinputstream.getNextJarEntry()) != null) {
                    String s2 = jarentry.getName();

                    if (!jarentry.isDirectory() && s2.startsWith(s) && this.isTestApplicable(resolverutil_test, s2)) {
                        this.addIfMatching(resolverutil_test, s2);
                    }
                    continue;
                }
            } catch (IOException ioexception) {
                ResolverUtil.LOGGER.error("Could not search jar file \'" + s1 + "\' for classes matching criteria: " + resolverutil_test + " due to an IOException", (Throwable) ioexception);
            }

            return;
        }
    }

    protected void addIfMatching(ResolverUtil.Test resolverutil_test, String s) {
        try {
            ClassLoader classloader = this.getClassLoader();

            if (resolverutil_test.doesMatchClass()) {
                String s1 = s.substring(0, s.indexOf(46)).replace('/', '.');

                if (ResolverUtil.LOGGER.isDebugEnabled()) {
                    ResolverUtil.LOGGER.debug("Checking to see if class " + s1 + " matches criteria [" + resolverutil_test + "]");
                }

                Class oclass = classloader.loadClass(s1);

                if (resolverutil_test.matches(oclass)) {
                    this.classMatches.add(oclass);
                }
            }

            if (resolverutil_test.doesMatchResource()) {
                URL url = classloader.getResource(s);

                if (url == null) {
                    url = classloader.getResource(s.substring(1));
                }

                if (url != null && resolverutil_test.matches(url.toURI())) {
                    this.resourceMatches.add(url.toURI());
                }
            }
        } catch (Throwable throwable) {
            ResolverUtil.LOGGER.warn("Could not examine class \'" + s + "\' due to a " + throwable.getClass().getName() + " with message: " + throwable.getMessage());
        }

    }

    public static class NameIs extends ResolverUtil.ResourceTest {

        private final String name;

        public NameIs(String s) {
            this.name = "/" + s;
        }

        public boolean matches(URI uri) {
            return uri.getPath().endsWith(this.name);
        }

        public String toString() {
            return "named " + this.name;
        }
    }

    public static class AnnotatedWith extends ResolverUtil.ClassTest {

        private final Class annotation;

        public AnnotatedWith(Class oclass) {
            this.annotation = oclass;
        }

        public boolean matches(Class oclass) {
            return oclass != null && oclass.isAnnotationPresent(this.annotation);
        }

        public String toString() {
            return "annotated with @" + this.annotation.getSimpleName();
        }
    }

    public static class NameEndsWith extends ResolverUtil.ClassTest {

        private final String suffix;

        public NameEndsWith(String s) {
            this.suffix = s;
        }

        public boolean matches(Class oclass) {
            return oclass != null && oclass.getName().endsWith(this.suffix);
        }

        public String toString() {
            return "ends with the suffix " + this.suffix;
        }
    }

    public static class IsA extends ResolverUtil.ClassTest {

        private final Class parent;

        public IsA(Class oclass) {
            this.parent = oclass;
        }

        public boolean matches(Class oclass) {
            return oclass != null && this.parent.isAssignableFrom(oclass);
        }

        public String toString() {
            return "is assignable to " + this.parent.getSimpleName();
        }
    }

    public abstract static class ResourceTest implements ResolverUtil.Test {

        public boolean matches(Class oclass) {
            throw new UnsupportedOperationException();
        }

        public boolean doesMatchClass() {
            return false;
        }

        public boolean doesMatchResource() {
            return true;
        }
    }

    public abstract static class ClassTest implements ResolverUtil.Test {

        public boolean matches(URI uri) {
            throw new UnsupportedOperationException();
        }

        public boolean doesMatchClass() {
            return true;
        }

        public boolean doesMatchResource() {
            return false;
        }
    }

    public interface Test {

        boolean matches(Class oclass);

        boolean matches(URI uri);

        boolean doesMatchClass();

        boolean doesMatchResource();
    }
}
