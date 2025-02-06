package org.iromu.openfeature.boot.autoconfigure;

import dev.openfeature.sdk.OpenFeatureAPI;

/**
 * Callback interface that can be used to customize {@link OpenFeatureAPI}.
 *
 * @author Ivan Rodriguez
 */
@FunctionalInterface
public interface OpenFeatureAPICustomizer {

	/**
	 * Callback to customize a {@link OpenFeatureAPI} instance.
	 * @param openFeatureAPI to customize
	 */
	void customize(OpenFeatureAPI openFeatureAPI);

}
