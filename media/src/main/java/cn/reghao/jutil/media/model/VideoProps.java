package cn.reghao.jutil.media.model;

/**
 * @author reghao
 * @date 2023-03-28 10:07:53
 */
public class VideoProps {
    private String codecName;
    private String codecTagString;
    private long bitRate;
    private double duration;
    private double codedWidth;
    private double codedHeight;

    public VideoProps(String codecName, String codecTagString, long bitRate, double duration, double codedWidth, double codedHeight) {
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
}
