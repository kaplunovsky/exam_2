package hooks;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//Метод для прослушивания конфигурационного файла
public class GetConfig {
    private static final String config_file_name = "/test.properties";
    private static final Properties properties;

    static {
        properties = new Properties();
        try (InputStream inputStream = GetConfig.class.getResourceAsStream(config_file_name)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file" + config_file_name, e);
        }
    }
    // Метод для получения значения свойств
    public static String getConfigurationValue(String key) {
        return ((System.getProperty(key) == null) ? properties.getProperty(key) : System.getProperty(key));
    }

// Метод на светлое будущее
    public GetConfig(){
    }

}
