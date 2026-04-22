package com.example.service1;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PubSubController {

    private final PubSubTemplate pubSubTemplate;

    public PubSubController(PubSubTemplate pubSubTemplate) {
        this.pubSubTemplate = pubSubTemplate;
    }
    @GetMapping("/publish")
    public String publish(@RequestParam("msg") String message) {
        try {
            String id = pubSubTemplate.publish("my-first-topic", message).get();
            log.info("Successfully published message to GCP. ID: {}", id);
            return "Success! ID: " + id;
        } catch (Exception e) {
            log.error("Failed to publish message to Pub/Sub: ", e);
            return "Error occurred while publishing. Check server logs.";
        }
    }
}
