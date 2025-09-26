package cn.reghao.jutil.jdk.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 字节单位转换器
 *
 * @author reghao
 * @date 2019-10-26 22:39:23
 */
public class ByteConverter {
    private final Map<Integer, String> map = new HashMap<>();
    private final Map<Long, ByteType> map1 = new HashMap<>();

    public ByteConverter() {
        for (ByteType byteType : ByteType.values()) {
            map.put(byteType.ordinal(), byteType.name());
            map1.put(byteType.getValue(), byteType);
        }
    }

    /**
     * @date 2020-10-20 上午11:26
     */
    public String convert(ByteType byteType, long value) {
        int base = 1024;
        for (int i = byteType.ordinal(); i <= ByteType.TiB.ordinal(); i++) {
            if (value < base) {
                return value + map.get(i);
            }
            value = value >> 10;
        }

        if (value < base) {
            return value + "PiB";
        } else {
            return "data too large...";
        }
    }

    /**
     * @param value 单位为 byte, 且值不大于 ByteType.PiB
     * @return 精度为两位小数
     * @date 2025-09-26 14:42:55
     */
    public String convert(long value) {
        List<Long> list = new ArrayList<>(map1.keySet());
        Collections.sort(list);
        int i = list.size()-1;
        for (; i > 0; i--) {
            if (value > list.get(i)) {
                break;
            }
        }
        ByteType byteType = map1.get(list.get(i));

        int scale = 2;
        BigDecimal b1 = new BigDecimal(value);
        BigDecimal b2 = new BigDecimal(byteType.getValue());
        BigDecimal result = b1.divide(b2, scale, RoundingMode.HALF_UP);
        return String.format("%s%s", result, byteType.name());
    }
}
