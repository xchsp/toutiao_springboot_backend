package com.neusoft.controller;
import com.neusoft.domain.TopicCategory;
import com.neusoft.mapper.TopicCategoryMapper;
import com.neusoft.mapper.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.neusoft.jwt.JwtUtil.USER_NAME;

@RestController
public class CategoryController {
    @Autowired
    TopicCategoryMapper categoryMapper;

    @GetMapping("/api/category")
    @ResponseBody
    List<TopicCategory> getCategory(@RequestHeader(value = USER_NAME) String userId) {
        List<TopicCategory> categories = categoryMapper.getAllCategories();
        return categories;
    }
}


