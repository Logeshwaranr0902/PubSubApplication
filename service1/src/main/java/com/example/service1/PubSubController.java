package com.example.service1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PubSubController {

    @GetMapping("/hello")
    public String getString() {

        return "Hello World!";
    }
}
