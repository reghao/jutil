package cn.reghao.jutil.jdk.media.model;

import java.io.Serializable;

/**
 * @author reghao
 * @date 2025-10-27 10:25:45
 */
public class ImageProps implements Serializable {
    private static final long serialVersionUID = 1L;

    private int width;
    private int height;
    private String format;
    private long size;

    public ImageProps(int width, int height, String format, long size) {
        this.width = width;
        this.height = height;
        this.format = format;
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getFormat() {
        return format;
    }

    public long getSize() {
        return size;
    }
}
