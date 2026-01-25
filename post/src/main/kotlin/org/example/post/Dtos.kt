package org.example.post



import java.util.Date

data class BaseMessage(
    val code: Long? = null,
    val message: String? = null
)

data class UserResponse(
    val id: Long?,
    val fullName: String?,
    val phoneNumber: String,
    val username: String,
    val bio: String?,
)


data class CreatePostRequest(
    var userId: Long,
    var caption: String,
    var visibility: PostVisibility,
    var parentPostId: Long? = null,
    var mediaUrls: List<String>? = null  // yangi field
)

data class PostMediaRequest(
    val url: String,
    val type: MediaType,
    val fileName: String? = null,
    val orderIndex: Int = 0
)

data class PostResponse(
    val id: Long,
    val userId: Long,
    val caption: String?,
    val visibility: PostVisibility,
    val parentPostId: Long?,
    val media: List<PostMediaResponse>,
    val createdDate: Date?,
    val modifiedDate: Date?
)

data class PostMediaResponse(
    val id: Long,
    val url: String,
    val type: MediaType,
    val fileName: String?,
    val fileSize: Long?,
    val duration: Int?,
    val orderIndex: Int
)