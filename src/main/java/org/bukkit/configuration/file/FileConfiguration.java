package org.bukkit.configuration.file;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemoryConfiguration;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public abstract class FileConfiguration extends MemoryConfiguration {

    /** @deprecated */
    @Deprecated
    public static final boolean UTF8_OVERRIDE;
    /** @deprecated */
    @Deprecated
    public static final boolean UTF_BIG;
    /** @deprecated */
    @Deprecated
    public static final boolean SYSTEM_UTF;

    static {
        byte[] testBytes = Base64Coder.decode("ICEiIyQlJicoKSorLC0uLzAxMjM0NTY3ODk6Ozw9Pj9AQUJDREVGR0hJSktMTU5PUFFSU1RVVldYWVpbXF1eX2BhYmNkZWZnaGlqa2xtbm9wcXJzdHV2d3h5ent8fX4NCg==");
        Charset defaultCharset = Charset.defaultCharset();
        String resultString = new String(testBytes, defaultCharset);
        boolean trueUTF = defaultCharset.name().contains("UTF");

        UTF8_OVERRIDE = !" !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\r\n".equals(resultString) || defaultCharset.equals(Charset.forName("US-ASCII"));
        SYSTEM_UTF = trueUTF || FileConfiguration.UTF8_OVERRIDE;
        UTF_BIG = trueUTF && FileConfiguration.UTF8_OVERRIDE;
    }

    public FileConfiguration() {}

    public FileConfiguration(Configuration defaults) {
        super(defaults);
    }

    public void save(File file) throws IOException {
        Validate.notNull(file, "File cannot be null");
        Files.createParentDirs(file);
        String data = this.saveToString();
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), FileConfiguration.UTF8_OVERRIDE && !FileConfiguration.UTF_BIG ? Charsets.UTF_8 : Charset.defaultCharset());

        try {
            writer.write(data);
        } finally {
            writer.close();
        }

    }

    public void save(String file) throws IOException {
        Validate.notNull(file, "File cannot be null");
        this.save(new File(file));
    }

    public abstract String saveToString();

    public void load(File file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        Validate.notNull(file, "File cannot be null");
        FileInputStream stream = new FileInputStream(file);

        this.load((Reader) (new InputStreamReader(stream, FileConfiguration.UTF8_OVERRIDE && !FileConfiguration.UTF_BIG ? Charsets.UTF_8 : Charset.defaultCharset())));
    }

    /** @deprecated */
    @Deprecated
    public void load(InputStream stream) throws IOException, InvalidConfigurationException {
        Validate.notNull(stream, "Stream cannot be null");
        this.load((Reader) (new InputStreamReader(stream, FileConfiguration.UTF8_OVERRIDE ? Charsets.UTF_8 : Charset.defaultCharset())));
    }

    public void load(Reader reader) throws IOException, InvalidConfigurationException {
        BufferedReader input = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
        StringBuilder builder = new StringBuilder();

        String line;

        try {
            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        } finally {
            input.close();
        }

        this.loadFromString(builder.toString());
    }

    public void load(String file) throws FileNotFoundException, IOException, InvalidConfigurationException {
        Validate.notNull(file, "File cannot be null");
        this.load(new File(file));
    }

    public abstract void loadFromString(String s) throws InvalidConfigurationException;

    protected abstract String buildHeader();

    public FileConfigurationOptions options() {
        if (this.options == null) {
            this.options = new FileConfigurationOptions(this);
        }

        return (FileConfigurationOptions) this.options;
    }
}
