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




Ideas:
Blog page editing history - keep a history of edits so users can revert to previous versions if needed.