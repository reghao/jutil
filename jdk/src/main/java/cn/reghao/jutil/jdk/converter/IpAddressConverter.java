package cn.reghao.jutil.jdk.converter;

/**
 * IP 地址转换
 *
 * @author reghao
 * @date 2019-10-26 22:44:16
 */
public class IpAddressConverter {
    /**
     * 十六进制转换为点分十进制格式
     *
     * @param
     * @return
     * @date 2019-10-26 下午11:43
     */
    public String hex2dotDecimal(String hex) {
        long decimal = Long.parseLong(hex, 16);
        String binary = Long.toBinaryString(decimal);
        int len = binary.length();
        int size = 32;
        if (len != size) {
            StringBuilder sb = new StringBuilder();

            int minus = size - len;
            for (int i = 0; i < minus; i++) {
                sb.append(0);
            }
            sb.append(binary);
            binary = sb.toString();
        }

        String a = binary.substring(0, 8);
        String b = binary.substring(8, 16);
        String c = binary.substring(16, 24);
        String d = binary.substring(24);

        int a1 = Integer.parseInt(a, 2);
        int b1 = Integer.parseInt(b, 2);
        int c1 = Integer.parseInt(c, 2);
        int d1 = Integer.parseInt(d, 2);

        return d1 + ":" + c1 + ":" + b1 + ":"  + a1;
    }
}
