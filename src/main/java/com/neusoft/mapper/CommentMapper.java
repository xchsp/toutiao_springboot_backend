package com.neusoft.mapper;

import com.neusoft.domain.Comment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Component
public interface CommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);

    List<Map<String,Object>> getCommentsByTopicID(Map<String, Object> params);

    List<Map<String,Object>> getCommentsByUserID(int userid);

    int getCommentsCountsByTopicID(int tid);
}