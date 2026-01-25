package org.example.interaction




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

data class FollowCheckResponse(
    val isFollowing: Boolean,
)
data class CommentRequest(
    val userId: Long,
    val postId: Long,
    val comment: String,
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
    val commentId: Long?,
    val postId: Long?,
    val reactionType: ReactionType
)