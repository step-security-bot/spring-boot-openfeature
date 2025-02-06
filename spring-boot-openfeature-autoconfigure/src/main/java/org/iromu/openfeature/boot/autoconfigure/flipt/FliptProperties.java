package org.iromu.openfeature.boot.autoconfigure.flipt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring properties for
 * {@link dev.openfeature.contrib.providers.flipt.FliptProviderConfig}
 *
 * @author Ivan Rodriguez
 */
@ConfigurationProperties(prefix = FliptProperties.FLIPT_PREFIX)
@Data
public class FliptProperties {

	public static final String FLIPT_PREFIX = "spring.openfeature.flipt";

	private boolean enabled = true;

	private String namespace = "default";

	private String baseURL;

	private Map<String, String> headers = new HashMap<>();

	private Duration timeout = Duration.ofSeconds(60);

}
