package cn.reghao.jutil.jdk.media.model;

/**
 * @author reghao
 * @date 2023-03-28 10:07:48
 */
public class AudioProps {
    private String codecName;
    private String codecTagString;
    private long bitRate;
    private double duration;

    public AudioProps(String codecName, String codecTagString, long bitRate, double duration) {
        this.codecName = codecName;
        this.codecTagString = codecTagString;
        this.bitRate = bitRate;
        this.duration = duration;
    }

    public void setCodecName(String codecName) {
        this.codecName = codecName;
    }

    public String getCodecName() {
        return codecName;
    }

    public void setCodecTagString(String codecTagString) {
        this.codecTagString = codecTagString;
    }

    public String getCodecTagString() {
        return codecTagString;
    }

    public void setBitRate(long bitRate) {
        this.bitRate = bitRate;
    }

    public Long getBitRate() {
        return bitRate;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Double getDuration() {
        return duration;
    }
}
