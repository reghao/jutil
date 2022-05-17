package cn.reghao.jutil.redis.ds;

import io.lettuce.core.api.sync.RedisListCommands;

/**
 * @author reghao
 * @date 2022-02-25 17:49:51
 */
public class RedisList {
    private final RedisListCommands<String, Object> listCommands;

    public RedisList(RedisListCommands<String, Object> listCommands) {
        this.listCommands = listCommands;
    }

    public void lpush(String key, Object value) {
        listCommands.lpush(key, value);
    }

    public Object lpop(String key) {
        return listCommands.lpop(key);
    }
}
