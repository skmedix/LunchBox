package org.apache.logging.log4j.core.helpers;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

public final class FileUtils {

    private static final String PROTOCOL_FILE = "file";
    private static final String JBOSS_FILE = "vfsfile";
    private static final Logger LOGGER = StatusLogger.getLogger();

    public static File fileFromURI(URI uri) {
        if (uri != null && (uri.getScheme() == null || "file".equals(uri.getScheme()) || "vfsfile".equals(uri.getScheme()))) {
            if (uri.getScheme() == null) {
                try {
                    uri = (new File(uri.getPath())).toURI();
                } catch (Exception exception) {
                    FileUtils.LOGGER.warn("Invalid URI " + uri);
                    return null;
                }
            }

            try {
                return new File(URLDecoder.decode(uri.toURL().getFile(), "UTF8"));
            } catch (MalformedURLException malformedurlexception) {
                FileUtils.LOGGER.warn("Invalid URL " + uri, (Throwable) malformedurlexception);
            } catch (UnsupportedEncodingException unsupportedencodingexception) {
                FileUtils.LOGGER.warn("Invalid encoding: UTF8", (Throwable) unsupportedencodingexception);
            }

            return null;
        } else {
            return null;
        }
    }

    public static boolean isFile(URL url) {
        return url != null && (url.getProtocol().equals("file") || url.getProtocol().equals("vfsfile"));
    }

    public static void mkdir(File file, boolean flag) throws IOException {
        if (!file.exists()) {
            if (!flag) {
                throw new IOException("The directory " + file.getAbsolutePath() + " does not exist.");
            }

            if (!file.mkdirs()) {
                throw new IOException("Could not create directory " + file.getAbsolutePath());
            }
        }

        if (!file.isDirectory()) {
            throw new IOException("File " + file + " exists and is not a directory. Unable to create directory.");
        }
    }
}
