package cn.reghao.jutil.jdk.web.db;

import java.util.List;

/**
 * @author reghao
 * @date 2021-09-10 18:55:38
 */
public interface BaseCrud<T> {
    T save(T t);
    void saveAll(List<T> list);
    void update(T t);
    void delete(T t);
}
