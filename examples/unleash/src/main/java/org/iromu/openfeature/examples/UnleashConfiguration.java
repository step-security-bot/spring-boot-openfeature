package org.iromu.openfeature.examples;

import dev.openfeature.contrib.providers.unleash.UnleashProvider;
import dev.openfeature.contrib.providers.unleash.UnleashProviderConfig;
import dev.openfeature.sdk.FeatureProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author Ivan Rodriguez
 */
@Configuration
@Slf4j
public class UnleashConfiguration {

	@SneakyThrows
	@Bean
	MockWebServer unleashApi() {
		MockWebServer mockWebServer = new MockWebServer();
		mockWebServer.start(54242);
		return mockWebServer;
	}

	@Bean
	@DependsOn("mockedUnleashApiServer")
	public FeatureProvider unleashProvider(UnleashProviderConfig unleashProviderConfig) {
		log.info("UnleashProvider init.");
		return new UnleashProvider(unleashProviderConfig);
	}

}
