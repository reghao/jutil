package cn.reghao.jutil.media.video;

import cn.reghao.jutil.jdk.converter.DateTimeConverter;
import cn.reghao.jutil.jdk.serializer.JsonConverter;
import cn.reghao.jutil.jdk.shell.ShellExecutor;
import cn.reghao.jutil.jdk.shell.ShellResult;
import cn.reghao.jutil.media.Shell;
import cn.reghao.jutil.media.po.AudioProps;
import cn.reghao.jutil.media.po.MediaProps;
import cn.reghao.jutil.media.po.VideoProps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDateTime;

/**
 * @author reghao
 * @date 2022-03-04 11:04:32
 */
public class FFmpegWrapper {
    static ShellExecutor shellExecutor = new ShellExecutor();
    private final static String ffprobe = "/usr/bin/ffprobe";
    private final static String ffmpeg = "/usr/bin/ffmpeg";

    public static MediaProps getMediaProps(String src) {
        String cmd = String.format("%s -v quiet -print_format json -show_format -show_streams -i \"%s\"", ffprobe, src);
        String result = Shell.execWithResult(cmd);
        if (result != null) {
            JsonObject jsonObject = JsonConverter.jsonToJsonElement(result).getAsJsonObject();
            JsonArray streams = jsonObject.get("streams").getAsJsonArray();
            AudioProps audioProps = null;
            VideoProps videoProps = null;
            for (JsonElement jsonElement : streams) {
                JsonObject jsonObject1 = jsonElement.getAsJsonObject();
                String codecType = jsonObject1.get("codec_type").getAsString();
                if (codecType.equals("audio")) {
                    String codecName = jsonObject1.get("codec_name").getAsString();
                    String codecTagString = jsonObject1.get("codec_tag_string").getAsString();
                    double bitRate = jsonObject1.get("bit_rate").getAsDouble();
                    double duration = jsonObject1.get("duration").getAsDouble();
                    audioProps = new AudioProps(codecName, codecTagString, bitRate, duration);
                } else if (codecType.equals("video")) {
                    String codecName = jsonObject1.get("codec_name").getAsString();
                    String codecTagString = jsonObject1.get("codec_tag_string").getAsString();
                    double bitRate = jsonObject1.get("bit_rate").getAsDouble();
                    double duration = jsonObject1.get("duration").getAsDouble();
                    double codedWidth = jsonObject1.get("coded_width").getAsDouble();
                    double codedHeight = jsonObject1.get("coded_height").getAsDouble();
                    videoProps = new VideoProps(codecName, codecTagString, bitRate, duration, codedWidth, codedHeight);
                }
            }

            JsonObject format = jsonObject.get("format").getAsJsonObject();
            double duration = format.get("duration").getAsDouble();
            double size = format.get("size").getAsDouble();
            double bitRate = format.get("bit_rate").getAsDouble();

            MediaProps mediaProps = new MediaProps(audioProps, videoProps);
            JsonElement tagsElement = format.get("tags");
            if (tagsElement != null) {
                JsonElement jsonElement = tagsElement.getAsJsonObject().get("creation_time");
                if (jsonElement != null) {
                    String creationTime = jsonElement.getAsString();
                    LocalDateTime localDateTime = DateTimeConverter.localDateTime(creationTime);
                    mediaProps.setCreateTime(localDateTime);
                }
            }

            return mediaProps;
        }
        return null;
    }

    public static int formatCovert(String src, String dest) {
        String cmd = String.format("%s -y -i %s -c:a aac -c:v libx264 %s", ffmpeg, src, dest);
        return Shell.exec(cmd);
    }

    public static int qualityCovert(String src, int width, int height, String dest) {
        String audioBitRate = "128k";
        String videoBitRate = "1500k";
        String cmd = String.format("%s -i %s -s %sx%s -c:a aac -b:a %s -c:v libx264 -b:v %s -g 90 %s",
                ffmpeg, src, width, height, audioBitRate, videoBitRate, dest);
        return Shell.exec(cmd);
    }

    public static void mergeToMp4(String dir, String videoId, String videoFilePath, String audioFilePath) throws Exception {
        String mp4FilePath = String.format("%s/%s.mp4", dir, videoId);

        StringBuilder sb = new StringBuilder();
        sb.append("ffmpeg -i ").append(audioFilePath).append(" ")
                .append("-i ").append(videoFilePath).append(" ")
                .append("-codec copy ").append(mp4FilePath);
        ShellResult shellResult = shellExecutor.exec(dir, sb.toString().split("\\s+"));
        if (!shellResult.isSuccess()) {
            throw new Exception("合并成 mp4 文件异常: " + shellResult.getResult());
        }
    }

    public static void generateDash(String dir, String video, String audio) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("MP4Box -dash 5000 -rap -frag-rap -profile dashavc264:onDemand -frag 5000 ")
                .append(video).append(" ").append(audio).append(" ")
                .append("-out index.mpd");
        ShellResult shellResult = shellExecutor.exec(dir, sb.toString().split("\\s+"));
        if (!shellResult.isSuccess()) {
            throw new Exception("生成 dash 异常: " + shellResult.getResult());
        }
    }
}
