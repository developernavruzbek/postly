package org.example.interaction

enum class ReactionType {
    LIKE,
    LOVE,
    HAHA,
    WOW,
    SAD,
    ANGRY
}

enum class ErrorCodes(val code: Long) {
    USERNAME_ALREADY_EXISTS(103),
    USER_NOT_FOUND(104),
    COMMENT_NOT_FOUND(301),
    COMMENT_NOT_ALLOWED(302),
    COMMENT_PARENT_NOT_FOUND(303), // Agar parent comment topilmasa

    REACTION_NOT_FOUND(401),
    REACTION_NOT_ALLOWED(402),


}

