package com.example.springIntegration.demo;

import org.springframework.stereotype.Component;

@Component
public class MyService {

    public String process(Object input) {
        System.out.println("Processed: " + input);
        return "Done";
    }

    public String enrich(String name, String requestId) {
        return "Enriched " + name + " with ID " + requestId;
    }
}