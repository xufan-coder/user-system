package com.zerody.user.constant;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CheckCompare {

    String name() default  "";

    String value() default "";

}