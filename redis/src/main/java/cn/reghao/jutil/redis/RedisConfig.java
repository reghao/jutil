package cn.reghao.jutil.redis;

/**
 * @author reghao
 * @date 2022-02-25 17:49:51
 */
public class RedisConfig {
    private final String host;
    private final String password;
    private final int db;

    public RedisConfig(String host, String password, int db) {
        this.host = host;
        this.password = password;
        this.db = db;
    }

    public String getHost() {
        return host;
    }

    public String getPassword() {
        return password;
    }

    public int getDb() {
        return db;
    }
}
