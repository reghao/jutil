package cn.reghao.jutil.jdk.event.router;

import cn.reghao.jutil.jdk.event.handler.Channel;
import cn.reghao.jutil.jdk.event.message.Message;

/**
 * @author reghao
 * @date 2022-05-05 09:52:19
 */
public interface DynamicRouter<E extends Message> {
    void register(Class<? extends E> eventType, Channel<? extends E> channel);
    void dispatch(E content);
}
