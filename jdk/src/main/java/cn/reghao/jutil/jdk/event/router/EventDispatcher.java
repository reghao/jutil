package cn.reghao.jutil.jdk.event.router;

import cn.reghao.jutil.jdk.event.handler.Channel;
import cn.reghao.jutil.jdk.event.message.Event;
import cn.reghao.jutil.jdk.event.handler.Handler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author reghao
 * @date 2022-05-05 09:55:18
 */
public class EventDispatcher implements DynamicRouter<Event> {
    private final Map<Class<? extends Event>, Handler> handlers = new HashMap<>();

    @Override
    public void register(Class<? extends Event> contentType, Channel<? extends Event> channel) {
        handlers.put(contentType, (Handler) channel);
    }

    @Override
    public void dispatch(Event content) {
        handlers.get(content.getClass()).handle(content);
    }
}
