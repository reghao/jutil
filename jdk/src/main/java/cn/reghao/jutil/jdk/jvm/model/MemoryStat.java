package cn.reghao.jutil.jdk.jvm.model;

import cn.reghao.jutil.jdk.converter.ByteConverter;
import cn.reghao.jutil.jdk.converter.ByteType;

import java.lang.management.MemoryUsage;

/**
 * @author reghao
 * @date 2020-10-21 15:49:56
 */
public class MemoryStat {
    private String init;
    private String max;
    private String used;
    private String committed;

    public MemoryStat(MemoryUsage memoryUsage) {
        ByteConverter convert = new ByteConverter();
        this.init = convert.convertStr(ByteType.Bytes, ByteType.MiB, memoryUsage.getInit());
        this.max = convert.convertStr(ByteType.Bytes, ByteType.MiB, memoryUsage.getMax());
        this.used = convert.convertStr(ByteType.Bytes, ByteType.MiB, memoryUsage.getUsed());
        this.committed = convert.convertStr(ByteType.Bytes, ByteType.MiB, memoryUsage.getCommitted());
    }
}
