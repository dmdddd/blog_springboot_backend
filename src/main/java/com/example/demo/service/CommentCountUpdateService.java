package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.model.Article;
import com.example.demo.model.ArticleCommentChange;
import com.example.demo.repository.ArticleCommentChangeRepository;
import com.example.demo.repository.ArticleRepository;

import jakarta.transaction.Transactional;

@Service
public class CommentCountUpdateService {

    @Autowired
    private ArticleRepository articleRepository;  // Repository for your Articles

    @Autowired
    private ArticleCommentChangeRepository articleCommentChangeRepository;  // Repository for your temporary collection

    // This method will run every minute (cron expression: "0 * * * * *" means every minute)
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void updateCommentCounts() {
        // Get all records from the temporary collection (article_comment_count) that have not been processed yet
        Iterable<ArticleCommentChange> commentCounts = articleCommentChangeRepository.findAll();

        for (ArticleCommentChange commentCount : commentCounts) {
            int netChange = commentCount.getNetChange();  // The net change in comment count

            Optional<Article> optionalArticle = articleRepository.findByBlogAndName(commentCount.getBlogSlug(), commentCount.getArticleSlug());

            if (optionalArticle.isPresent()) {
                Article article = optionalArticle.get();
                article.setCommentCount(article.getCommentCount() + netChange);
                articleRepository.save(article);  // Save the updated article
            }

            // Once processed, delete the entry from the temporary collection
            articleCommentChangeRepository.delete(commentCount);
        }
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
        ArticleCommentChange count = articleCommentChangeRepository.findByBlogSlugAndArticleSlug(blogSlug, articleSlug)
        .orElseGet(() -> new ArticleCommentChange(null, blogSlug, articleSlug, 0)); // Use orElseGet for lazily creating new instance

        count.setNetChange(count.getNetChange() + delta);
        articleCommentChangeRepository.save(count);
    }
}