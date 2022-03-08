package cn.reghao.jutil.jdk.machine.data.detail;

import java.io.Serializable;

/**
 * @author reghao
 * @date 2021-10-16 18:15:16
 */
public class MemoryDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    // byte
    private long total;
    private long avail;
    private long used;
    private long swapTotal;
    private long swapAvail;

    public MemoryDetail(long total, long avail, long used, long swapTotal, long swapAvail) {
        this.total = total;
        this.avail = avail;
        this.used = used;
        this.swapTotal = swapTotal;
        this.swapAvail = swapAvail;
    }

    public long getTotal() {
        return total;
    }

    public void setAvail(long avail) {
        this.avail = avail;
    }

    public long getAvail() {
        return avail;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public long getUsed() {
        return used;
    }

    public long getSwapTotal() {
        return swapTotal;
    }

    public void setSwapAvail(long swapAvail) {
        this.swapAvail = swapAvail;
    }

    public long getSwapAvail() {
        return swapAvail;
    }
}
