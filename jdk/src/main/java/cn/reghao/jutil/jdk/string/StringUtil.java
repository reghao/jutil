package cn.reghao.jutil.jdk.string;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author reghao
 * @date 2026-05-19 15:41:53
 */
public class StringUtil {
    /**
     * 将 double 类型的时长格式化为 hh:mm:ss 字符串
     *
     * @param
     * @return
     * @date 2026-05-19 15:42:30
     */
    public static String formatSeconds(double seconds) {
        // 将 double 转换为 Duration
        Duration duration = Duration.ofMillis((long) (seconds * 1000));
        // Java 9+ 提供的便捷方法
        long hours = duration.toHours();
        int minutes = duration.toMinutesPart();
        int secs = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }

    /**
     * 距离当前时间的
     *
     * @param
     * @return
     * @date 2026-05-19 15:57:20
     */
    public static String getPubDateStr(LocalDateTime pubDate) {
        LocalDateTime current = LocalDateTime.now();
        Duration duration = Duration.between(pubDate, current);
        long days = duration.toDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes();
        long seconds = duration.toSeconds();

        String dateStr;
        if (days != 0) {
            if (days > 365) {
                long years = days/365;
                dateStr = String.format("%s 年前", years);
            } else if (days > 30) {
                long months = days/30;
                dateStr = String.format("%s 个月前", months);
            } else if (days > 7) {
                long weeks = days/7;
                dateStr = String.format("%s 周前", weeks);
            } else {
                dateStr = String.format("%s 天前", days);
            }
        } else if (hours != 0) {
            dateStr = String.format("%s 小时前", hours);
        } else if (minutes != 0) {
            dateStr = String.format("%s 分钟前", minutes);
        } else {
            dateStr = String.format("%s 秒钟前", seconds);
        }

        return dateStr;
    }
}
