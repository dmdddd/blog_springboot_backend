# Blog  
### Overview  
This is a backend for the blog application, using Spring Boot.  

# Tech
### APIs  

| Function | Method | Endpoint |
|---|---|---|
| Get blogs | ``GET`` | ``/api/blogs`` |
| Get articles of blog | ``GET`` | ``/api/blogs/{blog_id}/articles`` |
| Update custom page | ``PUT`` | ``/api/blogs/{blog_id}/pages/{page_id}`` |
| Create article | ``POST`` | ``/api/blogs/{blog_id}/articles`` |
| Vote article | ``POST`` | ``/api/blogs/{blog_id}/articles/{article_id}/vote?type=[up/down]`` |
| Add comment to article | ``POST`` | ``/api/blogs/{blog_id}/articles/{article_id}/comments`` |
| Get comments of article | ``GET`` | ``/api/blogs/{blog_id}/articles/{article_id}/comments`` |
| Edit comment | ``PUT`` | ``/api/comments/{comment_id}`` |
| Delete comment | ``DELETE`` | ``/api/comments/{comment_id}`` |

### Prerequisites
Install java jdk - https://www.oracle.com/cis/java/technologies/downloads/#jdk23-windows
### How to run 
Backend  
``./gradlew bootRun``  

#### Backend  
Spring Boot  
Packages: firebase-admin, mockito