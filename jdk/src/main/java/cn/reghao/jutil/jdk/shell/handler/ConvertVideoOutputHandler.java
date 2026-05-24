package cn.reghao.jutil.jdk.shell.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author reghao
 * @date 2026-04-26 13:35:22
 */
public class ConvertVideoOutputHandler implements OutputHandler {
    static Pattern timePattern = Pattern.compile("time=(\\d{2}:\\d{2}:\\d{2}.\\d{2})");
    private final double totalSeconds;

    public ConvertVideoOutputHandler(double totalSeconds) {
        this.totalSeconds = totalSeconds;
    }

    @Override
    public void handle(String line) {
        Matcher matcher = timePattern.matcher(line);
        if (matcher.find()) {
            String timeStr = matcher.group(1);
            double currentSeconds = convertToSeconds(timeStr);
            double percent = (currentSeconds / totalSeconds) * 100;
            String percentStr = String.format("%.2f", percent);
            System.out.printf("FFmpeg ERROR: %s\n", percentStr);
            // 限制频率，防止每帧都发消息给前端（比如每增加 1% 发一次）
            //updateProgressToFrontend(videoId, percent);
        } else {
            //System.out.println(line);
        }
    }

    /**
     * 将 FFmpeg 的时间字符串 (HH:mm:ss.SS) 转换为总秒数
     * @param timeStr 例如 "00:02:16.65"
     * @return 总秒数，例如 136.65
     */
    static double convertToSeconds(String timeStr) {
        if (timeStr == null || !timeStr.contains(":")) {
            return 0.0;
        }

        try {
            String[] parts = timeStr.split(":");
            if (parts.length != 3) return 0.0;

            double hours = Double.parseDouble(parts[0]);
            double minutes = Double.parseDouble(parts[1]);
            double seconds = Double.parseDouble(parts[2]);

            // 计算总秒数: 时*3600 + 分*60 + 秒
            return hours * 3600 + minutes * 60 + seconds;
        } catch (NumberFormatException e) {
            // 记录异常日志，防止解析错误导致线程崩溃
            return 0.0;
        }
    }
}
