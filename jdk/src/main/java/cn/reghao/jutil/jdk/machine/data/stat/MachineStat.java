package cn.reghao.jutil.jdk.machine.data.stat;

import cn.reghao.jutil.jdk.machine.data.detail.DiskDetail;
import cn.reghao.jutil.jdk.machine.data.detail.MemoryDetail;

import java.io.Serializable;
import java.util.List;

/**
 * @author reghao
 * @date 2021-10-16 19:19:01
 */
public class MachineStat implements Serializable {
    private static final long serialVersionUID = 1L;

    private String machineId;
    private MemoryDetail memoryDetail;
    private List<DiskDetail> diskDetails;

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMemoryDetail(MemoryDetail memoryDetail) {
        this.memoryDetail = memoryDetail;
    }

    public MemoryDetail getMemoryDetail() {
        return memoryDetail;
    }

    public void setDiskDetails(List<DiskDetail> diskDetails) {
        this.diskDetails = diskDetails;
    }

    public List<DiskDetail> getDiskDetails() {
        return diskDetails;
    }
}
