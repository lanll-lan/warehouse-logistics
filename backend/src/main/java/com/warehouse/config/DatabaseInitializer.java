package com.warehouse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${warehouse.db.init-mode:none}")
    private String initMode;

    @Override
    public void run(String... args) throws Exception {
        String mode = initMode == null ? "none" : initMode.trim().toLowerCase();

        switch (mode) {
            case "full":
                executeScript("classpath:sql/init.sql");
                break;
            case "seed":
                executeScript("classpath:sql/seed.sql");
                break;
            case "none":
            default:
                System.out.println("Database initialization skipped (init-mode=" + mode + ")");
                break;
        }
    }

    private void executeScript(String location) throws Exception {
        Resource resource = resourceLoader.getResource(location);
        if (resource.exists()) {
            System.out.println("Executing database script: " + location);
            ScriptUtils.executeSqlScript(dataSource.getConnection(), resource);
            System.out.println("Database script executed successfully: " + location);
        } else {
            System.out.println("Database script not found: " + location);
        }
    }
}