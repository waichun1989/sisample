package com.example.springIntegration.demo;

import com.example.springIntegration.demo.gateway.CustomerGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @Autowired
    private CustomerGateway customerGateway;

    @PostMapping("/onboard")
    public ResponseEntity<?> onboard(@RequestBody Object customer) {
        Object response = customerGateway.onboard(customer);
        return ResponseEntity.ok(response);
    }
}