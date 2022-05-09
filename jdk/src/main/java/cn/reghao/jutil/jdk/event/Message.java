package cn.reghao.jutil.jdk.event;

import java.io.Serializable;

/**
 * @author reghao
 * @date 2022-05-05 09:51:15
 */
public interface Message extends Serializable {
    Class<? extends Message> getType();
}
