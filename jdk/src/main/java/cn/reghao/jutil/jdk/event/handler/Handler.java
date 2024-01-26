package cn.reghao.jutil.jdk.event.handler;

import cn.reghao.jutil.jdk.event.message.Event;

/**
 * @author reghao
 * @date 2022-05-05 09:54:21
 */
public class Handler implements Channel<Event> {
    @Override
    public void handle(Event message) {
    }
}
