package org.example.post


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.validation.constraints.*
import org.hibernate.validator.constraints.URL
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

    @field:NotNull(message = "User ID bo'lishi kerak")
    var userId: Long,

    @field:NotBlank(message = "Caption bo'sh bo'lmasligi kerak")
    var caption: String,

    @field:NotNull(message = "Visibility bo'lishi kerak")
    var visibility: PostVisibility,

    var parentPostId: Long? = null,

    @field:Size(max = 10, message = "Bir post uchun maksimal 10 ta media URL bo'lishi mumkin")
    var mediaUrls: List<@URL(message = "Media URL noto'g'ri formatda") String>? = null
)

data class PostMediaRequest(

    @field:NotBlank(message = "Media URL bo'sh bo'lmasligi kerak")
    @field:URL(message = "Media URL noto'g'ri formatda")
    val url: String,

    @field:NotNull(message = "Media turi (type) bo'lishi kerak")
    val type: MediaType,

    val fileName: String? = null,

    @field:Min(value = 0, message = "orderIndex 0 dan kichik bo'lmasligi kerak")
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

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserInfoResponse(
    val id: Long,
    val fullName: String,
    val username: String,
    val role: String,
)