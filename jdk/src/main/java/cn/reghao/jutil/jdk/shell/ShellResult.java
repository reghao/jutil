package cn.reghao.jutil.jdk.shell;

/**
 * @author reghao
 * @date 2019-08-24 14:47:57
 */
public class ShellResult {
    private int exitCode;
    // 包含 stdout 和/或 stderr
    private String result;

    public ShellResult(int exitCode) {
        this.exitCode = exitCode;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    /**
     * 是否成功执行
     *
     * @param
     * @return
     * @date 2021-05-22 上午11:11
     */
    public boolean isSuccess() {
        return exitCode == 0;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
