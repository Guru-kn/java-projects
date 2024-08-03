package ai.cloud.gemini_demo.bean;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ImageGenAIRequest {
	
	private List<Instance> instances;
	private Parameter parameters;
}
