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

package org.iromu.openfeature.boot.aop;

import java.lang.reflect.Method;

import dev.openfeature.sdk.Client;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.stereotype.Component;

/**
 * Aspect to handle the conditional execution of methods or classes annotated with
 * {@link ToggleOnFlag}.
 *
 * <p>
 * This aspect evaluates the condition defined by the feature flag's key. If the condition
 * evaluates to {@code true}, the original method is executed. Otherwise, a fallback
 * method specified by the {@code orElse} attribute is invoked.
 * </p>
 *
 * @author Ivan Rodriguez
 */
@Aspect
@Component
@RequiredArgsConstructor
public class ToggleOnFlagAspect {

	private final Client client;

	/**
	 * Around advice to handle conditional execution based on the {@link ToggleOnFlag}
	 * annotation.
	 * @param joinPoint the join point representing the method being executed
	 * @return the result of the primary or fallback method execution
	 * @throws Throwable if any error occurs during method invocation
	 */
	@Around("@annotation(org.iromu.openfeature.boot.aop.ToggleOnFlag) || @within(org.iromu.openfeature.boot.aop.ToggleOnFlag)")
	public Object toggleOnFlagExecution(ProceedingJoinPoint joinPoint) throws Throwable {
		// Retrieve method and class level annotations
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		ToggleOnFlag methodAnnotation = method.getAnnotation(ToggleOnFlag.class);
		ToggleOnFlag classAnnotation = joinPoint.getTarget().getClass().getAnnotation(ToggleOnFlag.class);

		// Determine which annotation to use
		ToggleOnFlag toggleOnFlag = (methodAnnotation != null) ? methodAnnotation : classAnnotation;

		if (toggleOnFlag != null) {
			String key = toggleOnFlag.key();
			boolean condition = evaluateCondition(key);

			if (condition) {
				// Execute the original method if the condition is true
				return joinPoint.proceed();
			}
			else if (Strings.isNotEmpty(toggleOnFlag.orElse())) {
				// Invoke the fallback method specified in the `orElse` attribute
				String fallbackMethodName = toggleOnFlag.orElse();
				Object targetBean = joinPoint.getTarget();
				Method fallbackMethod = targetBean.getClass().getMethod(fallbackMethodName, method.getParameterTypes());
				return fallbackMethod.invoke(targetBean, joinPoint.getArgs());
			}
		}

		// Proceed normally if no annotation is found
		return joinPoint.proceed();
	}

	/**
	 * Evaluates the condition based on the provided feature flag key.
	 * @param key the key of the feature flag
	 * @return {@code true} if the feature flag is enabled, {@code false} otherwise
	 */
	private boolean evaluateCondition(String key) {
		return this.client.getBooleanValue(key, false);
	}

}
