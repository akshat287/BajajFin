package com.bajajfinserv;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Step 1: Send POST request to generate webhook
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", "John Doe");
        requestBody.put("regNo", "REG12347");
        requestBody.put("email", "john@example.com");

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        String webhookUrl = (String) response.getBody().get("webhook");
        String accessToken = (String) response.getBody().get("accessToken");

        System.out.println("Received Webhook URL: " + webhookUrl);
        System.out.println("Received Access Token: " + accessToken);

        // Step 2: Get question based on regNo (odd or even regNo)
        String question;
        if (Integer.parseInt("12347".substring(7)) % 2 == 0) {
            question = "Question 2";
        } else {
            question = "Question 1";
        }

        // Example SQL solution (just as an example, modify per actual question)
        String finalQuery = "SELECT * FROM students WHERE age > 18";

        // Step 3: Send final SQL query to the webhook
        HttpHeaders finalHeaders = new HttpHeaders();
        finalHeaders.setContentType(MediaType.APPLICATION_JSON);
        finalHeaders.set("Authorization", "Bearer " + accessToken);

        Map<String, String> queryBody = new HashMap<>();
        queryBody.put("finalQuery", finalQuery);

        HttpEntity<Map<String, String>> finalEntity = new HttpEntity<>(queryBody, finalHeaders);
        ResponseEntity<String> finalResponse = restTemplate.exchange(webhookUrl, HttpMethod.POST, finalEntity, String.class);

        System.out.println("Final Response: " + finalResponse.getBody());
    }
}
