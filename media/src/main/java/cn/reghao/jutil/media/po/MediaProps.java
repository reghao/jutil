package cn.reghao.jutil.media.po;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author reghao
 * @date 2023-03-28 10:07:59
 */
@Getter
@Setter
public class MediaProps {
    private AudioProps audioProps;
    private VideoProps videoProps;
    private LocalDateTime createTime;

    public MediaProps(AudioProps audioProps, VideoProps videoProps) {
        this.audioProps = audioProps;
        this.videoProps = videoProps;
    }
}
