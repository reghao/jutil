package cn.reghao.jutil.jdk.web.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 日期时间类型校验注解
 *
 * @author reghao
 * @date 2026-01-09 17:28:42
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {DateVerifyValidator.class}) // 指定自定义的校验器
public @interface DateVerify {

    // 提示信息
    String message() default "日期格式不正确";

    // 日期时间格式
    String dateFormat() default "yyyy-MM-dd HH:mm:ss";

    /**
     * 必须包含以下两个属性
     * 否则会报错 error msg: contains Constraint annotation, but does not contain a groups parameter.
     *
     * @return
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
