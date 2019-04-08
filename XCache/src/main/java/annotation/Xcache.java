package annotation;

import java.lang.annotation.*;

/**
 * @Auther: allanyang
 * @Date: 2019/4/8 20:35
 * @Description:
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Xcache {

    int DEFAULT_MAX_SIZE = 100;

}