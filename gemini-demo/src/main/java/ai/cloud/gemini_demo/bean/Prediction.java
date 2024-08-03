package ai.cloud.gemini_demo.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Prediction {
	private String mimeType;
	private String bytesBase64Encoded;
}
