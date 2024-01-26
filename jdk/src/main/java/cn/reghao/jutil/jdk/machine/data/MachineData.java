package cn.reghao.jutil.jdk.machine.data;

/**
 * @author reghao
 * @date 2021-10-16 18:42:28
 */
public interface MachineData<T, K> {
    T detail();
    K stat();
}
