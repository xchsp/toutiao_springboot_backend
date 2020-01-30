package com.neusoft.mapper;

import com.neusoft.domain.UserTopicAgree;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface UserTopicAgreeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserTopicAgree record);

    int insertSelective(UserTopicAgree record);

    UserTopicAgree selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserTopicAgree record);

    int updateByPrimaryKey(UserTopicAgree record);

    int deleteByUserIDAndTopicID(Map<String, Object> params);
    int getIsAgreeInfo(Map<String, Integer> map);
    int delAgreeInfo(Map<String, Integer> map);
}