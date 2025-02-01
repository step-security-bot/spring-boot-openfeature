package org.iromu.openfeature.boot.autoconfigure.unleash;

import io.getunleash.CustomHttpHeadersProvider;
import io.getunleash.UnleashContextProvider;
import io.getunleash.UnleashException;
import io.getunleash.event.UnleashSubscriber;
import io.getunleash.lang.Nullable;
import io.getunleash.repository.ToggleBootstrapProvider;
import io.getunleash.strategy.Strategy;
import io.getunleash.util.MetricSenderFactory;
import io.getunleash.util.UnleashFeatureFetcherFactory;
import io.getunleash.util.UnleashScheduledExecutor;
import io.getunleash.util.UnleashURLs;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.net.Proxy;
import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.function.Consumer;

import static org.iromu.openfeature.boot.autoconfigure.unleash.UnleashProperties.UNLEASH_PREFIX;

/**
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

	private final String environment;

	private final String instanceId;

	private final String sdkVersion;

	private final Resource backupFile;

	private final String clientSpecificationVersion;

	@Nullable
	private final String projectName;

	@Nullable
	private final String namePrefix;

	private final long fetchTogglesInterval;

	private final Duration fetchTogglesConnectTimeout;

	private final Duration fetchTogglesReadTimeout;

	private final boolean disablePolling;

	private final long sendMetricsInterval;

	private final Duration sendMetricsConnectTimeout;

	private final Duration sendMetricsReadTimeout;

	private final boolean disableMetrics;

	private final boolean isProxyAuthenticationByJvmProperties;

	private final UnleashFeatureFetcherFactory unleashFeatureFetcherFactory;

	private final MetricSenderFactory metricSenderFactory;

	private final UnleashContextProvider contextProvider;

	private final boolean synchronousFetchOnInitialisation;

	private final UnleashScheduledExecutor unleashScheduledExecutor;

	private final UnleashSubscriber unleashSubscriber;

	@Nullable
	private final Strategy fallbackStrategy;

	@Nullable
	private final ToggleBootstrapProvider toggleBootstrapProvider;

	@Nullable
	private final Proxy proxy;

	@Nullable
	private final Consumer<UnleashException> startupExceptionHandler;

}
