package org.iromu.openfeature.boot.autoconfigure.jsonlogic;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

/**
 * @author Ivan Rodriguez
 */
@ConfigurationProperties(prefix = JsonlogicProperties.JSONLOGIC_PREFIX)
@Data
public class JsonlogicProperties {

	public static final String JSONLOGIC_PREFIX = "spring.openfeature.json-logic";

	private boolean enabled = true;

	private Resource filename;

}
