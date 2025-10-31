package cn.reghao.jutil.jdk.web.db;

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
    private int pageNumber;
    // 每页大小
    private int pageSize;
    // 总记录数量
    private int totalSize;
    // 是否最后一页
    private boolean hasNext;
    private String prevId;
    private final String nextId;
    // 当前页元素
    private final List<T> list;

    public static <T> PageList<T> empty() {
        return new PageList<>(1, 10, 0, Collections.emptyList());
    }

    public static <T> PageList<T> pageList(String nextId, List<T> list) {
        return new PageList<>(nextId, list);
    }

    public static <T> PageList<T> pageList(int pageNumber, int pageSize, int total, List<T> list) {
        return new PageList<>(pageNumber, pageSize, total, list);
    }

    public static <T> PageList<T> pageList(Page page, int total, List<T> list) {
        return new PageList<>(page.getPage(), page.getSize(), total, list);
    }

    public static <T> PageList<T> pageList(int pageNumber, int pageSize, int total, String lastId, List<T> list) {
        return new PageList<>(pageNumber, pageSize, total, lastId, list);
    }

    public static <T> PageList<T> pageList(int pageNumber, int pageSize, int total, String prevId, String nextId, List<T> list) {
        return new PageList<>(pageNumber, pageSize, total, prevId, nextId, list);
    }

    private PageList(int pageNumber, int pageSize, int totalSize, List<T> list) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
        this.list = list;
        this.hasNext = (totalSize - pageSize*pageNumber > 0);
        this.prevId = "0";
        this.nextId = "0";
    }

    private PageList(int pageNumber, int pageSize, int totalSize, String lastId, List<T> list) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
        this.list = list;
        this.hasNext = (totalSize - pageSize*pageNumber > 0);
        this.prevId = "0";
        this.nextId = lastId;
    }

    private PageList(String nextId, List<T> list) {
        this.list = list;
        this.nextId = nextId;
    }

    private PageList(int pageNumber, int pageSize, int totalSize, String prevId, String lastId, List<T> list) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
        this.list = list;
        this.hasNext = (totalSize - pageSize*pageNumber > 0);
        this.prevId = prevId;
        this.nextId = lastId;
    }

    private PageList(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalSize = 0;
        this.list = Collections.emptyList();
        this.hasNext = false;
        this.prevId = "0";
        this.nextId = "0";
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public boolean hasNext() {
        return hasNext;
    }

    public String getPrevId() {
        return prevId;
    }

    public String getNextId() {
        return nextId;
    }

    public List<T> getList() {
        return list;
    }
}
