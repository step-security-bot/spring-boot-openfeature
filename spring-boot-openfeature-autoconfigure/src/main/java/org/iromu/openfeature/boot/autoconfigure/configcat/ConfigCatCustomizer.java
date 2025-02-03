package org.iromu.openfeature.boot.autoconfigure.configcat;

import com.configcat.ConfigCatClient;

/**
 * Callback interface that can be used to customize ConfigCatClient with a
 * {@link ConfigCatClient.Options}.
 *
 * @author Ivan Rodriguez
 */
@FunctionalInterface
public interface ConfigCatCustomizer {

	/**
	 * Callback to customize a {@link ConfigCatClient.Options} instance.
	 * @param options ConfigCatClient Options to customize
	 */
	void customize(ConfigCatClient.Options options);

}
