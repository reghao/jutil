package cn.reghao.jutil.jdk.converter;

import java.text.DecimalFormat;

/**
 * 百分比计算器
 *
 * @author reghao
 * @date 2019-10-29 12:54:28
 */
public class PercentCalculator {
    private static DecimalFormat df = new DecimalFormat("0.00");

    public static double percentValue(long small, long big) {
        return ((double)small / (double)big);
    }

    public static String percent(long small, long big) {
        double value = ((double)small / (double)big);
        return df.format(value * 100) + "%";
    }

    public static String percent(double value) {
        return df.format(value * 100) + "%";
    }
}
