package ai.cloud.gemini_demo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class GenAIChatResponse {
	
	private byte[] generatedImage;
	private String chatResponse;
	private String mimeType;
}
