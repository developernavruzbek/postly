package org.example.interaction


import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "user-service", url = "http://localhost:8084")
interface UserClient{
    @GetMapping("api/v1/users/{userId}")
    fun getById(@PathVariable userId: Long): UserResponse
}

@FeignClient(name = "post-service", url = "http://localhost:8081")
interface PostClient {
    @GetMapping("api/v1/posts/{postId}")
    fun getById(@PathVariable postId: Long):PostResponse
}

@FeignClient(name = "follow-service", url = "http://localhost:8084")
interface FollowClient {
    @GetMapping("/api/v1/follows/isFollowed/{followerId}/{followingId}")
    fun isFollowed(@PathVariable followerId: Long, @PathVariable followingId: Long):Boolean

}