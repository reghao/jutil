package cn.reghao.jutil.jdk.db;

import java.util.Collections;
import java.util.List;

/**
 * 分页列表
 *
 * @author reghao
 * @date 2020-03-05 17:39:53
 */
public class PageList<T> {
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
    // 当前页元素
    private final List<T> list;

    // 当前页
    @Deprecated
    private int page;
    // 当前页大小
    @Deprecated
    private int size;
    // 根据当前页大小计算得出的总页数
    @Deprecated
    private int pages;
    // 总数量
    @Deprecated
    private int total;

    private PageList(int pageNumber, int pageSize, int totalSize, List<T> list) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
        this.totalPages = totalSize/pageSize + (totalSize%pageSize != 0 ? 1 : 0);
        this.list = list;
        this.hasNext = (total - pageSize*pageNumber > 0);

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

    public List<T> getList() {
        return list;
    }

    public static <T> PageList<T> empty() {
        return new PageList<>(1, 0, 0, Collections.emptyList());
    }

    public static <T> PageList<T> pageList(List<T> list) {
        return new PageList<>(1, 0, 0, Collections.emptyList());
    }

    public static <T> PageList<T> pageList(int pageNumber, int pageSize, int total, List<T> list) {
        return new PageList<>(pageNumber, pageSize, total, list);
    }
}
