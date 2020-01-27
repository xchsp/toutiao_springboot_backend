package com.neusoft.domain;

import java.util.Date;

public class Topic {
    private Integer id;

    private String title;

    private Integer isDelete;

    private Integer viewTimes;

    private Integer kissNum;

    private Integer isTop;

    private Integer isGood;

    private Integer isEnd;

    private Integer commentNum;

    private Date createTime;

    private Integer userid;

    private Integer topicCategoryId;

    private String content;

    private String createTimeStr;


    private String cover_url1;

    private String cover_url2;

    private String cover_url3;

    private String video_url;

    private int topic_type;


    public String getCover_url1() {
        return cover_url1;
    }

    public void setCover_url1(String cover_url1) {
        this.cover_url1 = cover_url1;
    }

    public String getCover_url2() {
        return cover_url2;
    }

    public void setCover_url2(String cover_url2) {
        this.cover_url2 = cover_url2;
    }

    public String getCover_url3() {
        return cover_url3;
    }

    public void setCover_url3(String cover_url3) {
        this.cover_url3 = cover_url3;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public int getTopic_type() {
        return topic_type;
    }

    public void setTopic_type(int topic_type) {
        this.topic_type = topic_type;
    }



    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(Integer viewTimes) {
        this.viewTimes = viewTimes;
    }

    public Integer getKissNum() {
        return kissNum;
    }

    public void setKissNum(Integer kissNum) {
        this.kissNum = kissNum;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Integer getIsGood() {
        return isGood;
    }

    public void setIsGood(Integer isGood) {
        this.isGood = isGood;
    }

    public Integer getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(Integer isEnd) {
        this.isEnd = isEnd;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getTopicCategoryId() {
        return topicCategoryId;
    }

    public void setTopicCategoryId(Integer topicCategoryId) {
        this.topicCategoryId = topicCategoryId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}