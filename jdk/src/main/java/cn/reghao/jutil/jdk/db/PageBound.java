package cn.reghao.jutil.jdk.db;

/**
 * @author reghao
 * @date 2023-03-08 10:23:25
 */
public class PageBound {
    private final int start;
    private final int end;

    private PageBound(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public static PageBound getPageBound(int pageNumber, int pageSize, int total) {
        if (total == 0) {
            return new PageBound(0, 0);
        }

        int totalPage = total/pageSize;
        int mod = total%pageSize;
        if (mod != 0) {
            totalPage += 1;
        }

        int start, end;
        if (pageNumber < totalPage) {
            start = (pageNumber-1)*pageSize;
            end = start + pageSize;
        } else {
            start = (totalPage-1)*pageSize;
            end = total;
        }

        return new PageBound(start, end);
    }
}
