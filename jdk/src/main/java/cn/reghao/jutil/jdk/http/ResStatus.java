package cn.reghao.jutil.jdk.http;

/**
 * HTTP 资源状态
 *
 * @author reghao
 * @date 2021-11-19 14:50:18
 */
public enum ResStatus {
    avail(200), notFound(404), notAvail(600);

    private final int value;
    ResStatus(int value) {
        this.value = value;
    }

    public String getName() {
        return this.name();
    }

    public Integer getValue() {
        return value;
    }
}
