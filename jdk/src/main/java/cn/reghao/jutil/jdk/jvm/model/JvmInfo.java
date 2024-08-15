package cn.reghao.jutil.jdk.jvm.model;

/**
 * @author reghao
 * @date 2020-10-21 10:43:37
 */
public class JvmInfo {
    private String osName;
    private String osVersion;
    private String jvmName;
    private String jvmVersion;
    private int jvmPid;
    private String jvmStartTime;

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setJvmName(String jvmName) {
        this.jvmName = jvmName;
    }

    public String getJvmName() {
        return jvmName;
    }

    public void setJvmVersion(String jvmVersion) {
        this.jvmVersion = jvmVersion;
    }

    public String getJvmVersion() {
        return jvmVersion;
    }

    public void setJvmPid(int jvmPid) {
        this.jvmPid = jvmPid;
    }

    public int getJvmPid() {
        return jvmPid;
    }

    public void setJvmStartTime(String jvmStartTime) {
        this.jvmStartTime = jvmStartTime;
    }

    public String getJvmStartTime() {
        return jvmStartTime;
    }
}
