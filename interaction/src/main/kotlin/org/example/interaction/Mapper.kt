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

@Component
class ReactionMapper{

    fun toEntity(reactionRequest: ReactionRequest): Reaction {
        reactionRequest.run {
            return Reaction(
                postId = postId,
                userId = userId,
                reactionType = reactionType,
            )
        }
    }

    fun toDto(reaction: Reaction, user: UserResponse): ReactionResponse {
        reaction.run {
            return ReactionResponse(
                id  = id!!,
                postId = postId,
                userId = userId,
                reactionType = reactionType,
                username = user.username,
            )
        }
    }
}