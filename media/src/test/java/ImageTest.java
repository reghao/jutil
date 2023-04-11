import cn.reghao.jutil.jdk.converter.DateTimeConverter;
import cn.reghao.jutil.jdk.serializer.JsonConverter;
import cn.reghao.jutil.media.image.ImageOps;
import cn.reghao.jutil.media.po.AudioInfo;
import cn.reghao.jutil.media.po.MediaInfo;
import cn.reghao.jutil.media.po.VideoInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author reghao
 * @date 2023-02-21 21:09:37
 */
public class ImageTest {
    static String bash = "/bin/bash";
    static String file = "/bin/file";
    static String ffprobe = "/usr/bin/ffprobe";
    static String ffmpeg = "/usr/bin/ffmpeg";

    static String fileInfo(String src) {
        return String.format("%s -b \"%s\"", file, src);
        //return String.format("%s -b -i \"%s\"", file, src);
    }

    static String probe(String src) {
        return String.format("%s -v quiet -print_format json -show_format -show_streams -i \"%s\"", ffprobe, src);
    }

    static String covert(String src, String dest) {
        return String.format("%s -y -i %s -c:a aac -c:v libx264 %s", ffmpeg, src, dest);
    }

    static String covert1(String src, int width, int height, String dest) {
        String audioBitRate = "128k";
        String videoBitRate = "1500k";
        return String.format("%s -i %s -s %sx%s -c:a aac -b:a %s -c:v libx264 -b:v %s -g 90 %s",
                ffmpeg, src, width, height, audioBitRate, videoBitRate, dest);
    }

    static String split(String src, String start, String duration, String dest) {
        return String.format("%s -i %s -ss %s -t %s -vcodec copy -acodec copy %s", ffmpeg, src, start, duration, dest);
    }

    static void exec(String cmd) {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec(new String[]{bash, "-c", cmd});
            new Thread(new Output(proc)).start();
            int status = proc.waitFor();
            if (status != 0) {
                System.out.println("失败");
            }

            proc.getOutputStream().close();
            proc.getInputStream().close();
            proc.getErrorStream().close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            runtime.freeMemory();
        }
    }

    static String execWithResult(String cmd) {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process proc = runtime.exec(new String[]{bash, "-c", cmd});
            int status = proc.waitFor();
            if (status != 0) {
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            StringBuilder sb = new StringBuilder();
            try {
                String line;
                while((line = br.readLine()) != null){
                    sb.append(line);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            proc.getOutputStream().close();
            proc.getInputStream().close();
            proc.getErrorStream().close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            runtime.freeMemory();
        }
        return null;
    }

    static Map<File, MediaInfo> map = new HashMap<>();
    static void walkDir(String dirPath) throws IOException {
        Path path = Path.of(dirPath);
        Files.walkFileTree(path, new FileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                File file1 = file.toFile();
                String format = ImageOps.getFormat(file1);
                if (format == null) {
                    System.out.println("文件格式未知");
                } else if (format.equalsIgnoreCase("jpg") || format.equalsIgnoreCase("jpeg")) {

                } else {
                    if (format.equalsIgnoreCase("png")) {
                        System.out.printf("%s covert to jpg\n", format);
                        byte[] bytes = ImageOps.png2jpg(file1);
                        System.out.println();
                    }
                }

                /*MediaInfo mediaInfo = getMediaInfo(file1);
                map.put(file1, mediaInfo);*/
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    static MediaInfo getMediaInfo(File file) {
        String cmd = probe(file.getAbsolutePath());
        String result = execWithResult(cmd);
        if (result != null) {
            JsonObject jsonObject = JsonConverter.jsonToJsonElement(result).getAsJsonObject();
            JsonArray streams = jsonObject.get("streams").getAsJsonArray();
            AudioInfo audioInfo = null;
            VideoInfo videoInfo = null;
            for (JsonElement jsonElement : streams) {
                JsonObject jsonObject1 = jsonElement.getAsJsonObject();
                String codecType = jsonObject1.get("codec_type").getAsString();
                if (codecType.equals("audio")) {
                    String codecName = jsonObject1.get("codec_name").getAsString();
                    String codecTagString = jsonObject1.get("codec_tag_string").getAsString();
                    double bitRate = jsonObject1.get("bit_rate").getAsDouble();
                    double duration = jsonObject1.get("duration").getAsDouble();
                    audioInfo = new AudioInfo(codecName, codecTagString, bitRate, duration);
                } else if (codecType.equals("video")) {
                    String codecName = jsonObject1.get("codec_name").getAsString();
                    String codecTagString = jsonObject1.get("codec_tag_string").getAsString();
                    double bitRate = jsonObject1.get("bit_rate").getAsDouble();
                    double duration = jsonObject1.get("duration").getAsDouble();
                    double codedWidth = jsonObject1.get("coded_width").getAsDouble();
                    double codedHeight = jsonObject1.get("coded_height").getAsDouble();
                    videoInfo = new VideoInfo(codecName, codecTagString, bitRate, duration, codedWidth, codedHeight);
                }
            }

            JsonObject format = jsonObject.get("format").getAsJsonObject();
            double duration = format.get("duration").getAsDouble();
            double size = format.get("size").getAsDouble();
            double bitRate = format.get("bit_rate").getAsDouble();

            MediaInfo mediaInfo = new MediaInfo(audioInfo, videoInfo);
            JsonObject tags = format.get("tags").getAsJsonObject();
            JsonElement jsonElement = tags.get("creation_time");
            if (jsonElement != null) {
                String creationTime = jsonElement.getAsString();
                LocalDateTime localDateTime = DateTimeConverter.localDateTime(creationTime);
                mediaInfo.setCreateTime(localDateTime);
            }

            return mediaInfo;
        }
        return null;
    }

    static void test() throws IOException {
        String readFormats[] = ImageIO.getReaderFormatNames();
        String writeFormats[] = ImageIO.getWriterFormatNames();
        //walkDir("/home/reghao/data/video/");
        walkDir("/home/reghao/data/picture/");

        /*String filePath1 = "/home/reghao/data/picture/2.png";
        File file1 = new File(filePath1);
        String format = ImageOps.getFormat(file1);
        System.out.println();*/

        map.forEach((file, mediaInfo) -> {
            if (mediaInfo == null) {
                return;
            }

            String audioCodec = mediaInfo.getAudioInfo().getCodecName();
            String videoCodec = mediaInfo.getVideoInfo().getCodecName();
            if (audioCodec.equals("aac") && videoCodec.equals("h264")) {
                String result = execWithResult(fileInfo(file.getAbsolutePath()));
                System.out.println();
            } else {
                String filePath = file.getAbsolutePath();
                String filename = file.getName();
                String cmd = covert(filePath, "/home/reghao/Downloads/0/" + filename);
                exec(cmd);
            }
        });
    }

    public static void main(String[] args) throws IOException {
        String src = "/home/reghao/Downloads/sxd.mp4";
        String start = "00:00:00";
        String duration = "00:02:00";
        String dest = "/home/reghao/Downloads/sxd-1.mp4";

        String cmd = split(src, start, duration, dest);
        exec(cmd);
    }

    static class Output implements Runnable {
        private final Process proc;

        public Output(Process proc) {
            this.proc = proc;
        }

        @Override
        public void run() {
            BufferedReader br = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            try {
                String line;
                while((line = br.readLine()) != null){
                    System.out.println(line);
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
