package org.example.post


enum class PostVisibility {
    PUBLIC,
    FOLLOWERS,
    PRIVATE
}
enum class MediaType {
    IMAGE, VIDEO
}

enum class ErrorCodes(val code: Long) {

    USERNAME_ALREADY_EXISTS(103),
    USER_NOT_FOUND(104),

    POST_NOT_FOUND(404),

}