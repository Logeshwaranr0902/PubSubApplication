package com.example.service1;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PubSubController {

    private final PubSubTemplate pubSubTemplate;

    public PubSubController(PubSubTemplate pubSubTemplate) {
        this.pubSubTemplate = pubSubTemplate;
    }
    @GetMapping("/publish")
    public String send(@RequestParam("msg") String message) {

        this.pubSubTemplate.publish("my-first-topic", message);

        return "Successfully sent: " + message;
    }
}
