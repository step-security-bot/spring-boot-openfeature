package org.iromu.openfeature.boot.autoconfigure.envvar;

import dev.openfeature.contrib.providers.envvar.EnvVarProvider;
import dev.openfeature.contrib.providers.envvar.EnvironmentGateway;
import dev.openfeature.contrib.providers.envvar.EnvironmentKeyTransformer;
import dev.openfeature.sdk.FeatureProvider;
import org.iromu.openfeature.boot.autoconfigure.ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import static org.iromu.openfeature.boot.autoconfigure.envvar.EnvVarProperties.ENVVAR_PREFIX;

/**
 * @author Ivan Rodriguez
 */
@AutoConfiguration
@AutoConfigureBefore(ClientAutoConfiguration.class)
@ConditionalOnClass({ EnvVarProvider.class })
@ConditionalOnProperty(prefix = ENVVAR_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(EnvVarProperties.class)
public class EnvVarAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public EnvironmentGateway environmentGateway() {
		return System::getenv;
	}

	@Bean
	@ConditionalOnMissingBean
	public EnvironmentKeyTransformer environmentKeyTransformer() {
		return EnvironmentKeyTransformer.doNothing();
	}

	@Bean
	@ConditionalOnMissingBean
	public FeatureProvider envVarProvider(EnvironmentGateway environmentGateway,
			EnvironmentKeyTransformer environmentKeyTransformer) {
		return new EnvVarProvider(environmentGateway, environmentKeyTransformer);
	}

}
