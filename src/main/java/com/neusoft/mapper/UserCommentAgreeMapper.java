package com.neusoft.mapper;

import com.neusoft.domain.UserCommentAgree;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public interface UserCommentAgreeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCommentAgree record);

    int insertSelective(UserCommentAgree record);

    UserCommentAgree selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCommentAgree record);

    int updateByPrimaryKey(UserCommentAgree record);

    int deleteByUserIDAndCommentID(Map<String, Object> params);
}