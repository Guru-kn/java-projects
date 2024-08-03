package ai.cloud.gemini_demo.service.impl;

import java.util.Base64;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ai.cloud.gemini_demo.bean.GenAIChatResponse;
import ai.cloud.gemini_demo.bean.GenAIImageResponse;
import ai.cloud.gemini_demo.bean.Prediction;
import ai.cloud.gemini_demo.service.GenAIService;
import ai.cloud.gemini_demo.service.VertexAPIService;

@Service
public class GenAIServiceImpl implements GenAIService{
	
	@Autowired
	private VertexAPIService vertexAPIService;
	
	@Override
	public GenAIChatResponse generateImageFromText(String text) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		
		GenAIImageResponse aiImageResponse = vertexAPIService.generateAIImage(text);
		Prediction prediction = null;
		GenAIChatResponse aiChatResponse = null;
		
		if(null != aiImageResponse && !aiImageResponse.getPredictions().isEmpty()) {
			prediction = aiImageResponse.getPredictions().get(0);
			aiChatResponse = new GenAIChatResponse(Base64.getDecoder().decode(prediction.getBytesBase64Encoded()), null, prediction.getMimeType());
			return aiChatResponse;
		}
		
		return aiChatResponse = new GenAIChatResponse(Base64.getDecoder().decode(""), null, "");
	}
}
