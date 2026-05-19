package cn.reghao.jutil.jdk.shell.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author reghao
 * @date 2026-04-26 13:41:49
 */
public class SoundOutputHandler implements OutputHandler {
    // 定义正则表达式匹配 max_volume 和 mean_volume
    Pattern maxVolPattern = Pattern.compile("max_volume: ([-+]?\\d+\\.?\\d*) dB");
    Pattern meanVolPattern = Pattern.compile("mean_volume: ([-+]?\\d+\\.?\\d*) dB");

    @Override
    public void handle(String line) {
        Matcher maxMatcher = maxVolPattern.matcher(line);
        if (maxMatcher.find()) {
            System.out.println(">>> 检测到最大音量: " + maxMatcher.group(1) + " dB");
        }

        Matcher meanMatcher = meanVolPattern.matcher(line);
        if (meanMatcher.find()) {
            System.out.println(">>> 检测到平均音量: " + meanMatcher.group(1) + " dB");
        }
    }
}
