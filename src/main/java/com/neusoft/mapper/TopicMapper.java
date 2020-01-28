package com.neusoft.mapper;

import com.neusoft.domain.PageInfo;
import com.neusoft.domain.Topic;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Component
public interface TopicMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Topic record);

    int insertSelective(Topic record);

    Topic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Topic record);

    int updateByPrimaryKeyWithBLOBs(Topic record);

    int updateByPrimaryKey(Topic record);

    List<Map<String,Object>> getAllTopics();

    Map<String,Object> getTopicInfo(int topicID);

    int getTopicTotal(PageInfo pageInfo);
    List<Map<String,Object>> getPagedTopics(PageInfo pageInfo);

//    int getTopicByCategoryID(int cid);
//    List<Map<String,Object>> getPagedTopicsByCategoryID(PageInfo pageInfo);

    List<Map<String,Object>> getTopTopics();

    List<Topic> getTopicsByUserID(int userid);
    int getTopicNumByUserID(int userid);
    List<Topic> getTop10Topics();
}