package com.neusoft.mapper;

import com.neusoft.domain.UserMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Component
public interface UserMessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserMessage record);

    int insertSelective(UserMessage record);

    UserMessage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserMessage record);

    int updateByPrimaryKey(UserMessage record);
    List<Map<String,Object>> getMessagesByUserID(int userid);
    int getUnreadMsgCountByUserID(int userid);

    int updateUserMsgReadState(int userid);
    int delAllUserMsg(int userid);
}