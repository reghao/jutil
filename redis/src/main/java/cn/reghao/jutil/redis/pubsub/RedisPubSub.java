package cn.reghao.jutil.redis.pubsub;

import cn.reghao.jutil.redis.ObjectSerializeCodec;
import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;

/**
 * @author reghao
 * @date 2022-02-25 17:49:51
 */
public class RedisPubSub {
    private final StatefulRedisPubSubConnection<String, Object> subConn;
    private final StatefulRedisPubSubConnection<String, Object> pubConn;

    public RedisPubSub(RedisClient redisClient, ObjectSerializeCodec<Object> objectSerializeCodec) {
        this.subConn = redisClient.connectPubSub(objectSerializeCodec);
        this.pubConn = redisClient.connectPubSub(objectSerializeCodec);
    }

    public void publish(String channel, Object message) {
        pubConn.async().publish(channel, message);
        //pubConn.sync().publish(channel, message);
    }

    public void subscribe(String channel, RedisPubSubListener<String, Object> listener) {
        subConn.addListener(listener);
        subConn.async().subscribe(channel);
    }
}
