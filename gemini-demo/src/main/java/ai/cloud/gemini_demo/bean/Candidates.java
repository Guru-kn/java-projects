package ai.cloud.gemini_demo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Candidates{
	class Content {
		private String role;
		
		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		class Parts {
			private String text;
		}
	}
}
