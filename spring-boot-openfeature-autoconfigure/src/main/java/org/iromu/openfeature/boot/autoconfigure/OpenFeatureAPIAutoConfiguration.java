package org.iromu.openfeature.boot.autoconfigure;

import dev.openfeature.sdk.OpenFeatureAPI;
import lombok.extern.slf4j.Slf4j;
import org.iromu.openfeature.boot.autoconfigure.multiprovider.MultiProviderAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Autoconfiguration for {@link OpenFeatureAPI}.
 *
 * @author Ivan Rodriguez
 */
@AutoConfiguration
@AutoConfigureBefore({ ClientAutoConfiguration.class, MultiProviderAutoConfiguration.class })
@Slf4j
public class OpenFeatureAPIAutoConfiguration {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public OpenFeatureAPICustomizer loggerOpenFeatureAPICustomizer() {
		return openFeatureAPI -> {
			openFeatureAPI.onProviderReady(eventDetails -> log.warn("{} ready", eventDetails.getProviderName()));
			openFeatureAPI.onProviderError(eventDetails -> log.warn("{} error", eventDetails.getProviderName()));
			openFeatureAPI.onProviderStale(eventDetails -> log.warn("{} stale", eventDetails.getProviderName()));
			openFeatureAPI.onProviderConfigurationChanged(eventDetails -> {
				log.warn("{} configuration changed", eventDetails.getProviderName());
				for (String flag : eventDetails.getFlagsChanged()) {
					log.warn("CHANGE {} {}", flag, eventDetails.getEventMetadata().getString(flag));
				}
			});
		};
	}

	@Bean
	@ConditionalOnMissingBean
	public OpenFeatureAPI openFeatureAPI(ObjectProvider<OpenFeatureAPICustomizer> customizers) {
		OpenFeatureAPI instance = OpenFeatureAPI.getInstance();
		customizers.orderedStream().forEach((customizer) -> customizer.customize(instance));
		return instance;
	}

}
