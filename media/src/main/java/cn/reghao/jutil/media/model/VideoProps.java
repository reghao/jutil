package cn.reghao.jutil.media.model;

/**
 * @author reghao
 * @date 2023-03-28 10:07:53
 */
public class VideoProps {
    private String codecName;
    private String codecTagString;
    private Double bitRate;
    private Double duration;
    private Double codedWidth;
    private Double codedHeight;

    public VideoProps(String codecName, String codecTagString, double bitRate, double duration, double codedWidth, double codedHeight) {
        this.codecName = codecName;
        this.codecTagString = codecTagString;
        this.bitRate = bitRate;
        this.duration = duration;
        this.codedWidth = codedWidth;
        this.codedHeight = codedHeight;
    }

    public String getCodecName() {
        return codecName;
    }

    public String getCodecTagString() {
        return codecTagString;
    }

    public void setBitRate(Double bitRate) {
        this.bitRate = bitRate;
    }

    public Double getBitRate() {
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
}
