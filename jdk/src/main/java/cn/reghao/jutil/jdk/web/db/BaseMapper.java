package cn.reghao.jutil.jdk.web.db;

import java.util.List;

/**
 * @author reghao
 * @date 2021-09-10 23:34:39
 */
public interface BaseMapper<T> {
    int save(T t);
    void saveAll(List<T> list);
    void update(T t);
    void delete(T t);

    int count();
    List<T> findAll();
    List<T> findAllByPage(int page, int size);
    T findById(int id);
}
