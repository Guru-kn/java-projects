package ai.cloud.gemini_demo.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Bean(name = "webClientBuilder")
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
    
    @Bean(name = "webClientWithMaxSizeBuffer")
    public WebClient.Builder webClient() {
        final int size = 16 * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
            .build();
        return WebClient.builder()
            .exchangeStrategies(strategies);
    }
}
