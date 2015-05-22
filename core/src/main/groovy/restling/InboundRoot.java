package restling;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.*;

/**
 * Denotes that the annotated {@link org.restlet.routing.Router} is the entry-point of the Restling application.
 */
@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface InboundRoot {
}
