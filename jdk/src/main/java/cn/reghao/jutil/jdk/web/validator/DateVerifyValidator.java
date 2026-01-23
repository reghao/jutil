package cn.reghao.jutil.jdk.web.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 日期时间类型校验器
 *
 * @author reghao
 * @date 2026-01-09 17:29:25
 */
public class DateVerifyValidator implements ConstraintValidator<DateVerify, String> {
    private String dateFormat;

    @Override
    public void initialize(DateVerify obj) {
        dateFormat = obj.dateFormat();
    }

    /**
     * 对参数进行验证
     *
     * @param value   修饰字段的值
     * @param context 上下文
     * @return true：验证通过, false：验证不通过
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !value.isBlank() && isValidDate(value, dateFormat);
    }

    boolean isValidDate(String str, String dateFormat) {
        boolean convertSuccess = true;
        if (null != str && null != dateFormat && str.length() == dateFormat.length()) {
            SimpleDateFormat format = new SimpleDateFormat(dateFormat);
            try {
                // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
                format.setLenient(false);
                format.parse(str);
            } catch (ParseException e) {
                convertSuccess = false;
            }
        } else {
            convertSuccess = false;
        }

        return convertSuccess;
    }
}
