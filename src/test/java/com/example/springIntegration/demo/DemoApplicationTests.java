package com.example.springIntegration.demo;

import com.example.springIntegration.demo.supporting.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

@SpringBootTest
class DemoApplicationTests {

//    @Autowired
//    private MessageChannel inputChannel;

    @Test
    void testCustomerFlow() {
//        Customer customer = new Customer("Alice", "alice@example.com");
//        inputChannel.send(MessageBuilder.withPayload(customer).build());
    }
}
