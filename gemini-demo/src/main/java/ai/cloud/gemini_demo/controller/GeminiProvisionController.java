package ai.cloud.gemini_demo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseHandler;

@RestController
@RequestMapping("/gemini-pro-vision")
public class GeminiProvisionController {
	
	private final GenerativeModel generativeModel;
	
	public GeminiProvisionController(@Qualifier("geminiProVisionGenerativeModel") GenerativeModel generativeModel) {
		this.generativeModel = generativeModel;
	}
	
	@PostMapping("/ai-answer")
	public String file(@RequestParam("file")MultipartFile file, @RequestParam("question") String question) throws IOException{
		
		GenerateContentResponse generateContentResponse = this.generativeModel.generateContent(
				ContentMaker.fromMultiModalData(PartMaker.fromMimeTypeAndData(file.getContentType(), file.getBytes()), question));
		
		return ResponseHandler.getText(generateContentResponse);
	}
	
}
