package com.neusoft.domain;

/**
 * Created by Administrator on 2018/12/11.
 */
public class PageInfo {
    private int pageIndex;
    private int pageSize;
    private int pageStart;
    private int cid;
    private int userid;
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getPageStart() {
        return pageStart;
    }



    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        this.pageStart = this.pageSize * (this.pageIndex - 1);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        this.pageStart = this.pageSize * (this.pageIndex - 1);
    }
}
