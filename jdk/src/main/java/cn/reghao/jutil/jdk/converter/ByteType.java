package cn.reghao.jutil.jdk.converter;

/**
 * @author reghao
 * @date 2019-10-29 16:23:15
 */
public enum ByteType {
    Bytes(1L),
    KiB(1024L),
    MiB(1024L*1024),
    GiB(1024L*1024*1024),
    TiB(1024L*1024*1024*1024),
    PiB(1024L*1024*1024*1024*1024);

    private final long value;

    ByteType(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
