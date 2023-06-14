package cn.reghao.jutil.media;

/**
 * 视频图像分辨率
 *
 * 横屏视频宽高比 = 16:9
 * 竖屏视频宽高比 = 9:16
 *
 * 横屏视频分辨率
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
    p144("144p", 256, 144),
    p288("288p", 512, 288),
    p360("360p", 640, 360),
    p480("480p", 854, 480),
    p720("720p", 1280, 720),
    p1080("1080p", 1920, 1080),
    p1440("2k", 2560, 1440),
    p2160("4k", 3840, 2160),
    p4320("8k", 7680, 4320);

    private final String quality;
    private final int width;
    private final int height;

    MediaResolution(String quality, int width, int height) {
        this.quality = quality;
        this.width = width;
        this.height = height;
    }

    public String getQuality() {
        return quality;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
