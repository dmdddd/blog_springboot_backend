# API Endpoints Documentation

## Blog Management

### POST `/api/blogs`
**Description**: Create a new blog.

#### Request
- **Method**: `POST`
- **URL**: `/api/blogs`

**Example Request**:  
```url
POST /api/blogs
```

#### **Response**
- **Status Code**: `201 Created`

##### Response Body:
Returns json of the newly created blog.

```json
{
  "id": "blog-id",
  "name" : "tech-blog",
  "title": "Tech Blog",
  "description": "A blog about technology and programming",
  "createdAt": "2025-01-01T11:22:33.456Z",
  "updatedAt": "2025-01-01T11:22:33.456Z",
  "admin": true,
  "editor": false
}
```

#### **Errors**

| Status Code     | Error Message                       | Description                                                                                   |
|-----------------|-------------------------------------|-----------------------------------------------------------------------------------------------|
| `409 CONFLICT`  | `Blog with this slug already exists` | The slug for the new blog is not unique and already exists.                                   |
| `400 BAD_REQUEST` | `Illegal slug`                     | The provided slug format is invalid or does not meet the required pattern.                   |
| `400 BAD_REQUEST` | `Validation failed for field XYZ`  | One or more fields in the request body failed validation (e.g., missing or incorrectly formatted fields). |

---

### GET `/api/blogs`

**Description**:  
Retrieves a list of blogs with optional pagination and sorting. This endpoint allows users to browse through available blogs efficiently.

#### Request
- **Method**: `GET`
- **URL**: `/api/blogs`

##### Query Parameters:
| Parameter  | Type     | Required | Default | Description                                                                 |
|------------|----------|----------|---------|-----------------------------------------------------------------------------|
| `page`     | Integer  | No       | `1`     | The page number to retrieve for pagination.                                |
| `size`     | Integer  | No       | `10`    | The number of blogs to retrieve per page.                                  |
| `sortBy`   | String   | No       | `createdAt`  | The field to sort by (e.g., `name`, `title`, `updatedAt`).                 |
| `sortDir`  | String   | No       | `desc`   | The sort direction. Use `asc` for ascending or `desc` for descending.      |

**Example Request**:  
```url
GET /api/blogs?page=2&size=5&sortBy=updatedAt&sortDir=desc
```

#### **Response**
- **Status Code**: `200 OK`

##### Response Body:
Returns a paginated list of blogs along with metadata for pagination.

```json
{
  "data": [
    {
      "id": "1",
      "name": "Tech Blog",
      "title": "Tech Trends",
      "description": "Latest updates in tech.",
      "updatedAt": "2025-01-01T12:34:56.789Z"
    },
    {
      "id": "2",
      "name": "Health Blog",
      "title": "Healthy Living",
      "description": "Tips and tricks for a healthier lifestyle.",
      "updatedAt": "2025-01-01T11:22:33.456Z"
    }
  ],
  "pagination": {
    "totalItems": 20,
    "currentPage": 1,
    "totalPages": 10,
    "pageSize": 2,
    "hasNext": true,
    "hasPrev": false,
    "links": {
        "self": "/api/blogs?page=1&size=2",
        "first": "/api/blogs?page=1&size=2",
        "next": "/api/blogs?page=2&size=2",
        "prev": null,
        "last": "/api/blogs?page=10&size=2"
    }
  }
}
```

#### **Errors**

| Status Code | Error Message                 | Description                                        |
|-------------|-------------------------------|--------------------------------------------------|
| `400`       | `Invalid query parameters`    | One or more query parameters are invalid.         |
| `500`       | `Internal server error`       | An unexpected error occurred on the server.       |  

---

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