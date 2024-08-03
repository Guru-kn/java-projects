package ai.cloud.gemini_demo.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class SimpleCORSFilter implements WebMvcConfigurer {

	private final Logger log = LoggerFactory.getLogger(SimpleCORSFilter.class);

	public SimpleCORSFilter() {
		log.info("SimpleCORSFilter init");
	}

	@Override 
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "OPTION").allowedOrigins("*")
        .allowedHeaders("*");
    }

}
