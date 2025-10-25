package cn.reghao.jutil.jdk.db;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author reghao
 * @date 2019-10-18 14:42:48
 */
public class BaseObject<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    protected T id;
    // 逻辑删除
    protected Boolean deleted;
    protected LocalDateTime createTime;
    protected LocalDateTime updateTime;

    public BaseObject() {
        this.deleted = false;
        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.updateTime = now;
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
