package com.gmail.alexejkrawez.entities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Testcontainers
public class DBContainer {

    public static final Logger logger = LogManager.getLogger(DBContainer.class);

    private static final String FILL_DB_SCRIPT = "FILL_DB.sql";

    @Container
    private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0.31-debian")
            .withDatabaseName("alexej_krawez_todo_db")
            .withUsername("deus")
            .withPassword("exmachina")
            .withInitScript(FILL_DB_SCRIPT);

    private static final String DB_PROPERTIES_PATH = Thread.currentThread().getContextClassLoader()
            .getResource("db.properties").getPath();

    private final Properties dbProperties = new Properties();


    public void overwriteURL() {
        try {
            dbProperties.load(new FileInputStream(DB_PROPERTIES_PATH));
            String regExString = "[0-9]{4,}";
            Pattern pat = Pattern.compile(regExString);
            Matcher mat = pat.matcher(MY_SQL_CONTAINER.getJdbcUrl());

            String dbUrl = dbProperties.getProperty("url");
            if (mat.find() && !dbUrl.contains(mat.group())) {
                dbUrl = dbUrl.replaceAll(regExString, mat.group());
                dbProperties.setProperty("url", dbUrl);
                dbProperties.store(new FileOutputStream(DB_PROPERTIES_PATH), null);
            }

        } catch (IOException e) {
            logger.error("Cannot load or save db.properties.");
            logger.error(e.getMessage(), e);
        }

    }
}
