package com.neusoft.mapper;

import com.neusoft.domain.TopicCategory;
import com.neusoft.domain.TopicCategoryRelation;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface TopicCategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicCategory record);

    int insertSelective(TopicCategory record);

    int insertTopicCategory(TopicCategoryRelation record);

    TopicCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicCategory record);

    int updateByPrimaryKey(TopicCategory record);

    List<TopicCategory> getAllCategories();
    List<TopicCategory> getCategoriesByTopicID(Integer topicID);
    int deleteCategoriesByTopicID(Integer topicID);
}