package com.atg.openssp.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.atg.openssp.common.configuration.ContextProperties;

/**
 * @author André Schmer
 *
 */
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface RuntimeConfig {

	ContextProperties type();

}
