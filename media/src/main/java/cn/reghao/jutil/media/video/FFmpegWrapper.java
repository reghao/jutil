package cn.reghao.jutil.media.video;

import cn.reghao.jutil.jdk.shell.ShellExecutor;
import cn.reghao.jutil.jdk.shell.ShellResult;
import cn.reghao.jutil.media.Shell;

/**
 * @author reghao
 * @date 2022-03-04 11:04:32
 */
public class FFmpegWrapper {
    static ShellExecutor shellExecutor = new ShellExecutor();
    private final static String ffmpeg = "/usr/bin/ffmpeg";

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
