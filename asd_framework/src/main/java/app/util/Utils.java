package app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class Utils {
    public static Properties getProperties(String fullName) {

        String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();

        try {
            String fullPath = rootPath + "/" + fullName;
            File file = new File(fullPath);
            if (!file.exists()) {
                return null;
            }

            Properties prop = new Properties();
            // load the properties file
            prop.load(new FileInputStream(fullPath)); // get the property value

            return prop;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
