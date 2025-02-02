package org.iromu.openfeature.boot.autoconfigure.unleash;

import io.getunleash.CustomHttpHeadersProvider;
import io.getunleash.util.UnleashURLs;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.net.URI;
import java.time.Duration;
import java.util.Map;

import static org.iromu.openfeature.boot.autoconfigure.unleash.UnleashProperties.UNLEASH_PREFIX;

/**
 * Spring properties for {@link io.getunleash.util.UnleashConfig}
 *
 * @author Ivan Rodriguez
 */
@ConfigurationProperties(prefix = UNLEASH_PREFIX)
@Data
public class UnleashProperties {

	public static final String UNLEASH_PREFIX = "spring.openfeature.unleash";

	private boolean enabled = true;

	private final URI unleashAPI;

	private final String unleashToken;

	private final UnleashURLs unleashURLs;

	private final Map<String, String> customHttpHeaders;

	private final CustomHttpHeadersProvider customHttpHeadersProvider;

	private final String appName;

	private final String environment = "default";

	private final String instanceId;

	private final String sdkVersion;

	private final Resource backupFile;

	private final String clientSpecificationVersion;

	private final String projectName;

	private final String namePrefix;

	private final long fetchTogglesInterval = 10;

	private final Duration fetchTogglesConnectTimeout = Duration.ofSeconds(10);

	private final Duration fetchTogglesReadTimeout = Duration.ofSeconds(10);

	private final boolean disablePolling;

	private final long sendMetricsInterval = 60;

	private final Duration sendMetricsConnectTimeout = Duration.ofSeconds(10);

	private final Duration sendMetricsReadTimeout = Duration.ofSeconds(10);

	private final boolean disableMetrics;

	private final boolean isProxyAuthenticationByJvmProperties;

	private final boolean synchronousFetchOnInitialisation;

}
