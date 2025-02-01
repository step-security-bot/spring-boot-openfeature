package org.iromu.openfeature.boot.autoconfigure.envvar;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ivan Rodriguez
 */
@ConfigurationProperties(prefix = EnvVarProperties.ENVVAR_PREFIX)
@Data
public class EnvVarProperties {

	public static final String ENVVAR_PREFIX = "spring.openfeature.env-var";

	private boolean enabled = true;

}
