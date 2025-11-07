package com.lawQA.lawQA.Controllers;

import com.lawQA.lawQA.DTOs.LawRequest;
import com.lawQA.lawQA.DTOs.LawResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/law")
@CrossOrigin(origins = "http://localhost:4200")
public class LawController {

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    @PostMapping("/ask")
    public ResponseEntity<LawResponse> askQuestion(@RequestBody LawRequest request) {
        String geminiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

        String prompt = "Law:\n" + request.getLawText() + "\n\nQuestion: " + request.getQuestion();

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-goog-api-key", geminiApiKey);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(geminiUrl, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new LawResponse("Error: Empty response from Gemini API"));
            }

            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
            if (candidates == null || candidates.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new LawResponse("Error: No candidates returned by Gemini API"));
            }

            Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
            List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
            String answer = parts != null && !parts.isEmpty()
                    ? (String) parts.get(0).get("text")
                    : "No answer generated.";

            return ResponseEntity.ok(new LawResponse(answer));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LawResponse("Error: " + e.getMessage()));
        }
    }
}
