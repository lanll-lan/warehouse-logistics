package com.warehouse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public void run(String... args) throws Exception {
        try {
            Resource resource = resourceLoader.getResource("classpath:sql/init.sql");
            if (resource.exists()) {
                ScriptUtils.executeSqlScript(dataSource.getConnection(), resource);
            }
        } catch (Exception e) {
            System.out.println("Database initialization skipped or already done: " + e.getMessage());
        }
    }
}
