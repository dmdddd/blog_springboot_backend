# Blog Management API

## Overview
This repository contains the backend API for a **Blog Management Application**. The API supports core features such as blog creation, custom page management, article handling, and user engagement functionalities like commenting and upvoting. It is designed with a focus on scalability and security, using modern tools and frameworks.

---

## Features
- **Blog Management**:
  - Users can create and manage blogs.
  - Admins can add custom pages to blogs (e.g., "About" or "FAQ").
- **Role-Based Access Control**:
  - **Admins**: Manage blogs, pages, and articles.
  - **Editors**: Write and edit articles.
  - **Users**: Comment and upvote articles.
- **Article Engagement**:
  - Users can comment on and upvote articles.
- **Authentication and Authorization**:
  - Secure login/signup with Firebase Authentication.

---

## Tech Stack
- **Backend Framework**: Spring Boot (using Gradle for build automation)
- **Database**: MongoDB (NoSQL database)
- **Authentication**: Firebase Authentication
- **Frontend (Integrated)**: React.js

---

## Installation and Setup

### Prerequisites
Ensure the following software is installed on your machine:
- Java JDK23 or higher - https://www.oracle.com/cis/java/technologies/downloads/#jdk23-windows
- Gradle
- MongoDB (local instance or cloud setup like MongoDB Atlas)
- Firebase service account credentials (download JSON file)

### Steps to Run Locally
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/dmdddd/blog_springboot_backend.git
   cd blog_springboot_backend
   ```
2. **Configure Firebase**:  
   Place `firebase-service-account.json` in:
   ```
   src/main/resources/firebase-service-account.json
   ```
3. **Configure MongoDB**:  
   Configure `application.properties`: Open the `src/main/resources/application.properties` file and configure the MongoDB connection settings.
   ```properties
   spring.application.name=blog-management
   spring.data.mongodb.uri=mongodb+srv://<username>:<password>@cluster0.mongodb.net/myDatabase?retryWrites=true&w=majority
   spring.data.mongodb.database=react_blog
   ```
4. **Build and Run the Application**:
   ```bash
   .\gradlew bootRun
   ```
5. **Access the API**:
   The API will be available at `http://localhost:8080`.
   
---

## Design Decisions

For detailed explanations of important design decisions, such as handling of `updatedAt` fields for various entities, please refer to the [Design Decisions Wiki](./docs/DesignDecisions.md).

---

## API Endpoints (Quick Reference)
For detailed documentation, see [API Details](./docs/API_DETAILS.md).

### Blogs
- `POST /api/blogs` - Create a new blog (logged users only)
- `GET /api/blogs` - Get all blogs with optional pagination and sorting.
  Example:  
  ```bash
  curl "https://api.example.com/api/blogs?page=1&size=10&sortBy=name&sortDir=asc"
  ```
- `GET /api/blogs/{id}` - Get blog details
- `GET /api/blogs/checkSlug/{slug}` - Check if blog name is unique (logged users only)
<!-- - `PUT /api/blogs/{id}` - Update a blog (Admin only) -->
<!-- - `DELETE /api/blogs/{id}` - Delete a blog (Admin only) -->

### Articles
- `POST /api/blogs/{blogId}/articles` - Create an article (Admin/Editor)
- `GET /api/blogs/{blogId}/articles` - Get all articles for a blog
- `GET /api/blogs/{blogId}/articles/{id}` - Get article details
- `PUT /api/blogs/{blogId}/articles/{id}` - Edit an article (Admin/Editor)
- `DELETE /api/blogs/{blogId}/articles/{id}` - Delete an article (Admin only)
- `GET /api/blogs/{blogId}/articles/checkSlug/{slug}` - Check if article name is unique in a blog (logged users only)
- `PUT /api/blogs/{blogId}/articles/{articleId}/vote?type=[up/down]`: Vote article (logged users only)

### Pages
- `GET /api/blogs/{blogId}/pages` - Get all pages for a blog
- `PUT /api/blogs/{blogId}/pages/{id}` - Edit a page (Admin/Editor)
- `DELETE /api/blogs/{blogId}/pages/{id}` - Delete a page (Admin)

### Comments
- `POST /api/blogs/{blogId}/articles/{articleId}/comments`: Add a comment to an article (logged users only)
- `GET /api/blogs/{blogId}/articles/{articleId}/comments`: Get all comments for a specific article
- `PUT /api/blogs/{blogId}/articles/{articleId}/comments/{id}`: Edit a specific comment (logged users only)
- `DELETE /api/blogs/{blogId}/articles/{articleId}/comments/{id}`: Delete a specific comment (logged users only)
- `POST /api/comments/updateIcon`: Update photo URL on all comments of the user (logged users only)

---

## Database Schema
### Blogs Collection Schema
The `blogs` collection in the database stores information about individual blogs. The schema is structured as follows:
```javascript
{
  _id: ObjectId,          // Unique identifier for the blog (auto-generated)
  name: String,           // Name of the blog
  title: String,          // Title of the blog
  description: String,    // Description of the blog
  updatedAt: Date,        // The date and time when the blog was last updated
  users: [Object]         // Array of user objects associated with the blog
}
```

### Article Collection Schema
The `article` collection in the database stores all relevant information about blog articles. The schema is structured as follows:
```javascript
{
  _id: ObjectId,       // Unique identifier for the article (auto-generated)
  name: String,        // Name of the article (slug)
  blog: String,        // The blog or source to which the article belongs (slug)
  title: String,       // Title of the article
  content: String,     // The main content of the article (text or HTML)
  upvotes: Number,     // Number of upvotes for the article
  upvoteIds: [String], // Array of user IDs who have upvoted the article
  author: String,      // The author of the article (could be user ID or name)
  createdAt: Date,     // The date and time when the article was created
  admins: [String],  // List of user IDs who are admins for the blog
  editors: [String]  // List of user IDs who are editors for the blog
}
```

### Comments Collection Schema
The `comments` collection in the database stores information about individual comments made on articles. The schema is structured as follows:
```javascript
{
  _id: ObjectId,        // Unique identifier for the comment (auto-generated)
  text: String,         // The content of the comment
  blog: String,         // The blog to which the comment belongs
  articleName: String,  // The name of the article associated with the comment
  userEmail: String,    // Email address of the user who made the comment
  createdAt: Date,      // The date and time when the comment was created
  updatedAt: Date       // The date and time when the comment was last updated
}
```

### Pages Collection Schema
The `pages` collection in the database stores information about custom pages associated with blogs. The schema is structured as follows:
```javascript
{
  _id: ObjectId,       // Unique identifier for the page (auto-generated)
  blog: String,        // The blog to which the page belongs
  slug: String,        // A URL-friendly identifier for the page
  title: String,       // Title of the page
  content: String,     // The main content of the page
  order: Number,       // Display order of the page in navigation
  createdAt: Date,     // The date and time when the page was created
  updatedAt: Date      // The date and time when the page was last updated
}
```

### Article Comment Change Collection Schema
The `article_comment_change` collection in the database stores information about the net changes in the comments for each article. The schema is structured as follows:
```javascript
{
  _id: ObjectId,         // Unique identifier for the comment change record (auto-generated)
  blogSlug: String,      // Slug of the blog to which the article belongs
  articleSlug: String,   // Slug of the article that the comment change relates to
  netChange: Number      // Net change in the number of comments (positive for new comments, negative for deleted comments)
}
```