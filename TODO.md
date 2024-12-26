- [ ] fix the getAllArticles_shouldReturnCorrectArticle test
- [x] When adding a comment, check that the article exists, before doing so
- [ ] Add logging to existing services
- [x] Add endpoint for deleting a comment
- [x] Add endpoint for editing a comment - put /api/comments/edit/:id
- [x] Update icon - post /api/comments/updateIcon
- [ ] FE: arror when logging out and I'm in the edit user information window, need to reroute to main page(?)
- [x] BE: Create API endpoint to add a new article
- [x] Change endpoints to follow best REST practices:
    - [x] POST /api/articles/{article_id}/vote?type=up
    - [x] POST /api/articles/{article_id}/vote?type=down
    - [x] GET /api/articles/{article_id}/comments
    - [x] POST /api/articles/{article_id}/comments
    - [x] DELETE /api/comments/{comment_id}
    - [x] PUT  /api/comments/{comment_id}


IMPORTANT:
Deleting an article should delete comments
Deleting a blog, should delete articles and comments

Ideas:
Pagination to blogs, articles and comments
Blog page editing history - keep a history of edits so users can revert to previous versions if needed.
Check: Context clean on logout
Categories - add categories to articles and be able switch between categories in the blog
Article status - some articles can be saved as drafts. status=[Draft, published, pending review, scheduled, archived, trashed (recycle bin before removing completely)]
Article - thumbnail: A URL or path to a thumbnail image for the article.
Article - views: A count of how many times the article has been viewed.
Blog: followers, likes, thumbnail, status (active, inactive), categories, createdAt, updatedAt, author

Create a custom annotation "MinLengthExcludingHtml" when adding or editing rich text

Prompt:
Blog management application where users can create their own blogs, write articles and others can comment and upvote articles.
Blogs have admins and editors, these can write and edit articles.
Tech stack: react and spring boot, mongoDB database, firebase authentication