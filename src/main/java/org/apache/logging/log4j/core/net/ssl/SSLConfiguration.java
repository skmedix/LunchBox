package org.apache.logging.log4j.core.net.ssl;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(
    name = "ssl",
    category = "Core",
    printObject = true
)
public class SSLConfiguration {

    private static final StatusLogger LOGGER = StatusLogger.getLogger();
    private KeyStoreConfiguration keyStoreConfig;
    private TrustStoreConfiguration trustStoreConfig;
    private SSLContext sslContext;

    private SSLConfiguration(KeyStoreConfiguration keystoreconfiguration, TrustStoreConfiguration truststoreconfiguration) {
        this.keyStoreConfig = keystoreconfiguration;
        this.trustStoreConfig = truststoreconfiguration;
        this.sslContext = null;
    }

    public SSLSocketFactory getSSLSocketFactory() {
        if (this.sslContext == null) {
            this.sslContext = this.createSSLContext();
        }

        return this.sslContext.getSocketFactory();
    }

    public SSLServerSocketFactory getSSLServerSocketFactory() {
        if (this.sslContext == null) {
            this.sslContext = this.createSSLContext();
        }

        return this.sslContext.getServerSocketFactory();
    }

    private SSLContext createSSLContext() {
        SSLContext sslcontext = null;

        try {
            sslcontext = this.createSSLContextBasedOnConfiguration();
            SSLConfiguration.LOGGER.debug("Creating SSLContext with the given parameters");
        } catch (TrustStoreConfigurationException truststoreconfigurationexception) {
            sslcontext = this.createSSLContextWithTrustStoreFailure();
        } catch (KeyStoreConfigurationException keystoreconfigurationexception) {
            sslcontext = this.createSSLContextWithKeyStoreFailure();
        }

        return sslcontext;
    }

    private SSLContext createSSLContextWithTrustStoreFailure() {
        SSLContext sslcontext;

        try {
            sslcontext = this.createSSLContextWithDefaultTrustManagerFactory();
            SSLConfiguration.LOGGER.debug("Creating SSLContext with default truststore");
        } catch (KeyStoreConfigurationException keystoreconfigurationexception) {
            sslcontext = this.createDefaultSSLContext();
            SSLConfiguration.LOGGER.debug("Creating SSLContext with default configuration");
        }

        return sslcontext;
    }

    private SSLContext createSSLContextWithKeyStoreFailure() {
        SSLContext sslcontext;

        try {
            sslcontext = this.createSSLContextWithDefaultKeyManagerFactory();
            SSLConfiguration.LOGGER.debug("Creating SSLContext with default keystore");
        } catch (TrustStoreConfigurationException truststoreconfigurationexception) {
            sslcontext = this.createDefaultSSLContext();
            SSLConfiguration.LOGGER.debug("Creating SSLContext with default configuration");
        }

        return sslcontext;
    }

    private SSLContext createSSLContextBasedOnConfiguration() throws KeyStoreConfigurationException, TrustStoreConfigurationException {
        return this.createSSLContext(false, false);
    }

    private SSLContext createSSLContextWithDefaultKeyManagerFactory() throws TrustStoreConfigurationException {
        try {
            return this.createSSLContext(true, false);
        } catch (KeyStoreConfigurationException keystoreconfigurationexception) {
            SSLConfiguration.LOGGER.debug("Exception occured while using default keystore. This should be a BUG");
            return null;
        }
    }

    private SSLContext createSSLContextWithDefaultTrustManagerFactory() throws KeyStoreConfigurationException {
        try {
            return this.createSSLContext(false, true);
        } catch (TrustStoreConfigurationException truststoreconfigurationexception) {
            SSLConfiguration.LOGGER.debug("Exception occured while using default truststore. This should be a BUG");
            return null;
        }
    }

    private SSLContext createDefaultSSLContext() {
        try {
            return SSLContext.getDefault();
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            SSLConfiguration.LOGGER.error("Failed to create an SSLContext with default configuration");
            return null;
        }
    }

    private SSLContext createSSLContext(boolean flag, boolean flag1) throws KeyStoreConfigurationException, TrustStoreConfigurationException {
        try {
            KeyManager[] akeymanager = null;
            TrustManager[] atrustmanager = null;
            SSLContext sslcontext = SSLContext.getInstance("SSL");

            if (!flag) {
                KeyManagerFactory keymanagerfactory = this.loadKeyManagerFactory();

                akeymanager = keymanagerfactory.getKeyManagers();
            }

            if (!flag1) {
                TrustManagerFactory trustmanagerfactory = this.loadTrustManagerFactory();

                atrustmanager = trustmanagerfactory.getTrustManagers();
            }

            sslcontext.init(akeymanager, atrustmanager, (SecureRandom) null);
            return sslcontext;
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
            SSLConfiguration.LOGGER.error("No Provider supports a TrustManagerFactorySpi implementation for the specified protocol");
            throw new TrustStoreConfigurationException(nosuchalgorithmexception);
        } catch (KeyManagementException keymanagementexception) {
            SSLConfiguration.LOGGER.error("Failed to initialize the SSLContext");
            throw new KeyStoreConfigurationException(keymanagementexception);
        }
    }

    private TrustManagerFactory loadTrustManagerFactory() throws TrustStoreConfigurationException {
        KeyStore keystore = null;
        TrustManagerFactory trustmanagerfactory = null;

        if (this.trustStoreConfig == null) {
            throw new TrustStoreConfigurationException(new Exception("The trustStoreConfiguration is null"));
        } else {
            try {
                keystore = this.trustStoreConfig.getTrustStore();
                trustmanagerfactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustmanagerfactory.init(keystore);
                return trustmanagerfactory;
            } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
                SSLConfiguration.LOGGER.error("The specified algorithm is not available from the specified provider");
                throw new TrustStoreConfigurationException(nosuchalgorithmexception);
            } catch (KeyStoreException keystoreexception) {
                SSLConfiguration.LOGGER.error("Failed to initialize the TrustManagerFactory");
                throw new TrustStoreConfigurationException(keystoreexception);
            } catch (StoreConfigurationException storeconfigurationexception) {
                throw new TrustStoreConfigurationException(storeconfigurationexception);
            }
        }
    }

    private KeyManagerFactory loadKeyManagerFactory() throws KeyStoreConfigurationException {
        KeyStore keystore = null;
        KeyManagerFactory keymanagerfactory = null;

        if (this.keyStoreConfig == null) {
            throw new KeyStoreConfigurationException(new Exception("The keyStoreConfiguration is null"));
        } else {
            try {
                keystore = this.keyStoreConfig.getKeyStore();
                keymanagerfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                keymanagerfactory.init(keystore, this.keyStoreConfig.getPasswordAsCharArray());
                return keymanagerfactory;
            } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
                SSLConfiguration.LOGGER.error("The specified algorithm is not available from the specified provider");
                throw new KeyStoreConfigurationException(nosuchalgorithmexception);
            } catch (KeyStoreException keystoreexception) {
                SSLConfiguration.LOGGER.error("Failed to initialize the TrustManagerFactory");
                throw new KeyStoreConfigurationException(keystoreexception);
            } catch (StoreConfigurationException storeconfigurationexception) {
                throw new KeyStoreConfigurationException(storeconfigurationexception);
            } catch (UnrecoverableKeyException unrecoverablekeyexception) {
                SSLConfiguration.LOGGER.error("The key cannot be recovered (e.g. the given password is wrong)");
                throw new KeyStoreConfigurationException(unrecoverablekeyexception);
            }
        }
    }

    public boolean equals(SSLConfiguration sslconfiguration) {
        if (sslconfiguration == null) {
            return false;
        } else {
            boolean flag = false;
            boolean flag1 = false;

            if (this.keyStoreConfig != null) {
                flag = this.keyStoreConfig.equals(sslconfiguration.keyStoreConfig);
            } else {
                flag = this.keyStoreConfig == sslconfiguration.keyStoreConfig;
            }

            if (this.trustStoreConfig != null) {
                flag1 = this.trustStoreConfig.equals(sslconfiguration.trustStoreConfig);
            } else {
                flag1 = this.trustStoreConfig == sslconfiguration.trustStoreConfig;
            }

            return flag && flag1;
        }
    }

    @PluginFactory
    public static SSLConfiguration createSSLConfiguration(@PluginElement("keyStore") KeyStoreConfiguration keystoreconfiguration, @PluginElement("trustStore") TrustStoreConfiguration truststoreconfiguration) {
        return new SSLConfiguration(keystoreconfiguration, truststoreconfiguration);
    }
}
