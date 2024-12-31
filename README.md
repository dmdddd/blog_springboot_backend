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
- Java JDK 11 or higher - https://www.oracle.com/cis/java/technologies/downloads/#jdk23-windows
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
   gradle bootRun
   ```
5. **Access the API**:
   The API will be available at `http://localhost:8080`.
   
---

## Design Decisions

For detailed explanations of important design decisions, such as handling of `updatedAt` fields for various entities, please refer to the [Design Decisions Wiki](./docs/DesignDecisions.md).

---

## API Endpoints (Quick Reference)
For detailed documentation, see [API Details](./docs/API_DETAILS.md).

### Blog Management
- `POST /api/blogs` - Create a new blog (Admin only)
- `GET /api/blogs` - Get all blogs - supports pagination (query query parameters: page, size)
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
- `PUT /api/comments/{id}`: Edit a specific comment (logged users only)
- `DELETE /api/comments/{id}`: Delete a specific comment (logged users only)
- `POST /api/comments/updateIcon`: Update photo URL on all comments of the user (logged users only)

---

## Database Schema
- **Users Collection**:
  - `id`: Unique identifier
  - `email`: User email
  - `role`: Role (Admin, Editor, User)

- **Blogs Collection**:
  - `id`: Blog ID
  - `title`: Blog title
  - `createdBy`: User ID of the creator

- **Articles Collection**:
  - `id`: Article ID
  - `blogId`: Associated Blog ID
  - `content`: Article content
  - `upvotes`: Number of upvotes