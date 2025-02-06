package org.iromu.openfeature.boot.autoconfigure.flipt;

import dev.openfeature.contrib.providers.flipt.FliptProvider;
import dev.openfeature.contrib.providers.flipt.FliptProviderConfig;
import dev.openfeature.sdk.FeatureProvider;
import io.flipt.api.FliptClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.iromu.openfeature.boot.autoconfigure.ClientAutoConfiguration;
import org.iromu.openfeature.boot.autoconfigure.multiprovider.MultiProviderAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import static org.iromu.openfeature.boot.autoconfigure.flipt.FliptProperties.FLIPT_PREFIX;

/**
 * Autoconfiguration for {@link FliptProvider}.
 *
 * @author Ivan Rodriguez
 */
@AutoConfiguration
@AutoConfigureBefore({ ClientAutoConfiguration.class, MultiProviderAutoConfiguration.class })
@ConditionalOnClass({ FliptProvider.class })
@ConditionalOnProperty(prefix = FLIPT_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(FliptProperties.class)
@Slf4j
public class FliptAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public FliptProviderConfig fliptProviderConfig(ObjectProvider<FliptCustomizer> customizers,
			FliptProperties properties) {
		FliptClient.FliptClientBuilder fliptClientBuilder = FliptClient.builder()
			.url(properties.getBaseURL() != null ? StringUtils.removeEnd(properties.getBaseURL(), "/") : null)
			.timeout(properties.getTimeout())
			.headers(properties.getHeaders());

		FliptProviderConfig.FliptProviderConfigBuilder fliptProviderConfigBuilder = FliptProviderConfig.builder()
			.fliptClientBuilder(fliptClientBuilder)
			.namespace(properties.getNamespace());

		customizers.orderedStream().forEach((customizer) -> customizer.customize(fliptProviderConfigBuilder));

		return fliptProviderConfigBuilder.build();
	}

	@Bean
	@ConditionalOnMissingBean
	public FeatureProvider fliptProvider(FliptProviderConfig fliptProviderConfig) {
		return new FliptProvider(fliptProviderConfig);
	}

}
