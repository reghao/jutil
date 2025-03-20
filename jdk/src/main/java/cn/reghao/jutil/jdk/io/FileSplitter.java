package cn.reghao.jutil.jdk.io;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 文件分片
 *
 * @author reghao
 * @date 2023-03-24 10:32:58
 */
public class FileSplitter {
    private final int partSize;

    public FileSplitter() {
        // 10MiB
        this.partSize = 10*1024*1024;
    }

    public FileSplitter(int len) {
        this.partSize = len*1024*1024;
    }

    public int getPartSize() {
        return partSize;
    }

    public void split(String srcFile, String destDir) throws IOException {
        File file = new File(srcFile);

        long len = file.length();
        long total = len/partSize;
        if (len % partSize != 0) {
            total += 1;
        }

        for (int i = 0; i < total; i++) {
            long start = (long) i*partSize;
            byte[] part = getPart(file.getAbsolutePath(), start);
            if (part.length == 0) {
                break;
            }

            String path = destDir + i;
            File file1  = new File(path);
            boolean ret = file1.createNewFile();
            if (!ret) {
                throw new IOException("创建新文件失败");
            }

            FileOutputStream fos = new FileOutputStream(file1);
            fos.write(part);
            fos.close();
        }
    }

    public byte[] getPart(String absolutePath, long start) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(absolutePath, "r");
        raf.seek(start);

        byte[] buf = new byte[partSize];
        int readLen = raf.read(buf, 0, partSize);
        raf.close();
        if (readLen == -1) {
            return new byte[0];
        }

        return readLen == partSize ? buf : Arrays.copyOfRange(buf, 0, readLen);
    }

    public void merge(String srcDir, String destFile) throws IOException {
        File dir = new File(srcDir);
        List<File> list = Arrays.asList(Objects.requireNonNull(dir.listFiles()));
        list.sort((o1, o2) -> {
            Integer o1name = Integer.parseInt(o1.getName());
            Integer o2name = Integer.parseInt(o2.getName());
            return o1name.compareTo(o2name);
        });

        long start = 0;
        RandomAccessFile raf = new RandomAccessFile(destFile, "rw");
        for (File file : list) {
            long length = file.length();
            FileInputStream fis = new FileInputStream(file);
            raf.seek(start);
            raf.write(fis.readAllBytes());
            start += length;
        }
        raf.close();
    }
}
