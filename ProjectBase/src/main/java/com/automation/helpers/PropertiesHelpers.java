package com.automation.helpers;

import com.automation.utils.LanguageUtils;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Properties;

@Log4j2
@Data
public class PropertiesHelpers {
    private static Properties properties;
    private static String linkFile;
    private static FileInputStream file;
    private static String relPropertiesFilePathDefault = "src/main/resources/application.properties";

    public static Properties loadAllFiles() {
        LinkedList<String> files = new LinkedList<>();
        // Add tất cả file Properties vào đây theo mẫu
        files.add("src/main/resources/application.properties");

        try {
            properties = new Properties();

            for (String f : files) {
                Properties tempProp = new Properties();
                linkFile = Helpers.getCurrentDir() + f;
                file = new FileInputStream(linkFile);
                tempProp.load(file);
                properties.putAll(tempProp);
            }
            file.close();
            log.info("Loaded all properties files.");
            log.info(properties);
            return properties;
        } catch (IOException e) {
            log.info("Warning !! Can not Load All File.");
            return new Properties();
        }
    }

    public static String getValue(String key) {
        String keyValue = null;
        try {
            if (file == null && properties == null) {
                properties = new Properties();
                linkFile = Helpers.getCurrentDir() + relPropertiesFilePathDefault;
                file = new FileInputStream(linkFile);
                properties.load(file);
                file.close();
            }
            // Lấy giá trị từ file đã Set
            keyValue = properties.getProperty(key);
            return LanguageUtils.convertCharset_ISO_8859_1_To_UTF8(keyValue);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return keyValue;
        }
    }
}
