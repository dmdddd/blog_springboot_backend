# Blog  
### Overview  
This is a backend for the blog application, using Spring Boot.  

# Tech
### APIs  

| Function | Method | Endpoint | Permissions |
|---|---|---|---|
| Get blogs | ``GET`` | ``/api/blogs`` | Anyone |
| Create blogs | ``POST`` | ``/api/blogs`` |  |
| Check unique blog name | ``GET`` | ``/api/blogs/checkSlug/{slug}`` |  |
| Update custom page | ``PUT`` | ``/api/blogs/{blog_id}/pages/{page_id}`` |  |
| Get articles of blog | ``GET`` | ``/api/blogs/{blog_id}/articles`` | Anyone |
| Check unique article name | ``GET`` | ``/api/blogs/{blog_id}/articles/checkSlug/{slug}`` |  |
| Create article | ``POST`` | ``/api/blogs/{blog_id}/articles`` | Admin/editor of a blog |
| Edit article | ``PUT`` | ``/api/blogs/{blog_id}/articles/{article_id}`` | Admin/editor of a blog |
| Delete article | ``DELETE`` | ``/api/blogs/{blog_id}/articles/{article_id}`` | Admin/editor of a blog |
| Vote article | ``POST`` | ``/api/blogs/{blog_id}/articles/{article_id}/vote?type=[up/down]`` |  |
| Add comment to article | ``POST`` | ``/api/blogs/{blog_id}/articles/{article_id}/comments`` |  |
| Get comments of article | ``GET`` | ``/api/blogs/{blog_id}/articles/{article_id}/comments`` |  |
| Edit comment | ``PUT`` | ``/api/comments/{comment_id}`` |  |
| Delete comment | ``DELETE`` | ``/api/comments/{comment_id}`` |  |

### Prerequisites
Install java jdk - https://www.oracle.com/cis/java/technologies/downloads/#jdk23-windows
### How to run 
Backend  
``./gradlew bootRun``  

#### Backend  
Spring Boot  
Packages: firebase-admin, mockito