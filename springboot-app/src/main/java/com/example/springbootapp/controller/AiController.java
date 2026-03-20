package com.example.springbootapp.controller;

import com.example.springbootapp.service.AiService;
import com.example.springbootapp.vo.AiChatRequest;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    @Resource
    private AiService aiService;

    @PostMapping("/chat")
    public Map<String, Object> chat(@Valid @RequestBody AiChatRequest request) {
        String answer = aiService.chat(request.getQuestion());
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "查询成功");
        result.put("data", answer);
        return result;
    }

}

