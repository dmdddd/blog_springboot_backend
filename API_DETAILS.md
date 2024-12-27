# API Endpoints Documentation

## Blog Management

### **POST** `/api/blogs`
**Description**: Create a new blog (Admin only).

- **Request Body**:
  ```json
  {
  "title": "Tech Blog",
  "description": "A blog about technology and programming"
  }
  ```
- **Response**:
  - Success (201 Created):
    ```json
    {
    "id": "blog-id",
    "title": "Tech Blog",
    "description": "A blog about technology and programming"
    }
    ```
  - Error (400 Bad Request):
    ```json
    {
    "message": "Blog title is required"
    }
    ```
  - Error (403 Forbidden):
    ```json
    {
    "message": "Only admins can create blogs"
    }
    ```


## Pages
## Articles
## Comments and Upvotes

## Status Codes

| Code   | Description                                    |
|--------|------------------------------------------------|
| **200** | OK - The request was successful                |
| **201** | Created - Resource was created successfully    |
| **400** | Bad Request - Invalid request parameters       |
| **401** | Unauthorized - Authentication required        |
| **403** | Forbidden - The user does not have permission  |
| **404** | Not Found - Resource not found                 |
| **409** | Conflict - Resource already exists             |
| **500** | Internal Server Error                          |