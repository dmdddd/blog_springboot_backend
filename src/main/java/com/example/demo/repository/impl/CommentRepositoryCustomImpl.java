package com.example.demo.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Comment;
import com.example.demo.repository.CommentRepositoryCustom;

@Repository
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void updatePhotoUrlForEmail(String email, String photoUrl) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userEmail").is(email)); // Match documents with the given email

        Update update = new Update();
        update.set("userIcon", photoUrl); // Set the new photoUrl for matching documents

        mongoTemplate.updateMulti(query, update, Comment.class); // Perform bulk update
    }
}
