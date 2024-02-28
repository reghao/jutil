package cn.reghao.jutil.jdk.jvm.model;

import java.lang.management.ThreadInfo;

/**
 * @author reghao
 * @date 2020-10-21 17:31:47
 */
public class ThreadStat {
    private String threadName;
    private String threadState;
    private long blockedTime;
    private long blockedCount;
    private long waitedTime;
    private long waitedCount;
    private String lockName;
    private String lockOwnerName;
    private int lockedMonitorCount;
    private int lockedSynchronizerCount;

    public ThreadStat(ThreadInfo threadInfo) {
        this.threadName = threadInfo.getThreadName();
        this.threadState = threadInfo.getThreadState().name();
        this.blockedTime = threadInfo.getBlockedTime();
        this.blockedCount = threadInfo.getBlockedCount();
        this.waitedTime = threadInfo.getWaitedTime();
        this.waitedCount = threadInfo.getWaitedCount();
        this.lockName = threadInfo.getLockName();
        this.lockOwnerName = threadInfo.getLockOwnerName();
        this.lockedMonitorCount = threadInfo.getLockedMonitors().length;
        this.lockedSynchronizerCount = threadInfo.getLockedSynchronizers().length;
    }
}
