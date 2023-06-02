package cn.reghao.jutil.jdk.db;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页列表
 *
 * @author reghao
 * @date 2020-03-05 17:39:53
 */
public class PageList<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    // 当前页
    private final int pageNumber;
    // 每页大小
    private final int pageSize;
    // 总页数
    private final int totalPages;
    // 总记录数量
    private final int totalSize;
    // 是否最后一页
    private final boolean hasNext;
    private final int lastId;
    // 当前页元素
    private final List<T> list;

    public static <T> PageList<T> empty() {
        return new PageList<>(1, 10, 0, Collections.emptyList());
    }

    public static <T> PageList<T> pageList(int pageNumber, int pageSize, int total, List<T> list) {
        return new PageList<>(pageNumber, pageSize, total, list);
    }

    public static <T> PageList<T> pageList(int pageNumber, int pageSize, int total, int lastId, List<T> list) {
        return new PageList<>(pageNumber, pageSize, total, lastId, list);
    }

    private PageList(int pageNumber, int pageSize, int totalSize, List<T> list) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
        this.totalPages = totalSize/pageSize + (totalSize%pageSize != 0 ? 1 : 0);
        this.list = list;
        this.hasNext = (totalSize - pageSize*pageNumber > 0);
        this.lastId = 0;
    }

    private PageList(int pageNumber, int pageSize, int totalSize, int lastId, List<T> list) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
        this.totalPages = totalSize/pageSize + (totalSize%pageSize != 0 ? 1 : 0);
        this.list = list;
        this.hasNext = (totalSize - pageSize*pageNumber > 0);
        this.lastId = lastId;
    }

    private PageList(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalSize = 0;
        this.totalPages = 0;
        this.list = Collections.emptyList();
        this.hasNext = false;
        this.lastId = 0;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public int getLastId() {
        return lastId;
    }

    public List<T> getList() {
        return list;
    }
}
