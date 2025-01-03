package com.example.demo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.example.demo.model.Article;
import com.example.demo.model.Comment;
import com.mongodb.bulk.BulkWriteResult;

@Service
public class ArticleCommentAggregationFailsafeService {

    private static final Logger logger = LoggerFactory.getLogger(ArticleCommentAggregationFailsafeService.class);

    private final MongoTemplate mongoTemplate;

    private static final int BATCH_SIZE = 5;

    @Autowired
    public ArticleCommentAggregationFailsafeService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // @Scheduled(cron = "0 * * * * *") // Every minute
    @Scheduled(cron = "0 0 */12 * * *") // Every 12 hours
    public void aggregateCommentCountsInBatches() {
        Object lastProcessedId = null;
        long totalArticles = mongoTemplate.count(new Query(), Article.class);
        logger.info("Starting comment count aggregation for {} articles.", totalArticles);

        int i = 0;
        while (true) {
            List<Article> articles = fetchArticlesInBatch(lastProcessedId);
            if (articles.isEmpty()) { // No more articles to process
                break;
            }

            logger.info("Processing batch {}", i++);

            BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Article.class);
            boolean toExecute = false;
            for (Article article : articles) {
                long totalComments = calculateTotalCommentsForArticle(article);
                if (article.getCommentCount() == totalComments) {
                    continue;
                }
                toExecute = true;
                Update update = new Update().set("commentCount", totalComments);
                bulkOperations.updateOne(Query.query(Criteria.where("_id").is(article.getId())), update);
            }

            if (toExecute) {
                BulkWriteResult bulkWriteResult = bulkOperations.execute();
                logger.info("Batch update completed. Updated {} articles' comment count.", bulkWriteResult.getModifiedCount());
            } else {
                logger.info("Batch completed with no updates.");
            }

            lastProcessedId = articles.get(articles.size() - 1).getId();
        }

        logger.info("Comment count aggregation finished.");
    }

    private List<Article> fetchArticlesInBatch(Object lastProcessedId) {
        Query query = new Query();

        if (lastProcessedId != null) {
            // Fetch articles with `_id` greater than the last processed `_id`
            query.addCriteria(Criteria.where("_id").gt(lastProcessedId));
        }

        // Limit to batch size
        query.limit(BATCH_SIZE);

        // Fetch articles from MongoDB
        return mongoTemplate.find(query, Article.class);
    }

    private long calculateTotalCommentsForArticle(Article article) {
        Query query = new Query(Criteria.where("articleName").is(article.getName()).and("blog").is(article.getBlog()));
        return mongoTemplate.count(query, Comment.class);
    }
}