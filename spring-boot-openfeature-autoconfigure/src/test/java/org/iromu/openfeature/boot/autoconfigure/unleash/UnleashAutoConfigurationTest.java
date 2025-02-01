package org.iromu.openfeature.boot.autoconfigure.unleash;

import dev.openfeature.contrib.providers.unleash.UnleashProviderConfig;
import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.FeatureProvider;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.iromu.openfeature.boot.autoconfigure.ClientAutoConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.io.IOException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.iromu.openfeature.boot.autoconfigure.unleash.UnleashProperties.UNLEASH_PREFIX;

/**
 * Tests for {@link UnleashAutoConfiguration}.
 *
 * @author Ivan Rodriguez
 */
class UnleashAutoConfigurationTest {

	private static MockWebServer mockWebServer;

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(ClientAutoConfiguration.class, UnleashAutoConfiguration.class));
	static String[] requiredProperties;

	@BeforeAll
	public static void setUp() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();
		requiredProperties = new String[] { UNLEASH_PREFIX + ".appName=Foo",
				UNLEASH_PREFIX + ".unleashAPI=" + mockWebServer.url("/api") };
	}

	@AfterAll
	public static void tearDown() throws IOException {
		mockWebServer.shutdown();
	}

	@Test
	void shouldSupplyDefaultBeans() {
		mockWebServer.setDispatcher(new Dispatcher() {
			@Override
			public MockResponse dispatch(RecordedRequest request) {
				String path = request.getPath();
				if ("/api/client/register".equals(path)) {
					return new MockResponse().setResponseCode(200);
				}
				else if ("/api/client/features".equals(path)) {
					return new MockResponse().setBody("{\"version\":1,\"features\":[]}")
						.addHeader("Content-Type", "application/json")
						.setResponseCode(200);
				}
				else {
					return new MockResponse().setResponseCode(404);
				}
			}
		});

		this.contextRunner.withPropertyValues(requiredProperties).run((context) -> {
			assertThat(context).hasSingleBean(UnleashProviderConfig.class)
				.hasBean("unleashProviderConfig")
				.hasSingleBean(FeatureProvider.class)
				.hasBean("unleashProvider")
				.hasSingleBean(Client.class)
				.hasBean("client");

			Set<String> paths = Set.of(mockWebServer.takeRequest().getPath(), mockWebServer.takeRequest().getPath());
			assertThat(paths).containsExactlyInAnyOrder("/api/client/features", "/api/client/register");
		});
	}

}
