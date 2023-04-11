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
public class MediaInfo {
    private AudioInfo audioInfo;
    private VideoInfo videoInfo;
    private LocalDateTime createTime;

    public MediaInfo(AudioInfo audioInfo, VideoInfo videoInfo) {
        this.audioInfo = audioInfo;
        this.videoInfo = videoInfo;
    }
}
