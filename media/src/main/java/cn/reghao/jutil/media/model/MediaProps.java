package cn.reghao.jutil.media.model;

import java.time.LocalDateTime;

/**
 * @author reghao
 * @date 2023-03-28 10:07:59
 */
public class MediaProps {
    private AudioProps audioProps;
    private VideoProps videoProps;
    private LocalDateTime createTime;

    public MediaProps(AudioProps audioProps, VideoProps videoProps) {
        this.audioProps = audioProps;
        this.videoProps = videoProps;
    }

    public AudioProps getAudioProps() {
        return audioProps;
    }

    public VideoProps getVideoProps() {
        return videoProps;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }
}
