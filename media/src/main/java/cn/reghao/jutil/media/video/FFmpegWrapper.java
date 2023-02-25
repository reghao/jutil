package cn.reghao.jutil.media.video;

import cn.reghao.jutil.jdk.shell.ShellExecutor;
import cn.reghao.jutil.jdk.shell.ShellResult;

/**
 * @author reghao
 * @date 2022-03-04 11:04:32
 */
public class FFmpegWrapper {
    static ShellExecutor shellExecutor = new ShellExecutor();

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

    public static void generateM3u8() {
    }
}
