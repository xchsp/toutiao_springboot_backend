package com.neusoft.mapper;

import com.neusoft.domain.UserQiandao;
import org.springframework.stereotype.Component;

@Component
public interface UserQiandaoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserQiandao record);

    int insertSelective(UserQiandao record);

    UserQiandao selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserQiandao record);

    int updateByPrimaryKey(UserQiandao record);

    UserQiandao selectByUserID(Integer userid);
}