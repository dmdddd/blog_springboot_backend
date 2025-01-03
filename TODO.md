# TODO
- [ ] Context Bug  
    GO TO:  
    http://localhost:3000/blogs/web-programming/articles  
    Enter article  
    Refresh  
    Navbar is not correct ->>>> the currentBlog in the context is null  
- [ ] fix the getAllArticles_shouldReturnCorrectArticle test
- [ ] Let admins add custom pages
- [ ] Let admins rearrange custom pages
- [ ] Article list for all blogs (discover articles )
- [ ] Pagination and sorting for artciles
- [ ] CommentCountUpdateService update paging to cursor based
- [ ] Pagination and sorting for comments
- [ ] Add categories for blogs
- [ ] Add categories for articles
- [ ] Article status - some articles can be saved as drafts. status=[Draft, published, pending review, scheduled, archived, trashed (recycle bin before removing completely)]
- [ ] Article - thumbnail: A URL or path to a thumbnail image for the article.
- [ ] Article - views: A count of how many times the article has been viewed.
- [ ] Followers for blogs
- [ ] Likes for blogs
- [ ] Blog page editing history - keep a history of edits so users can revert to previous versions if needed.

# Prompt
Blog management platform where users can create their own blogs.
When a blog is created admins can add pages like about or FAQ, admins and editors can write articles and other signed in users can comment and upvote articles.
Tech stack: react and spring boot using Gradle, mongoDB database(collections: blogs, pages, articles, comments), firebase authentication

# Previous 
- [x] When adding a comment, check that the article exists, before doing so
- [x] Add logging to existing services
- [x] Add endpoint for deleting a comment
- [x] Add endpoint for editing a comment - put /api/comments/edit/:id
- [x] Update icon - post /api/comments/updateIcon
- [x] BE: Create API endpoint to add a new article
- [x] Change endpoints to follow best REST practices:
    - [x] POST /api/articles/{article_id}/vote?type=up
    - [x] POST /api/articles/{article_id}/vote?type=down
    - [x] GET /api/articles/{article_id}/comments
    - [x] POST /api/articles/{article_id}/comments
    - [x] DELETE /api/comments/{comment_id}
    - [x] PUT  /api/comments/{comment_id}
- [x] Add article count failsafe for the blog collection
