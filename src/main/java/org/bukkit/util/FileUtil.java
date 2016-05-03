package org.bukkit.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtil {

    public static boolean copy(File inFile, File outFile) {
        if (!inFile.exists()) {
            return false;
        } else {
            FileChannel in = null;
            FileChannel out = null;

            try {
                in = (new FileInputStream(inFile)).getChannel();
                out = (new FileOutputStream(outFile)).getChannel();
                long pos = 0L;

                for (long size = in.size(); pos < size; pos += in.transferTo(pos, 10485760L, out)) {
                    ;
                }

                return true;
            } catch (IOException ioexception) {
                ;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }

                    if (out != null) {
                        out.close();
                    }
                } catch (IOException ioexception1) {
                    return false;
                }

            }

            return false;
        }
    }
}
