# Design Decisions

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