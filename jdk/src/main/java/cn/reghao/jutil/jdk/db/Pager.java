package cn.reghao.jutil.jdk.db;

import java.io.Serializable;
import java.util.List;

/**
 * @author reghao
 * @date 2022-05-31 16:23:04
 */
@Deprecated
public class Pager<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private long total;
    private int pageSize;
    private long totalPages;
    private int currentPage;
    private List<T> list;
    private boolean hasNext;

    private Pager(long total, int pageSize, long totalPages, int currentPage, List<T> list, boolean hasNext) {
        this.total = total;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.currentPage = currentPage;
        this.list = list;
        this.hasNext = hasNext;
    }

    public static <T> Pager<T> pageList(long total, int pageSize, int currentPage, List<T> list) {
        long pages = total/pageSize;
        int mod = (int)total%pageSize;
        long totalPages = (mod == 0 ? pages : pages+1);
        boolean hasNext = total - ((long) pageSize *currentPage) > 0;
        return new Pager<>(total, pageSize, totalPages, currentPage, list, hasNext);
    }
}
