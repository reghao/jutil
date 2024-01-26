package cn.reghao.jutil.jdk.converter;

import java.util.HashMap;
import java.util.Map;

/**
 * 字节单位转换器
 *
 * @author reghao
 * @date 2019-10-26 22:39:23
 */
public class ByteConverter {
    private final Map<Integer, String> map = new HashMap<>();

    public ByteConverter() {
        for (ByteType byteType : ByteType.values()) {
            map.put(byteType.ordinal(), byteType.name());
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

    public long convert(ByteType src, ByteType dest, long value) {
        for (int i = src.ordinal(); i < dest.ordinal(); i++) {
            value = value >> 10;
        }

        return value;
    }

    public String convertStr(ByteType src, ByteType dest, long value) {
        for (int i = src.ordinal(); i < dest.ordinal(); i++) {
            value = value >> 10;
        }
        return value + dest.name();
    }
}
