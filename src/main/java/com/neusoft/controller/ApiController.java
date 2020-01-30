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
    UserTopicAgreeMapper userTopicAgreeMapper;


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


}
