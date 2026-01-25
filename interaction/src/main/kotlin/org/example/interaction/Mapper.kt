package org.example.interaction



import org.springframework.stereotype.Component

@Component
class CommentMapper{

    fun toEntity(commentRequest: CommentRequest): Comment {
        commentRequest.run {
            return Comment(
                postId = postId,
                userId = userId,
                comment = comment,
                parentId = parentCommentId
            )
        }
    }
}