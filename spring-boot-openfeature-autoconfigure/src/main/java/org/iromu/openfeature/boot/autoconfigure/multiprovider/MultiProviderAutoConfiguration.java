package org.iromu.openfeature.boot.autoconfigure.multiprovider;

import dev.openfeature.contrib.providers.multiprovider.FirstMatchStrategy;
import dev.openfeature.contrib.providers.multiprovider.MultiProvider;
import dev.openfeature.contrib.providers.multiprovider.Strategy;
import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.FeatureProvider;
import dev.openfeature.sdk.OpenFeatureAPI;
import org.iromu.openfeature.boot.autoconfigure.ClientAutoConfiguration;
import org.iromu.openfeature.boot.autoconfigure.ClientCustomizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Autoconfiguration for {@link MultiProvider}.
 *
 * @author Ivan Rodriguez
 */
@AutoConfiguration
@AutoConfigureBefore(ClientAutoConfiguration.class)
@AutoConfigureAfter(FeatureProvider.class)
@ConditionalOnClass(MultiProvider.class)
public class MultiProviderAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public Strategy firstMatchStrategy() {
		return new FirstMatchStrategy();
	}

	@Bean
	@ConditionalOnMissingBean
	public MultiProvider multiProvider(ObjectProvider<FeatureProvider> providers, Strategy strategy) {
		return new MultiProvider(providers.orderedStream().toList(), strategy);
	}

	@Bean
	@ConditionalOnMissingBean
	public Client multiClient(MultiProvider multiProvider, ObjectProvider<ClientCustomizer> customizers) {
		OpenFeatureAPI.getInstance().setProviderAndWait(multiProvider);
		Client client = OpenFeatureAPI.getInstance().getClient();
		customizers.orderedStream().forEach((customizer) -> customizer.customize(client));
		return client;
	}

}
