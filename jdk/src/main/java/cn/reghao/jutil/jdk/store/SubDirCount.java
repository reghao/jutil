package cn.reghao.jutil.jdk.store;

/**
 * @author reghao
 * @date 2023-11-01 21:38:59
 */
public class SubDirCount {
    private String relativeDir;
    private int total;

    public void setRelativeDir(String relativeDir) {
        this.relativeDir = relativeDir;
    }

    public String getRelativeDir() {
        return relativeDir;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }
}
