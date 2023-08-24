package cn.reghao.jutil.media;

import cn.reghao.jutil.jdk.converter.DateTimeConverter;
import cn.reghao.jutil.jdk.serializer.JsonConverter;
import cn.reghao.jutil.jdk.shell.Shell;
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

    public static MediaProps getMediaProps(String srcPath) {
        String cmd = String.format("%s -v quiet -print_format json -show_format -show_streams -i '%s'", ffprobe, srcPath);
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
                    long bitRate;
                    if (bitRateElement == null) {
                        bitRate = 0L;
                    } else {
                        bitRate = bitRateElement.getAsLong();
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

                    long bitRate;
                    JsonElement biteRateElement = jsonObject1.get("bit_rate");
                    if (biteRateElement == null) {
                        bitRate = 0;
                    } else {
                        bitRate = biteRateElement.getAsLong();
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

            JsonObject format = jsonObject.get("format").getAsJsonObject();
            if (videoProps != null) {
                if (format.get("duration") != null) {
                    Double duration = format.get("duration").getAsDouble();
                    videoProps.setDuration(duration);
                }

                Long size = format.get("size").getAsLong();
                if (format.get("bit_rate") != null) {
                    long bitRate = format.get("bit_rate").getAsLong();
                    if (videoProps.getBitRate() == 0) {
                        videoProps.setBitRate(bitRate);
                    }
                }
            }

            MediaProps mediaProps = new MediaProps(audioProps, videoProps);
            // Metadata
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

    public static int formatCovert(String srcPath, String destPath, String format) {
        String cmd = String.format("%s -loglevel error -y -i '%s' -c:a aac -c:v libx264 -f %s '%s'",
                ffmpeg, srcPath, format, destPath);
        return Shell.exec(cmd);
    }

    public static int convertAudio(String srcPath, String destPath) {
        String cmd = String.format("%s -loglevel error -y -i '%s' -c:a aac '%s'", ffmpeg, srcPath, destPath);
        return Shell.exec(cmd);
    }

    public static int qualityCovert(String srcPath, int width, int height, String destPath) {
        String audioBitRate = "128k";
        String videoBitRate = "1500k";
        String cmd = String.format("%s -loglevel error -i '%s' -s %sx%s -c:a aac -b:a %s -c:v libx264 -b:v %s -g 90 '%s'",
                ffmpeg, srcPath, width, height, audioBitRate, videoBitRate, destPath);
        return Shell.exec(cmd);
    }

    public static int split(String srcPath, String audioPath, String videoPath) {
        String cmd = String.format("%s -loglevel error -y -i '%s' -acodec copy -vn '%s'", ffmpeg, srcPath, audioPath);
        int ret = Shell.exec(cmd);
        if (ret != 0) {
            return ret;
        }

        String cmd1 = String.format("%s -loglevel error -y -i '%s' -vcodec copy –an '%s'", ffmpeg, srcPath, videoPath);
        int ret1 = Shell.exec(cmd1);
        if (ret1 != 0) {
            return ret1;
        }

        return -1;
    }

    public static int merge(String audioPath, String videoPath, String destPath) {
        String cmd = String.format("%s -loglevel error -y -i '%s' -i %s -codec copy '%s'",
                ffmpeg, audioPath, videoPath, destPath);
        return Shell.exec(cmd);
    }

    public static int covertToM3u8(String srcPath, String m3u8Path) {
        String cmd = String.format("%s -loglevel error -i '%s' -c:v libx264 -c:a aac -strict -2 " +
                "-f hls -hls_list_size 0 -hls_time 10 '%s'", ffmpeg, srcPath, m3u8Path);
        return Shell.exec(cmd);
    }

    public static int m3u8ToMp4(String m3u8Dir, String destPath) {
        String cmd = String.format("%s -allowed_extensions ALL -protocol_whitelist \"file,http,crypto,tcp\" " +
                "-i '%s' -c:a aac -c:v libx264 -f mp4 '%s'", ffmpeg, m3u8Dir, destPath);
        return Shell.exec(cmd);
    }
}
