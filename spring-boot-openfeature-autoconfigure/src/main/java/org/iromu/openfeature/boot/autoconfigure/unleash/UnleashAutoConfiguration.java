package org.iromu.openfeature.boot.autoconfigure.unleash;

import dev.openfeature.contrib.providers.unleash.UnleashProvider;
import dev.openfeature.contrib.providers.unleash.UnleashProviderConfig;
import dev.openfeature.sdk.FeatureProvider;
import io.getunleash.util.UnleashConfig;
import lombok.SneakyThrows;
import org.iromu.openfeature.boot.autoconfigure.ClientAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import static org.iromu.openfeature.boot.autoconfigure.unleash.UnleashProperties.UNLEASH_PREFIX;

/**
 * @author Ivan Rodriguez
 */
@AutoConfiguration
@AutoConfigureBefore(ClientAutoConfiguration.class)
@ConditionalOnClass({ UnleashProvider.class })
@ConditionalOnProperty(prefix = UNLEASH_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(UnleashProperties.class)
public class UnleashAutoConfiguration {

	@SneakyThrows
	@Bean
	@ConditionalOnMissingBean
	public UnleashProviderConfig unleashProviderConfig(ObjectProvider<UnleashCustomizer> customizers,
			UnleashProperties unleashProperties) {
		UnleashConfig.Builder unleashConfigBuilder = UnleashConfig.builder()
			.unleashAPI(unleashProperties.getUnleashAPI())
			.appName(unleashProperties.getAppName());

		if (unleashProperties.getUnleashToken() != null) {
			unleashConfigBuilder.customHttpHeader("Authorization", unleashProperties.getUnleashToken());
		}

		if (unleashProperties.getBackupFile() != null) {
			// NOTE: Unleash uses FileWriter
			unleashConfigBuilder.backupFile(unleashProperties.getBackupFile().getFile().getAbsolutePath());
		}

		customizers.orderedStream().forEach((customizer) -> customizer.customize(unleashConfigBuilder));

		return UnleashProviderConfig.builder().unleashConfigBuilder(unleashConfigBuilder).build();
	}

	@Bean
	@ConditionalOnMissingBean
	public FeatureProvider unleashProvider(UnleashProviderConfig unleashProviderConfig) {
		return new UnleashProvider(unleashProviderConfig);
	}

}
