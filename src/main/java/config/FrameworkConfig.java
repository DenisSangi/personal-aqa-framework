package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FrameworkConfig {

    protected static Properties PROPERTIES;
    protected static Properties DB_PROPERTIES;
    private static final String CONFIG_FILE = "/framework.properties";
    private static final String DB_CONFIG_FILE = "/dbconnect.properties";

    static {
        PROPERTIES = new Properties();
        DB_PROPERTIES = new Properties();

        try (InputStream in = FrameworkConfig.class.getResourceAsStream(CONFIG_FILE)) {
            PROPERTIES.load(in);
        } catch (IOException e) {
            System.out.println("framework.properties file is missing");
            throw new RuntimeException(e);
        }

        try (InputStream in = FrameworkConfig.class.getResourceAsStream(DB_CONFIG_FILE)) {
            DB_PROPERTIES.load(in);
        } catch (IOException e) {
            System.out.println("dbconnect.properties file is missing");
            throw new RuntimeException(e);
        }
    }

    public static final String APP_URL = getValue("BASE_URL");
    public static final String APP_USERNAME = getValue("USERNAME");
    public static final String APP_USER_EMAIL = getValue("USER_EMAIL");
    public static final String APP_USER_PASSWORD = getValue("USER_PASSWORD");
    public static final String DB_HOST = getValue("DB_HOST");
    public static final String DB_PORT = getValue("DB_PORT");
    public static final String DB_NAME = getValue("DB_NAME");
    public static final String DB_USERNAME = getValue("DB_USERNAME");
    public static final String DB_PASSWORD = getValue("DB_PASSWORD");

    public static String getValue(String key) {
        String envValue = System.getenv(key);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue;
        }

        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.trim().isEmpty()) {
            return systemValue;
        }

        String propertiesValue = PROPERTIES.getProperty(key);
        if (propertiesValue != null) {
            return propertiesValue;
        }

        String dbPropertiesValue = DB_PROPERTIES.getProperty(key);
        if (dbPropertiesValue != null) {
            return dbPropertiesValue;
        }

        throw new IllegalStateException("Missing required configuration for: " + key);
    }
}