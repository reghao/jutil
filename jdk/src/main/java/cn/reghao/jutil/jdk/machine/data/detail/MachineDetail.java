package cn.reghao.jutil.jdk.machine.data.detail;

import java.io.Serializable;
import java.util.List;

/**
 * @author reghao
 * @date 2021-10-16 19:15:34
 */
public class MachineDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private String machineId;
    private OsDetail osDetail;
    private List<NetworkDetail> networkDetails;
    private CpuDetail cpuDetail;
    private MemoryDetail memoryDetail;
    private List<DiskDetail> diskDetails;

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setOsDetail(OsDetail osDetail) {
        this.osDetail = osDetail;
    }

    public OsDetail getOsDetail() {
        return osDetail;
    }

    public void setNetworkDetails(List<NetworkDetail> networkDetails) {
        this.networkDetails = networkDetails;
    }

    public List<NetworkDetail> getNetworkDetails() {
        return networkDetails;
    }

    public void setCpuDetail(CpuDetail cpuDetail) {
        this.cpuDetail = cpuDetail;
    }

    public CpuDetail getCpuDetail() {
        return cpuDetail;
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
