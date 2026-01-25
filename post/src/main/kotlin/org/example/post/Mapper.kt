package org.example.post

import org.springframework.stereotype.Component


@Component
class PostMapper {

    fun toPostEntity(request: CreatePostRequest, userId: Long): Post {
        return Post(
            userId = userId,
            caption = request.caption,
            visibility = request.visibility,
            parentPostId = request.parentPostId
        )
    }

    fun toPostMediaEntity(postId: Long, request: PostMediaRequest, fileSize: Long? = null, duration: Int? = null): PostMedia {
        return PostMedia(
            postId = postId,
            url = request.url,
            type = request.type,
            fileName = request.fileName,
            orderIndex = request.orderIndex,
            fileSize = fileSize,
            duration = duration
        )
    }


    fun toPostMediaResponse(entity: PostMedia): PostMediaResponse {
        return PostMediaResponse(
            id = entity.id!!,
            url = entity.url,
            type = entity.type,
            fileName = entity.fileName,
            fileSize = entity.fileSize,
            duration = entity.duration,
            orderIndex = entity.orderIndex
        )
    }

    fun toPostResponse(post: Post, media: List<PostMedia>): PostResponse {
        return PostResponse(
            id = post.id!!,
            userId = post.userId,
            caption = post.caption,
            visibility = post.visibility,
            parentPostId = post.parentPostId,
            media = media.map { toPostMediaResponse(it) },
            createdDate = post.createdDate,
            modifiedDate = post.modifiedDate
        )
    }
}