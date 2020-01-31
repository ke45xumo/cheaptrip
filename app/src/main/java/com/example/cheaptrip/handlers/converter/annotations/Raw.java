package com.example.cheaptrip.handlers.converter.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * This is Used to Identify how to handle Retrofit Responses.
 * The {@link com.example.cheaptrip.handlers.converter.MultiConverterFactory} takes care of
 * converting them into JAVA-Objects by selecting the Converter Based on the Annotation.
 *
 * In this case (Raw):
 *
 *      The Rest-API Response String will be returned as Raw String.
 *
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface Raw {
}
