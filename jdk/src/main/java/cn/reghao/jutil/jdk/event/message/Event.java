package cn.reghao.jutil.jdk.event.message;

/**
 * @author reghao
 * @date 2022-05-04 17:44:42
 */
public class Event implements Message {
    private static final long serialVersionUID = 1L;

    @Override
    public Class<? extends Message> getType() {
        return getClass();
    }
}
