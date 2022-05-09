package cn.reghao.jutil.jdk.event;

/**
 * @author reghao
 * @date 2022-05-05 09:52:19
 */
public interface DynamicRouter<E extends Message> {
    void registerChannel(Class<? extends E> contentType, Channel<? extends E> channel);
    void dispatch(E content);
}
