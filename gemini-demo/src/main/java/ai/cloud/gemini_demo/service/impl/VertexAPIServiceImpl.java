package ai.cloud.gemini_demo.service.impl;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ai.cloud.gemini_demo.bean.GenAIImageResponse;
import ai.cloud.gemini_demo.bean.ImageGenAIRequest;
import ai.cloud.gemini_demo.bean.Instance;
import ai.cloud.gemini_demo.bean.Parameter;
import ai.cloud.gemini_demo.service.VertexAPIService;
import reactor.core.publisher.Mono;

@Service
public class VertexAPIServiceImpl implements VertexAPIService{
	
	String AUTH_TOKEN = "";
	
	String IMAGE_GENERATION_URL = "https://us-central1-aiplatform.googleapis.com/v1/projects/gemini-test-429108/locations/us-central1/publishers/google/models/imagegeneration:predict";
	
	private final WebClient webClient;
	
	public VertexAPIServiceImpl(@Qualifier("webClientWithMaxSizeBuffer") WebClient.Builder builder) {
		this.webClient = builder.build();
	}
	
	@Override
	public GenAIImageResponse generateAIImage(String textForImgGeneration) throws InterruptedException, ExecutionException {
		
		ImageGenAIRequest imageGenAIRequest = new ImageGenAIRequest();
		
		Instance instance = new Instance();
		instance.setPrompt(textForImgGeneration);
		
		imageGenAIRequest.setInstances(List.of(instance));
		imageGenAIRequest.setParameters(new Parameter(1));
		
		Mono<GenAIImageResponse> aiImageResponseMono = this.webClient.post()
        .uri(IMAGE_GENERATION_URL)
        .body(Mono.just(imageGenAIRequest), ImageGenAIRequest.class)
        .headers(headers -> headers.setBearerAuth(AUTH_TOKEN))
        .retrieve()
        .bodyToMono(GenAIImageResponse.class);
		
		return aiImageResponseMono.toFuture().get();
	}
}
