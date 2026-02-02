package org.example.interaction


import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "user-service", url = "http://localhost:8080",configuration = [FeignOAuth2TokenConfig::class])
interface UserClient{
    @GetMapping("api/v1/user/{userId}")
    fun getById(@PathVariable userId: Long): UserResponse
}

@FeignClient(name = "post-service", url = "http://localhost:8082", configuration = [FeignOAuth2TokenConfig::class])
interface PostClient {
    @GetMapping("api/v1/post/{postId}")
    fun getById(@PathVariable postId: Long):PostResponse
}

@FeignClient(name = "follow-service", url = "http://localhost:8080", configuration = [FeignOAuth2TokenConfig::class])
interface FollowClient {
    @GetMapping("/api/v1/follows/isFollowed/{followerId}/{followingId}")
    fun isFollowed(@PathVariable followerId: Long, @PathVariable followingId: Long):Boolean

}