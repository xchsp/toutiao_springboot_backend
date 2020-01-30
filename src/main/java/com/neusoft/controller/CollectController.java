package com.neusoft.controller;


import com.neusoft.domain.User;
import com.neusoft.domain.UserCollectTopic;
import com.neusoft.domain.UserFollow;
import com.neusoft.mapper.UserCollectTopicMapper;
import com.neusoft.mapper.UserFollowMapper;
import com.neusoft.response.RegRespObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.neusoft.jwt.JwtUtil.USER_NAME;

/**
 * Created by Administrator on 2018/12/21.
 */
@Controller
public class CollectController {
    @Autowired
    UserCollectTopicMapper userCollectTopicMapper;
    @Autowired
    UserFollowMapper userFollowMapper;

    @RequestMapping("/api/user_star")
    @ResponseBody
    public  List<Map<String,Object>> getCollectInfo(@RequestHeader(value = USER_NAME) String userId) throws IOException {
        List<Map<String,Object>> lst = userCollectTopicMapper.getCollectionsByUserID(Integer.parseInt(userId));

        for(Map<String,Object> map : lst)
        {
            List<Map<String,Object>> covers = new ArrayList<>();
            Map<String,Object> mapUrl = new HashMap<>();
            if(!map.get("cover_url1").equals(""))
            {
                mapUrl.put("url",map.get("cover_url1"));
                covers.add(mapUrl);
            }
            if(!map.get("cover_url2").equals(""))
            {
                mapUrl = new HashMap<>();
                mapUrl.put("url",map.get("cover_url2"));
                covers.add(mapUrl);
            }
            if(!map.get("cover_url3").equals(""))
            {
                mapUrl = new HashMap<>();
                mapUrl.put("url",map.get("cover_url3"));
                covers.add(mapUrl);
            }

            map.put("cover",covers);
        }

        return lst;

    }

    @RequestMapping("/api/post_star/{tid}")
    @ResponseBody
    public RegRespObj add_remove_CollectInfo(@PathVariable  Integer tid, @RequestHeader(value = USER_NAME) String userId) throws IOException {

        Map<String,Integer> map2 = new HashMap<>();
        map2.put("topicid",tid);
        map2.put("userid",Integer.parseInt(userId));
        int is_collect = userCollectTopicMapper.getIsCollectInfo(map2);
        if (is_collect == 0)
        {
            UserCollectTopic userCollectTopic = new UserCollectTopic();
            userCollectTopic.setTopicId(tid);
            userCollectTopic.setUserId(Integer.parseInt(userId));
            userCollectTopicMapper.insertSelective(userCollectTopic);
        }
        else
        {
            userCollectTopicMapper.delCollectInfo(map2);
        }

        RegRespObj regRespObj = new RegRespObj();
        regRespObj.setStatus(0);
        return regRespObj;
    }

    @RequestMapping("/api/user_follows/{uid}")
    @ResponseBody
    public RegRespObj add_remove_user_follows_Info(@PathVariable  Integer uid, @RequestHeader(value = USER_NAME) String userId) throws IOException {

        RegRespObj regRespObj = new RegRespObj();

        if(uid == Integer.parseInt(userId))
        {
            regRespObj.setMessage("不能关注自己");
            regRespObj.setStatus(1);
            return regRespObj;
        }

        Map<String,Integer> map2 = new HashMap<>();
        map2.put("followerid",Integer.parseInt(userId));
        map2.put("followedid",uid);
        int is_collect = userFollowMapper.getIsFollowedInfo(map2);



        if (is_collect == 0)
        {
            UserFollow userCollectTopic = new UserFollow();
            userCollectTopic.setFollowedid(uid);
            userCollectTopic.setFollowerid(Integer.parseInt(userId));
            userFollowMapper.insertSelective(userCollectTopic);
            regRespObj.setMessage("关注成功");
        }
        else
        {
            userFollowMapper.delFollowedInfo(map2);
            regRespObj.setMessage("取消关注成功");
        }

        regRespObj.setStatus(0);
        return regRespObj;
    }




    @RequestMapping("/api/user_follows")
    @ResponseBody
    public  List<User> getFollowedInfo(@RequestHeader(value = USER_NAME) String userId) throws IOException {
        List<User> lst = userFollowMapper.getFollowedByUserID(Integer.parseInt(userId));
        return lst;

    }
}
