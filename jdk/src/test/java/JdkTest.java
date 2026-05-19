import cn.reghao.jutil.jdk.converter.ByteConverter;
import cn.reghao.jutil.jdk.converter.ByteType;
import cn.reghao.jutil.jdk.media.FFmpegWrapper;
import cn.reghao.jutil.jdk.media.model.MediaProps;
import cn.reghao.jutil.jdk.media.model.VideoProps;
import cn.reghao.jutil.jdk.string.StringUtil;

import java.io.File;

/**
 * @author reghao
 * @date 2025-09-26 14:06:56
 */
public class JdkTest {
    static void test() {
        ByteConverter byteConverter = new ByteConverter();

        long total = 510873600L;
        total = 235152568320L;
        String totalStr = byteConverter.convert(total);
        System.out.println(totalStr);
    }

    public static void main(String[] args) {
        String path = "/home/reghao/Downloads/1/video.mp4";
        //path = "/home/reghao/Downloads/26.mkv";
        File videoFile = new File(path);
        MediaProps mediaProps = FFmpegWrapper.getMediaProps(videoFile.getAbsolutePath());
        VideoProps videoProps = mediaProps.getVideoProps();
        if (videoProps != null) {
            double duration = videoProps.getDuration();
            System.out.println(StringUtil.formatSeconds(duration));

            //System.out.println(FFmpegWrapper.getVideoCover(videoFile));
            System.out.println(FFmpegWrapper.getSpriteCover(videoFile));
        }
    }
}
