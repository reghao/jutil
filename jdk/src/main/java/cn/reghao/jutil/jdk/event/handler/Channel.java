package cn.reghao.jutil.jdk.event.handler;

import cn.reghao.jutil.jdk.event.message.Message;

/**
 * @author reghao
 * @date 2022-05-05 09:51:45
 */
public interface Channel<E extends Message> {
    void handle(E message);
}
