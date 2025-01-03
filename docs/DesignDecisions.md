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

## Article Slug Update

### Overview
When an article's slug is updated, the article slug of each associated comment is also updated.

This is done to maintain consistency and ensure that the comments remain linked to the correct article, even after the article's slug changes.

Each comment document contains both the blog slug and the article slug, which allows for efficient querying and updating. We assume that the number of article slug changes will be minimal, making this approach manageable in terms of performance.

By updating the article slug in the comments, we avoid any issues related to stale or incorrect links, ensuring that all comments remain correctly associated with the respective articles, even after a change in the article's slug.

### Transactional Integrity
This update process is handled within a single transaction to ensure consistency. If any failure occurs during the process, the entire operation is rolled back, preventing partial updates and ensuring that the system remains in a consistent state.

---

## Handling of `updatedAt` Fields

The `updatedAt` field is updated immediately upon the following actions to ensure accurate and up-to-date metadata for blogs, articles, and comments.

### Blog's `updatedAt`
- **Updated when**: 
  - Creating a blog
  - Adding, editing, or deleting an article
  - Adding or editing a page
- **Reason**: Articles and pages are key content updates that impact the blog's state. Immediate updates are necessary to reflect changes accurately.

### Article's `updatedAt`
- **Updated when**:
  - Adding or editing an article
- **Reason**: Articles are the core content of blogs, and real-time reflection of updates in `updatedAt` ensures consistency.

## Rationale
Immediate updates are chosen for simplicity, consistency, and performance, as these updates are infrequent but important for user experience. Batch processing was considered but is not necessary for these infrequent changes.

---

## Real-Time Updates for `articleCount` in Blogs with a failsafe

### Reasoning
- **Low Write Frequency**: Blogs generally have fewer articles compared to the number of comments an article might receive. This makes real-time updates efficient and manageable.
- **User Experience**: Accurate `articleCount` is crucial for displaying up-to-date statistics on blog dashboards or lists. Users expect this number to reflect the current state of their blogs instantly.
- **Performance**: Incrementing or decrementing the count using MongoDB's atomic `$inc` operation is lightweight and ensures minimal database overhead.
- **Consistency**: Spring Boot's transaction management ensures that operations like adding/removing an article and updating the `articleCount` are part of the same atomic operation, preventing inconsistencies.

### Implementation Highlights
- Use MongoDB's `$inc` operation for atomic updates.
- Leverage Spring Boot transactions to bundle operations, ensuring consistency.

### Failsafe: Periodic Aggregation Queries:  
The system implements a failsafe mechanism to ensure that the article counts for blogs are accurate and consistently updated, even in cases where real-time updates may be delayed or missed. This mechanism uses a batch processing approach to aggregate comment counts across blogs, ensuring that the counts reflect the actual number of articles in the system.
- **Batch Processing for Data Scanning**  
  The system uses **batch processing** to scan and update comment counts for articles. This approach is efficient for processing large datasets, as it minimizes the overhead of querying and updating the entire collection at once. Instead, the data is divided into manageable chunks, reducing memory consumption and improving processing time.

- **Bulk Operations in MongoDB**  
  To optimize performance, **bulk operations** are used for updating the article counts. This approach allows us to execute multiple updates in a single request to MongoDB, significantly reducing the number of database round-trips and improving throughput.

- **Cursor-Based Pagination with `lastProcessedId`**  
  Instead of relying on `skip`, the system uses **cursor-based pagination** by leveraging the `_id` field of documents. This method ensures consistent and efficient processing for large datasets by avoiding the performance overhead associated with skipping records in large collections. Each batch fetches articles with `_id` greater than the last processed `_id`, making it scalable for growing data sizes.

---

## Updating `commentCount` in Articles
To ensure `commentCount` in articles remains accurate and the system is both efficient and resilient, I adopted a **hybrid approach** combining **Incremental Updates via Temporary Collection** with **Periodic Aggregation Queries** as a fallback.

### Approaches Considered
#### 1. Event-Driven Updates
- **Description**: Emit events on comment changes; update `commentCount` asynchronously.  
- **Pros**: Real-time updates, scalable for high traffic.  
- **Cons**: Requires message brokers, increased complexity, potential for missed events.  

#### 2. Periodic Rescan (Aggregation Queries)
- **Description**: Regularly aggregate comments in the `comments` collection to compute `commentCount`.  
- **Pros**: Guarantees accuracy, simple implementation.  
- **Cons**: High latency, resource-intensive, inefficient for frequent changes.  

#### 3. Incremental Updates via Temporary Collection
- **Description**: Track comment changes in a `commentChanges` collection; update `commentCount` in bulk.  
- **Pros**: Efficient bulk updates, scales well, avoids real-time complexity.  
- **Cons**: Relies on scheduled jobs and temporary tracking.

### Chosen Approach: Hybrid Strategy
I combined **Incremental Updates via Temporary Collection** with **Periodic Aggregation Queries** for optimal performance and reliability.

#### How It Works
1. **Incremental Updates**:  
   - Changes (add/delete) are logged in a `commentChanges` collection.  
   - A scheduled job processes these changes in bulk and updates `commentCount` in articles.  
   - After processing, entries in `commentChanges` are cleared.  

2. **Periodic Aggregation Queries**:  
  The system implements a failsafe mechanism to ensure that the comment counts for articles are accurate and consistently updated, even in cases where real-time updates may be delayed or missed. This mechanism uses a batch processing approach to aggregate comment counts across articles, ensuring that the counts reflect the actual number of comments in the system.
   - **Batch Processing for Data Scanning**  
    The system uses **batch processing** to scan and update comment counts for articles. This approach is efficient for processing large datasets, as it minimizes the overhead of querying and updating the entire collection at once. Instead, the data is divided into manageable chunks, reducing memory consumption and improving processing time.

   - **Bulk Operations in MongoDB**  
    To optimize performance, **bulk operations** are used for updating the comment counts. This approach allows us to execute multiple updates in a single request to MongoDB, significantly reducing the number of database round-trips and improving throughput.

   - **Cursor-Based Pagination with `lastProcessedId`**  
    Instead of relying on `skip`, the system uses **cursor-based pagination** by leveraging the `_id` field of documents. This method ensures consistent and efficient processing for large datasets by avoiding the performance overhead associated with skipping records in large collections. Each batch fetches articles with `_id` greater than the last processed `_id`, making it scalable for growing data sizes.

### Why Hybrid?
- **Efficiency**: Incremental updates handle the majority of changes, reducing database load.  
- **Reliability**: Periodic aggregation queries act as a safety net, ensuring long-term data integrity.  
- **Scalability**: The hybrid approach balances the needs of high-traffic systems and resource efficiency.  
- **Maintainability**: Easier to implement and debug compared to purely event-driven systems.  

This hybrid solution balances **accuracy**, **performance**, and **scalability**, making it suitable for our platform’s needs.