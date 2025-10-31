package cn.reghao.jutil.jdk.web.db;

/**
 * @author reghao
 * @date 2021-08-08 20:35:32
 */
public class Page {
    private int page;
    private int size;

    public Page(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
