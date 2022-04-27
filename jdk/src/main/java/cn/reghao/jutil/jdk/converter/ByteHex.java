package cn.reghao.jutil.jdk.converter;

/**
 * 字节数组和十六进制字符串转换
 *
 * @author reghao
 * @date 2022-04-27 16:38:34
 */
public class ByteHex {
    private static final char[] HEX = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 字节数组转换为十六进制字符串
     *
     * @param
     * @return
     * @date 2021-11-26 下午5:25
     */
    public static String bytes2Hex(byte[] bytes) {
        StringBuilder hexStr = new StringBuilder();
        for (byte b : bytes) {
            int num = b < 0 ? 256 + b : b;
            hexStr.append(HEX[num/16]).append(HEX[num%16]);
        }

        return hexStr.toString();
    }

    /**
     * 十六进制字符串转换为字节数组
     *
     * @param
     * @return
     * @date 2021-11-26 下午5:25
     */
    public static byte[] hex2Bytes(String hex) {
        int len = hex.length();
        byte[] bytes = new byte[len/2];
        for (int i = 0; i < len/2; i++) {
            String tmp = hex.substring(i*2, i*2+2);
            bytes[i] = (byte) Integer.parseInt(tmp, 16);
        }

        return bytes;
    }
}
