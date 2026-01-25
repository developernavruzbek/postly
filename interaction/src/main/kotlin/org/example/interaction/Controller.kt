package org.example.interaction


import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/comments")
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping
    fun createComment(@RequestBody request: CommentRequest) =
        commentService.createComment(request)

    @DeleteMapping("/{id}")
    fun deleteComment(@PathVariable id: Long) =
        commentService.deleteComment(id)

    @GetMapping("/{id}")
    fun getComment(@PathVariable id: Long): CommentResponse =
        commentService.getComment(id)

    @PutMapping("/{id}")
    fun editComment(
        @PathVariable id: Long,
        @RequestBody request: CommentUpdateRequest
    ) = commentService.editComment(id, request)

    @GetMapping
    fun getAllComments(): List<CommentResponse> =
        commentService.getAllComments()

    @GetMapping("/post/{postId}/user/{userId}")
    fun getCommentsByPostId(
        @PathVariable postId: Long,
        @PathVariable userId: Long
    ): List<CommentResponse> =
        commentService.getCommentsPostId(postId, userId)
}


@RestController
@RequestMapping("/api/v1/reactions")
class ReactionController(
    private val reactionService: ReactionService
) {

    @PostMapping
    fun createReaction(@RequestBody request: ReactionRequest) =
        reactionService.createReaction(request)

    @DeleteMapping("/{id}")
    fun deleteReaction(@PathVariable id: Long) =
        reactionService.deleteReaction(id)

    @PutMapping("/{id}")
    fun updateReaction(
        @PathVariable id: Long,
        @RequestBody request: ReactionUpdateRequest
    ) = reactionService.updateReaction(id, request)

    @GetMapping("/post/{postId}/user/{userId}")
    fun getReactionsByPostId(
        @PathVariable postId: Long,
        @PathVariable userId: Long
    ): List<ReactionResponse> =
        reactionService.getReactionsPostId(postId, userId)

    @GetMapping
    fun getAllReactions(): List<ReactionResponse> =
        reactionService.getAllReactions()

    @GetMapping("/{id}")
    fun getReaction(@PathVariable id: Long): ReactionResponse =
        reactionService.getReaction(id)
}