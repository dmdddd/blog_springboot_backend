package com.example.demo.model;

import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = "article_comment_change")
@CompoundIndexes({
    @CompoundIndex(name = "blog_article_idx", def = "{'blogSlug': 1, 'articleSlug': 1}", unique = true)
})
public class ArticleCommentChange {

    @Id
    private String id;

    @Field("blogSlug")
    private String blogSlug;

    @Field("articleSlug")
    private String articleSlug;

    @Field("netChange")
    private int netChange;
}
