package org.iromu.openfeature.boot.autoconfigure.configcat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

/**
 * @author Ivan Rodriguez
 */
@ConfigurationProperties(prefix = ConfigCatProperties.CONFIGCAT_PREFIX)
@Data
public class ConfigCatProperties {

	public static final String CONFIGCAT_PREFIX = "spring.openfeature.config-cat";

	private boolean enabled = true;

	private String sdkKey;

	private Resource filename;

}
