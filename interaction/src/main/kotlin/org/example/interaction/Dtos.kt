package org.example.interaction



import org.hibernate.engine.spi.Status


data class UserResponse(
    val id: Long,
    val username: String,
    val fullName: String,
    val bio: String,
    val phoneNumber: String,
)

data class PostResponse(
    val postId: Long,
    val userId: Long,
    val visibility: String,
)

data class CommentRequest(
    val userId: Long,
    val postId: Long,
    val comment: String,
    val parentCommentId: Long?,
)
data class CommentUpdateRequest(
    val userId: Long,
    val comment: String?,
    val parentCommentId: Long?,
)
data class CommentResponse(
    val id: Long,
    val userId: Long,
    val username: String,
    val postId: Long,
    val comment: String,
    val parentCommentId: Long?,
)
data class ReactionRequest(
    val userId: Long,
    val postId: Long,
    val reactionType: ReactionType
)

data class ReactionUpdateRequest(
    val userId: Long,
    val reactionType: ReactionType,
)

data class ReactionResponse(
    val id: Long,
    val userId: Long,
    val username: String,
    val postId: Long,
    val reactionType: ReactionType

)