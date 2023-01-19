package com.bizzman.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesManager.class);

    public static Properties readProperties(Class<?> clasz, String fileName) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = clasz.getClassLoader().getResourceAsStream(fileName);
            properties.load(inputStream);

        } catch(IOException e) {
            logger.error("Could not find a file {} containing the properties for JWT!", fileName);
        }
        return properties;
    }
}
