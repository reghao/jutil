package cn.reghao.jutil.jdk.shell;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author reghao
 * @date 2023-05-10 15:32:18
 */
public class Shell {
    private final static String bash = "/bin/bash";

    public static int exec(String cmd) {
        Runtime runtime = Runtime.getRuntime();
        int status;
        try {
            Process proc = runtime.exec(new String[]{bash, "-c", cmd});
            new Thread(new Output(proc)).start();
            status = proc.waitFor();
            if (status != 0) {
                return status;
            }

            proc.getOutputStream().close();
            proc.getInputStream().close();
            proc.getErrorStream().close();
        } catch (Exception e) {
            e.printStackTrace();
            status = 1;
        } finally {
            runtime.freeMemory();
        }

        return status;
    }

    public static String execWithResult(String cmd) {
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
