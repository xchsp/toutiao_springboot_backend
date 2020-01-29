package com.neusoft.controller;

import com.alibaba.fastjson.JSON;
import com.neusoft.domain.User;
import com.neusoft.domain.UserCollectTopic;
import com.neusoft.mapper.UserCollectTopicMapper;
import com.neusoft.response.Data;
import com.neusoft.response.RegRespObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
//@RequestMapping("collection")
public class CollectController {
    @Autowired
    UserCollectTopicMapper userCollectTopicMapper;

    @RequestMapping("/api/user_star")
    @ResponseBody
    public  List<Map<String,Object>> getCollectInfo(@RequestHeader(value = USER_NAME) String userId) throws IOException {
        List<Map<String,Object>> lst = userCollectTopicMapper.getCollectionsByUserID(Integer.parseInt(userId));

        for(Map<String,Object> map : lst)
        {
//            Date date = (Date)map.get("create_time");
//            String strDate = StringDate.getStringDate(date);
//            map.put("create_time",strDate);
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

//    @RequestMapping("remove")
//    public void removeCollectInfo(Integer cid, HttpSession httpSession, HttpServletResponse response) throws IOException {
//
//        User user = (User)httpSession.getAttribute("userinfo");
//        if(user != null)
//        {
//            Map<String,Integer> map2 = new HashMap<>();
//            map2.put("topicid",cid);
//            map2.put("userid",user.getId());
//
//            userCollectTopicMapper.delCollectInfo(map2);
//
//            RegRespObj regRespObj = new RegRespObj();
//            regRespObj.setStatus(0);
//
//            response.getWriter().println(JSON.toJSONString(regRespObj));
//        }
//    }
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
}
