package org.bukkit.craftbukkit.v1_8_R3.util;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MojangNameLookup {

    private static final Logger logger = LogManager.getFormatterLogger(MojangNameLookup.class);

    public static String lookupName(UUID id) {
        if (id == null) {
            return null;
        } else {
            InputStream inputStream = null;

            try {
                URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + id.toString().replace("-", ""));
                URLConnection connection = url.openConnection();

                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);
                connection.setUseCaches(false);
                inputStream = connection.getInputStream();
                String result = IOUtils.toString(inputStream, Charsets.UTF_8);
                Gson gson = new Gson();
                MojangNameLookup.Response response = (MojangNameLookup.Response) gson.fromJson(result, MojangNameLookup.Response.class);

                if (response != null && response.name != null) {
                    if (response.cause != null && response.cause.length() > 0) {
                        MojangNameLookup.logger.warn("Failed to lookup name from UUID: %s", new Object[] { response.errorMessage});
                        return null;
                    } else {
                        String s = response.name;

                        return s;
                    }
                } else {
                    MojangNameLookup.logger.warn("Failed to lookup name from UUID");
                    return null;
                }
            } catch (MalformedURLException malformedurlexception) {
                MojangNameLookup.logger.warn("Malformed URL in UUID lookup");
                return null;
            } catch (IOException ioexception) {
                IOUtils.closeQuietly(inputStream);
                return null;
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        }
    }

    private class Response {

        String errorMessage;
        String cause;
        String name;
    }
}
