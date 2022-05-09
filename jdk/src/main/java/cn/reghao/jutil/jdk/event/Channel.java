package cn.reghao.jutil.jdk.event;

/**
 * @author reghao
 * @date 2022-05-05 09:51:45
 */
public interface Channel<E extends Message> {
    void dispatch(E message);
}
