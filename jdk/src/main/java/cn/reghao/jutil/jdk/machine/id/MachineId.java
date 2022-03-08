package cn.reghao.jutil.jdk.machine.id;

/**
 * 唯一标识一台机器
 *
 * @author reghao
 * @date 2021-10-16 18:33:58
 */
public interface MachineId {
    /**
     * 机器全局唯一 ID
     *
     * @param
     * @return
     * @date 2021-10-16 下午6:34
     */
    String id();

    /**
     * 只能在一个网络内唯一标识一台机器
     *
     * @param
     * @return
     * @date 2021-10-16 下午6:35
     */
    String ipv4();
}
