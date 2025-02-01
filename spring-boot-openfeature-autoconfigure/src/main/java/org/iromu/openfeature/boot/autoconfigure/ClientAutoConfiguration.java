package org.iromu.openfeature.boot.autoconfigure;

import dev.openfeature.sdk.Client;
import dev.openfeature.sdk.FeatureProvider;
import dev.openfeature.sdk.OpenFeatureAPI;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author Ivan Rodriguez
 */
@AutoConfiguration
@ConditionalOnBean(FeatureProvider.class)
@ConditionalOnMissingBean(name = "multiProvider")
public class ClientAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public Client client(FeatureProvider featureProvider, ObjectProvider<ClientCustomizer> customizers) {
		OpenFeatureAPI.getInstance().setProviderAndWait(featureProvider);
		Client client = OpenFeatureAPI.getInstance().getClient();
		customizers.orderedStream().forEach((customizer) -> customizer.customize(client));
		return client;
	}

}
