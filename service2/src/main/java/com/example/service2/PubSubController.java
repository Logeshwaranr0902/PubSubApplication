package com.example.service2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@Slf4j
public class PubSubController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/receive")
    public ResponseEntity<String> receive(@RequestBody String requestBody) {
        try {

            JsonNode node = objectMapper.readTree(requestBody);


            String base64Data = node.path("message").path("data").asText();


            String decodedMessage = new String(Base64.getDecoder().decode(base64Data));

            log.info("MNC Service 2 received message: {}", decodedMessage);


            return ResponseEntity.ok("Message Processed");

        } catch (Exception e) {
            log.error("Error decoding Pub/Sub message: ", e);

            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }

    @PostMapping("/receive-task")
    public ResponseEntity<String> getTask(@RequestBody String messagePayload) {
        try {
            // Your logic here
            log.info("Message received: {}", messagePayload);

            // Return 200 ONLY after the work is successfully finished
            return ResponseEntity.ok("Success");

        } catch (Exception e) {
            log.error("Task processing failed: ", e);


            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}