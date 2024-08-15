package cn.reghao.jutil.jdk.jvm.model;

import cn.reghao.jutil.jdk.converter.ByteConverter;
import cn.reghao.jutil.jdk.converter.ByteType;

import java.lang.management.BufferPoolMXBean;

/**
 * @author reghao
 * @date 2020-10-21 15:49:56
 */
public class BufferPoolStat {
    private String poolName;
    private long bufferCount;
    private String totalMemory;
    private String usedMemory;

    public BufferPoolStat(BufferPoolMXBean bufferPoolBean) {
        ByteConverter converter = new ByteConverter();
        this.poolName = bufferPoolBean.getName();
        this.bufferCount = bufferPoolBean.getCount();
        this.totalMemory = converter.convert(ByteType.Bytes, bufferPoolBean.getTotalCapacity());
        this.usedMemory = converter.convert(ByteType.Bytes, bufferPoolBean.getMemoryUsed());
    }

    public String getPoolName() {
        return poolName;
    }

    public long getBufferCount() {
        return bufferCount;
    }

    public String getTotalMemory() {
        return totalMemory;
    }

    public String getUsedMemory() {
        return usedMemory;
    }
}
