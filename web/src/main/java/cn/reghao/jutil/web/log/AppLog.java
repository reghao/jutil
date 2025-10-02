package cn.reghao.jutil.web.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author reghao
 * @date 2025-07-18 11:33:46
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AppLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private String requestId;
    private String app;
    private String host;
    private long timestamp;
    private String level;
    private String thread;
    private String logger;
    private String message;
}
