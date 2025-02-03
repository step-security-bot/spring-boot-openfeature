package org.iromu.openfeature.boot.autoconfigure.jsonlogic;

import dev.openfeature.contrib.providers.jsonlogic.FileBasedFetcher;
import dev.openfeature.contrib.providers.jsonlogic.JsonlogicProvider;
import dev.openfeature.contrib.providers.jsonlogic.RuleFetcher;
import dev.openfeature.sdk.EvaluationContext;
import dev.openfeature.sdk.FeatureProvider;
import io.github.jamsesso.jsonlogic.JsonLogic;
import lombok.SneakyThrows;
import org.iromu.openfeature.boot.autoconfigure.ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.Nullable;

import static org.iromu.openfeature.boot.autoconfigure.jsonlogic.JsonlogicProperties.JSONLOGIC_PREFIX;

/**
 * Autoconfiguration for {@link JsonlogicProvider}.
 *
 * @author Ivan Rodriguez
 */
@AutoConfiguration
@AutoConfigureBefore(ClientAutoConfiguration.class)
@ConditionalOnClass({ JsonlogicProvider.class })
@ConditionalOnProperty(prefix = JSONLOGIC_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(JsonlogicProperties.class)
public class JsonlogicAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public JsonLogic jsonLogic() {
		return new JsonLogic();
	}

	@SneakyThrows
	@Bean
	@ConditionalOnProperty(prefix = JSONLOGIC_PREFIX, name = "filename")
	@ConditionalOnMissingBean
	public RuleFetcher fileBasedFetcher(JsonlogicProperties properties) {
		return new FileBasedFetcher(properties.getFilename().getURI());
	}

	@Bean
	@ConditionalOnProperty(prefix = JSONLOGIC_PREFIX, name = "filename", matchIfMissing = true)
	@ConditionalOnMissingBean
	public RuleFetcher noopFetcher() {
		return new RuleFetcher() {
			@Override
			public void initialize(EvaluationContext evaluationContext) {

			}

			@Nullable
			@Override
			public String getRuleForKey(String s) {
				return null;
			}
		};
	}

	@Bean
	@ConditionalOnMissingBean
	public FeatureProvider jsonlogicProvider(JsonLogic logic, RuleFetcher fetcher) {
		return new JsonlogicProvider(logic, fetcher);
	}

}
