package cn.reghao.jutil.jdk.db;

/**
 * @author reghao
 * @date 2021-08-08 20:35:32
 */
public class PageParam {
    private int pageSize;
    private int pageNumber;

    public PageParam(int pageSize, int pageNumber) {
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }
}
