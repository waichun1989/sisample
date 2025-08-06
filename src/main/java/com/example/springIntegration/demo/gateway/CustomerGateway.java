package com.example.springIntegration.demo.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface CustomerGateway {
    @Gateway(requestChannel = "httpInboundChannel", replyTimeout = 10_000)
    Object onboard(Object customer);
}