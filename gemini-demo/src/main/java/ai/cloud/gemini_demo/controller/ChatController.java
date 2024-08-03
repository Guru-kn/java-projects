package ai.cloud.gemini_demo.controller;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.ResponseHandler;

import ai.cloud.gemini_demo.bean.GenAIChatResponse;
import ai.cloud.gemini_demo.service.GenAIService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ai-chat")
@RequiredArgsConstructor
public class ChatController {
	
	private final ChatSession chatSession;
	
	private final GenAIService genAIService;
	
	@GetMapping("/{text}")
	public String chat(@PathVariable String text) throws IOException{
		GenerateContentResponse generateContentResponse = this.chatSession.sendMessage(text);
		return ResponseHandler.getText(generateContentResponse);
	}
	
	@PostMapping("/{text}")
	public GenAIChatResponse generateAIImageFromText(@PathVariable String text) throws IOException, InterruptedException, ExecutionException{
		return this.genAIService.generateImageFromText(text);
	}
}
