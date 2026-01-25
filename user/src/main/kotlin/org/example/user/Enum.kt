package org.example.user


enum class AccountType{
    PUBLIC,PRIVATE
}

enum class FollowStatus {
    PENDING,
    ACCEPTED
}



enum class ErrorCodes(val code: Long) {
    USERNAME_ALREADY_EXISTS(103),
    USER_NOT_FOUND(104),
    USER_ALREADY_EXISTS(105),
    PASSWORD_IS_INCORRECT(106),
    FOLLOW_ALREADY_EXISTS(108),
    PHONE_NUMBER_ALREADY_EXISTS(202),
    FOLLOW_NOT_FOUND(205),

}
