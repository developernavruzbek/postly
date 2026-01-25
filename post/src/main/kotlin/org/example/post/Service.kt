package org.example.post



import org.springframework.stereotype.Service

interface PostService {
    fun createPost(request: CreatePostRequest): PostResponse
    fun getPost(postId: Long): PostResponse
    fun getAllUserId(userId: Long, viewerId:Long): List<PostResponse>
}

@Service
class PostServiceImpl(
    private val postRepository: PostRepository,
    private val postMediaRepository: PostMediaRepository,
    private val userClient: UserClient,
    private val postMapper: PostMapper,
    private val followClient: FollowClient
) : PostService {

    override fun createPost(request: CreatePostRequest): PostResponse {
        val user = userClient.getUserById(request.userId)
            ?: throw UserNotFoundException()

        if (request.parentPostId != null && request.parentPostId != 0L) {
            val parentPost = postRepository.findByIdAndDeletedFalse(request.parentPostId!!)
                ?: throw PostNotFoundException()
            if (parentPost.userId != user.id) {
                throw RuntimeException("Parent post does not belong to this user")
            }
        }

        val post = postRepository.save(postMapper.toPostEntity(request, user.id!!))

        val mediaEntities = request.mediaUrls?.map { mediaUrl ->

            val mediaRequest = PostMediaRequest(url = mediaUrl, type = MediaType.IMAGE)
            postMediaRepository.save(postMapper.toPostMediaEntity(post.id!!, mediaRequest))
        } ?: emptyList()

        return postMapper.toPostResponse(post, mediaEntities)
    }

    override fun getPost(postId: Long): PostResponse {
        val post = postRepository.findByIdAndDeletedFalse(postId)
            ?: throw PostNotFoundException()

        val media = postMediaRepository.findAllNotDeleted().filter { it.postId == post.id }

        return postMapper.toPostResponse(post, media)
    }

    override fun getAllUserId(userId: Long, viewerId:Long): List<PostResponse> {
        val user = userClient.getUserById(userId)
            ?: throw UserNotFoundException()

        val viewer = userClient.getUserById(viewerId)
            ?: throw UserNotFoundException()

        var posts = postRepository.findAllByUserIdAndDeletedFalse(userId)

        if (posts!=null) {

            val filteredPosts = posts.filter { post ->
                when (post.visibility) {
                    PostVisibility.PUBLIC -> true
                    PostVisibility.PRIVATE -> {
                        followClient.isFollowed(followerId = viewerId, followingId = post.userId)
                    }
                    PostVisibility.FOLLOWERS -> post.userId == viewerId
                }
            }
            return filteredPosts.map { post ->
                val media = postMediaRepository.findAllNotDeleted().filter { it.postId == post.id }
                postMapper.toPostResponse(post, media)
            }

        }
        return emptyList()

    }
}