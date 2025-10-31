package cn.reghao.jutil.jdk.io;

import java.io.*;
import java.util.*;

/**
 * 文本文件工具类
 *
 * @author reghao
 * @date 2019/02/27 15:49:32
 */
public class TextFile {
    // 匹配所有空白符
    private String whiteSpace = "\\s+";
    // 8 MiB
    private final int bufSize = 8*1024*1024;

    /**
     * 清空空白字符
     *
     * @param
     * @return
     * @date 2019-06-10 下午3:31
     */
    private String clearWhiteSpace(String str) {
        return str.replace(whiteSpace, "");
    }

    /**
     * 读取文件内容
     *
     * @param
     * @return
     * @date 2019-09-02 上午12:22
     */
    public List<String> read(String filePath) {
        String charset = "utf-8";
        return read(filePath, charset);
    }

    public List<String> read(String filePath, String charset) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        List<String> lines = new ArrayList<>();
        try {
            BufferedReader in =  new BufferedReader(new InputStreamReader(new FileInputStream(file), charset), bufSize);
            String line;
            while ((line = in.readLine()) != null) {
                lines.add(clearWhiteSpace(line));
            }

            in.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return lines;
    }

    public List<String> tailRead(String filePath, int count) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(filePath, "r");
        long len = raf.length();
        long next = len-1;
        int ch;
        String line;
        List<String> list = new ArrayList<>();
        while (next >= 0 && count > 0) {
            ch = raf.read();
            if (ch == '\n') {
                line = raf.readLine();
                checkAndAdd(line, list);
                count--;
            }

            raf.seek(next);
            if (next == 0) {
                line = raf.readLine();
                checkAndAdd(line, list);
                count--;
            }
            next--;
        }

        Collections.reverse(list);
        return list;
    }

    private void checkAndAdd(String line, List<String> list) {
        if (line != null) {
            list.add(line);
        }
    }

    public String readFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }

        StringBuilder content = new StringBuilder();
        try {
            BufferedReader in =  new BufferedReader(new InputStreamReader(new FileInputStream(file)), bufSize);
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            in.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        int index = content.lastIndexOf(System.lineSeparator());
        return content.substring(0, index);
    }

    public String readFile(File file, String charset) {
        StringBuilder content = new StringBuilder();
        try {
            BufferedReader in =  new BufferedReader(new InputStreamReader(new FileInputStream(file), charset), bufSize);
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
            in.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        int index = content.lastIndexOf(System.lineSeparator());
        return content.substring(0, index);
    }

    /**
     * 向文件写入内容
     *
     * @param
     * @return
     * @date 2019-09-02 上午12:22
     */
    public void write(String filePath, List<String> list) {
        File file = new File(filePath);
        try {
            /*if (file.exists() || !file.createNewFile()) {
                log.info("{} exists or create file failed...", filePath);
                return;
            }*/

            if (list == null || list.size() == 0) {
                return;
            }

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)), bufSize);
            list.forEach(str -> {
                try {
                    out.write(str + System.lineSeparator());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            out.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * 向文件中写入内容
     *
     * @param
     * @return
     * @date 2020-03-10 上午10:47
     */
    public void write(File file, String content) throws IOException {
        int bufSize = 8*1024*1024;
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)), bufSize);
        for (String str : content.split(System.lineSeparator())) {
            out.write(str + System.lineSeparator());
        }

        out.close();
    }

    /**
     * 清空文件内容
     *
     * @param
     * @return
     * @date 2020-03-10 上午10:47
     */
    public static void empty(File file) throws IOException {
        file.delete();
        file.createNewFile();
    }

    /**
     * 将空白字符分隔的 key-value 格式文本存放到 Map
     *
     * @param
     * @return
     * @date 2019-04-29 下午2:44
     */
    public Map<String, String> readByLineMap(String filePath) {
        // 有序 map
        Map<String, String> map = new LinkedHashMap<>();
        // 4MiB
        // TODO: 如何确认缓冲区大小
        final int bufSize = 4*1024*1024;

        try {
            BufferedReader in =  new BufferedReader(new InputStreamReader(new FileInputStream(filePath)), bufSize);
            String line;
            while ((line = in.readLine()) != null) {
                String[] ss = line.split(whiteSpace);
                if (ss.length == 2 && !line.split(whiteSpace)[0].equals("")
                        && ss[0].charAt(0) > 48 && ss[0].charAt(0) < 57) {
                    map.put(ss[0], ss[1]);
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return map;
    }

    public void append(String filePath, List<String> lines) throws IOException {
        File file = new File(filePath);
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        String line = raf.readLine();
        while (line != null) {
            line = raf.readLine();
        }

        for (String str : lines) {
            String str1 = str + System.lineSeparator();
            // 调用 writeChars 方法会出现多余的空格,因为这个方法以 2 字节为基准
            raf.write(str1.getBytes());
        }

        raf.close();
    }
}
