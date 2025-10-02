package cn.reghao.jutil.jdk.math;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author reghao
 * @date 2024-10-22 17:09:44
 */
public class Calculator {
    public static double getPercentage(long total, long avail) {
        long used = total - avail;
        BigDecimal bigDecimal1 = new BigDecimal(total);
        BigDecimal bigDecimal2 = new BigDecimal(used);
        BigDecimal result = bigDecimal2.divide(bigDecimal1, 4, RoundingMode.DOWN);
        return result.multiply(new BigDecimal(100)).setScale(2, RoundingMode.DOWN).doubleValue();
    }

    public static double divide(long d1, long d2) {
        int scale = 2;
        BigDecimal b1 = new BigDecimal(d1);
        BigDecimal b2 = new BigDecimal(d2);
        BigDecimal result = b1.divide(b2, scale, RoundingMode.HALF_UP);
        double doubleValue = result.doubleValue();
        /*DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String result2 = df.format(d1/d2);
        System.out.println(result2);*/
        return doubleValue;
    }
}
