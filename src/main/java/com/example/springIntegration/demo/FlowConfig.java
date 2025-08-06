package com.example.springIntegration.demo;

import com.example.springIntegration.demo.component.data.ServiceActivatorConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "integration")
@Getter
@Setter
public class FlowConfig {

    private List<Map<String, String>> flow;
    private List<ServiceActivatorConfig> serviceActivators;
}
