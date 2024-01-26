package cn.reghao.jutil.jdk.store;

import java.util.Comparator;
import java.util.List;

/**
 * @author reghao
 * @date 2022-03-22 13:53:08
 */
public class LoadBalancer {
    public StoreDir getStoreDir(long fileSize) {
        LocalStore localStore = LocalStores.getMaxStore(fileSize);
        List<StoreDir> subDirs = LocalStores.getSubDirs(localStore.getDiskDir());
        subDirs.sort(Comparator.comparingInt(StoreDir::getTotal));
        return subDirs.get(0);
    }
}
