# Design Decisions

## Async Deletion of Comments on Article Deletion

### Overview
When an article is deleted, all associated comments need to be removed. To avoid performance issues from deleting a large number of comments synchronously, I handle the comment deletion **asynchronously** in the background. This improves response time and user experience while ensuring the article deletion process remains fast and non-blocking.

### How It Works
- When an article is deleted, an `ArticleDeletedEvent` is published.
- The event is picked up by the `ArticleDeletedEventListener`, which triggers the deletion of related comments.
- Comment deletion is handled asynchronously by the `ArticleCommentService` using Spring's `@Async` annotation.

### Benefits
- **Faster Response Times**: The client can receive an immediate response about the article deletion without waiting for comments to be deleted.
- **Better User Experience**: Article deletion is not delayed by the comment removal process.
- **Scalability**: Asynchronous handling allows the system to scale more effectively as the number of comments grows.

### Considerations
- **Eventual Consistency**: Comments are deleted in the background, which may cause a slight delay before they are fully removed.
- **Error Handling**: Requires proper error handling and monitoring for async tasks to ensure eventual consistency.

---

## Handling of `updatedAt` Fields

The `updatedAt` field is updated immediately upon the following actions to ensure accurate and up-to-date metadata for blogs, articles, and comments.

### Blog's `updatedAt`
- **Updated when**: 
  - Creating a blog
  - Adding, editing, or deleting an article
  - Adding or editing a page
- **Reason**: Articles and pages are key content updates that impact the blog's state. Immediate updates are necessary to reflect changes accurately.

### Page's `updatedAt`
- **Updated when**:
  - Adding or editing a page
- **Reason**: Pages are the core content of blogs, and real-time reflection of updates in `updatedAt` ensures consistency.

### Article's `updatedAt`
- **Updated when**:
  - Adding or editing an article
- **Reason**: Articles are the core content of blogs, and real-time reflection of updates in `updatedAt` ensures consistency.

### Comment's `updatedAt`
- **Updated when**:
  - Adding or editing a comment
- **Reason**: Comments reflect user interactions with content. Immediate updates ensure the latest activity is captured.

## Rationale
Immediate updates are chosen for simplicity, consistency, and performance, as these updates are infrequent but important for user experience. Batch processing was considered but is not necessary for these infrequent changes.