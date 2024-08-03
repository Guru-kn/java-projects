package ai.cloud.gemini_demo.service;

import java.util.concurrent.ExecutionException;

import ai.cloud.gemini_demo.bean.GenAIChatResponse;

public interface GenAIService {
	
	public GenAIChatResponse generateImageFromText(String text) throws InterruptedException, ExecutionException;
}
