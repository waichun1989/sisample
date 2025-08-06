package com.example.springIntegration.demo.component.data;

import lombok.Data;

import java.util.List;

@Data
public class ServiceActivatorConfig {
    private String bean;
    private String method;
    private List<ServiceActivatorArg> args;
}