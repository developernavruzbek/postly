package org.example.user

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "post-service", url = "http://localhost:8082")
interface PostClient {
    @GetMapping("api/v1/post/user/count/{userId}")
    fun countPostsByUserId(@PathVariable userId: Long): Long
}