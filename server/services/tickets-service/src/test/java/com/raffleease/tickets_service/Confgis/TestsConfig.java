package com.raffleease.tickets_service.Confgis;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestsConfig {
    public static void loadEnvironment() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("./envs/env-tests")) {
            properties.load(fis);
            properties.forEach((key, value) -> System.setProperty(key.toString(), value.toString()));
        }
    }
}

