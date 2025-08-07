package com.example.springIntegration.demo;

import org.springframework.stereotype.Component;

@Component
public class MyService {
    public String print(String name, String email) {
        return "Enriched " + name + " with email " + email;
    }
    public String print2(String name, String email) {
        return "Data " + name + " with source " + email;
    }
}