package cn.reghao.jutil.jdk.media.model;

public class WebVideoCheck {
    private boolean videoFormat = false;
    private boolean vcodec = false;
    private boolean pixFormat = false;
    private boolean acodec = false;
    private boolean fastStart = false;

    public void setVideoFormat(boolean videoFormat) {
        this.videoFormat = videoFormat;
    }

    public void setVcodec(boolean vcodec) {
        this.vcodec = vcodec;
    }

    public void setPixFormat(boolean pixFormat) {
        this.pixFormat = pixFormat;
    }

    public void setAcodec(boolean acodec) {
        this.acodec = acodec;
    }

    public void setFastStart(boolean fastStart) {
        this.fastStart = fastStart;
    }

    public boolean isWebVideo() {
        return videoFormat && vcodec && pixFormat && acodec && fastStart;
    }

    public boolean isPassed() {
        return videoFormat && vcodec && pixFormat && acodec;
    }
}
