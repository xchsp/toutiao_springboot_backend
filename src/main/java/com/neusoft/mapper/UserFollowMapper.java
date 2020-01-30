package com.neusoft.mapper;

import com.neusoft.domain.User;
import com.neusoft.domain.UserFollow;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface UserFollowMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserFollow record);

    int insertSelective(UserFollow record);

    UserFollow selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserFollow record);

    int updateByPrimaryKey(UserFollow record);

//    int deleteByFollowerIDAndFollowedID(Map<String, Object> params);
    int getIsFollowedInfo(Map<String, Integer> map);
    int delFollowedInfo(Map<String, Integer> map);
    List<User> getFollowedByUserID(int userid);
}