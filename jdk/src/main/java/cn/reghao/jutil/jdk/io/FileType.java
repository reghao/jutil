package cn.reghao.jutil.jdk.io;

import cn.reghao.jutil.jdk.shell.Shell;

/**
 * @author reghao
 * @date 2023-06-15 14:08:45
 */
public class FileType {
    public static String getMediaType(String srcPath) {
        String cmd = String.format("/bin/file -b --mime-type \"%s\"", srcPath);
        return Shell.execWithResult(cmd);
    }
}
