package com.api.gateway.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {
    private static Logger logger = LoggerFactory.getLogger(AppProperties.class);
    private static final String FILE_LOAD_ERROR = "Error opening config file: ";
    private static final Properties AppProps = new Properties();

    public static Properties getProperties() {
        return AppProps;
    }

    public static String getProperty(String sPropertyName) {
        return AppProps.getProperty(sPropertyName);
    }

    private AppProperties() {
        throw new IllegalStateException("Utility class");
    }

    static {
        try {
            InputStream in = AppProperties.class.getResourceAsStream("/appconfig.properties");
            AppProps.load(in);
        } catch (Exception var8) {
            logger.error("Error opening config file: ", var8);
        }

        String externalProperties = System.getProperty("app.properties");

        try {
            InputStream exteralIn = new FileInputStream(externalProperties);

            try {
                AppProps.load(exteralIn);
            } catch (Throwable var5) {
                try {
                    exteralIn.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }

                throw var5;
            }

            exteralIn.close();
        } catch (NullPointerException var6) {
            logger.info("No external properties file");
        } catch (Exception var7) {
            logger.error("Error opening config file: ", var7);
        }

    }
}
