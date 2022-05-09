package cn.reghao.jutil.jdk.event;

import java.util.HashMap;
import java.util.Map;

/**
 * @author reghao
 * @date 2022-05-05 09:55:18
 */
public class EventDispatcher implements DynamicRouter<Event> {
    private final Map<Class<? extends Event>, Handler> handlers = new HashMap<>();

    @Override
    public void registerChannel(Class<? extends Event> contentType, Channel<? extends Event> channel) {
        handlers.put(contentType, (Handler) channel);
    }

    @Override
    public void dispatch(Event content) {
        handlers.get(content.getClass()).dispatch(content);
    }
}
