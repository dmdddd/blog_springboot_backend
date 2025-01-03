package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import com.example.demo.exceptions.GlobalExceptionHandler;
import com.example.demo.model.Article;
import com.example.demo.model.ArticleCommentChange;
import com.example.demo.repository.ArticleCommentChangeRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentCountUpdateService {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ArticleCommentChangeRepository articleCommentChangeRepository;  // Repository for your temporary collection

    private static final int BATCH_SIZE = 100;  // Batch size for processing

    // This method will run every minute (cron expression: "0 * * * * *" means every minute)
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void updateCommentCounts() {
        Page<ArticleCommentChange> commentCountsPage;
        int processedRecords = 0;

        do {
            commentCountsPage = articleCommentChangeRepository.findAll(PageRequest.of(processedRecords, BATCH_SIZE));

            for (ArticleCommentChange commentCount : commentCountsPage) {
                try {
                    int netChange = commentCount.getNetChange();

                    long updatedCount = mongoTemplate.updateFirst(
                            Query.query(Criteria.where("blog").is(commentCount.getBlogSlug()).and("name").is(commentCount.getArticleSlug())),
                            new Update().inc("commentCount", netChange),
                            Article.class
                    ).getModifiedCount();

                    if (updatedCount == 0) {
                        logger.warn("Article not found for [blog={}, article={}]", commentCount.getBlogSlug(), commentCount.getArticleSlug());
                    }

                    // Once processed, delete the entry from the temporary collection
                    articleCommentChangeRepository.delete(commentCount);
                } catch (Exception e) {
                    logger.error("Error processing comment count for article: " + commentCount.getArticleSlug());
                }
            }

            processedRecords += BATCH_SIZE; // Update the processed record counter

        } while (commentCountsPage.hasNext());  // Continue until all records are processed
    }

        /**
     * Increment the netChange for the specified article.
     *
     * @param blogSlug     The slug of the blog.
     * @param articleSlug  The slug of the article.
     * @param delta        The value to increment (usually +1).
     */
    public void incrementCommentCount(String blogSlug, String articleSlug, int delta) {
        updateCommentCount(blogSlug, articleSlug, delta);
    }

    /**
     * Decrement the netChange for the specified article.
     *
     * @param blogSlug     The slug of the blog.
     * @param articleSlug  The slug of the article.
     * @param delta        The value to decrement (usually -1).
     */
    public void decrementCommentCount(String blogSlug, String articleSlug, int delta) {
        updateCommentCount(blogSlug, articleSlug, -delta);
    }

    /**
     * Update the netChange value for the specified article.
     *
     * @param blogSlug     The slug of the blog.
     * @param articleSlug  The slug of the article.
     * @param delta        The value to increment or decrement.
     */
    private void updateCommentCount(String blogSlug, String articleSlug, int delta) {
        // Create a query to match the document based on blogSlug and articleSlug
        Query query = new Query();
        query.addCriteria(Criteria.where("blogSlug").is(blogSlug).and("articleSlug").is(articleSlug));

        // Create an update to increment the netChange field by the given delta
        Update update = new Update();
        update.inc("netChange", delta);

        // Perform the update operation using MongoTemplate
        mongoTemplate.upsert(query, update, ArticleCommentChange.class);
    }
}