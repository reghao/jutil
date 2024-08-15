package cn.reghao.jutil.jdk.jvm.model;

import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;

/**
 * @author reghao
 * @date 2020-10-21 15:49:56
 */
public class MemoryPoolStat {
    private String poolName;
    private String memoryType;
    private MemoryStat poolUsage;
    // 内存池峰值时的使用情况
    private MemoryStat poolPeakUsage;
    // 最近一次 GC 后的内存池的使用情况
    private MemoryStat afterGcUsage;

    public MemoryPoolStat(MemoryPoolMXBean memoryPoolBean) {
        this.poolName = memoryPoolBean.getName();
        this.memoryType = memoryPoolBean.getType().name();
        this.poolUsage = new MemoryStat(memoryPoolBean.getUsage());
        this.poolPeakUsage = new MemoryStat(memoryPoolBean.getPeakUsage());
        MemoryUsage collectionMemoryUsage = memoryPoolBean.getCollectionUsage();
        if (collectionMemoryUsage != null) {
            this.afterGcUsage = new MemoryStat(collectionMemoryUsage);
        }
    }
}
