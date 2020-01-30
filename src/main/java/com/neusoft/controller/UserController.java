package com.neusoft.controller;

import com.neusoft.domain.User;
import com.neusoft.jwt.JwtUtil;
import com.neusoft.mapper.*;
import com.neusoft.response.RegRespObj;
import com.neusoft.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.neusoft.jwt.JwtUtil.USER_NAME;

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



    @RequestMapping("/register")
    @ResponseBody
    public RegRespObj doReg(@RequestBody User user) throws Exception {
        RegRespObj regRespObj = new RegRespObj();
        User user1 = userMapper.selectByEmail(user.getEmail());
        if(user1==null){
            user.setJoinTime(new Date());
            String passwd = user.getPasswd();
            String pwd = MD5Utils.getPwd(passwd);
            user.setPasswd(pwd);

            int i = userMapper.insertSelective(user);
            if(i>0){
                regRespObj.setStatus(0);
                regRespObj.setMessage("注册成功");
            }else {
                regRespObj.setStatus(1);
                regRespObj.setMessage("数据库错误，联系管理员");
            }
        }else {
            regRespObj.setStatus(1);
            regRespObj.setMessage("邮箱重复");
        }
        return regRespObj;

    }

//    @RequestMapping("logout")
//    public String logout(HttpServletRequest request)
//    {
//        request.getSession().invalidate();
//        return "redirect:" + request.getServletContext().getContextPath() +"/";
//    }


    @RequestMapping("/api/user_comments")
    @ResponseBody
    public List<Map<String, Object>> user_comments(@RequestHeader(value = USER_NAME) String userId)
    {

        List<Map<String, Object>> commentsByUserID = commentMapper.getCommentsByUserID(Integer.parseInt(userId));

        return commentsByUserID;
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
            regRespObj.put("message","文件上传成功");
        }
        else
        {
            Map<String,Object> dataMap =  new HashMap<String,Object>();
            regRespObj.put("data",dataMap);
        }
        return regRespObj;
    }
}
