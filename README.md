# Blog  
### Overview  
This is a backend for the blog application, using Spring Boot.  

# Tech
### APIs  

| Function | Method | Endpoint |
|---|---|---|
| Create article | ``POST`` | ``/api/articles`` |
| Vote article | ``POST`` | ``/api/articles/{article_id}/vote?type=[up/down]`` |
| Add comment to article | ``POST`` | ``/api/articles/{article_id}/comments`` |
| Get comments of article | ``GET`` | ``/api/articles/{article_id}/comments`` |
| Edit comment | ``PUT`` | ``/api/comments/{comment_id}`` |
| Delete comment | ``DELETE`` | ``/api/comments/{comment_id}`` |

### How to run 
Backend  
``./gradlew bootRun``  

#### Backend  
Spring Boot  
Packages: firebase-admin, mockito