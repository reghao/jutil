package cn.reghao.jutil.media;

import cn.reghao.jutil.jdk.converter.DateTimeConverter;
import cn.reghao.jutil.jdk.serializer.JsonConverter;
import cn.reghao.jutil.jdk.shell.Shell;
import cn.reghao.jutil.jdk.shell.ShellExecutor;
import cn.reghao.jutil.jdk.shell.ShellResult;
import cn.reghao.jutil.media.model.AudioProps;
import cn.reghao.jutil.media.model.MediaProps;
import cn.reghao.jutil.media.model.VideoProps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDateTime;

/**
 * @author reghao
 * @date 2022-03-04 11:04:32
 */
public class FFmpegWrapper {
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

                    JsonElement bitRateElement = jsonObject1.get("bit_rate");
                    double bitRate;
                    if (bitRateElement == null) {
                        bitRate = 0.0;
                    } else {
                        bitRate = bitRateElement.getAsDouble();
                    }

                    JsonElement durationElement = jsonObject1.get("duration");
                    double duration;
                    if (durationElement == null) {
                        duration = 0;
                    } else {
                        duration = durationElement.getAsDouble();
                    }
                    audioProps = new AudioProps(codecName, codecTagString, bitRate, duration);
                } else if (codecType.equals("video")) {
                    String codecName = jsonObject1.get("codec_name").getAsString();
                    String codecTagString = jsonObject1.get("codec_tag_string").getAsString();

                    double bitRate;
                    JsonElement biteRateElement = jsonObject1.get("bit_rate");
                    if (biteRateElement == null) {
                        bitRate = 0;
                    } else {
                        bitRate = biteRateElement.getAsDouble();
                    }

                    double duration;
                    JsonElement durationElement = jsonObject1.get("duration");
                    if (durationElement == null) {
                        duration = 0;
                    } else {
                        duration = durationElement.getAsDouble();
                    }

                    double codedWidth = jsonObject1.get("coded_width").getAsDouble();
                    double codedHeight = jsonObject1.get("coded_height").getAsDouble();
                    videoProps = new VideoProps(codecName, codecTagString, bitRate, duration, codedWidth, codedHeight);
                }
            }

            if (videoProps == null) {
                return null;
            }

            JsonObject format = jsonObject.get("format").getAsJsonObject();
            if (format.get("duration") != null) {
                Double duration = format.get("duration").getAsDouble();
                videoProps.setDuration(duration);
            }

            Long size = format.get("size").getAsLong();
            if (format.get("bit_rate") != null) {
                Double bitRate = format.get("bit_rate").getAsDouble();
                videoProps.setBitRate(bitRate);
            }

            MediaProps mediaProps = new MediaProps(audioProps, videoProps);
            JsonElement tagsElement = format.get("tags");
            if (tagsElement != null) {
                JsonObject tags = tagsElement.getAsJsonObject();
                JsonElement jsonElement = tags.get("creation_time");
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
        String cmd = String.format("%s -y -i %s -c:a aac -c:v libx264 -f mp4 %s", ffmpeg, src, dest);
        return Shell.exec(cmd);
    }

    public static int qualityCovert(String src, int width, int height, String dest) {
        String audioBitRate = "128k";
        String videoBitRate = "1500k";
        String cmd = String.format("%s -i %s -s %sx%s -c:a aac -b:a %s -c:v libx264 -b:v %s -g 90 %s",
                ffmpeg, src, width, height, audioBitRate, videoBitRate, dest);
        return Shell.exec(cmd);
    }

    public static void mp4ToM3u8() {

    }

    public static void m3u8ToMp4(String dir, String videoId, String videoFilePath, String audioFilePath) throws Exception {
        String mp4FilePath = String.format("%s/%s.mp4", dir, videoId);

        StringBuilder sb = new StringBuilder();
        sb.append("ffmpeg -i ").append(audioFilePath).append(" ")
                .append("-i ").append(videoFilePath).append(" ")
                .append("-codec copy ").append(mp4FilePath);
        Shell.exec(sb.toString());
    }
}
