package com.neusoft.controller;

import com.alibaba.fastjson.JSON;
import com.neusoft.domain.Topic;
import com.neusoft.domain.User;
import com.neusoft.domain.UserMessage;
import com.neusoft.jwt.JwtUtil;
import com.neusoft.mapper.*;
import com.neusoft.response.Data;
import com.neusoft.response.RegRespObj;
import com.neusoft.util.MD5Utils;
import com.neusoft.util.StringDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2018/12/6.
 */
@Controller
//@RequestMapping("user")
public class UserController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    TopicMapper topicMapper;

    @Autowired
    CommentMapper commentMapper;

    @Autowired
    UserMessageMapper userMessageMapper;

    @Autowired
    UserCollectTopicMapper userCollectTopicMapper;

    @RequestMapping("reg")
    public String reg()
    {
        return "user/reg";
    }

    @RequestMapping("active")
    public String active()
    {
        return "user/active";
    }



    @RequestMapping("doreg")
    @ResponseBody
    public RegRespObj doReg(User user, HttpServletRequest request) throws Exception {
        RegRespObj regRespObj = new RegRespObj();
        User user1 = userMapper.selectByEmail(user.getEmail());
        if(user1==null){
            user.setKissNum(100);
            user.setJoinTime(new Date());
            String passwd = user.getPasswd();
            String pwd = MD5Utils.getPwd(passwd);
            user.setPasswd(pwd);

            int i = userMapper.insertSelective(user);
            if(i>0){
                User userReg = userMapper.selectByNickname(user.getNickname());
                //插入一条系统欢迎消息
                UserMessage userMessage = new UserMessage();
                userMessage.setCreateTime(new Date());
                userMessage.setTopicId(-1);
                userMessage.setMsgType(0);
                userMessage.setTriggerMsgUserId(0);
                userMessage.setRecvMsgUserId(userReg.getId());
                userMessageMapper.insertSelective(userMessage);

                request.getSession().setAttribute("userinfo",userReg);

                regRespObj.setStatus(0);
                System.out.println(request.getServletContext().getContextPath());
                regRespObj.setAction(request.getServletContext().getContextPath() + "/");
            }else {
                regRespObj.setStatus(1);
//                regRespObj.setMsg("数据库错误，联系管理员");
            }
        }else {
            regRespObj.setStatus(1);
//            regRespObj.setMsg("邮箱重复，请换邮箱注册");
        }
        return regRespObj;

    }

    @RequestMapping("logout")
    public String logout(HttpServletRequest request)
    {
        request.getSession().invalidate();
        return "redirect:" + request.getServletContext().getContextPath() +"/";
    }


    @RequestMapping("index")
    public ModelAndView index(HttpSession httpSession)
    {
        User userLogin = (User)httpSession.getAttribute("userinfo");
        List<Map<String,Object>> mapList = userCollectTopicMapper.getCollectionsByUserID(userLogin.getId());


        List<Topic> topicList = topicMapper.getTopicsByUserID(userLogin.getId());

        for(Topic topic : topicList)
        {
            Date date = topic.getCreateTime();
            String strDate = StringDate.getStringDate(date);
            topic.setCreateTimeStr(strDate);
        }

        int topic_num = topicMapper.getTopicNumByUserID(userLogin.getId());
        int collect_num = userCollectTopicMapper.getCollectNumByUserID(userLogin.getId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/index");
        modelAndView.addObject("collections",mapList);
        modelAndView.addObject("topics",topicList);
        modelAndView.addObject("topic_num",topic_num);
        modelAndView.addObject("collect_num",collect_num);
        return modelAndView;
    }

    @RequestMapping("api/user/{uid}")
    @ResponseBody
    public User getUserByID(@PathVariable Integer uid)
    {
        User user = userMapper.selectByPrimaryKey(uid);
        System.out.println("getUserByID");
        System.out.println(user.getPicPath());
        return user;
    }
    @PostMapping("dologin")
    @ResponseBody
    public Map<String,Object> dologin(@RequestBody User user)
    {
        user.setPasswd(MD5Utils.getPwd(user.getPasswd()));
        User userResult = userMapper.selectByEmailAndPass(user);
        Map<String,Object> respMap =  new HashMap<String,Object>();
        if(userResult == null)
        {
            respMap.put("message","登录失败");
        }
        else
        {
            String jwt = JwtUtil.generateToken(String.valueOf(userResult.getId()));
            Map<String,Object> dataMap =  new HashMap<String,Object>();
            dataMap.put("token",jwt);
            dataMap.put("user",userResult.getId());

            respMap.put("message","登录成功");
            respMap.put("data",dataMap);
        }

        return respMap;
    }
    @PostMapping("login_c")
    @ResponseBody
    public Map<String,Object> client_login(@RequestBody User user)
    {
        user.setPasswd(MD5Utils.getPwd(user.getPasswd()));
        User userResult = userMapper.selectByEmailAndPass(user);
        Map<String,Object> respMap =  new HashMap<String,Object>();
        if(userResult == null)
        {
            respMap.put("message","登录失败");
        }
        else
        {
            String jwt = JwtUtil.generateToken(String.valueOf(userResult.getId()));
            Map<String,Object> dataMap =  new HashMap<String,Object>();
            dataMap.put("token",jwt);
            dataMap.put("user_id",userResult.getId());

            respMap.put("message","登录成功");
            respMap.put("statusCode","200");
            respMap.put("data",dataMap);
        }

        return respMap;
    }

    @PostMapping("/api/user_update/{uid}")
    @ResponseBody
    public RegRespObj user_update(@PathVariable Integer uid,@RequestBody Map<String,Object> params) throws IOException {
        User user = userMapper.selectByPrimaryKey(uid);
        if(params.containsKey("picPath"))
            user.setPicPath(params.get("picPath").toString());
        if(params.containsKey("gender"))
            user.setSex(Integer.parseInt(params.get("gender").toString()));
        if(params.containsKey("password"))
            user.setPasswd(MD5Utils.getPwd(params.get("password").toString()));
        if(params.containsKey("nickname"))
            user.setNickname(params.get("nickname").toString());

        userMapper.updateByPrimaryKeySelective(user);
        System.out.println("user_update");
        System.out.println(user.getPicPath());
        RegRespObj regRespObj = new RegRespObj();
        regRespObj.setStatus(0);

        return regRespObj;
    }

    @PostMapping("/api/upload")
    @ResponseBody
    public Map<String,Object> upload(@RequestParam MultipartFile file, HttpServletRequest request) throws IOException {
        Map<String,Object> regRespObj = new HashMap<String,Object>();
        if(file.getSize()>0){
            String realPath = request.getServletContext().getRealPath("/res/uploadImgs");
            File file1 = new File(realPath);
            if(!file1.exists()){
                file1.mkdirs();
            }
            UUID uuid = UUID.randomUUID();
            File file2 = new File(realPath+File.separator+uuid+file.getOriginalFilename());
            file.transferTo(file2);
            Map<String,Object> dataMap =  new HashMap<String,Object>();
            dataMap.put("id",1);
            dataMap.put("url","/res/uploadImgs"+File.separator+uuid+file.getOriginalFilename());
            System.out.println("upload");
            System.out.println("/res/uploadImgs"+File.separator+uuid+file.getOriginalFilename());
            regRespObj.put("data",dataMap);
        }
        else
        {
            Map<String,Object> dataMap =  new HashMap<String,Object>();
            regRespObj.put("data",dataMap);
        }
        return regRespObj;
    }
}
