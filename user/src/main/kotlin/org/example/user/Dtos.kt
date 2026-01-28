package org.example.user
import jakarta.validation.constraints.*
import org.hibernate.validator.constraints.URL


data class BaseMessage(
    val code: Long? = null,
    val message: String? = null
)


data class UserCreateRequest(
    @field:NotBlank(message = "Full name bo'sh bo'lmasligi kerak")
    val fullName: String,

    @field:NotBlank(message = "Telefon raqam bo'sh bo'lmasligi kerak")
    @field:Pattern(
        regexp = "\\+?[0-9]{9,15}",
        message = "Telefon raqam noto'g'ri formatda"
    )
    val phoneNumber: String,

    @field:NotBlank(message = "Username bo'sh bo'lmasligi kerak")
    val username: String,

    @field:NotBlank(message = "Parol bo'sh bo'lmasligi kerak")
    @field:Size(min = 6, message = "Parol kamida 6 ta belgidan iborat bo'lishi kerak")
    val password: String,

    val bio: String?,

    @field:URL(message = "Avatar URL noto'g'ri formatda")
    val avatarUrl: String?,

    val accountType: AccountType
)

data class UserUpdateRequest(
    @field:Size(min = 1, message = "Full name bo'sh bo'lmasligi kerak")
    val fullName: String?,

    @field:Pattern(
        regexp = "\\+?[0-9]{9,15}",
        message = "Telefon raqam noto'g'ri formatda"
    )
    val phoneNumber: String?,

    @field:Size(min = 1, message = "Username bo'sh bo'lmasligi kerak")
    val username: String?,

    @field:Size(min = 6, message = "Parol kamida 6 ta belgidan iborat bo'lishi kerak")
    val password: String?,

    val bio: String?,

    @field:URL(message = "Avatar URL noto'g'ri formatda")
    val avatarUrl: String?,

    val accountType: AccountType
)



data class UserResponse(
    val id: Long?,
    val fullName: String?,
    val phoneNumber: String,
    val username: String,
    val bio: String?,
    val avatarUrl: String?,
    val accountType: AccountType
)

data class MyProfile(
    val id:Long?,
    val fullName: String?,
    val phoneNumber: String?,
    val username: String?,
    val bio: String?,
    val myFollowers: Long,
    val myFollowing: Long,
    val myPostCount: Long,
)

data class UserFollowRequest(
    @field:NotNull(message = "Follower ID bo'lishi kerak")
    val followerId: Long,

    @field:NotNull(message = "Following ID bo'lishi kerak")
    val followingId: Long,
)


data class UserFollowConfirmRequest(
    @field:NotNull val followerId: Long,
    @field:NotNull val followingId: Long,
    @field:NotNull val followStatus: FollowStatus
)


data class UserFollowResponse(
    val id: Long?,
    val followerId: Long?,
    val followerUsername: String,
    val followingId: Long?,
    val followingUsername: String,
    val status: FollowStatus
)