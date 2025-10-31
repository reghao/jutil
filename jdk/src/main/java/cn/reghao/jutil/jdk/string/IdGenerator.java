package cn.reghao.jutil.jdk.string;

import org.hashids.Hashids;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author reghao
 * @date 2021-11-22 14:05:13
 */
public class IdGenerator {
    private final int len;
    private final Hashids hashids;
    private final SnowFlake snowFlake = new SnowFlake(1, 1);

    public IdGenerator(String salt) {
        this.len = 10;
        this.hashids = new Hashids(salt, len);
    }

    public IdGenerator(int len, String salt) {
        this.len = len;
        this.hashids = new Hashids(salt, len);
    }

    public String getUuid() {
        long nextId = snowFlake.nextId();
        return hashids.encode(nextId);
    }

    /**
     * 字符串形式的 ID
     *
     * @param
     * @return
     * @date 2021-12-24 上午11:30
     */
    public String stringId() {
        //long nextId = snowFlake.nextId();
        long nextId = ThreadLocalRandom.current().nextLong(Hashids.MAX_NUMBER);
        return hashids.encode(nextId);
    }
}
