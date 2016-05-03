package org.apache.logging.log4j.core.net.ssl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(
    name = "keyStore",
    category = "Core",
    printObject = true
)
public class KeyStoreConfiguration extends StoreConfiguration {

    private KeyStore keyStore = null;
    private String keyStoreType = "JKS";

    public KeyStoreConfiguration(String s, String s1) {
        super(s, s1);
    }

    protected void load() throws StoreConfigurationException {
        FileInputStream fileinputstream = null;

        KeyStoreConfiguration.LOGGER.debug("Loading keystore from file with params(location={})", new Object[] { this.getLocation()});

        try {
            if (this.getLocation() == null) {
                throw new IOException("The location is null");
            }

            fileinputstream = new FileInputStream(this.getLocation());
            KeyStore keystore = KeyStore.getInstance(this.keyStoreType);

            keystore.load(fileinputstream, this.getPasswordAsCharArray());
            this.keyStore = keystore;
        } catch (CertificateException certificateexception) {
            KeyStoreConfiguration.LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type {}", new Object[] { this.keyStoreType});
            throw new StoreConfigurationException(certificateexception);
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            KeyStoreConfiguration.LOGGER.error("The algorithm used to check the integrity of the keystore cannot be found");
            throw new StoreConfigurationException(nosuchalgorithmexception);
        } catch (KeyStoreException keystoreexception) {
            KeyStoreConfiguration.LOGGER.error((Object) keystoreexception);
            throw new StoreConfigurationException(keystoreexception);
        } catch (FileNotFoundException filenotfoundexception) {
            KeyStoreConfiguration.LOGGER.error("The keystore file({}) is not found", new Object[] { this.getLocation()});
            throw new StoreConfigurationException(filenotfoundexception);
        } catch (IOException ioexception) {
            KeyStoreConfiguration.LOGGER.error("Something is wrong with the format of the keystore or the given password");
            throw new StoreConfigurationException(ioexception);
        } finally {
            try {
                if (fileinputstream != null) {
                    fileinputstream.close();
                }
            } catch (IOException ioexception1) {
                ;
            }

        }

        KeyStoreConfiguration.LOGGER.debug("Keystore successfully loaded with params(location={})", new Object[] { this.getLocation()});
    }

    public KeyStore getKeyStore() throws StoreConfigurationException {
        if (this.keyStore == null) {
            this.load();
        }

        return this.keyStore;
    }

    @PluginFactory
    public static KeyStoreConfiguration createKeyStoreConfiguration(@PluginAttribute("location") String s, @PluginAttribute("password") String s1) {
        return new KeyStoreConfiguration(s, s1);
    }
}
