package cn.reghao.jutil.jdk.media;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class VideoWebChecker {
    /**
     * 辅助方法：从 ffprobe 输出的 key=value 列表中提取指定 key 的值
     */
    private static String getValue(List<String> lines, String key) {
        for (String line : lines) {
            if (line.startsWith(key + "=")) {
                return line.split("=")[1].trim();
            }
        }
        return "";
    }

    /**
     * 在 Linux 环境下执行 Shell 命令并捕获返回文本
     */
    private static List<String> executeLinuxCmd(String cmd) {
        List<String> output = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        commands.add("/bin/bash");
        commands.add("-c");
        commands.add(cmd);

        try {
            ProcessBuilder pb = new ProcessBuilder(commands);
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
            e.printStackTrace();
        }
        return output;
    }

    public static void checkWebVideo(File file) {
        String videoPath = file.getAbsolutePath();
        System.out.println("====== 开始分析视频: " + videoPath + " ======\n");
        // 1. 获取视频流和像素格式信息
        String cmdVideo = String.format("ffprobe -v error -select_streams v:0 -show_entries stream=codec_name,pix_fmt -of default=noprint_wrappers=1 %s", videoPath);
        List<String> videoInfo = executeLinuxCmd(cmdVideo);

        // 2. 获取音频流信息
        String cmdAudio = String.format("ffprobe -v error -select_streams a:0 -show_entries stream=codec_name -of default=noprint_wrappers=1 %s", videoPath);
        List<String> audioInfo = executeLinuxCmd(cmdAudio);

        // 3. 获取 moov/mdat 位置信息
        String cmdStructure = String.format("ffprobe -v trace %s 2>&1 | grep -E \"type:'moov'|type:'mdat'\" | head -n 2", videoPath);
        List<String> structureInfo = executeLinuxCmd(cmdStructure);

        // ====== 开始比对绿灯标准 ======
        System.out.println("====== Web 播放兼容性检测报告 ======");

        // 检查封装格式 (通过扩展名简单判断，实际复杂判断需要看 format 字段)
        String ext = videoPath.substring(videoPath.lastIndexOf(".")).toLowerCase();
        if (ext.equals(".mp4") || ext.equals(".webm")) {
            System.out.println("🟢 [封装格式]: " + ext + " (符合标准)");
        } else {
            System.out.println("🔴 [封装格式]: " + ext + " (不推荐！网页可能无法识别，建议转换为 .mp4 或 .webm)");
        }

        // 解析并检查视频编码与像素格式
        String videoCodec = getValue(videoInfo, "codec_name");
        String pixFmt = getValue(videoInfo, "pix_fmt");

        if ("h264".equals(videoCodec) || "vp9".equals(videoCodec)) {
            System.out.println("🟢 [视频编码]: " + videoCodec + " (符合标准)");
        } else {
            System.out.println("🔴 [视频编码]: " + videoCodec + " (不兼容！高级编码如 h265/hevc 在部分网页会黑屏)");
        }

        if ("yuv420p".equals(pixFmt)) {
            System.out.println("🟢 [像素格式]: " + pixFmt + " (符合标准 8-bit)");
        } else {
            System.out.println("🔴 [像素格式]: " + pixFmt + " (不兼容！10-bit 或 yuv422/444 极大可能导致网页黑屏)");
        }

        // 解析并检查音频编码
        String audioCodec = getValue(audioInfo, "codec_name");
        if ("aac".equals(audioCodec) || "opus".equals(audioCodec) || "mp3".equals(audioCodec)) {
            System.out.println("🟢 [音频编码]: " + audioCodec + " (符合标准)");
        } else {
            System.out.println("🔴 [音频编码]: " + (audioCodec.isEmpty() ? "无音频流" : audioCodec) + " (不兼容！杜比或AC3音轨在网页上会静音)");
        }

        // 检查是否支持边下边播 (FastStart)
        if (structureInfo.size() >= 2) {
            String firstLine = structureInfo.get(0);
            if (firstLine.contains("type:'moov'")) {
                System.out.println("🟢 [边下边播]: 支持 (moov 索引已在文件头，可秒开)");
            } else {
                System.out.println("🔴 [边下边播]: 不支持 (moov 索引在文件尾，网页播放需要下载完整个视频才能开播，建议修复)");
            }
        } else {
            System.out.println("🟡 [边下边播]: 无法检测视频流结构");
        }

        System.out.println("\n====== 检测结束 ======");
    }
}
