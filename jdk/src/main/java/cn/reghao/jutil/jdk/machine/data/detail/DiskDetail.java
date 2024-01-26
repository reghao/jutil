package cn.reghao.jutil.jdk.machine.data.detail;

import java.io.Serializable;

/**
 * @author reghao
 * @date 2021-10-16 18:15:16
 */
public class DiskDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private String diskPath;
    private String mountedOn;
    private String fsType;
    // bytes
    private long total;
    private long avail;
    private long used;
    private long inodeTotal;
    private long inodeAvail;

    public DiskDetail(String diskPath, String mountedOn, String fsType, long total, long avail, long used,
                      long inodeTotal, long inodeAvail) {
        this.diskPath = diskPath;
        this.mountedOn = mountedOn;
        this.fsType = fsType;
        this.total = total;
        this.avail = avail;
        this.used = used;
        this.inodeTotal = inodeTotal;
        this.inodeAvail = inodeAvail;
    }

    public String getDiskPath() {
        return diskPath;
    }

    public String getMountedOn() {
        return mountedOn;
    }

    public String getFsType() {
        return fsType;
    }

    public long getTotal() {
        return total;
    }

    public void setAvail(long avail) {
        this.avail = avail;
    }

    public long getAvail() {
        return avail;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    public long getUsed() {
        return used;
    }

    public long getInodeTotal() {
        return inodeTotal;
    }

    public void setInodeAvail(long inodeAvail) {
        this.inodeAvail = inodeAvail;
    }

    public long getInodeAvail() {
        return inodeAvail;
    }
}
