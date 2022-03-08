package cn.reghao.jutil.jdk.shell;

import java.io.*;
import java.util.UUID;

/**
 * @author reghao
 * @date 2019-08-20 23:45:06
 */
public class ShellExecutor {
    private final ProcessBuilder pb = new ProcessBuilder();;

    /**
     * 传入单个命令和其参数
     * 或者传入 sh 命令和其参数，脚本应该是绝对路径
     *
     * Java 运行时不能执行 cd 命令
     *
     * @param commands shell 命令(脚本) 参数
     * @return
     * @date 2021-10-28 下午4:26
     */
    public ShellResult exec(String... commands) {
        String output = System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID() + ".out";
        File ofile = new File(output);

        ShellResult shellResult;
        try {
            boolean ret = ofile.createNewFile();
            // 单个命令和其参数
            pb.command(commands)
                    // 将标准错误合并到标准输出
                    .redirectErrorStream(true)
                    // 将所有输出重定向到文件
                    .redirectOutput(ofile);
            shellResult = exec(pb, ofile);
        } catch (IOException | InterruptedException e) {
            shellResult = new ShellResult(1);
            shellResult.setResult(e.getMessage());
        } finally {
            ofile.delete();
        }

        return shellResult;
    }

    /**
     * @param dir 工作目录
     * @param commands shell 命令(脚本) 参数
     * @return
     * @date 2021-10-28 下午4:26
     */
    public ShellResult exec(String dir, String... commands) {
        String output = System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID() + ".out";
        File ofile = new File(output);

        ShellResult shellResult;
        try {
            boolean ret = ofile.createNewFile();
            // 命令和其参数
            pb.command(commands)
                    // 将标准错误合并到标准输出
                    .redirectErrorStream(true)
                    // 将所有输出重定向到文件
                    .redirectOutput(ofile);
            if (dir != null) {
                pb.directory(new File(dir));
            }
            shellResult = exec(pb, ofile);
        } catch (IOException | InterruptedException e) {
            shellResult = new ShellResult(1);
            shellResult.setResult(e.getMessage());
        } finally {
            ofile.delete();
        }

        return shellResult;
    }

    private ShellResult exec(ProcessBuilder pb, File ofile) throws IOException, InterruptedException {
        Process newProcess = pb.start();
        ProcessHandle handle = newProcess.toHandle();
        // 子进程 PID
        long pid = handle.pid();

        // 父进程等待子进程结束
        int exitCode = newProcess.waitFor();
        ShellResult shellResult = new ShellResult(exitCode);
        shellResult.setExitCode(exitCode);
        shellResult.setResult(output(ofile));
        return shellResult;
    }

    private String output(File ofile) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader in =  new BufferedReader(new InputStreamReader(new FileInputStream(ofile)));
        String line;
        while ((line = in.readLine()) != null) {
            sb.append(line).append(System.lineSeparator());
        }
        in.close();
        return sb.toString();
    }
}
