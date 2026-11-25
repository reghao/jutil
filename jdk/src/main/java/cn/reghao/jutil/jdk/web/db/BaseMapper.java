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
    /**
     * @param id mysql 自增 ID
     * @return 1 - 成功删除了 id = #{id} 对应的那条记录; 0 - 表中不存在该 id(或已被删过)，没有行被删除
     * @date 2026-11-25 15:25:17
     */
    int deleteById(int id);

    int count();
    List<T> findAll();
    List<T> findAllByPage(Page page);
    T findById(int id);
}
