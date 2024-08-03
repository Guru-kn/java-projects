package ai.cloud.gemini_demo.service;

import java.util.concurrent.ExecutionException;

import ai.cloud.gemini_demo.bean.GenAIImageResponse;

public interface VertexAPIService {
	
	public GenAIImageResponse generateAIImage(String textForImgGeneration) throws InterruptedException, ExecutionException;
}
