package org.iromu.openfeature.boot.autoconfigure;

import dev.openfeature.sdk.Client;

/**
 * Callback interface that can be used to customize {@link Client}.
 *
 * @author Ivan Rodriguez
 */
@FunctionalInterface
public interface ClientCustomizer {

	/**
	 * Callback to customize a {@link Client} instance.
	 * @param client to customize
	 */
	void customize(Client client);

}
