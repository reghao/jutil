package cn.reghao.jutil.media.video;

import cn.reghao.jutil.media.image.ImageOps;
import org.bytedeco.ffmpeg.avformat.AVFormatContext;
import org.bytedeco.ffmpeg.avutil.AVDictionary;
import org.bytedeco.ffmpeg.avutil.AVDictionaryEntry;
import org.bytedeco.ffmpeg.global.avformat;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author reghao
 * @date 2021-08-04 09:51:30
 */
public class VideoOps {
    static {
        avutil.av_log_set_level(avutil.AV_LOG_QUIET);
    }

    private static final Java2DFrameConverter frameConverter = new Java2DFrameConverter();

    /**
     * 视频元数据
     *
     * @param
     * @return
     * @date 2023-02-06 5:55 AM
     */
    public static Map<String, String> getMetadata(String filePath) throws IOException {
        VideoOps.videoProps(new File(filePath));
        Map<String, String> metadata = new HashMap<>();
        AVFormatContext fmt_ctx = new AVFormatContext(null);
        AVDictionaryEntry tag = new AVDictionaryEntry(null);
        try {
            int ret = avformat.avformat_open_input(fmt_ctx, filePath, null, null);
            if (ret < 0) {
                throw new IOException(ret + ":avformat_open_input error");
            }

            ret = avformat.avformat_find_stream_info(fmt_ctx, (AVDictionary) null);
            if (ret < 0) {
                throw new IOException(ret + ":avformat_find_stream_info error");
            }

            // To iterate through all the dictionary entries, you can set the matching key
            // to the null string "" and set the AV_DICT_IGNORE_SUFFIX flag.
            // ::av_dict_iterate::
            while (Objects.nonNull(tag = avutil.av_dict_get(fmt_ctx.metadata(), "", tag, avutil.AV_DICT_IGNORE_SUFFIX))) {
                metadata.put(tag.key().getString(), tag.value().getString());
            }
        } finally {
            avformat.avformat_close_input(fmt_ctx);
        }
        return metadata;
    }

    /**
     * 视频属性
     *
     * @param
     * @return
     * @date 2021-08-13 上午9:08
     */
    public static Map<String, Object> videoProps(File file) throws IOException {
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(file);
        grabber.start();
        // 截取第一帧
        Frame frame = grabber.grabImage();
        BufferedImage bufferedImage = frameConverter.getBufferedImage(frame);

        Map<String, Object> map = new HashMap<>();
        int width = bufferedImage.getWidth();
        map.put("width", width);
        int height = bufferedImage.getHeight();
        map.put("height", height);
        double fps = grabber.getFrameRate();
        map.put("fps", fps);
        int frameLength = grabber.getLengthInFrames();
        map.put("frameLength", frameLength);
        int length = frameLength / (int) fps;
        map.put("length", length);
        long duration = grabber.getLengthInTime() / (1000 * 1000);
        map.put("duration", duration);
        String format = grabber.getFormat();
        map.put("format", format);
        // 视频旋转的角度
        String rotate = grabber.getVideoMetadata("rotate");
        map.put("rotate", rotate);
        grabber.stop();
        return map;
    }

    /**
     * 取出指定位置的帧生成视频封面缩略图
     *
     * @param
     * @return
     * @date 2021-08-18 上午11:12
     */
    public static ByteArrayOutputStream thumbnailCover(File file) throws IOException {
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(file);
        grabber.start();
        int frameLength = grabber.getLengthInFrames();
        int interval = frameLength/16;
        List<Integer> frames = new ArrayList<>();
        for (int i = 1; i < frameLength; i += interval) {
            frames.add(i);
        }

        if (frames.size() > 16) {
            frames = frames.subList(0, 16);
        }

        List<List<BufferedImage>> lists = new ArrayList<>();
        List<BufferedImage> list = new ArrayList<>();
        Frame frame;
        for (int i : frames) {
            grabber.setFrameNumber(i);
            frame = grabber.grabImage();
            if (list.size() == 4) {
                lists.add(new ArrayList<>(list));
                list.clear();
            }

            if (frame != null) {
                // TODO 在截取的帧上添加视频中所处的时间水印
                list.add(ImageOps.resize(frameConverter.getBufferedImage(frame), 4));
            }
        }
        grabber.stop();
        if (list.size() == 4) {
            lists.add(new ArrayList<>(list));
            list.clear();
        }

        List<BufferedImage> list1 = new ArrayList<>();
        for (List<BufferedImage> tmp : lists) {
            list1.add(ImageOps.merge(tmp, false));
        }

        BufferedImage resultImage = ImageOps.merge(list1, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resultImage, "jpg", baos);
        return baos;
    }

    public static ByteArrayOutputStream videoCover(File file) throws IOException {
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(file);
        grabber.start();
        int frameNumber = grabber.getLengthInFrames()/100;
        grabber.setFrameNumber(frameNumber);
        // 截取帧总长度的 1/100 位置处作为视频封面
        Frame frame = grabber.grabImage();
        BufferedImage bufferedImage = frameConverter.getBufferedImage(frame);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);
        return baos;
    }

    public static void videoCover(File file, String savedFilePath) throws IOException {
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(file);
        grabber.start();
        // 截取第一帧作为视频封面
        Frame frame = grabber.grabImage();
        BufferedImage bufferedImage = frameConverter.getBufferedImage(frame);
        ImageOps.saveImage(bufferedImage, savedFilePath);
    }
}
