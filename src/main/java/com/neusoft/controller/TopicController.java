package com.neusoft.controller;

import com.alibaba.fastjson.JSON;
import com.neusoft.domain.Comment;
import com.neusoft.domain.PageInfo;
import com.neusoft.mapper.CommentMapper;
import com.neusoft.mapper.TopicMapper;
import com.neusoft.response.RegRespObj;
import com.neusoft.util.StringDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static com.neusoft.jwt.JwtUtil.USER_NAME;

/**
 * Created by Administrator on 2018/12/12.
 */
@Controller
//@RequestMapping("topic")
public class TopicController {
    @Autowired
    TopicMapper topicMapper;

    @Autowired
    CommentMapper commentMapper;

    @GetMapping("/api/post")
    @ResponseBody
    public Map<String,Object> getPagedTopic(@RequestHeader(value = USER_NAME) String userId, PageInfo pageInfo) throws IOException {
        pageInfo.setUserid(Integer.parseInt(userId));
        int total = topicMapper.getTopicTotal(pageInfo);
        List<Map<String,Object>> mapList = topicMapper.getPagedTopics(pageInfo);


        for(Map<String,Object> map : mapList)
        {
//            Date date = (Date)map.get("create_time");
//            String strDate = StringDate.getStringDate(date);
//            map.put("create_time",strDate);
            List<Map<String,Object>> covers = new ArrayList<>();
            Map<String,Object> mapUrl = new HashMap<>();
            mapUrl.put("url",map.get("cover_url1").toString());
            covers.add(mapUrl);
            mapUrl = new HashMap<>();
            mapUrl.put("url",map.get("cover_url2").toString());
            covers.add(mapUrl);
            mapUrl = new HashMap<>();
            mapUrl.put("url",map.get("cover_url3").toString());
            covers.add(mapUrl);
            map.put("cover",covers);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("num",total);
        map.put("topics",mapList);

        return map;

    }

    @GetMapping("/api/post_comment/{tid}")
    @ResponseBody
    public List<Map<String,Object>> getPagedTopicsByCategory(@RequestHeader(value = USER_NAME) String userId
            , @PathVariable Integer tid,Integer pageSize) throws IOException
    {
        if (pageSize == null)
        {
            pageSize = Integer.MAX_VALUE;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("topic_id",tid);

        params.put("pageSize",pageSize);
        List<Map<String,Object>> lst = commentMapper.getCommentsByTopicID(params);
        return lst;
    }

    @PostMapping("/api/post_comment/{tid}")
    @ResponseBody
    public RegRespObj postComment(@RequestHeader(value = USER_NAME) String userId
            , @PathVariable Integer tid, @RequestBody Map<String,Object> json) throws IOException
    {
        RegRespObj regRespObj = new RegRespObj();

        //插入评论
        Comment comment = new Comment();
        comment.setCommentContent(json.get("content").toString());
        comment.setUserId(Integer.parseInt(userId));
        comment.setCommentTime(new Date());
        comment.setTopicId(tid);
        commentMapper.insertSelective(comment);
        regRespObj.setStatus(0);

        return regRespObj;

    }
    @GetMapping("/api/posts_cate")
    @ResponseBody
    public Map<String,Object> getPagedTopicsByCategory(@RequestHeader(value = USER_NAME) String userId, PageInfo pageInfo) throws IOException {
        pageInfo.setUserid(Integer.parseInt(userId));
//        int total = topicMapper.getTopicTotal(pageInfo);
        List<Map<String,Object>> mapList = topicMapper.getPagedTopicsByCategory(pageInfo);


        for(Map<String,Object> map : mapList)
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

        Map<String,Object> map = new HashMap<>();
//        map.put("num",total);
        map.put("topics",mapList);

        return map;

    }
}
