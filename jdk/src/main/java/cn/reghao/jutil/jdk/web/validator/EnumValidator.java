package cn.reghao.jutil.jdk.web.validator;

import cn.reghao.jutil.jdk.clazz.ClassUtil;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author reghao
 * @date 2023-08-10 10:59:34
 */
public class EnumValidator implements ConstraintValidator<ValidEnum, Object> {
    private final List<Object> values = new ArrayList();

    public EnumValidator() {
    }

    public void initialize(ValidEnum constraintAnnotation) {
        Class<?> enumClazz = constraintAnnotation.value();
        Object[] enumConstants = enumClazz.getEnumConstants();
        if (enumConstants != null) {
            Method method = ClassUtil.findMethod(enumClazz, constraintAnnotation.method(), new Class[0]);
            if (method == null) {
                System.out.printf("枚举对象:[%s]中不存在方法:[%s],请检查.\n", enumClazz.getName(), constraintAnnotation.method());
            } else {
                method.setAccessible(true);

                try {
                    Object[] var5 = enumConstants;
                    int var6 = enumConstants.length;

                    for(int var7 = 0; var7 < var6; ++var7) {
                        Object enumConstant = var5[var7];
                        this.values.add(method.invoke(enumConstant));
                    }
                } catch (Exception var9) {
                    System.out.printf("获取枚举类:[%s]的枚举对象的值失败.\n", enumClazz);
                }

            }
        }
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return value == null || this.values.contains(value);
    }
}
