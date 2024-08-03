package ai.cloud.gemini_demo.Config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.GenerativeModel;

@Configuration(proxyBeanMethods = false)
public class GeminiConfig {
	
	@Bean
	public VertexAI vertexAI() {
		return new VertexAI("PROJECT_ID", "asia-south1");
	}
	
	@Bean
	public GenerativeModel geminiProVisionGenerativeModel(VertexAI vertexAI) {
		return new GenerativeModel("gemini-pro-vision", vertexAI);
	}
	
	@Bean
	public GenerativeModel geminiProGenerativeModel(VertexAI vertexAI) {
		return new GenerativeModel("gemini-pro", vertexAI);
	}
	
	@Bean
	public ChatSession chatSession(@Qualifier("geminiProGenerativeModel")GenerativeModel generativeModel) {
		return new ChatSession(generativeModel);
	}
}
