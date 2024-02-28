package cn.reghao.jutil.jdk.jvm.model;

import java.util.List;
import java.util.Map;

/**
 * @author reghao
 * @date 2020-10-22 15:06:29
 */
public class JvmStat {
    /* 类加载情况 */
    private long jvmClassesLoaded;
    private long jvmClassesUnloaded;
    private long jvmClassesTotal;

    /* 堆区和非堆区总体内存使用情况 */
    private String jvmMemoryHeapInit;
    private String jvmMemoryHeapMax;
    private String jvmMemoryHeapUsed;
    private String jvmMemoryHeapCommitted;

    private String jvmMemoryNonheapInit;
    private String jvmMemoryNonheapMax;
    private String jvmMemoryNonheapUsed;
    private String jvmMemoryNonheapCommitted;

    private List<BufferPoolStat> jvmBufferPools;
    private Map<String, MemoryPoolStat> jvmMemoryPools;
    private List<GarbageCollectorStat> jvmGarbageCollectors;

    /* 线程使用情况 */
    private int jvmThreadsLive;
    private int jvmThreadsDaemon;
    private int jvmThreadsPeak;
    private List<ThreadStat> jvmThreads;
}
