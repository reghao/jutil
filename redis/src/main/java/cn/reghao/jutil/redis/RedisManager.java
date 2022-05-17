package cn.reghao.jutil.redis;

import cn.reghao.jutil.redis.ds.RedisList;
import cn.reghao.jutil.redis.ds.RedisSet;
import cn.reghao.jutil.redis.pubsub.RedisPubSub;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.api.sync.RedisListCommands;
import io.lettuce.core.api.sync.RedisSetCommands;

import java.time.Duration;

/**
 * Redis 客户端
 *
 * @author reghao
 * @date 2022-02-25 17:49:51
 */
public class RedisManager {
    private final RedisClient redisClient;
    private final ObjectSerializeCodec<Object> objectSerializeCodec;
    private RedisSet redisSet;
    private RedisList redisList;
    private RedisPubSub redisPubSub;

    public RedisManager(RedisConfig config) {
        RedisURI uri = RedisURI.Builder
                .redis(config.getHost())
                .withPassword(config.getPassword().toCharArray())
                .withDatabase(config.getDb())
                .withTimeout(Duration.ofSeconds(60))
                .build();
        this.redisClient = RedisClient.create(uri);
        this.objectSerializeCodec = new ObjectSerializeCodec<>();
    }

    public RedisSet redisSet() {
        if (redisSet == null) {
            StatefulRedisConnection<String, Object> conn = redisClient.connect(objectSerializeCodec);
            RedisSetCommands<String, Object> setCommands = conn.sync();
            this.redisSet = new RedisSet(setCommands);
        }
        return redisSet;
    }

    public RedisList redisList() {
        if (redisList == null) {
            StatefulRedisConnection<String, Object> conn = redisClient.connect(objectSerializeCodec);
            RedisListCommands<String, Object> listCommands = conn.sync();
            this.redisList = new RedisList(listCommands);
        }
        return redisList;
    }

    public RedisPubSub redisPubSub() {
        if (redisPubSub == null) {
            this.redisPubSub = new RedisPubSub(redisClient, objectSerializeCodec);
        }
        return redisPubSub;
    }

    public RedisCommands<String, Object> redisCommands() {
        return redisClient.connect(objectSerializeCodec).sync();
    }
}
