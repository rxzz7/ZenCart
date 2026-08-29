package com.zencart.product_service;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FlywayChecker implements CommandLineRunner {

    private final ApplicationContext context;

    public FlywayChecker(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(String... args) {
        System.out.println("Flyway bean exists: " + context.containsBean("flyway"));
    }
}