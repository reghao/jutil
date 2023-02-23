package cn.reghao.jutil.jdk.event.message;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author reghao
 * @date 2022-05-10 03:19:41
 */
public class EventMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String msgId;
    private final long sendTime;
    private Event event;

    private EventMessage() {
        this.msgId = UUID.randomUUID().toString();
        this.sendTime = System.currentTimeMillis();
    }

    public static EventMessage evt(Event event) {
        EventMessage evtMsg = new EventMessage();
        evtMsg.setEvent(event);
        return evtMsg;
    }

    public String getMsgId() {
        return msgId;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
