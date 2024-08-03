package ai.cloud.gemini_demo.bean;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class GenAIImageResponse {
	
	private List<Prediction> predictions;
}
