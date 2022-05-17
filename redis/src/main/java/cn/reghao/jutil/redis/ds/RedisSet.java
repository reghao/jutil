package cn.reghao.jutil.redis.ds;

import io.lettuce.core.api.sync.RedisSetCommands;

import java.util.Set;

/**
 * @author reghao
 * @date 2022-02-25 17:49:51
 */
public class RedisSet {
    private final RedisSetCommands<String, Object> setCommands;

    public RedisSet(RedisSetCommands<String, Object> setCommands) {
        this.setCommands = setCommands;
    }

    public void sadd(String key, Object value) {
        setCommands.sadd(key, value);
    }

    public void smove(String srcKey, String destKey, Object value) {
        setCommands.smove(srcKey, destKey, value);
    }

    public void sadd(String key, Object... values) {
        setCommands.sadd(key, values);
    }

    public Object spop(String key) {
        return setCommands.spop(key);
    }

    public Set<Object> spop(String key, long count) {
        return setCommands.spop(key, count);
    }

    public Set<Object> smembers(String key) {
        return setCommands.smembers(key);
    }

    public boolean sismember(String key, Object value) {
        return setCommands.sismember(key, value);
    }

    public long scard(String key) {
        return setCommands.scard(key);
    }
}
