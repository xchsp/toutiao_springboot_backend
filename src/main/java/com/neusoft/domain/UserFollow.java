package com.neusoft.domain;

public class UserFollow {
    private Integer id;

    private Integer followerid;
    private Integer followedid;

    public Integer getFollowerid() {
        return followerid;
    }

    public void setFollowerid(Integer followerid) {
        this.followerid = followerid;
    }

    public Integer getFollowedid() {
        return followedid;
    }

    public void setFollowedid(Integer followedid) {
        this.followedid = followedid;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



}