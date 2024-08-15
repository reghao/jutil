package cn.reghao.jutil.jdk.jvm;

import cn.reghao.jutil.jdk.jvm.model.*;
import cn.reghao.jutil.jdk.converter.DateTimeConverter;

import java.lang.management.*;
import java.util.*;

/**
 * JVM 信息和状态
 *
 * @author reghao
 * @date 2020-09-10 18:31:16
 */
public class JVM {
    private final RuntimeMXBean rtBean;
    private final ClassLoadingMXBean classLoadingBean;
    private final ThreadMXBean threadBean;
    private final MemoryMXBean memoryBean;
    private final List<MemoryPoolMXBean> memoryPoolBeans;
    private final List<BufferPoolMXBean> bufferPoolBeans;
    private final List<GarbageCollectorMXBean> gcBeans;

    public JVM() {
        this.rtBean = ManagementFactory.getRuntimeMXBean();
        this.classLoadingBean = ManagementFactory.getClassLoadingMXBean();
        this.threadBean = ManagementFactory.getThreadMXBean();
        this.memoryBean = ManagementFactory.getMemoryMXBean();
        this.memoryPoolBeans = ManagementFactory.getMemoryPoolMXBeans();
        this.bufferPoolBeans = ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class);
        this.gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
    }

    public JvmInfo info() {
        JvmInfo jvmInfo = new JvmInfo();
        jvmInfo.setOsName(System.getProperty("os.name"));
        jvmInfo.setOsVersion(System.getProperty("os.version"));

        jvmInfo.setJvmName(System.getProperty("java.vm.name"));
        jvmInfo.setJvmVersion(System.getProperty("java.vm.version"));

        // TODO pid 不一定准确
        jvmInfo.setJvmPid(Integer.parseInt(rtBean.getName().split("@")[0]));
        jvmInfo.setJvmStartTime(DateTimeConverter.msTimestampFormat(rtBean.getStartTime()));

        return jvmInfo;
    }

    private List<BufferPoolStat> bufferPoolStat() {
        List<BufferPoolStat> list = new ArrayList<>();
        bufferPoolBeans.forEach(bufferPoolBean -> list.add(new BufferPoolStat(bufferPoolBean)));
        return list;
    }

    private Map<String, MemoryPoolStat> memoryPoolStat() {
        Map<String, MemoryPoolStat> map = new HashMap<>();
        memoryPoolBeans.forEach(
                // TODO name 是否唯一？
                memoryPoolBean -> map.put(memoryPoolBean.getName(), new MemoryPoolStat(memoryPoolBean)));
        return map;
    }

    private List<GarbageCollectorStat> gcStat() {
        List<GarbageCollectorStat> list = new ArrayList<>();
        gcBeans.forEach(gcBean -> list.add(new GarbageCollectorStat(gcBean)));
        return list;
    }

    private List<ThreadStat> threadStat() {
        List<ThreadStat> list = new ArrayList<>();
        Arrays.stream(threadBean.getThreadInfo(threadBean.getAllThreadIds()))
                .forEach(threadInfo -> list.add(new ThreadStat(threadInfo)));
        return list;
    }
}
