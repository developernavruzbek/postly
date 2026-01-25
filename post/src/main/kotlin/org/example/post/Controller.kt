package org.example.post



import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/posts")
class PostController(
    private val postService: PostService
) {


    @PostMapping
    fun createPost(@RequestBody request: CreatePostRequest): PostResponse {

        return postService.createPost(request)
    }


    @GetMapping("/{id}")
    fun getPost(@PathVariable id: Long): PostResponse {
        return postService.getPost(id)
    }


    @GetMapping("/user/{userId}")
    fun getPostsByUser(
        @PathVariable userId: Long,
        @RequestParam viewerId: Long
    ): List<PostResponse> {
        return postService.getAllUserId(userId, viewerId)
    }
}