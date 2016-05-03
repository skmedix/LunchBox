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
    name = "trustStore",
    category = "Core",
    printObject = true
)
public class TrustStoreConfiguration extends StoreConfiguration {

    private KeyStore trustStore = null;
    private String trustStoreType = "JKS";

    public TrustStoreConfiguration(String s, String s1) {
        super(s, s1);
    }

    protected void load() throws StoreConfigurationException {
        KeyStore keystore = null;
        FileInputStream fileinputstream = null;

        TrustStoreConfiguration.LOGGER.debug("Loading truststore from file with params(location={})", new Object[] { this.getLocation()});

        try {
            if (this.getLocation() == null) {
                throw new IOException("The location is null");
            }

            keystore = KeyStore.getInstance(this.trustStoreType);
            fileinputstream = new FileInputStream(this.getLocation());
            keystore.load(fileinputstream, this.getPasswordAsCharArray());
        } catch (CertificateException certificateexception) {
            TrustStoreConfiguration.LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type {}", new Object[] { this.trustStoreType});
            throw new StoreConfigurationException(certificateexception);
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            TrustStoreConfiguration.LOGGER.error("The algorithm used to check the integrity of the keystore cannot be found");
            throw new StoreConfigurationException(nosuchalgorithmexception);
        } catch (KeyStoreException keystoreexception) {
            TrustStoreConfiguration.LOGGER.error((Object) keystoreexception);
            throw new StoreConfigurationException(keystoreexception);
        } catch (FileNotFoundException filenotfoundexception) {
            TrustStoreConfiguration.LOGGER.error("The keystore file({}) is not found", new Object[] { this.getLocation()});
            throw new StoreConfigurationException(filenotfoundexception);
        } catch (IOException ioexception) {
            TrustStoreConfiguration.LOGGER.error("Something is wrong with the format of the truststore or the given password: {}", new Object[] { ioexception.getMessage()});
            throw new StoreConfigurationException(ioexception);
        } finally {
            try {
                if (fileinputstream != null) {
                    fileinputstream.close();
                }
            } catch (Exception exception) {
                TrustStoreConfiguration.LOGGER.warn("Error closing {}", new Object[] { this.getLocation(), exception});
            }

        }

        this.trustStore = keystore;
        TrustStoreConfiguration.LOGGER.debug("Truststore successfully loaded with params(location={})", new Object[] { this.getLocation()});
    }

    public KeyStore getTrustStore() throws StoreConfigurationException {
        if (this.trustStore == null) {
            this.load();
        }

        return this.trustStore;
    }

    @PluginFactory
    public static TrustStoreConfiguration createTrustStoreConfiguration(@PluginAttribute("location") String s, @PluginAttribute("password") String s1) {
        return new TrustStoreConfiguration(s, s1);
    }
}
