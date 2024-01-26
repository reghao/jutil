package cn.reghao.jutil.jdk.exception;

/**
 * @author reghao
 * @date 2020-08-10 13:52:13
 */
public class ExceptionUtil {
    public static String errorMsg(Exception e) {
        // TODO 记录栈跟踪
        String stackTrace = stackTrace(e);

        StringBuilder sb = new StringBuilder();
        sb.append(e.getMessage());
        Throwable throwable = e.getCause();
        if (throwable != null) {
            sb.append(System.lineSeparator()).append(throwable.getMessage());
        }

        return sb.toString();
    }

    public static String stackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.toString()).append(System.lineSeparator());

        StackTraceElement[] elements = e.getStackTrace();
        for (StackTraceElement ele : elements) {
            sb.append("\t").append(ele.toString()).append(System.lineSeparator());
        }

        return sb.toString();
    }
}
