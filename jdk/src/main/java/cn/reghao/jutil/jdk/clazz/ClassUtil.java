package cn.reghao.jutil.jdk.clazz;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author reghao
 * @date 2023-02-21 10:06:26
 */
public class ClassUtil {
    public static Object getObject(Class<?> clazz, String[] strs) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        if (strs.length != fields.length) {
            return null;
        }

        Object object = clazz.getDeclaredConstructor().newInstance();
        for (int i = 0; i < fields.length; i++) {
            Class<?> clazzType = fields[i].getType();
            if (clazzType.equals(String.class)) {
                fields[i].setAccessible(true);
                fields[i].set(object, strs[i]);
            } else {
                Object result;
                if (clazzType.equals(Long.class)) {
                    result = Long.parseLong(strs[i]);
                } else if (clazzType.equals(Integer.class)) {
                    result = Integer.parseInt(strs[i]);
                } else {
                    result = Double.parseDouble(strs[i]);
                }

                fields[i].setAccessible(true);
                fields[i].set(object, result);
            }
        }
        return object;
    }

    public static Method findMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        try {
            return clazz.getMethod(methodName, paramTypes);
        }
        catch (NoSuchMethodException ex) {
            return findDeclaredMethod(clazz, methodName, paramTypes);
        }
    }

    public static Method findDeclaredMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, paramTypes);
        }
        catch (NoSuchMethodException ex) {
            if (clazz.getSuperclass() != null) {
                return findDeclaredMethod(clazz.getSuperclass(), methodName, paramTypes);
            }
            return null;
        }
    }
}
