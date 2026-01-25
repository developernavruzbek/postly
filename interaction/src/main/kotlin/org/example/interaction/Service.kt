package org.example.interaction



import org.springframework.stereotype.Service

interface CommentService{
    fun createComment(commentRequest: CommentRequest)
    fun deleteComment(commentId: Long)
    fun getComment(commentId: Long): CommentResponse
    fun editComment(commentRequest: CommentRequest)
    fun getAllComments(): List<CommentResponse>
    fun getCommentsPostId(postId: Long, userId: Long): List<CommentResponse>
}

@Service
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val userClient: UserClient,
    private val postClient: PostClient,
    private val commentMapper: CommentMapper,
    private val followClient: FollowClient,
): CommentService{
    override fun createComment(commentRequest: CommentRequest) {
        var user = userClient.getById(commentRequest.userId)
        if (user == null) {
            throw RuntimeException("User ${commentRequest.userId} not found")
        }
        var post = postClient.getById(commentRequest.postId)
        if (post == null) {
            throw RuntimeException("Post ${commentRequest.postId} not found")
        }

        if (post.visibility=="PRIVATE"){
            var isFollower = followClient.isFollowed(user.id, post.userId)
            if (!isFollower) {
                throw RuntimeException("Follower ${user.id} not found")
            }
        }
        commentRepository.save(commentMapper.toEntity(commentRequest))
    }

    override fun deleteComment(commentId: Long) {
        TODO("Not yet implemented")
    }

    override fun getComment(commentId: Long): CommentResponse {
        var comment = commentRepository.findByIdAndDeletedFalse(commentId)
            ?: throw RuntimeException("Comment ${commentId} not found")

        var commentUser = userClient.getById(comment.userId)
        return CommentResponse(
            id = comment.id!!,
            userId = comment.userId,
            postId = comment.postId,
            comment = comment.comment,
            username = commentUser.username,
            parentCommentId = comment.parentId,
        )
    }

    override fun editComment(commentRequest: CommentRequest) {
    }

    override fun getAllComments(): List<CommentResponse> {
        TODO("Not yet implemented")
    }

    override fun getCommentsPostId(postId: Long, userId: Long): List<CommentResponse> {
        var user = userClient.getById(userId)
        if (user == null) {
            throw RuntimeException("User ${userId} not found")
        }
        var post = postClient.getById(postId)
        if (post == null) {
            throw RuntimeException("Post ${postId} not found")
        }
        if (post.visibility=="PRIVATE"){
            var isFollower = followClient.isFollowed(user.id, post.userId)
            if (!isFollower) {
                throw RuntimeException("Follower ${user.id} not found")
            }
        }
        var comments = commentRepository.findAllByPostIdAndDeletedFalse(postId)
            ?:throw RuntimeException("Comments ${postId} not found")

        return   comments.map { comment->
            CommentResponse(
                id = comment.id!!,
                userId = comment.userId,
                postId = comment.postId,
                comment = comment.comment,
                username = user.username,
                parentCommentId = comment.parentId,
            )
        }

    }

}