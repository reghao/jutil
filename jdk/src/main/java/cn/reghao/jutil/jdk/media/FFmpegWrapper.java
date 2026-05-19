package cn.reghao.jutil.jdk.media;

import cn.reghao.jutil.jdk.converter.DateTimeConverter;
import cn.reghao.jutil.jdk.serializer.JsonConverter;
import cn.reghao.jutil.jdk.media.model.AudioProps;
import cn.reghao.jutil.jdk.media.model.MediaProps;
import cn.reghao.jutil.jdk.media.model.VideoProps;
import cn.reghao.jutil.jdk.shell.Shell;
import cn.reghao.jutil.jdk.shell.ShellExecutor;
import cn.reghao.jutil.jdk.shell.ShellResult;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author reghao
 * @date 2022-03-04 11:04:32
 */
public class FFmpegWrapper {
    private final static String ffprobe = "/usr/bin/ffprobe";
    private final static String ffmpeg = "/usr/bin/ffmpeg";
    private static final Gson gson = new Gson();

    public static MediaProps getMediaProps(String srcPath) {
        // 构建全量探测命令
        List<String> cmd = Arrays.asList(
                ffprobe,
                "-v", "quiet",           // 不打印日志头
                "-print_format", "json", // 输出 JSON
                "-show_format",          // 容器格式信息 (bitrate, size, duration, tags)
                "-show_streams",         // 所有流 (video, audio, subtitle, data)
                "-show_chapters",        // 章节信息
                "-show_programs",        // 节目信息 (常用于 TS 流)
                "-show_error",           // 如果解析出错，输出错误 JSON
                srcPath
        );

        try {
            Process process = new ProcessBuilder(cmd).start();
            StringBuilder jsonOutput = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonOutput.append(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("FFprobe 解析失败，退出码: " + exitCode);
            }

            JsonObject jsonObject = JsonParser.parseString(jsonOutput.toString()).getAsJsonObject();
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
                    if (videoProps != null) {
                        continue;
                    }

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
            if (format.get("format_name") != null) {
                String formatName = format.get("format_name").getAsString();
                mediaProps.setFormatName(formatName);
            }

            if (format.get("format_long_name") != null) {
                String formatLongName = format.get("format_long_name").getAsString();
                mediaProps.setFormatLongName(formatLongName);
            }

            if (format.get("start_time") != null) {
                double startTime = format.get("start_time").getAsDouble();
                mediaProps.setStartTime(startTime);
            }

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
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void checkVideo(File inputFile) {
        List<String> command = Arrays.asList(
                ffmpeg, "-v", "error",
                "-i", inputFile.getAbsolutePath(),
                "-f", "null", "-"
        );

        try {
            ShellResult shellResult = ShellExecutor.executeWithResult(command);
            if (shellResult.getExitCode() != 0) {
                String errorMsg = String.format("exec failed");
                throw new RuntimeException(errorMsg);
            } else if (!shellResult.getStdout().isEmpty() || !shellResult.getStderr().isEmpty()) {
                String errorMsg = String.format("video %s invalid", inputFile.getAbsolutePath());
                throw new RuntimeException(errorMsg);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Deprecated
    public static int formatCovert(String srcPath, String destPath, String format) {
        String cmd = String.format("%s -loglevel error -y -i \"%s\" -c:a aac -c:v libx264 -f %s \"%s\"",
                ffmpeg, srcPath, format, destPath);
        return Shell.exec(cmd);
    }

    public static int convert2AacH264(String srcPath, String destPath, String format) {
        String cmd = String.format("%s -loglevel error -y " +
                        "-i \"%s\" " +
                        "-c:a aac -c:v libx264 " +
                        "-f %s \"%s\"",
                ffmpeg, srcPath, format, destPath);
        return Shell.exec(cmd);
    }

    public static int convert2AacH264(String srcPath, String destPath, String format, int subIndex) {
        String cmd = String.format("%s -loglevel error -y " +
                        "-i \"%s\" -vf subtitles=\"%s\":si=%s " +
                        "-c:a aac -c:v libx264 " +
                        "-f %s \"%s\"",
                ffmpeg, srcPath, srcPath, subIndex, format, destPath);
        return Shell.exec(cmd);
    }

    public static int convertAudio(String srcPath, String destPath) {
        String cmd = String.format("%s -loglevel error -y -i \"%s\" -c:a aac \"%s\"", ffmpeg, srcPath, destPath);
        return Shell.exec(cmd);
    }

    public static int qualityCovert(String srcPath, int width, int height, String destPath) {
        String audioBitRate = "128k";
        String videoBitRate = "1500k";
        String cmd = String.format("%s -loglevel error -i \"%s\" -s %sx%s -c:a aac -b:a %s -c:v libx264 -b:v %s -g 90 \"%s\"",
                ffmpeg, srcPath, width, height, audioBitRate, videoBitRate, destPath);
        return Shell.exec(cmd);
    }

    public static int split(String srcPath, String audioPath, String videoPath) {
        String cmd = String.format("%s -loglevel error -y -i \"%s\" -acodec copy -vn \"%s\"", ffmpeg, srcPath, audioPath);
        int ret = Shell.exec(cmd);
        if (ret != 0) {
            return ret;
        }

        String cmd1 = String.format("%s -loglevel error -y -i \"%s\" -vcodec copy –an \"%s\"", ffmpeg, srcPath, videoPath);
        int ret1 = Shell.exec(cmd1);
        if (ret1 != 0) {
            return ret1;
        }

        return -1;
    }

    public static int merge(String audioPath, String videoPath, String destPath) {
        String cmd = String.format("%s -loglevel error -y -i \"%s\" -i %s -codec copy \"%s\"",
                ffmpeg, audioPath, videoPath, destPath);
        return Shell.exec(cmd);
    }

    public static int covertToM3u8(String srcPath, String m3u8Path) {
        String cmd = String.format("%s -loglevel error -i \"%s\" -c:v libx264 -c:a aac -strict -2 " +
                "-f hls -hls_list_size 0 -hls_time 10 \"%s\"", ffmpeg, srcPath, m3u8Path);
        return Shell.exec(cmd);
    }

    /**
     * 将 m3u8 的 ts 分片合并为 mp4 文件
     *
     * @param tsListPath 格式
     * file a/b/0.ts
     * file a/b/1.ts
     * @return
     * @date 2025-07-09 17:37:07
     */
    public static int m3u8ToMp4(String tsListPath, String destPath) {
        String cmd = String.format("%s -loglevel error " +
                "-f concat -safe 0 -i \"%s\" -c:a aac -c:v libx264 \"%s\"", ffmpeg, tsListPath, destPath);
        return Shell.exec(cmd);
    }
}
