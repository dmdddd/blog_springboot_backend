package com.example.demo.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Comment;
import com.example.demo.repository.CommentRepositoryCustom;
import com.mongodb.client.result.UpdateResult;

@Repository
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public long updatePhotoUrlForEmail(String email, String photoUrl) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userEmail").is(email)); // Match documents with the given email

        Update update = new Update();
        update.set("userIcon", photoUrl); // Set the new photoUrl for matching documents

        UpdateResult result = mongoTemplate.updateMulti(query, update, Comment.class); // Perform bulk update
        return result.getModifiedCount();
    }

    @Override
    public long updateCommentsArticleName(String blog, String oldName, String newName) {
        Query query = new Query();
        query.addCriteria(Criteria.where("blog").is(blog).and("articleName").is(oldName));

        Update update = new Update().set("articleName", newName);

        UpdateResult result = mongoTemplate.updateMulti(query, update, Comment.class);
        return result.getModifiedCount();
    }
}
