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
import com.example.demo.model.Blog;
import com.mongodb.bulk.BulkWriteResult;

@Service
public class BlogArticleAggregationFailsafeService {

    private static final Logger logger = LoggerFactory.getLogger(BlogArticleAggregationFailsafeService.class);

    private final MongoTemplate mongoTemplate;

    private static final int BATCH_SIZE = 100;

    @Autowired
    public BlogArticleAggregationFailsafeService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // @Scheduled(cron = "0 * * * * *") // Every minute
    @Scheduled(cron = "0 0 */12 * * *") // Every 12 hours
    public void aggregateArticleCountsInBatches() {
        Object lastProcessedId = null;
        long totalBlogs = mongoTemplate.count(new Query(), Blog.class);
        logger.info("Starting article count aggregation for {} blogs.", totalBlogs);

        int i = 0;
        while (true) {
            List<Blog> blogs = fetchBlogsInBatch(lastProcessedId);
            if (blogs.isEmpty()) { // No more articles to process
                break;
            }

            logger.info("Processing batch {}", i++);

            BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Blog.class);
            boolean toExecute = false;
            for (Blog blog : blogs) {
                long totalArticles = calculateTotalArticlesForBlog(blog);
                if (blog.getArticleCount() == totalArticles) {
                    continue;
                }
                toExecute = true;
                Update update = new Update().set("articleCount", totalArticles);
                bulkOperations.updateOne(Query.query(Criteria.where("_id").is(blog.getId())), update);
            }

            if (toExecute) {
                BulkWriteResult bulkWriteResult = bulkOperations.execute();
                logger.info("Batch update completed. Updated {} blogs' article count.", bulkWriteResult.getModifiedCount());
            } else {
                logger.info("Batch completed with no updates.");
            }

            lastProcessedId = blogs.get(blogs.size() - 1).getId();
        }

        logger.info("Article count aggregation finished.");
    }

    private List<Blog> fetchBlogsInBatch(Object lastProcessedId) {
        Query query = new Query();

        if (lastProcessedId != null) {
            // Fetch articles with `_id` greater than the last processed `_id`
            query.addCriteria(Criteria.where("_id").gt(lastProcessedId));
        }

        // Limit to batch size
        query.limit(BATCH_SIZE);

        // Fetch articles from MongoDB
        return mongoTemplate.find(query, Blog.class);
    }

    private long calculateTotalArticlesForBlog(Blog blog) {
        Query query = new Query(Criteria.where("blog").is(blog.getName()));
        return mongoTemplate.count(query, Article.class);
    }
}