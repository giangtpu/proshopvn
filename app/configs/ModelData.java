package configs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE) //on class level
public @interface ModelData {
    String collection() default "";
    String mapCacheName() default "";
    int mapCacheTTL() default 24 * 3600; // 1 day
}
