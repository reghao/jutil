package cn.reghao.jutil.media.po;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author reghao
 * @date 2023-03-28 10:07:48
 */
@AllArgsConstructor
@Getter
public class AudioInfo {
    private String codecName;
    private String codecTagString;
    private double bitRate;
    private double duration;
}
