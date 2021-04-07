package hu.alkfejl.model.DAO;

import java.io.IOException;
import java.util.Properties;

public class ConfigHelper {
    private static Properties props = new Properties();

    static{
        try {
            props.load(ConfigHelper.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            // TODO: logging
            e.printStackTrace();
        }

    }

    public static Properties getProps() {
        return props;
    }

    public static String getValue(String key) {
        return props.getProperty(key);
    }
}
