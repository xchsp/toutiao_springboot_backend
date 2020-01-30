package com.neusoft.controller;

import com.neusoft.domain.*;
import com.neusoft.mapper.*;
import com.neusoft.response.RegRespObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

import static com.neusoft.jwt.JwtUtil.USER_NAME;

/**
 * Created by Administrator on 2018/12/10.
 */
@Controller
//@RequestMapping("jie")
public class JieController {

    @Autowired
    TopicCategoryMapper topicCategoryMapper;
    @Autowired
    TopicMapper topicMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    UserMapper userMapper;

    @Autowired
    UserCollectTopicMapper userCollectTopicMapper;
    @Autowired
    UserTopicAgreeMapper userTopicAgreeMapper;

    @Autowired
    UserFollowMapper userFollowMapper;

    @RequestMapping("api/post/{tid}")
    @ResponseBody
    public Map<String,Object> detail(@RequestHeader(value = USER_NAME) String userId, @PathVariable Integer tid)
    {
        Map<String,Object> mapResult = new HashMap<>();
        //帖子的阅读数量加一
        Topic topic = topicMapper.selectByPrimaryKey(tid);



        List<TopicCategory> categories = topicCategoryMapper.getCategoriesByTopicID(tid);
        mapResult.put("categories",categories);

        List<Map<String,Object>> covers = new ArrayList<>();
        Map<String,Object> mapUrl = new HashMap<>();
        if(!topic.getCover_url1().equals(""))
        {
            mapUrl.put("url",topic.getCover_url1());
            covers.add(mapUrl);
        }
        if(!topic.getCover_url2().equals(""))
        {
            mapUrl = new HashMap<>();
            mapUrl.put("url",topic.getCover_url2());
            covers.add(mapUrl);
        }

        if(!topic.getCover_url3().equals(""))
        {
            mapUrl = new HashMap<>();
            mapUrl.put("url",topic.getCover_url3());
            covers.add(mapUrl);
        }

        mapResult.put("cover",covers);

        mapResult.put("title",topic.getTitle());
        mapResult.put("content",topic.getContent());
        mapResult.put("type",topic.getTopic_type());
        mapResult.put("id",topic.getId());

        int count = commentMapper.getCommentsCountsByTopicID(topic.getId());
        mapResult.put("comment_length",count);
        mapResult.put("like_length",topic.getViewTimes());

        Map<String,Integer> map2 = new HashMap<>();
        map2.put("topicid",topic.getId());
        map2.put("userid",Integer.parseInt(userId));
        int is_collect = userCollectTopicMapper.getIsCollectInfo(map2);
        mapResult.put("has_star",is_collect);
        int is_like =  userTopicAgreeMapper.getIsAgreeInfo(map2);
        mapResult.put("has_like",is_like);

        Map<String,Integer> map3 = new HashMap<>();
        map3.put("followerid",Integer.parseInt(userId));
        map3.put("followedid",topic.getUserid());
        int is_followed = userFollowMapper.getIsFollowedInfo(map3);
        mapResult.put("has_follow",is_followed);
        mapResult.put("userid",topic.getUserid());

        return mapResult;
    }
    @PostMapping("/api/posts")
    @ResponseBody
    public RegRespObj doadd(@RequestHeader(value = USER_NAME) String userId,@RequestBody Map<String,Object> json) throws IOException {
        RegRespObj regRespObj = new RegRespObj();
        Topic topic = new Topic();
        User user =  userMapper.selectByPrimaryKey(Integer.parseInt(userId));

        topic.setCreateTime(new Date());
        topic.setUserid(Integer.parseInt(userId));
        topic.setTitle(json.get("title").toString());
        topic.setContent(json.get("content").toString());
        topic.setTopic_type(Integer.parseInt(json.get("type").toString()));
        List<Map<String,Object>> lstCover = (List<Map<String, Object>>) json.get("cover");
        if(lstCover.size() > 0)
        {
            Map<String, Object> map = lstCover.get(0);
            topic.setCover_url1(map.get("url").toString());
        }
        if(lstCover.size() > 1)
        {
            Map<String, Object> map = lstCover.get(1);
            topic.setCover_url2(map.get("url").toString());
        }
        if(lstCover.size() > 2)
        {
            Map<String, Object> map = lstCover.get(2);
            topic.setCover_url3(map.get("url").toString());
        }

        if(topic.getTopic_type() == 2)
        {
            ;
        }

        int result = topicMapper.insertSelective(topic);
        List<Integer> cateLst = (ArrayList<Integer>)json.get("categories");
        for(int cid : cateLst)
        {
            TopicCategoryRelation tcr = new TopicCategoryRelation();
            tcr.setCategoryId(cid);
            tcr.setTopicId(topic.getId());
            topicCategoryMapper.insertTopicCategory(tcr);
        }

        if(result > 0)
        {
            regRespObj.setStatus(0);

        }

        return regRespObj;

    }

    @PostMapping("/api/post_update/{tid}")
    @ResponseBody
    public RegRespObj do_update(@RequestHeader(value = USER_NAME) String userId,@PathVariable Integer tid,@RequestBody Map<String,Object> json) throws IOException {
        RegRespObj regRespObj = new RegRespObj();
        Topic topic = topicMapper.selectByPrimaryKey(tid);
//        User user =  userMapper.selectByPrimaryKey(Integer.parseInt(userId));

        topic.setTitle(json.get("title").toString());
        topic.setContent(json.get("content").toString());
        topic.setTopic_type(Integer.parseInt(json.get("type").toString()));
        topic.setCover_url1("");
        topic.setCover_url2("");
        topic.setCover_url3("");
        List<Map<String,Object>> lstCover = (List<Map<String, Object>>) json.get("cover");
        if(lstCover.size() > 0)
        {
            Map<String, Object> map = lstCover.get(0);
            topic.setCover_url1(map.get("url").toString());
        }
        if(lstCover.size() > 1)
        {
            Map<String, Object> map = lstCover.get(1);
            topic.setCover_url2(map.get("url").toString());
        }
        if(lstCover.size() > 2)
        {
            Map<String, Object> map = lstCover.get(2);
            topic.setCover_url3(map.get("url").toString());
        }

        if(topic.getTopic_type() == 2)
        {
            ;
        }

       int result = topicCategoryMapper.deleteCategoriesByTopicID(tid);

        int res = topicMapper.updateByPrimaryKeySelective(topic);
        List<Integer> cateLst = (ArrayList<Integer>)json.get("categories");
        for(int cid : cateLst)
        {
            TopicCategoryRelation tcr = new TopicCategoryRelation();
            tcr.setCategoryId(cid);
            tcr.setTopicId(topic.getId());
            topicCategoryMapper.insertTopicCategory(tcr);
        }

        if(result > 0)
        {
            regRespObj.setStatus(0);

        }

        return regRespObj;

    }


}
