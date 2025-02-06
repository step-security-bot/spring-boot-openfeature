package org.iromu.openfeature.boot.autoconfigure.flipt;

import dev.openfeature.contrib.providers.flipt.FliptProviderConfig;

/**
 * Callback interface that can be used to customize Flipt with a
 * {@link FliptProviderConfig.FliptProviderConfigBuilder}.
 *
 * @author Ivan Rodriguez
 */
@FunctionalInterface
public interface FliptCustomizer {

	/**
	 * Callback to customize a {@link FliptProviderConfig.FliptProviderConfigBuilder}
	 * instance.
	 * @param builder Flipt builder to customize
	 */
	void customize(FliptProviderConfig.FliptProviderConfigBuilder builder);

}
