package com.maintenance.pojo;

import com.maintenance.context.response.ResponseResult;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@RestController
@ResponseResult
public @interface BaseControllerAnnotation {
}
