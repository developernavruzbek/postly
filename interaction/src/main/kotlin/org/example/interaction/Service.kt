package org.example.interaction




import org.springframework.stereotype.Service

interface CommentService{
    fun createComment(commentRequest: CommentRequest)
    fun deleteComment(commentId: Long)
    fun getComment(commentId: Long): CommentResponse
    fun editComment(commentId: Long, commentUpdateRequest: CommentUpdateRequest)
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
        commentRepository.findByIdAndDeletedFalse(commentId)?.let {
            commentRepository.trash(commentId)
        }?:throw RuntimeException("Comment ${commentId} not found")
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

    override fun editComment(commentId: Long, commentUpdateRequest: CommentUpdateRequest) {
        var comment1 = commentRepository.findByIdAndDeletedFalse(commentId)
            ?: throw RuntimeException("Comment ${commentId} not found")

        if (comment1.userId != commentUpdateRequest.userId) {
            throw RuntimeException("User ${commentUpdateRequest.userId} not found")
            // boshqa user
        }

        commentUpdateRequest.run {
            if (comment!=null && !comment.isBlank()){
                comment1.comment = comment
            }
            if (parentCommentId!=null && parentCommentId!=0L){
                comment1.parentId = parentCommentId
            }

            commentRepository.save(comment1)

        }

    }

    override fun getAllComments(): List<CommentResponse> {
        var comments = commentRepository.findAllNotDeleted()
        return   comments.map { comment->
            CommentResponse(
                id = comment.id!!,
                userId = comment.userId,
                postId = comment.postId,
                comment = comment.comment,
                username = userClient.getById(comment.userId).username,
                parentCommentId = comment.parentId,
            )
        }
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


interface ReactionService{
    fun createReaction(reactionRequest: ReactionRequest)
    fun deleteReaction(reactionId: Long)
    fun updateReaction(reactionId: Long, reactionUpdateRequest: ReactionUpdateRequest)
    fun getReactionsPostId(postId: Long, userId: Long): List<ReactionResponse>
    fun getAllReactions(): List<ReactionResponse>
    fun getReaction(reactionId: Long): ReactionResponse
}

@Service
class ReactionServiceImpl(
    private val reactionRepository: ReactionRepository,
    private val userClient: UserClient,
    private val postClient: PostClient,
    private val followClient: FollowClient,
    private val reactionMapper: ReactionMapper,

    ):ReactionService{
    override fun createReaction(reactionRequest: ReactionRequest) {
        var user = userClient.getById(reactionRequest.userId)
            ?: throw RuntimeException("User ${reactionRequest.userId} not found")
        var post = postClient.getById(reactionRequest.postId)
            ?:throw RuntimeException("Post ${reactionRequest.postId} not found")

        if (post.visibility=="PRIVATE"){
            var isFollower = followClient.isFollowed(user.id, post.userId)
            if (!isFollower) {
                throw RuntimeException("Follower ${user.id} not found")
            }
        }
        reactionRepository.save(reactionMapper.toEntity(reactionRequest))
    }

    override fun deleteReaction(reactionId: Long) {
        reactionRepository.findByIdAndDeletedFalse(reactionId)?.let {
            reactionRepository.trash(reactionId)
        }?:throw RuntimeException("Reaction ${reactionId} not found")

    }

    override fun updateReaction(reactionId: Long, reactionUpdateRequest: ReactionUpdateRequest) {
        reactionRepository.findByIdAndDeletedFalse(reactionId)?.let {reaction->
            if (reaction.userId!=reactionUpdateRequest.userId){
                throw RuntimeException("User ${reaction.userId} not found")
                // user boshqa user
            }

            reactionUpdateRequest.reactionType?.let {
                reaction.reactionType = it
                reactionRepository.save(reaction)
            }

        }?:throw RuntimeException("Reaction ${reactionId} not found")

    }

    override fun getReactionsPostId(postId: Long, userId: Long): List<ReactionResponse> {
        var user = userClient.getById(userId)
            ?: throw RuntimeException("User ${userId} not found")

        var post = postClient.getById(postId)
            ?: throw RuntimeException("Post ${postId} not found")

        if (post.visibility=="PRIVATE"){
            var isFollower = followClient.isFollowed(user.id, post.userId)
            if (!isFollower) {
                throw RuntimeException("Follower ${user.id} not found")
            }
        }

        var reactions = reactionRepository.findAllByPostIdAndDeletedFalse(postId)
            ?:throw RuntimeException("Post ${postId} not found")
        var reactionResponses = reactions.map { reaction ->
            reactionMapper.toDto(reaction, userClient.getById(reaction.userId))
        }
        return reactionResponses

    }
    override fun getAllReactions(): List<ReactionResponse> {
        var reactions = reactionRepository.findAllNotDeleted()
        return  reactions.map {
                reaction -> reactionMapper.toDto(reaction, userClient.getById(reaction.userId))
        }
    }

    override fun getReaction(reactionId: Long): ReactionResponse {
        reactionRepository.findByIdAndDeletedFalse(reactionId)?.let {
            return reactionMapper.toDto(it, userClient.getById(it.userId))
        }?:throw RuntimeException("Reaction ${reactionId} not found")
    }
}