package cn.reghao.jutil.jdk.store;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author reghao
 * @date 2022-05-23 23:31:21
 */
public class StoreDir {
    private final String baseDir;
    private final AtomicInteger total;

    public StoreDir(String baseDir) {
        this.baseDir = baseDir;
        this.total = new AtomicInteger(0);
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setTotal(int total) {
        this.total.setPlain(total);
    }

    public int getTotal() {
        return total.get();
    }

    public void incr() {
        this.total.incrementAndGet();
    }

    public void decr() {
        this.total.decrementAndGet();
    }
}
