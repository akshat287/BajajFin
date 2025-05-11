package com.example.bajajfinserv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class BajajFinservHealthQualifierApp {

    public static void main(String[] args) {
        SpringApplication.run(BajajFinservHealthQualifierApp.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommandLineRunner commandLineRunner(RestTemplate restTemplate) {
        return args -> {
            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String body = "{\"name\":\"John Doe\",\"regNo\":\"REG12347\",\"email\":\"john@example.com\"}";
            HttpEntity<String> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(response.getBody());
            String webhook = json.get("webhook").asText();
            String token = json.get("accessToken").asText();
            String regNo = "REG12347";
            String lastTwo = regNo.substring(regNo.length() - 2);
            int num = Integer.parseInt(lastTwo);
            String query = "SELECT p.AMOUNT AS SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, FLOOR(DATEDIFF('2025-05-11', e.DOB) / 365.25) AS AGE, d.DEPARTMENT_NAME FROM PAYMENTS p JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID WHERE DAY(p.PAYMENT_TIME) != 1 AND p.AMOUNT = (SELECT MAX(p2.AMOUNT) FROM PAYMENTS p2 WHERE DAY(p2.PAYMENT_TIME) != 1)";
            String submitUrl = "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA";
            HttpHeaders submitHeaders = new HttpHeaders();
            submitHeaders.setContentType(MediaType.APPLICATION_JSON);
            submitHeaders.set("Authorization", "Bearer " + token);
            String submitBody = "{\"finalQuery\":\"" + query.replace("\"", "\\\"") + "\"}";
            HttpEntity<String> submitRequest = new HttpEntity<>(submitBody, submitHeaders);
            restTemplate.exchange(submitUrl, HttpMethod.POST, submitRequest, String.class);
            System.out.println("Done!");
        };
    }
}
