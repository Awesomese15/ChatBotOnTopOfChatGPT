package com.home.MyChatBot.controller;

import com.home.MyChatBot.config.OpenAIConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import java.util.HashMap;
import java.util.Map;

@RestController
public class ChatController {
    @Autowired
    private OpenAIConfig openAIConfig;

    @PostMapping("/chat")
    public String chat(@RequestBody String request) {
       // String message = request.get("message");
        return generateResponse(request);
    }

    private String generateResponse(String inputText) {
        //String apiUrl = "https://api.openai.com/v1/engines/gpt-3.5-turbo-16k-0613/completions";
        String apiUrl ="https://api.openai.com/v1/engines/text-davinci-002/completions";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new  HttpHeaders();
        headers.set("Authorization", "Bearer " + openAIConfig.getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("prompt", inputText);
        requestMap.put("max_tokens", 50); // Adjust as needed

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestMap, headers);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, Map.class);
        Map<String, Object> responseBody = responseEntity.getBody();

        if (responseBody != null) {
            return responseBody.get("choices").toString();
        } else {
            return "Sorry, I couldn't understand that.";
        }
    }
}

