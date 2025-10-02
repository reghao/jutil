package cn.reghao.jutil.web.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author reghao
 * @date 2023-08-10 10:59:20
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Constraint(
        validatedBy = {EnumValidator.class}
)
public @interface ValidEnum {
    Class<?> value();

    String message() default "枚举类型的值不正确";

    String method() default "getValue";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
