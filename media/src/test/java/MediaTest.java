import cn.reghao.jutil.media.image.ImageOps;
import cn.reghao.jutil.media.model.MediaProps;
import cn.reghao.jutil.media.video.FFmpegWrapper;

import javax.imageio.ImageIO;
import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author reghao
 * @date 2023-02-21 21:09:37
 */
public class MediaTest {
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

    static void imageTest() throws IOException {
        String readFormats[] = ImageIO.getReaderFormatNames();
        String writeFormats[] = ImageIO.getWriterFormatNames();
        walkDir("/home/reghao/data/picture/");

        String filePath1 = "/home/reghao/data/picture/2.png";
        File file1 = new File(filePath1);
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
    }

    static void videoTest() throws IOException {
        String src = "/home/reghao/Downloads/video.mp4";
        MediaProps mediaProps = FFmpegWrapper.getMediaProps(src);
        if (mediaProps == null) {
            return;
        }

        String audioCodec = mediaProps.getAudioProps().getCodecName();
        String videoCodec = mediaProps.getVideoProps().getCodecName();
        if (audioCodec.equals("aac") && videoCodec.equals("h264")) {

        } else {

        }
    }

    public static void main(String[] args) throws IOException {
        videoTest();
    }
}
