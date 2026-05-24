package cn.reghao.jutil.jdk.media.model;

import java.io.Serializable;

/**
 * @author reghao
 * @date 2023-03-28 10:07:53
 */
public class VideoProps implements Serializable {
    private static final long serialVersionUID = 1L;

    private String codecName;
    private String codecTagString;
    private long bitRate;
    private double duration;
    private double codedWidth;
    private double codedHeight;
    private String pixFmt;

    public VideoProps(String codecName, String codecTagString, long bitRate, double duration,
                      double codedWidth, double codedHeight, String pixFmt) {
        this.codecName = codecName;
        this.codecTagString = codecTagString;
        this.bitRate = bitRate;
        this.duration = duration;
        this.codedWidth = codedWidth;
        this.codedHeight = codedHeight;
        this.pixFmt = pixFmt;
    }

    public String getCodecName() {
        return codecName;
    }

    public String getCodecTagString() {
        return codecTagString;
    }

    public void setBitRate(long bitRate) {
        this.bitRate = bitRate;
    }

    public long getBitRate() {
        return bitRate;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getDuration() {
        return duration;
    }

    public Double getCodedWidth() {
        return codedWidth;
    }

    public Double getCodedHeight() {
        return codedHeight;
    }

    public void setPixFmt(String pixFmt) {
        this.pixFmt = pixFmt;
    }

    public String getPixFmt() {
        return pixFmt;
    }
}
