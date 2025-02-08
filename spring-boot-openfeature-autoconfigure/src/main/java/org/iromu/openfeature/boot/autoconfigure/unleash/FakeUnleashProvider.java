/*
 * Copyright 2025-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.iromu.openfeature.boot.autoconfigure.unleash;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiPredicate;

import dev.openfeature.contrib.providers.unleash.UnleashProvider;
import dev.openfeature.contrib.providers.unleash.UnleashProviderConfig;
import dev.openfeature.sdk.EvaluationContext;
import dev.openfeature.sdk.exceptions.GeneralError;
import io.getunleash.FakeUnleash;
import io.getunleash.MoreOperations;
import io.getunleash.UnleashContext;
import io.getunleash.variant.Variant;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Provider implementation for FakeUnleash.
 *
 * @author Ivan Rodriguez
 */
@Slf4j
public class FakeUnleashProvider extends UnleashProvider {

	private final AtomicBoolean isInitialized = new AtomicBoolean(false);

	@Getter
	private FakeUnleash unleash;

	/**
	 * Constructor.
	 * @param unleashProviderConfig the UnleashProviderConfig
	 */
	public FakeUnleashProvider(UnleashProviderConfig unleashProviderConfig) {
		super(null);
	}

	/**
	 * Initialize the provider.
	 * @param evaluationContext evaluation context
	 * @throws Exception on error
	 */
	@Override
	public void initialize(EvaluationContext evaluationContext) throws Exception {
		boolean initialized = this.isInitialized.getAndSet(true);
		if (initialized) {
			throw new GeneralError("already initialized");
		}

		this.unleash = new FakeUnleash();
		setUnleash(this.unleash);

		// Unleash is per definition ready after it is initialized.
		log.info("finished initializing fake provider");
	}

	public boolean isEnabled(String toggleName, boolean defaultSetting) {
		return this.unleash.isEnabled(toggleName, defaultSetting);
	}

	public boolean isEnabled(String toggleName, UnleashContext context,
			BiPredicate<String, UnleashContext> fallbackAction) {
		return this.unleash.isEnabled(toggleName, context, fallbackAction);
	}

	public boolean isEnabled(String toggleName, UnleashContext context, boolean defaultSetting) {
		return this.unleash.isEnabled(toggleName, context, defaultSetting);
	}

	public void disable(String... features) {
		this.unleash.disable(features);
	}

	public void enableAllExcept(String... excludedFeatures) {
		this.unleash.enableAllExcept(excludedFeatures);
	}

	public boolean isEnabled(String toggleName, BiPredicate<String, UnleashContext> fallbackAction) {
		return this.unleash.isEnabled(toggleName, fallbackAction);
	}

	public void disableAllExcept(String... excludedFeatures) {
		this.unleash.disableAllExcept(excludedFeatures);
	}

	public void setVariant(String t1, Variant a) {
		this.unleash.setVariant(t1, a);
	}

	public boolean isEnabled(String toggleName) {
		return this.unleash.isEnabled(toggleName);
	}

	public Variant getVariant(String toggleName, Variant defaultValue) {
		return this.unleash.getVariant(toggleName, defaultValue);
	}

	public Variant getVariant(String toggleName) {
		return this.unleash.getVariant(toggleName);
	}

	public void disableAll() {
		this.unleash.disableAll();
	}

	public void reset(String... features) {
		this.unleash.reset(features);
	}

	public void resetAll() {
		this.unleash.resetAll();
	}

	public Variant getVariant(String toggleName, UnleashContext context) {
		return this.unleash.getVariant(toggleName, context);
	}

	public Variant getVariant(String toggleName, UnleashContext context, Variant defaultValue) {
		return this.unleash.getVariant(toggleName, context, defaultValue);
	}

	public void enableAll() {
		this.unleash.enableAll();
	}

	public boolean isEnabled(String toggleName, UnleashContext context) {
		return this.unleash.isEnabled(toggleName, context);
	}

	public MoreOperations more() {
		return this.unleash.more();
	}

	public void enable(String... features) {
		this.unleash.enable(features);
	}

	public void toggle(String feature) {

		if (this.unleash.isEnabled(feature)) {
			this.unleash.disable(feature);
		}
		else {
			this.unleash.enable(feature);
		}
		log.debug("{} updated to {}", feature, this.unleash.isEnabled(feature));
	}

}
