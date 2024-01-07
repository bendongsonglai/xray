package inext.ris.order.xray.localService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
@Configuration
public class WebClientConfig {
    @Bean
    public WebClient localApiClient() {
        return WebClient.create("http://127.0.0.1:8093/api/v1");
    }

    @Bean
    public WebClient hapiApiClient() {
        return WebClient.create("http://127.0.0.1:8093/fhir/api/v1");
    }

    /*@Bean
    public WebClient portalApiClient() {
        return WebClient.create("http://172.16.60.65:9092/api/v1");
    }*/
}
