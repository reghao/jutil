package cn.reghao.jutil.jdk.db;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页列表
 *
 * @author reghao
 * @date 2020-03-05 17:39:53
 */
public class PageList<T> {
    // 当前页
    private int page;
    // 当前页大小
    private int size;
    // 根据当前页大小计算得出的总页数
    private int pages;
    // 总记录数量
    private int total;
    // 每页大小
    @Deprecated
    private int pageSize;
    // 总数量
    @Deprecated
    private long totalSize;
    // 总页数
    @Deprecated
    private int totalPages;
    // 是否最后一页
    private boolean hasNext;
    private List<T> list;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public static <T> PageList<T> empty() {
        PageList<T> pageList = new PageList<>();
        pageList.setPageSize(0);
        pageList.setTotalSize(0);
        pageList.setTotalPages(0);
        pageList.setHasNext(false);
        pageList.setList(new ArrayList<>());
        return pageList;
    }

    public static <T> PageList<T> pageList(List<T> list) {
        PageList<T> pageList = new PageList<>();
        pageList.setPageSize(list.size());
        pageList.setTotalSize(list.size());
        pageList.setTotalPages(list.size());
        pageList.setHasNext(true);
        pageList.setList(list);
        return pageList;
    }
}
