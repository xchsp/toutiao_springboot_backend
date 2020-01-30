package com.neusoft.controller;

import com.alibaba.fastjson.JSON;
import com.neusoft.domain.*;
import com.neusoft.mapper.*;
import com.neusoft.response.RegRespObj;
import com.neusoft.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.neusoft.jwt.JwtUtil.USER_NAME;

/**
 * Created by Administrator on 2018/12/13.
 */
@Controller
@RequestMapping("api")
public class ApiController {
    @Autowired
    TopicMapper topicMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    UserCommentAgreeMapper userCommentAgreeMapper;
    @Autowired
    UserTopicAgreeMapper userTopicAgreeMapper;

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserMessageMapper userMessageMapper;
    @RequestMapping("jie-set")
    public void jieSet(Integer id, Integer rank, String field, HttpServletResponse response) throws IOException {
        Topic topic = topicMapper.selectByPrimaryKey(id);
        if(field.equals("stick"))
        {
            topic.setIsTop(rank);
        }
        else if(field.equals("status"))
        {
            topic.setIsGood(rank);
        }
        topicMapper.updateByPrimaryKeySelective(topic);
        RegRespObj regRespObj = new RegRespObj();
        regRespObj.setStatus(0);

        response.getWriter().println(JSON.toJSONString(regRespObj));
    }
    @RequestMapping("activate")
    public void activate(HttpSession httpSession, HttpServletResponse response) throws Exception {
        User userLogin = (User)httpSession.getAttribute("userinfo");
        UUID uuid = UUID.randomUUID();
        String strUuid = uuid.toString().replace("-","");
        userLogin.setActiveCode(strUuid);
        userMapper.updateByPrimaryKeySelective(userLogin);
        MailUtil.sendActiveMail(userLogin.getEmail(),strUuid);

        RegRespObj regRespObj = new RegRespObj();
        regRespObj.setStatus(0);
        response.getWriter().println(JSON.toJSONString(regRespObj));
    }

    @RequestMapping("jieda-accept")
    public void jiedaAccept(Integer id, HttpServletResponse response, HttpSession httpSession) throws IOException {
        RegRespObj regRespObj = new RegRespObj();
        regRespObj.setStatus(0);

        //评论记录的is_choose变成1
        Comment comment = commentMapper.selectByPrimaryKey(id);
        comment.setIsChoose(1);
        commentMapper.updateByPrimaryKeySelective(comment);
        //评论所在的topic的is_end变成1
        Topic topic = topicMapper.selectByPrimaryKey(comment.getTopicId());
        topic.setIsEnd(1);
        topicMapper.updateByPrimaryKeySelective(topic);

        //记录消息
        //在消息表中添加一条消息
        User userLogin = (User)httpSession.getAttribute("userinfo");
        UserMessage userMessage = new UserMessage();
        userMessage.setCreateTime(new Date());
        userMessage.setTopicId(comment.getTopicId());
        userMessage.setMsgType(2);
        userMessage.setTriggerMsgUserId(userLogin.getId());
        userMessage.setRecvMsgUserId(comment.getUserId());
        userMessageMapper.insertSelective(userMessage);


        User user = userMapper.selectByPrimaryKey(comment.getUserId());
        user.setKissNum(user.getKissNum() + topic.getKissNum());
        userMapper.updateByPrimaryKeySelective(user);
        response.getWriter().println(JSON.toJSONString(regRespObj));
    }

    @RequestMapping("upload")
    @ResponseBody
    public RegRespObj upload(@RequestParam MultipartFile file, HttpServletRequest request) throws IOException {
        RegRespObj regRespObj = new RegRespObj();
        if(file.getSize()>0){
            String realPath = request.getServletContext().getRealPath("/res/uploadImgs");
            File file1 = new File(realPath);
            if(!file1.exists()){
                file1.mkdirs();
            }
            UUID uuid = UUID.randomUUID();
            File file2 = new File(realPath+File.separator+uuid+file.getOriginalFilename());
            file.transferTo(file2);

//            HttpSession session = request.getSession();
//            User userinfo = (User)session.getAttribute("userinfo");
//            userinfo.setPicPath(uuid+file.getOriginalFilename());
//            session.setAttribute("userinfo",userinfo);


            regRespObj.setUrl(request.getServletContext().getContextPath()+"/res/uploadImgs/"+uuid+file.getOriginalFilename());
            regRespObj.setStatus(0);
        }
        else
        {
            regRespObj.setStatus(1);
        }
        return regRespObj;
    }

    @RequestMapping("/post_like/{tid}")
    @ResponseBody
    public RegRespObj add_remove_CollectInfo(@PathVariable  Integer tid, @RequestHeader(value = USER_NAME) String userId) throws IOException {

        Map<String,Integer> map2 = new HashMap<>();
        map2.put("topicid",tid);
        map2.put("userid",Integer.parseInt(userId));
        int is_agree = userTopicAgreeMapper.getIsAgreeInfo(map2);
        RegRespObj regRespObj = new RegRespObj();
        if (is_agree == 0)
        {
            UserTopicAgree userCollectTopic = new UserTopicAgree();
            userCollectTopic.setTopic_id(tid);
            userCollectTopic.setUserid(Integer.parseInt(userId));
            userTopicAgreeMapper.insertSelective(userCollectTopic);

            Topic topic = topicMapper.selectByPrimaryKey(tid);
            Integer viewTimes = topic.getViewTimes();
            viewTimes += 1;
            topic.setViewTimes(viewTimes);
            topicMapper.updateByPrimaryKey(topic);
            regRespObj.setMessage("点赞成功");
        }
        else
        {
            userTopicAgreeMapper.delAgreeInfo(map2);
            Topic topic = topicMapper.selectByPrimaryKey(tid);
            Integer viewTimes = topic.getViewTimes();
            viewTimes -= 1;
            topic.setViewTimes(viewTimes);
            topicMapper.updateByPrimaryKey(topic);
            regRespObj.setMessage("取消成功");
        }



        regRespObj.setStatus(0);
        return regRespObj;
    }
//    @RequestMapping("post_like/{tid}")
//    public void jiedaZan(@PathVariable Integer tid) throws IOException
//    {
//        RegRespObj regRespObj = new RegRespObj();
//
//        if(ok == false)//点赞
//        {
//            //当前评论的likenum++
//            Comment comment = commentMapper.selectByPrimaryKey(id);
//            comment.setLikeNum(comment.getLikeNum() + 1);
//            commentMapper.updateByPrimaryKeySelective(comment);
//            //在tab_user_comment_agree添加一条相应的记录
//            UserCommentAgree userCommentAgree = new UserCommentAgree();
//            userCommentAgree.setCommentid(id);
//            userCommentAgree.setUserid(user.getId());
//            userCommentAgreeMapper.insertSelective(userCommentAgree);
//
//        }
//        else//取消点赞
//        {
//            //当前评论的likenum--
//            Comment comment = commentMapper.selectByPrimaryKey(id);
//            comment.setLikeNum(comment.getLikeNum() - 1);
//            commentMapper.updateByPrimaryKeySelective(comment);
//            //在tab_user_comment_agree删除一条相应的记录
//
//            Map<String,Object> map = new HashMap<>();
//            map.put("userid",user.getId());
//            map.put("commentid",id);
//            userCommentAgreeMapper.deleteByUserIDAndCommentID(map);
//        }
//
//
//
//        response.getWriter().println(JSON.toJSONString(regRespObj));
//    }

}
