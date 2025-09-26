package cn.reghao.jutil.jdk.jvm.model;

import cn.reghao.jutil.jdk.converter.ByteConverter;

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
        this.init = convert.convert(memoryUsage.getInit());
        this.max = convert.convert(memoryUsage.getMax());
        this.used = convert.convert(memoryUsage.getUsed());
        this.committed = convert.convert(memoryUsage.getCommitted());
    }
}
