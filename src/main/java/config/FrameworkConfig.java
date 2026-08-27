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
            if (in != null){
                PROPERTIES.load(in);
            }
        } catch (IOException e) {
            throw new RuntimeException("framework.properties file is missing", e);
        }

        try (InputStream in = FrameworkConfig.class.getResourceAsStream(DB_CONFIG_FILE)) {
            if (in != null){
                DB_PROPERTIES.load(in);
            }
        } catch (IOException e) {
            throw new RuntimeException("dbconnect.properties file is missing", e);
        }
    }

    public static final String APP_URL = getValue("BASE_URL");
    public static final String DB_HOST = getValue("DB_HOST");
    public static final String DB_PORT = getValue("DB_PORT");
    public static final String DB_NAME = getValue("DB_NAME");
    public static final String DB_USERNAME = getValue("DB_USERNAME");
    public static final String DB_PASSWORD = getValue("DB_PASSWORD");
    public static final long DEFAULT_TIMEOUT = Long.parseLong(getValue("SELENIDE_TIMEOUT"));

    /**
     * Возвращает значение настройки, перебирая источники в фиксированном порядке:
     * переменная окружения → системное свойство (-D) → framework.properties → dbconnect.properties.
     * <p>
     * Порядок здесь — не оформление, а контракт, и на нём держится параметризация CI.
     * Внешние источники (env, -D) стоят ВЫШЕ файлов, поэтому значение, переданное
     * снаружи, перебивает закоммиченное. Из этого следует практическое:
     * <ul>
     *   <li>параметр Jenkins-джобы (BASE_URL) доезжает до тестов через environment
     *       и имеет силу, хотя тот же ключ лежит в framework.properties;</li>
     *   <li>секреты (DB_PASSWORD) приходят из Jenkins Credentials переменной окружения,
     *       и их не требуется хранить в репозитории вовсе;</li>
     *   <li>при обратном порядке (файлы выше env) параметризация джобы работать бы
     *       не могла: закоммиченное значение всегда выигрывало бы у переданного снаружи.</li>
     * </ul>
     * Отдельно от приоритета стоит ПРЕДУСЛОВИЕ: пока в статическом блоке не было
     * проверки {@code in != null}, отсутствие properties-файла роняло весь прогон
     * ещё до первого вызова этого метода — то есть цепочка источников не начинала
     * работать вовсе. Приоритет источников и момент их чтения — разные вещи.
     *
     * @throws IllegalStateException если ключ не найден ни в одном из источников
     */
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