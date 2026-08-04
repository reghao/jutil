package cn.reghao.jutil.jdk.media;

import cn.reghao.jutil.jdk.converter.DateTimeConverter;
import cn.reghao.jutil.jdk.media.model.WebVideoCheck;
import cn.reghao.jutil.jdk.media.model.AudioProps;
import cn.reghao.jutil.jdk.media.model.MediaProps;
import cn.reghao.jutil.jdk.media.model.VideoProps;
import cn.reghao.jutil.jdk.shell.Shell;
import cn.reghao.jutil.jdk.shell.ShellExecutor;
import cn.reghao.jutil.jdk.shell.ShellResult;
import cn.reghao.jutil.jdk.shell.handler.ConvertVideoOutputHandler;
import cn.reghao.jutil.jdk.shell.handler.EmptyHandler;
import cn.reghao.jutil.jdk.shell.handler.OutputHandler;
import cn.reghao.jutil.jdk.string.StringUtil;
import com.google.gson.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;

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

            String json = jsonOutput.toString();
            return parseAndGetMediaProps(json);
        } catch (Exception e) {
            String errorMsg = String.format("%s\n%s", cmd, e.getMessage());
            throw new RuntimeException(errorMsg);
        }
    }

    public static MediaProps parseAndGetMediaProps(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray streams = jsonObject.get("streams").getAsJsonArray();
        AudioProps audioProps = null;
        VideoProps videoProps = null;
        for (JsonElement jsonElement : streams) {
            JsonObject jsonObject1 = jsonElement.getAsJsonObject();
            String codecType = jsonObject1.get("codec_type").getAsString();
            if (codecType.equals("audio")) {
                if (jsonObject1.get("codec_name") == null) {
                    continue;
                }

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
                String pixFmt = "";
                if (jsonObject1.get("pix_fmt") != null) {
                    pixFmt = jsonObject1.get("pix_fmt").getAsString();
                }

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
                videoProps = new VideoProps(codecName, codecTagString, bitRate, duration, codedWidth, codedHeight, pixFmt);
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

        if (format.get("tags") != null && format.get("tags").getAsJsonObject().get("major_brand") != null) {
            String majorBrand = format.get("tags").getAsJsonObject().get("major_brand").getAsString();
            mediaProps.setMajorBrand(majorBrand);
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
    }

    public static void checkVideoContent(File inputFile) {
        List<String> command = Arrays.asList(
                ffmpeg, "-v", "error",
                "-i", inputFile.getAbsolutePath(),
                "-f", "null", "-"
        );

        try {
            ShellResult shellResult = ShellExecutor.executeWithResult(command);
            if (shellResult.getExitCode() != 0) {
                String stdout = shellResult.getStdout();
                String stderr = shellResult.getStderr();
                String errorMsg = String.format("video %s invalid\nstdout: %s\nstderr: %s\n",
                        inputFile.getAbsolutePath(), stdout, stderr);
                throw new RuntimeException(errorMsg);
            }
        } catch (Exception e) {
            String errorMsg = String.format("exec command %s throw exception, error message: %s", command, e.getMessage());
            throw new RuntimeException(errorMsg);
        }
    }

    public static WebVideoCheck checkWebVideo(MediaProps mediaProps, File file) {
        WebVideoCheck webVideoCheck = new WebVideoCheck();
        VideoProps videoProps = mediaProps.getVideoProps();
        if (videoProps != null) {
            String vcodec = videoProps.getCodecName();
            // 检查视频编码
            if ("h264".equals(vcodec) || "vp9".equals(vcodec)) {
                webVideoCheck.setVcodec(true);
            }

            // 检查像素格式
            if ("yuv420p".equals(videoProps.getPixFmt())) {
                webVideoCheck.setPixFormat(true);
            }
        }

        AudioProps audioProps = mediaProps.getAudioProps();
        if (audioProps != null) {
            String acodec = audioProps.getCodecName();
            // 检查音频编码
            if ("aac".equals(acodec) || "opus".equals(acodec) || "mp3".equals(acodec)) {
                webVideoCheck.setAcodec(true);
            }
        } else {
            webVideoCheck.setAcodec(true);
        }

        String majorBrand = mediaProps.getMajorBrand();
        String formatName = mediaProps.getFormatName();
        // 检查封装格式
        if (majorBrand != null && (majorBrand.equals("isom") || majorBrand.equals("mp42") || majorBrand.equals("mp41"))) {
            webVideoCheck.setVideoFormat(true);
        } else if (formatName != null && (formatName.contains(".mp4") || formatName.contains(".webm"))) {
            //System.out.println("🟢 [封装格式]: " + ext + " (符合标准)");
            webVideoCheck.setVideoFormat(true);
        }

        String videoPath = file.getAbsolutePath();
        // 3. 获取 moov/mdat 位置信息
        List<String> structureInfo = checkFastStart(videoPath);
        // 检查是否支持边下边播 (FastStart)
        if (structureInfo.size() >= 2) {
            String firstLine = structureInfo.get(0);
            if (firstLine.contains("type:'moov'")) {
                webVideoCheck.setFastStart(true);
            }
        }
        return webVideoCheck;
    }

    private static List<String> checkFastStart(String videoPath) {
        List<String> output = new ArrayList<>();
        // 1. 命令中不再拼接 videoPath，而是使用 Bash 变量 "$VIDEO_PATH"
        // 这样 Bash 会强制将其视为一个整体，绝对不会发生字符串切分
        String cmd = "ffprobe -v trace \"$VIDEO_PATH\" 2>&1 | grep -E \"type:'moov'|type:'mdat'\" | head -n 2";

        List<String> commands = new ArrayList<>();
        commands.add("/bin/bash");
        commands.add("-c");
        commands.add(cmd);

        try {
            ProcessBuilder pb = new ProcessBuilder(commands);

            // 2. 核心注入：把复杂的文件名丢进环境变量里
            Map<String, String> env = pb.environment();
            env.put("VIDEO_PATH", videoPath);

            pb.redirectErrorStream(true);
            Process process = pb.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.add(line);
                }
            }
            process.waitFor();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return output;
    }

    public static String getVideoCover(File videoFile) {
        String videoPath = videoFile.getAbsolutePath();
        String coverPath = String.format("%s/%s.jpg", videoFile.getParent(), UUID.randomUUID().toString().replace("-", ""));
        VideoProps videoProps = getMediaProps(videoFile.getAbsolutePath()).getVideoProps();
        double duration = videoProps.getDuration();
        String arg = "00:00:02";
        if (duration > 10.0) {
            arg = StringUtil.formatSeconds(duration/2.0);
        }

        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-y");
        command.add("-hide_banner");
        command.add("-ss");
        //command.add("00:00:02"); // 毫秒级快进，避开黑屏
        command.add(arg);
        command.add("-i");
        command.add(videoPath);
        command.add("-vf");
        command.add("select='eq(pict_type,I)'"); // 锁死关键帧并缩放
        command.add("-vframes");
        command.add("1");
        command.add("-q:v");
        command.add("5"); // 兼顾体积与画质
        command.add("-fps_mode");
        command.add("vfr"); // 【新版语法】防止凑帧，极速退出
        command.add("-update");
        command.add("1"); // 强制复写单张图
        command.add(coverPath);

        try {
            ShellResult shellResult = ShellExecutor.executeWithResult(command);
            if (shellResult.getExitCode() == 0) {
                return coverPath;
            }

            String errorMsg = shellResult.getStdout() +
                    System.lineSeparator() +
                    shellResult.getStderr();
            throw new RuntimeException(errorMsg);
        } catch (Exception e) {
            String errorMsg = String.format("exec command %s throw exception, error message: %s", command, e.getMessage());
            throw new RuntimeException(errorMsg);
        }
    }

    public static String getSpriteCover(File videoFile) {
        List<String> command0 = new ArrayList<>();
        command0.add(ffprobe);
        command0.add("-y");
        command0.add("-hide_banner");
        command0.add("-v");
        command0.add("error");
        command0.add("-select_streams");
        command0.add("v:0");
        command0.add("-count_frames");
        command0.add("-show_entries");
        command0.add("stream=nb_read_frames");
        command0.add("-skip_frame");
        command0.add("nokey");
        command0.add("-of");
        command0.add("default=nokey=1:noprint_wrappers=1");
        command0.add(videoFile.getAbsolutePath());
        int keyFrameCount = 0;
        try {
            ShellResult shellResult = ShellExecutor.executeWithResult(command0);
            if (shellResult.getExitCode() != 0) {
                String errorMsg = shellResult.getStdout() +
                        System.lineSeparator() +
                        shellResult.getStderr();
                throw new RuntimeException(errorMsg);
            }

            String totalStr = shellResult.getStdout()
                    .replaceAll("\\s+", "")
                    .replace("\"", "");
            keyFrameCount = Integer.parseInt(totalStr);
        } catch (Exception e) {
            String errorMsg = String.format("exec command %s throw exception, error message: %s", command0, e.getMessage());
            throw new RuntimeException(errorMsg);
        }

        String videoPath = videoFile.getAbsolutePath();
        String coverPath = String.format("%s/%s.jpg", videoFile.getParent(), UUID.randomUUID().toString().replace("-", ""));
        VideoProps videoProps = getMediaProps(videoFile.getAbsolutePath()).getVideoProps();
        double duration = videoProps.getDuration();

        double codedWidth = videoProps.getCodedWidth();
        double codedHeight = videoProps.getCodedHeight();
        double width, height;
        int rows, cols;
        if (duration < 60) {
            // 1min
            width = codedWidth;
            height = codedHeight;
            rows = 1;
            cols = 1;
        } else if (duration < 180) {
            // 1min ~ 3min
            width = codedWidth/2;
            height = codedHeight/2;
            rows = 2;
            cols = 2;
        } else if (duration < 300) {
            // 3min ~ 5min
            width = codedWidth/3;
            height = codedHeight/3;
            rows = 3;
            cols = 3;
        } else if (duration < 600) {
            // 5min ~ 10min
            width = codedWidth/4;
            height = codedHeight/4;
            rows = 4;
            cols = 4;
        } else if (duration < 1200) {
            // 10min ~ 20min
            width = codedWidth/5;
            height = codedHeight/5;
            rows = 5;
            cols = 5;
        } else {
            // > 20min
            width = codedWidth/6;
            height = codedHeight/6;
            rows = 6;
            cols = 6;
        }
        int totalGrid = rows * cols;

        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-y");
        command.add("-hide_banner");
        if (totalGrid == 1) {
            String arg = "00:00:02";
            if (duration > 5.0) {
                arg = StringUtil.formatSeconds(duration/2.0);
            }

            command.add("-ss");
            command.add(arg);
            command.add("-i");
            command.add(videoPath);
            command.add("-vf");
            command.add("select='eq(pict_type,I)'");
        } else {
            command.add("-skip_frame");
            command.add("nokey");
            command.add("-i");
            command.add(videoPath);
            command.add("-an");
            command.add("-sn");

            int interval = keyFrameCount / totalGrid;
            /*String fps = String.format("%.4f", keyFrameCount / totalGrid);
            String filter = String.format("fps=%s,scale=%s:%s,tile=%dx%d:nb_frames=0:padding=4:color=white",
                    fps, width, height, cols, rows);*/
            String filter = String.format("select='not(mod(n,%s))',scale=%s:%s,tile=%dx%d:padding=4:color=white",
                    interval, width, height, cols, rows);
            command.add("-vf");
            command.add(filter);
        }
        // 通用尾部参数
        command.add("-vframes");
        command.add("1");
        command.add("-q:v");
        command.add("5"); // 兼顾体积与画质
        command.add("-fps_mode");
        command.add("vfr"); // 【新版语法】防止凑帧，极速退出
        command.add("-update");
        command.add("1"); // 强制复写单张图
        command.add(coverPath);

        try {
            ShellResult shellResult = ShellExecutor.executeWithResult(command);
            if (shellResult.getExitCode() == 0) {
                return coverPath;
            }

            String errorMsg = shellResult.getStdout() +
                    System.lineSeparator() +
                    shellResult.getStderr();
            throw new RuntimeException(errorMsg);
        } catch (Exception e) {
            String errorMsg = String.format("exec command %s throw exception, error message: %s", command, e.getMessage());
            throw new RuntimeException(errorMsg);
        }
    }

    public static void convertWithWatermark(File videoFile, double duration, File outputFile) {
        // 文件名格式 rtmp_1778685159.flv
        String filename = videoFile.getName();
        String inputPath = videoFile.getAbsolutePath();
        String outputPath = outputFile.getAbsolutePath();
        String timestampStr = filename.replace(".flv", "").replace("rtmp_", "");

        // 1. 提取出你需要动态传入的参数
        String fontPath = "/usr/share/fonts/truetype/wqy/wqy-microhei.ttc";
        long timestamp = Long.parseLong(timestampStr);
        String watermarkText = "在线录制";

        // 2. 编写 String.format 的模板字符串
        // 注意：其中的 %%Y、%%m、%%d、%%H、%%M、%%S 是为了防止 Java 报错而做的转义
        String filterTemplate = "format=yuv420p," +
                "drawtext=fontfile='%s':text='%%{pts\\:localtime\\:%d\\:%%Y-%%m-%%d %%H\\\\\\:%%M\\\\\\:%%S}':x=w-tw-30:y=30:fontsize=28:fontcolor=white:alpha=0.9:borderw=1:bordercolor=black@0.4:shadowy=1:shadowcolor=black@0.3," +
                "drawtext=fontfile='%s':text='%s':x=w-tw-30:y=h-th-30:fontsize=22:fontcolor=white:alpha=0.8:borderw=1:bordercolor=black@0.4:shadowy=1:shadowcolor=black@0.3";
        // 3. 格式化并生成最终的滤镜字符串
        String finalFilterString = String.format(filterTemplate, fontPath, timestamp, fontPath, watermarkText);

        // 1. 构建 FFmpeg 参数列表
        List<String> command = new ArrayList<>();
        command.add("ffmpeg");
        command.add("-y");
        command.add("-hwaccel");
        command.add("cuda");
        command.add("-i");
        command.add(inputPath);

        // 核心避坑点：整个 -vf 的内容必须是一个紧凑的字符串，去掉终端里的换行符和多余空格
        command.add("-vf");
        command.add(finalFilterString);

        // 编码与画质控制参数
        command.add("-c:v");
        command.add("h264_nvenc");
        command.add("-preset");
        command.add("p7");
        command.add("-tune");
        command.add("hq");
        command.add("-rc");
        command.add("constqp");
        command.add("-cq");
        command.add("11");
        command.add("-spatial_aq");
        command.add("1");
        command.add("-temporal_aq");
        command.add("1");

        // 音频与 Web 优化参数
        command.add("-c:a");
        command.add("aac");
        command.add("-b:a");
        command.add("128k");
        command.add("-movflags");
        command.add("+faststart");
        // 输出文件
        command.add(outputPath);

        try {
            OutputHandler stdoutHandler = new EmptyHandler();
            OutputHandler stderrHandler = new ConvertVideoOutputHandler(duration);
            int exitCode = ShellExecutor.executeFFmpeg(command, stdoutHandler, stderrHandler);
            if (exitCode != 0) {
                throw new RuntimeException("convert video failed...");
            }
        } catch (Exception e) {
            String errorMsg = String.format("exec command %s throw exception, error message: %s", command, e.getMessage());
            throw new RuntimeException(errorMsg);
        }
    }

    public static List<Double> getSceneTimestamps(String videoPath) throws Exception {
        List<Double> timestamps = new ArrayList<>();
        timestamps.add(0.0); // 默认从0秒开始

        int threshold = 20;
        // 使用 scdet 滤镜，并将信息输出到 stdout/stderr
        List<String> cmd = Arrays.asList(
                ffmpeg, "-i", videoPath,
                "-vf", "scdet=threshold=" + threshold,
                "-f", "null", "-"
        );
        ProcessBuilder pb = new ProcessBuilder(cmd);
        Process process = pb.start();
        // 读取日志流
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            // 匹配日志行：[parsed_scdet_0 @ 0x...] lavfi.scd.time: 12.456
            // 在异步读取日志时，正则匹配 "lavfi.scd.time: ([0-9.]+)"
            // 提取出来的 time 就是场景切换的具体秒数
            if (line.contains("lavfi.scd.time:")) {
                String timeStr = line.substring(line.lastIndexOf(":") + 1).trim();
                timestamps.add(Double.parseDouble(timeStr));
            }
        }
        process.waitFor();
        return timestamps;
    }

    public static void splitByScenes(String input, List<Double> points) {
        File file = new File(input);
        String parentPath = file.getParent();
        String filename = file.getName();
        MediaProps mediaProps = getMediaProps(input);
        double total = mediaProps.getVideoProps().getDuration();
        for (int i = 0; i < points.size(); i++) {
            double start = points.get(i);
            double total0 = 0.0;
            if (i+1 == points.size()) {
                total0 = total - start;
            } else {
                total0 = points.get(i+1) - start;
            }
            // 如果是最后一段，时长由 ffprobe 获取的总长度决定，这里简化演示
            double duration = (i < points.size() - 1) ? (points.get(i + 1) - start) : -1;

            String output = String.format("%s/%s_part%s.mp4", parentPath, filename, i);
            // 构造精准剪切命令
            List<String> command = new ArrayList<>(Arrays.asList(
                    ffmpeg, "-hide_banner", "-y",
                    "-hwaccel", "cuda",
                    "-ss", String.valueOf(start),
                    "-i", input
            ));
            if (duration != -1) {
                command.add("-t");
                command.add(String.valueOf(duration));
            }
            command.addAll(Arrays.asList(
                    "-c:v", "h264_nvenc",
                    "-cq", "19",
                    "-preset", "slow",
                    "-pix_fmt", "yuv420p",
                    "-c:a", "aac",
                    "-b:a", "256k",
                    "-movflags", "+faststart",
                    output
            ));

            try {
                OutputHandler stdoutHandler = new EmptyHandler();
                OutputHandler stderrHandler = new ConvertVideoOutputHandler(total0);
                int exitCode = ShellExecutor.executeFFmpeg(command, stdoutHandler, stderrHandler);
                if (exitCode != 0) {
                    String errorMsg = String.format("exec command %s failed", command);
                    throw new RuntimeException(errorMsg);
                }
            } catch (Exception e) {
                String errorMsg = String.format("exec command %s throw exception, error message: %s", command, e.getMessage());
                throw new RuntimeException(errorMsg);
            }
        }
    }

    public static void fixVideoFastStart(File inputFile, File outputFile) {
        List<String> command = Arrays.asList(
                ffmpeg, "-y", "-hide_banner",
                "-i", inputFile.getAbsolutePath(),
                "-c", "copy",
                "-movflags", "+faststart",
                outputFile.getAbsolutePath()
        );

        try {
            OutputHandler stdoutHandler = new EmptyHandler();
            int exitCode = ShellExecutor.executeFFmpeg(command, stdoutHandler, stdoutHandler);
            if (exitCode != 0) {
                String errorMsg = String.format("exec command %s failed", command);
                throw new RuntimeException(errorMsg);
            }
        } catch (Exception e) {
            String errorMsg = String.format("exec command %s throw exception, error message: %s", command, e.getMessage());
            throw new RuntimeException(errorMsg);
        }
    }

    // cpu 转码, gop 5s
    public static void convertToWebVideo(File inputFile, File outputFile, double duration) {
        List<String> command = Arrays.asList(
                ffmpeg, "-y", "-hide_banner",
                "-i", inputFile.getAbsolutePath(),
                "-vf", "scale=in_range=pc:out_range=tv,format=yuv420p",
                "-c:v", "libx264",
                "-profile:v", "high",
                "-preset", "medium",
                // --- 画质设置：视觉无损核心参数 ---
                "-crf", "17",                                  // 设为 17 或 18，实现肉眼无损
                "-maxrate", "50M",                             // 设定码率上限，防止复杂画面下码率过高
                "-bufsize", "100M",
                // --- GOP 5秒 核心参数 ---
                "-force_key_frames", "expr:gte(t,n_forced*5)",
                "-sc_threshold", "0",

                "-pix_fmt", "yuv420p",
                "-color_range", "tv",
                "-color_primaries", "bt709",
                "-color_trc", "bt709",
                "-colorspace", "bt709",
                "-c:a", "copy",                                // 音频直接复制，100% 绝对无损
                "-movflags", "+faststart",
                outputFile.getAbsolutePath()
        );

        try {
            OutputHandler stdoutHandler = new EmptyHandler();
            OutputHandler stderrHandler = new ConvertVideoOutputHandler(duration);
            int exitCode = ShellExecutor.executeFFmpeg(command, stdoutHandler, stderrHandler);
            if (exitCode != 0) {
                String errorMsg = String.format("exec command %s failed", command);
                throw new RuntimeException(errorMsg);
            }
        } catch (Exception e) {
            String errorMsg = String.format("exec command %s throw exception, error message: %s", command, e.getMessage());
            throw new RuntimeException(errorMsg);
        }
    }

    public static void convertToWebVideoByCuda(File inputFile, File outputFile, double duration) {
        List<String> command = Arrays.asList(
                ffmpeg, "-y", "-hide_banner",
                "-i", inputFile.getAbsolutePath(),
                // 使用 scale 滤镜将输入的 pc 范围强制转为 tv 范围，并指定输出格式为 yuv420p
                "-vf", "scale=in_range=pc:out_range=tv,format=yuv420p",
                "-c:v", "h264_nvenc",
                "-profile:v", "high",
                "-pix_fmt", "yuv420p",
                // 显式设置颜色范围元数据为 limited (tv)，防止播放器误判
                "-color_range", "tv",
                "-rc", "constqp",
                "-cq", "19",
                "-c:a", "aac",
                "-b:a", "256k",
                "-movflags", "+faststart",
                outputFile.getAbsolutePath()
        );

        try {
            OutputHandler stdoutHandler = new EmptyHandler();
            OutputHandler stderrHandler = new ConvertVideoOutputHandler(duration);
            int exitCode = ShellExecutor.executeFFmpeg(command, stdoutHandler, stderrHandler);
            if (exitCode != 0) {
                String errorMsg = String.format("exec command %s failed", command);
                throw new RuntimeException(errorMsg);
            }
        } catch (Exception e) {
            String errorMsg = String.format("exec command %s throw exception, error message: %s", command, e.getMessage());
            throw new RuntimeException(errorMsg);
        }
    }

    // 转码监控视频
    public static void convertFlvToWebVideo(File inputFile, File outputFile, double duration) {
        List<String> command = Arrays.asList(
                ffmpeg, "-y",
                "-hwaccel", "cuda",
                "-i", inputFile.getAbsolutePath(),
                "-c:v", "h264_nvenc",
                "-preset", "p6",
                "-rc", "vbr",
                "-cq", "32",
                "-qmin", "30",
                "-g", "125",
                "-keyint_min", "125",
                "-no-scenecut", "1",
                "-bf", "3",
                "-b_ref_mode", "middle",
                "-pix_fmt", "yuv420p",
                "-movflags", "+faststart",
                "-c:a", "copy",
                "-f", "mp4",
                outputFile.getAbsolutePath()
        );

        try {
            OutputHandler stdoutHandler = new EmptyHandler();
            OutputHandler stderrHandler = new ConvertVideoOutputHandler(duration);
            int exitCode = ShellExecutor.executeFFmpeg(command, stdoutHandler, stderrHandler);
            if (exitCode != 0) {
                String errorMsg = String.format("exec command %s failed", command);
                throw new RuntimeException(errorMsg);
            }
        } catch (Exception e) {
            String errorMsg = String.format("exec command %s throw exception, error message: %s", command, e.getMessage());
            throw new RuntimeException(errorMsg);
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
