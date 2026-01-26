package org.example.user


data class BaseMessage(
    val code: Long? = null,
    val message: String? = null
)

data class UserCreateRequest(
    val fullName: String,
    val phoneNumber: String,
    val username: String,
    val password: String,
    val bio: String?,
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

data class UserFollowCreateRequest(
    val followerId: Long,
    val followingId: Long,
)

data class UserFollowResponse(
    val id: Long?,
    val followerId: Long?,
    val followerUsername: String,
    val followingId: Long?,
    val followingUsername: String,
    val status: FollowStatus
)