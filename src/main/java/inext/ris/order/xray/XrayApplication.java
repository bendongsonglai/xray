package inext.ris.order.xray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class XrayApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(XrayApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(XrayApplication.class, args);
	}
	@Bean
	public Executor executor() {
		LOGGER.debug("Creating Async Task Executor");
		final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("XrayThread-");
		executor.initialize();
		return executor;
	}
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
