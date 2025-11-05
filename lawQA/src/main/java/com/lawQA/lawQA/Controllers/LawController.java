package com.lawQA.lawQA.Controllers;

import com.lawQA.lawQA.DTOs.LawRequest;
import com.lawQA.lawQA.DTOs.LawResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/law")
@CrossOrigin(origins = "http://localhost:4200")
public class LawController {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @PostMapping("/ask")
    public ResponseEntity<LawResponse> askQuestion(@RequestBody LawRequest request) {
        String geminiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";

        String prompt = "Law:\n" + request.getLawText() + "\n\nQuestion: " + request.getQuestion();

        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[] { Map.of("text", prompt) })
                }
        );

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", geminiApiKey);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(geminiUrl, entity, Map.class);

            String answer = ((Map)((Map)((Map) ((java.util.List) response.getBody().get("candidates")).get(0))
                    .get("content")).get("parts")).get("0").toString();

            return ResponseEntity.ok(new LawResponse(answer));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LawResponse("Error: " + e.getMessage()));}
}}
