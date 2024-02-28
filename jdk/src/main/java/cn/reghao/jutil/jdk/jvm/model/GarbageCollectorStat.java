package cn.reghao.jutil.jdk.jvm.model;

import java.lang.management.GarbageCollectorMXBean;
import java.util.Arrays;

/**
 * @author reghao
 * @date 2020-10-21 15:49:56
 */
public class GarbageCollectorStat {
    private String gcName;
    // GC 管理的内存池
    private String memoryPools;
    // 自 JVM 启动后总共发生的 GC 次数
    private long gcCount;
    // 自 JVM 启动后 GC 总共耗费的时间
    private long gcTime;

    public GarbageCollectorStat(GarbageCollectorMXBean gcBean) {
        this.gcName = gcBean.getName();
        this.memoryPools = Arrays.toString(gcBean.getMemoryPoolNames()).replace("[", "").replace("]", "");
        this.gcCount = gcBean.getCollectionCount();
        this.gcTime = gcBean.getCollectionCount();
    }

    public String getGcName() {
        return gcName;
    }

    public String getMemoryPools() {
        return memoryPools;
    }

    public long getGcCount() {
        return gcCount;
    }

    public long getGcTime() {
        return gcTime;
    }
}
