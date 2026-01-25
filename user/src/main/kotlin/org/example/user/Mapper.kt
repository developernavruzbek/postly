package org.example.user

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserMapper(
    private val passwordEncoder: PasswordEncoder
){

    fun toEntity(userCreateRequest: UserCreateRequest): User {
        userCreateRequest.run {
            return User(
                fullName = fullName,
                username = username,
                password = passwordEncoder.encode(password),
                phoneNumber = phoneNumber,
                bio = bio,
                avatarUrl = avatarUrl,
                accountType = accountType,
            )
        }
    }

    fun toDto(entity: User): UserResponse {
        entity.run {
            return UserResponse(
                id = id,
                username = username,
                fullName = fullName,
                phoneNumber = phoneNumber,
                bio = bio,
                avatarUrl = avatarUrl,
                accountType = accountType,
            )
        }
    }
}

@Component
class UserFollowMapper{

    fun toDto(entity: UserFollow): UserFollowResponse {
        entity.run {
            return UserFollowResponse(
                id = id,
                followerId = follower.id,
                followerUsername = follower.username,
                followingId = following.id,
                followingUsername = following.username,
                status = status
            )
        }
    }
}