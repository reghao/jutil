package cn.reghao.jutil.jdk.machine.data.detail;

import java.io.Serializable;

/**
 * @author reghao
 * @date 2020-10-20 23:17:30
 */
public class OsDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private String arch;
    private String name;
    // 系统版本或内核版本
    private String version;
    private String byteOrder;
    private long bootTime;

    public OsDetail() {
        this.arch = System.getProperty("os.arch");
        this.name = System.getProperty("os.name");
        this.version = System.getProperty("os.version");
        this.byteOrder = System.getProperty("sun.cpu.endian");
        this.bootTime = 0;
    }

    public String getArch() {
        return arch;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getByteOrder() {
        return byteOrder;
    }

    public void setBootTime(long bootTime) {
        this.bootTime = bootTime;
    }

    public long getBootTime() {
        return bootTime;
    }
}
