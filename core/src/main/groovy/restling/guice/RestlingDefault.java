package restling.guice;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.*;

/**
 * Used as a key in Guice bindings to denote that this is the Restling default implementation.
 */
@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.TYPE)
public @interface RestlingDefault {
}
