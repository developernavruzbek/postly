package org.example.interaction
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.validation.constraints.*




data class BaseMessage(
    val code: Long? = null,
    val message: String? = null
)


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

    @field:NotNull(message = "User ID bo'lishi kerak")
    val userId: Long,

    @field:NotNull(message = "Post ID bo'lishi kerak")
    val postId: Long,

    @field:NotBlank(message = "Comment bo'sh bo'lmasligi kerak")
    @field:Size(max = 500, message = "Comment maksimal 500 ta belgidan oshmasligi kerak")
    val comment: String,

    val parentCommentId: Long? = null,
)


data class CommentUpdateRequest(

    @field:NotNull(message = "User ID bo'lishi kerak")
    val userId: Long,

    @field:Size(min = 1, max = 500, message = "Comment bo'sh bo'lmasligi kerak")
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

    @field:NotNull(message = "User ID required")
    val userId: Long,

    @field:NotNull(message = "Post ID required")
    val postId: Long,

    @field:NotNull(message = "Reaction type required")
    val reactionType: ReactionType
)



data class ReactionUpdateRequest(

    @field:NotNull(message = "User ID required")
    val userId: Long,

    @field:NotNull(message = "Reaction type required")
    val reactionType: ReactionType
)


data class ReactionResponse(
    val id: Long,
    val userId: Long,
    val username: String,
    val postId: Long,
    val reactionType: ReactionType

)

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserInfoResponse(
    val id: Long,
    val fullName: String,
    val username: String,
    val role: String,
)