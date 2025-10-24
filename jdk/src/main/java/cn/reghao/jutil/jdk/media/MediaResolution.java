package cn.reghao.jutil.jdk.media;

/**
 * 视频图像分辨率
 *
 * 横屏视频宽高比 = 16:9
 * 竖屏视频宽高比 = 9:16
 *
 * 横屏分辨率
 * 144p = 256x144
 * 288p = 512x288
 * 360p = 640x360
 * 480p = 854x480
 * 720p = 1280x720
 * 1080p = 1920x1080
 * 1440p(2k) = 2560x1440
 * 2160p(4k) = 3840x2160
 * 4320p(8k) = 7680×4320
 *
 * @author reghao
 * @date 2022-08-04 09:21:38
 */
public enum MediaResolution {
    p144(144, "144p", 256, 144),
    p288(288, "288p", 512, 288),
    p360(360, "360p", 640, 360),
    p480(480, "480p", 854, 480),
    p720(720, "720p", 1280, 720),
    p1080(1080, "1080p", 1920, 1080),
    p1440(1440, "1440p", 2560, 1440),
    p2160(2160, "2160p", 3840, 2160),
    p4320(4320, "4320p", 7680, 4320);

    private final int quality;
    private final String qualityStr;
    private final int width;
    private final int height;

    MediaResolution(int quality, String qualityStr, int width, int height) {
        this.quality = quality;
        this.qualityStr = qualityStr;
        this.width = width;
        this.height = height;
    }

    public int getQuality() {
        return quality;
    }

    public String getQualityStr() {
        return qualityStr;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
