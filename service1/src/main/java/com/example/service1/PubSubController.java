package com.example.service1;

import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.tasks.v2.*;
import com.google.protobuf.ByteString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
public class PubSubController {

    private final PubSubTemplate pubSubTemplate;
    @Value("${SPRING_CLOUD_GCP_PROJECT_ID}")
    private String projectId;

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

    @PostMapping("/publish2")
    public void createCloudTask(@RequestParam("msg") String messagePayload) throws IOException {
        try (CloudTasksClient client = CloudTasksClient.create()) {
            String queuePath = QueueName.of(projectId, "us-central1", "my-worker-queue").toString();

            // 1. Define the Task
            Task.Builder taskBuilder = Task.newBuilder()
                    .setHttpRequest(HttpRequest.newBuilder()
                            .setUrl("https://receiver-app-40481741273.us-central1.run.app/receive-task") // MUST be absolute
                            .setHttpMethod(HttpMethod.POST)
                            .putHeaders("Content-Type", "application/json")
                            .setBody(ByteString.copyFromUtf8(messagePayload))
                            .build());

            // 2. Send it
            client.createTask(queuePath, taskBuilder.build());
            log.info("Task created and assigned to Service 2!");
        }
    }
}
