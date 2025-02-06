package org.iromu.openfeature.boot.autoconfigure.configcat;

import dev.openfeature.contrib.providers.configcat.ConfigCatProviderConfig;
import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.FeatureProvider;
import org.iromu.openfeature.boot.autoconfigure.ClientAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.iromu.openfeature.boot.autoconfigure.configcat.ConfigCatProperties.CONFIGCAT_PREFIX;

class ConfigCatAutoConfigurationTest {

	public static final String[] REQUIRED = { CONFIGCAT_PREFIX + ".sdk-key=dummy",
			CONFIGCAT_PREFIX + ".filename=configcat/features.json" };

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(ClientAutoConfiguration.class, ConfigCatAutoConfiguration.class));

	@Test
	void shouldSupplyDefaultBeansWithOptionalClasspathFile() {
		this.contextRunner
			.withPropertyValues(CONFIGCAT_PREFIX + ".sdk-key=dummy",
					CONFIGCAT_PREFIX + ".filename=classpath:/configcat/features.json")
			.run((context) -> assertThat(context).hasSingleBean(ConfigCatProviderConfig.class)
				.hasBean("configCatProviderConfig")
				.hasSingleBean(FeatureProvider.class)
				.hasBean("configCatProvider")
				.hasSingleBean(Client.class)
				.hasBean("client"));
	}

	@Test
	void shouldUseFileRules() {
		this.contextRunner.withPropertyValues(REQUIRED)
			.run((context) -> assertThat(context.getBean(Client.class).getBooleanValue("enabledFeature", false))
				.isTrue());
	}

}
