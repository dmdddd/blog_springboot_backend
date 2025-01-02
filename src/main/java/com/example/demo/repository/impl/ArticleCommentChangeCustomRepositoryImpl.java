package com.example.demo.repository.impl;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.example.demo.model.ArticleCommentChange;
import com.example.demo.repository.ArticleCommentChangeCustom;

@Repository
public class ArticleCommentChangeCustomRepositoryImpl implements ArticleCommentChangeCustom {

    private final MongoTemplate mongoTemplate;

    public ArticleCommentChangeCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void incrementNetChange(String blogSlug, String articleSlug, int delta) {
        Query query = new Query();
        query.addCriteria(Criteria.where("blogSlug").is(blogSlug).and("articleSlug").is(articleSlug));

        Update update = new Update();
        update.inc("netChange", delta);

        mongoTemplate.upsert(query, update, ArticleCommentChange.class);
    }
}
