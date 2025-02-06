package org.iromu.openfeature.boot.autoconfigure.configcat;

import com.configcat.OverrideBehaviour;
import com.configcat.OverrideDataSourceBuilder;
import dev.openfeature.contrib.providers.configcat.ConfigCatProvider;
import dev.openfeature.contrib.providers.configcat.ConfigCatProviderConfig;
import dev.openfeature.sdk.FeatureProvider;
import lombok.extern.slf4j.Slf4j;
import org.iromu.openfeature.boot.autoconfigure.ClientAutoConfiguration;
import org.iromu.openfeature.boot.autoconfigure.multiprovider.MultiProviderAutoConfiguration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static com.configcat.OverrideDataSourceBuilder.classPathResource;
import static org.iromu.openfeature.boot.autoconfigure.configcat.ConfigCatProperties.CONFIGCAT_PREFIX;

/**
 * Autoconfiguration for {@link ConfigCatProvider}.
 *
 * @author Ivan Rodriguez
 */
@AutoConfiguration
@AutoConfigureBefore({ ClientAutoConfiguration.class, MultiProviderAutoConfiguration.class })
@ConditionalOnClass({ ConfigCatProvider.class })
@ConditionalOnProperty(prefix = CONFIGCAT_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(ConfigCatProperties.class)
@Slf4j
public class ConfigCatAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public ConfigCatProviderConfig configCatProviderConfig(ObjectProvider<ConfigCatCustomizer> customizers,
			ConfigCatProperties properties) {

		ConfigCatProviderConfig.ConfigCatProviderConfigBuilder builder = ConfigCatProviderConfig.builder();

		Resource filename = properties.getFilename();
		if (filename != null) {
			if (filename instanceof ClassPathResource) {
				builder.options(options -> options.flagOverrides(
						classPathResource(((ClassPathResource) filename).getPath()), OverrideBehaviour.LOCAL_ONLY));
			}
			else {
				builder.options(options -> {
					try {
						options.flagOverrides(OverrideDataSourceBuilder.localFile(filename.getURI().getPath(), true),
								OverrideBehaviour.LOCAL_ONLY);
					}
					catch (IOException e) {
						log.error(e.getMessage(), e);
					}
				});
			}
		}

		customizers.orderedStream().forEach((customizer) -> builder.options(customizer::customize));
		return builder.sdkKey(properties.getSdkKey()).build();
	}

	@Bean
	@ConditionalOnMissingBean
	public FeatureProvider configCatProvider(ConfigCatProviderConfig configCatProviderConfig) {
		return new ConfigCatProvider(configCatProviderConfig);
	}

}
