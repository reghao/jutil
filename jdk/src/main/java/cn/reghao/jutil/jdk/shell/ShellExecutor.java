package cn.reghao.jutil.jdk.shell;

import java.io.*;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author reghao
 * @date 2019-08-20 23:45:06
 */
public class ShellExecutor {
    private final ProcessBuilder pb = new ProcessBuilder();

    public static ShellResult executeWithResult(List<String> commands) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(commands);
        Process process = pb.start();

        StringBuilder stdout = new StringBuilder();
        StringBuilder stderr = new StringBuilder();
        // 父进程使用两个线程分别读取子进程的 stdout 和 stderr, 也就是子进程会向父进程写数据
        // FFmpeg 的日志输出在 stderr, Java 程序必须不断读取 process.getErrorStream(), 否则会导致缓冲区满造成进程挂起(Zombie Process)
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            executor.submit(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (stdout.length() < 1000) {
                            stdout.append(line).append(System.lineSeparator());
                        }
                    }
                } catch (IOException e) {
                    System.out.printf("读取标准流异常 %s\n", e);
                }
            });
            executor.submit(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                    String line;
                    // BufferedReader 是同步阻塞式的
                    // 有数据: 立即读取并返回
                    // 没数据但流没关: 线程进入等待状态，让出 CPU 资源，直到操作系统通知有新数据到达
                    // 流关闭时(子进程退出): readLine 返回 null, 此时阻塞解除
                    while ((line = reader.readLine()) != null) {
                        if (stderr.length() < 1000) {
                            stderr.append(line).append(System.lineSeparator());
                        }
                    }
                } catch (IOException e) {
                    System.out.printf("读取错误流异常 %s\n", e);
                }
            });
            // 停止接收新任务，但会把已提交的任务执行完
            // executor 默认创建的都是非守护
            executor.shutdown();

            // 执行 kill -9 会让操作系统直接从内存和 CPU 调度中抹除进程, JVM 进程还没来得及执行下一行指令就被杀死，所以 ShutdownHook 自然无法运行
            // ShutdownHook 依赖于 JVM 接收到操作系统的信号后的内部处理机制
            // 子进程默认情况下变成"孤儿进程", 它会立即被 1 号进程(init 或 systemd) 领养
            // 如果子进程正通过 stdin/stdout 与父进程进行实时通信, 父进程被杀时 pipe 关闭, 子进程在下一次尝试向 pipe 写数据时会收到操作系统的 SIGPIPE 信号, 大多数程序在收到 SIGPIPE 时的默认行为是退出, 但如果子进程忽略了该信号或没有写操作那它依然会继续运行
            // 如果子进程只是在读，那么它收不到 SIGPIPE 信号, 当子进程尝试从 pipe 读取数据, 而写入端(父进程)被关闭时, 读操作会立即返回 0(表示 EOF, 文件结束符), 读取到 EOF 的结果取决于子进程的代码逻辑
            // 注册钩子：当 JVM 退出时执行
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (process.isAlive()) {
                    process.destroyForcibly();
                    System.out.println("JVM 退出，已清理残留的 FFmpeg 进程");
                }
            }));

            // 1. 设置强制超时，防止 FFmpeg 陷入无限循环
            if (!process.waitFor(10, TimeUnit.MINUTES)) {
                // 超过 2 小时还没转完，直接干掉
                process.destroyForcibly();
            }
            // 2. 确保 Java 退出时，FFmpeg 也跟着死
            process.descendants().forEach(ProcessHandle::destroyForcibly);
            // 3. 等待进程结束
            int exitCode = process.waitFor();
            return new ShellResult(exitCode, stdout.toString(), stderr.toString());
        }
    }

    /**
     * 传入单个命令和其参数
     * 或者传入 sh 命令和其参数，脚本应该是绝对路径
     *
     * Java 运行时不能执行 cd 命令
     *
     * @param command 单个 shell 命令(脚本) 及其参数
     * @return
     * @date 2021-10-28 下午4:26
     */
    public ShellResult exec(String... command) {
        String output = System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID() + ".out";
        File ofile = new File(output);

        ShellResult shellResult;
        try {
            boolean ret = ofile.createNewFile();
            // 单个命令和其参数
            pb.command(command)
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
     * 在指定目录中执行 shell 命令, 解决 Java 运行时不能执行 cd 命令的问题
     *
     * @param dir 工作目录
     * @param command command 单个 shell 命令(脚本) 及其参数
     * @return
     * @date 2021-10-28 下午4:26
     */
    public ShellResult execWithDir(String dir, String... command) {
        String output = System.getProperty("java.io.tmpdir") + "/" + UUID.randomUUID() + ".out";
        File ofile = new File(output);

        ShellResult shellResult;
        try {
            boolean ret = ofile.createNewFile();
            // 命令和其参数
            pb.command(command)
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
