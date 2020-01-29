package com.neusoft.controller;

import com.alibaba.fastjson.JSON;
import com.neusoft.domain.*;
import com.neusoft.mapper.*;
import com.neusoft.response.RegRespObj;
import com.neusoft.util.StringDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    UserMessageMapper userMessageMapper;
    @Autowired
    UserCollectTopicMapper userCollectTopicMapper;

    @RequestMapping("index/{cid}/{typeid}")
    public ModelAndView index(@PathVariable Integer cid, @PathVariable Integer typeid)
    {
        List<Topic> topics = topicMapper.getTop10Topics();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("jie/index");
        modelAndView.addObject("cid",cid);
        modelAndView.addObject("typeid",typeid);
        modelAndView.addObject("topics",topics);
        return modelAndView;
    }

    @RequestMapping("add/{tid}")
    public ModelAndView add(@PathVariable Integer tid)
    {

        List<TopicCategory> topicCategoryList = topicCategoryMapper.getAllCategories();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("categories",topicCategoryList);
        modelAndView.addObject("tid",tid);
        if(tid > 0)
        {
            Topic topic = topicMapper.selectByPrimaryKey(tid);
            modelAndView.addObject("topic",topic);
        }

        modelAndView.setViewName("jie/add");
        return modelAndView;
    }
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
        Map<String,Integer> map2 = new HashMap<>();
        map2.put("topicid",topic.getId());
        map2.put("userid",Integer.parseInt(userId));
        int is_collect = userCollectTopicMapper.getIsCollectInfo(map2);
        mapResult.put("has_star",is_collect);

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

//        response.getWriter().println(JSON.toJSONString(regRespObj));
    }

    @PostMapping("/api/post_update/{tid}")
    @ResponseBody
    public RegRespObj do_update(@RequestHeader(value = USER_NAME) String userId,@PathVariable Integer tid,@RequestBody Map<String,Object> json) throws IOException {
        RegRespObj regRespObj = new RegRespObj();
        Topic topic = topicMapper.selectByPrimaryKey(tid);
        User user =  userMapper.selectByPrimaryKey(Integer.parseInt(userId));

//        topic.setCreateTime(new Date());
//        topic.setUserid(Integer.parseInt(userId));
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
//        List<TopicCategory> cateLstDel = topicCategoryMapper.getCategoriesByTopicID(tid);
//        for(TopicCategory tc : cateLstDel)
//        {
//
//        }

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

//        response.getWriter().println(JSON.toJSONString(regRespObj));
    }

    @RequestMapping("reply")
    public void reply(Comment comment, String content, HttpServletRequest request, HttpServletResponse response) throws IOException {
        RegRespObj regRespObj = new RegRespObj();
        HttpSession httpSession = request.getSession();
        User user = (User)httpSession.getAttribute("userinfo");
        if(user != null)
        {
            //插入评论
            comment.setCommentContent(content);
            comment.setUserId(user.getId());
            comment.setCommentTime(new Date());
            commentMapper.insertSelective(comment);
            regRespObj.setStatus(0);


            //更新topic的评论数字段
            Topic topic = topicMapper.selectByPrimaryKey(comment.getTopicId());
            int comment_num = topic.getCommentNum();
            topic.setCommentNum(comment_num + 1);
            topicMapper.updateByPrimaryKeySelective(topic);

            //在消息表中添加一条消息
            if(topic.getUserid() != user.getId())
            {
                UserMessage userMessage = new UserMessage();
                userMessage.setCreateTime(new Date());
                userMessage.setTopicId(comment.getTopicId());
                userMessage.setMsgType(1);
                userMessage.setTriggerMsgUserId(user.getId());
                userMessage.setRecvMsgUserId(topic.getUserid());
                userMessageMapper.insertSelective(userMessage);
            }


            Pattern pattern = Pattern.compile("@(.*?)\\s");
            Matcher matcher = pattern.matcher(comment.getCommentContent());
            Set<String> stringSet = new HashSet<>();
            while (matcher.find()) {
                String nickname = matcher.group(1);
                stringSet.add(nickname);
            }
            for(String username : stringSet)
            {
                User user1 = userMapper.selectByNickname(username);
                if(user1 != null)
                {
                    UserMessage userMessage = new UserMessage();
                    userMessage.setCreateTime(new Date());
                    userMessage.setTopicId(comment.getTopicId());
                    userMessage.setMsgType(1);
                    userMessage.setTriggerMsgUserId(user.getId());
                    userMessage.setRecvMsgUserId(user1.getId());
                    userMessageMapper.insertSelective(userMessage);
                }
            }

        }
        else
        {
            String referer = request.getHeader("Referer");
            httpSession.setAttribute("referer",referer);
            regRespObj.setStatus(0);
            regRespObj.setAction(request.getServletContext().getContextPath()+"/user/login");
        }

        response.getWriter().println(JSON.toJSONString(regRespObj));
        return;
    }
}
