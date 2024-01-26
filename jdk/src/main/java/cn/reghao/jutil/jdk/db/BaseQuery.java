package cn.reghao.jutil.jdk.db;

import java.util.Collections;
import java.util.List;

/**
 * @author reghao
 * @date 2021-07-12 15:32:26
 */
public interface BaseQuery<T> {
    default int count() {
        return 0;
    }
    default List<T> findAll() {
        return Collections.emptyList();
    }
    default T findById(int id) {
        return null;
    }
}
