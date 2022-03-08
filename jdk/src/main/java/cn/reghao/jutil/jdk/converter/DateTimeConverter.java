package cn.reghao.jutil.jdk.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

/**
 * 时间日期字符串格式转换器
 *
 * @author reghao
 * @date 2020-03-20 10:20:01
 */
public class DateTimeConverter {
    private final static String dateTimePattern = "yyyy-MM-dd HH:mm:ss";

    /**
     * 将 LocalDateTime 对象格式化为 yyyy-MM-dd HH:mm:ss 格式的字符串
     *
     * @param
     * @return
     * @date 2021-03-02 下午4:09
     */
    public static String format(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return "";
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dateTimePattern);
        return dtf.format(localDateTime);
    }

    /**
     * 将 2021-02-23T13:24:51.519422470Z 格式化为 2021-02-23 13:24:51
     *
     * @param
     * @return
     * @date 2021-03-02 下午4:04
     */
    public static String format(String datetime) {
        Instant instant = Instant.parse(datetime);
        String zoneId = "Asia/Shanghai";
        LocalDateTime localDateTime = instant.atZone(ZoneId.of(zoneId)).toLocalDateTime();
        return format(localDateTime);
    }

    /**
     * ms 时间戳格式化为 yyyy-MM-dd HH:mm:ss 格式的字符串
     *
     * @param
     * @return
     * @date 2021-03-02 下午5:05
     */
    public static String format(long timestamp) {
        LocalDateTime localDateTime = localDateTime(timestamp);
        return format(localDateTime);
    }

    public static String now() {
        LocalDateTime now = LocalDateTime.now();
        return format(now);
    }

    /**
     * 将 2021-02-23T13:24:51.519422470Z 字符串转换为 LocalDateTime 对象
     *
     * @param
     * @return
     * @date 2021-03-02 下午4:13
     */
    public static LocalDateTime localDateTime(String datetime) {
        Instant instant = Instant.parse(datetime);
        String zoneId = "Asia/Shanghai";
        return instant.atZone(ZoneId.of(zoneId)).toLocalDateTime();
    }

    /**
     * @param
     * @return
     * @date 2021-03-09 上午3:30
     */
    public static LocalDateTime localDateTime1(String dateTime) throws ParseException {
        // Mar 4, 2021, 11:01:52 PM 格式
        String pattern = "MMM d, yyyy, h:m:s aa";
        // Tue Mar 01 16:05:20 +0800 2022 格式
        pattern = "EEE MMM d HH:mm:ss '+0800' yyyy";

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        Date date = dateFormat.parse(dateTime);
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
    }

    public static LocalDateTime utc0ToUtc8(String dateTime) throws ParseException {
        // Mar 4, 2021, 11:01:52 PM 格式
        String pattern = "MMM d, yyyy, h:m:s aa";
        // Sun Jan 26 16:43:14 +0000 2022 格式
        pattern = "EEE MMM d HH:mm:ss '+0000' yyyy";

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        Date date = dateFormat.parse(dateTime);
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
    }

    public static LocalDateTime localDateTime2(String dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateTimePattern);
        try {
            Date date = dateFormat.parse(dateTime);
            Instant instant = date.toInstant();
            return instant.atZone(ZoneId.of("Asia/Shanghai")).toLocalDateTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return LocalDateTime.now();
    }

    /**
     * ms 时间戳转换为 LocalDateTime 对象
     *
     * @param
     * @return
     * @date 2021-03-02 下午4:59
     */
    public static LocalDateTime localDateTime(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        String zoneId = "Asia/Shanghai";
        ZonedDateTime dateTime = instant.atZone(ZoneId.of(zoneId));
        return dateTime.toLocalDateTime();
    }

    /**
     * 将 0 时区时间日期字符串转换为东 8 区时间日期字符串
     *
     * @param
     * @return
     * @date 2020-06-03 上午10:39
     */
    public static String convert(String dateTime) {
        ZoneId zoneId = ZoneId.of("UTC+8");
        ZonedDateTime zonedDateTime = Instant.parse(dateTime).atZone(zoneId);
        return format(zonedDateTime.toLocalDateTime());
    }

    /**
     * 2021-02-23T13:24:51.519422470Z
     * 将日期时间转换为毫秒级时间戳
     *
     * @param
     * @return
     * @date 2021-02-25 上午11:00
     */
    public static long convertToTimestamp(String dateTime) {
        Instant instant = Instant.parse(dateTime);
        return instant.toEpochMilli();
    }

    /**
     * LocalDateTime 转换为 ms 时间戳
     *
     * @param
     * @return
     * @date 2021-06-23 下午4:04
     */
    public static long msTimestamp(LocalDateTime localDateTime) {
        String timeZone = "+8";
        return localDateTime.toInstant(ZoneOffset.of(timeZone)).toEpochMilli();
    }

    /**
     * 毫秒时间戳转换为日期时间
     *
     * @param
     * @return
     * @date 2020-03-20 上午10:20
     */
    public static String msTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime dateTime = instant.atZone(ZoneId.of("Asia/Shanghai"));
        return dateTime.toLocalDateTime().toString();
    }

    /**
     * 毫秒时间戳转换为指定格式的日期时间字符串
     *
     * @param
     * @return
     * @date 2020-06-02 下午5:22
     */
    public static String msTimestampFormat(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime dateTime = instant.atZone(ZoneId.of("Asia/Shanghai"));
        LocalDateTime localDateTime = dateTime.toLocalDateTime();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dtf.format(localDateTime);
    }

    /**
     * 秒时间戳转换为日期时间
     *
     * @param
     * @return
     * @date 2020-03-20 上午10:20
     */
    public static String timestamp(int timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime dateTime = instant.atZone(ZoneId.of("Asia/Shanghai"));
        return dateTime.toLocalDateTime().toString();
    }

    public static long duration(LocalDateTime future) {
        LocalDateTime now = LocalDateTime.now();
        long second = Duration.between(now, future).getSeconds();
        return second * 1000;
    }

    /**
     * 将 hh:mm:ss 转换为秒长度
     *
     * @param
     * @return
     * @date 2021-06-09 下午10:16
     */
    public static int toSecond(String len) {
        String[] arr = len.split(":");
        int h = 0, m = 0, s = 0;
        if (arr.length == 2) {
            s = Integer.parseInt(arr[1]);
            m = convert(arr[0], 60);
            return m + s;
        } else if (arr.length == 3) {
            s = Integer.parseInt(arr[2]);
            m = convert(arr[1], 60);
            h = convert(arr[0], 3600);
            return h + m + s;
        } else {
            return 0;
        }
    }

    private static int convert(String str, int base) {
        int res = 0;
        if (!str.equals("00")) {
            if (str.startsWith("0")) {
                // 03:22
                res = Integer.parseInt(str.split("0")[1])*base;
            } else {
                // 10:23
                res = Integer.parseInt(str)*base;
            }
        }
        return res;
    }
}
