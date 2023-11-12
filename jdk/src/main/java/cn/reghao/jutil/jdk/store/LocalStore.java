package cn.reghao.jutil.jdk.store;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 表示一块磁盘
 *
 * @author reghao
 * @date 2022-05-23 15:00:59
 */
public class LocalStore {
    private final String logicalDisk;
    // disk mounted directory
    private final String diskDir;
    private final long total;
    private final AtomicLong available;
    private final long max;

    public LocalStore(String logicalDisk, String diskDir, long total, long available, double maxPercent) {
        this.logicalDisk = logicalDisk;
        this.diskDir = diskDir;
        this.total = total;
        this.available = new AtomicLong(available);
        BigDecimal bigDecimal1 = new BigDecimal(total*10);
        BigDecimal bigDecimal2 = new BigDecimal(maxPercent*10);
        BigDecimal result = bigDecimal1.divide(bigDecimal2, RoundingMode.DOWN);
        this.max = result.longValue();
    }

    public String getLogicalDisk() {
        return logicalDisk;
    }

    public String getDiskDir() {
        return diskDir;
    }

    public long getTotal() {
        return total;
    }

    public long getAvailable() {
        return available.get();
    }

    public void setCurrentAvailable(long currentAvailable) {
        available.getAndSet(currentAvailable);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + diskDir.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other instanceof LocalStore) {
            LocalStore o = (LocalStore) other;
            return o.diskDir.equals(diskDir);
        } else {
            return false;
        }
    }
}
