import cn.reghao.jutil.jdk.converter.ByteConverter;
import cn.reghao.jutil.jdk.converter.ByteType;

/**
 * @author reghao
 * @date 2025-09-26 14:06:56
 */
public class JdkTest {
    public static void main(String[] args) {
        ByteConverter byteConverter = new ByteConverter();

        long total = 510873600L;
        total = 235152568320L;
        String totalStr = byteConverter.convert(total);
        System.out.println(totalStr);
    }
}
