package cn.reghao.jutil.jdk.machine.data.detail;

import java.io.Serializable;

/**
 * @author reghao
 * @date 2021-10-16 18:15:16
 */
public class CpuDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private String vendor;
    private String name;
    private int physicalCore;
    private int logicalCore;

    public CpuDetail(String vendor, String name, int physicalCore, int logicalCore) {
        this.vendor = vendor;
        this.name = name;
        this.physicalCore = physicalCore;
        this.logicalCore = logicalCore;
    }

    public String getVendor() {
        return vendor;
    }

    public String getName() {
        return name;
    }

    public int getPhysicalCore() {
        return physicalCore;
    }

    public int getLogicalCore() {
        return logicalCore;
    }
}
