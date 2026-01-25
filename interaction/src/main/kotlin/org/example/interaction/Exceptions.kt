package org.example.interaction



import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import kotlin.code

@ControllerAdvice
class ExceptionHandler(private val errorMessageSource: ResourceBundleMessageSource) {

    @ExceptionHandler(DemoExceptionHandler::class)
    fun handleAccountException(exception: DemoExceptionHandler): ResponseEntity<BaseMessage> {
        return ResponseEntity.badRequest().body(exception.getErrorMessage(errorMessageSource))
    }
}

sealed class DemoExceptionHandler() : RuntimeException() {
    abstract fun errorCode(): ErrorCodes
    open fun getArguments(): Array<Any?>? = null


    fun getErrorMessage(resourceBundleMessageSource: ResourceBundleMessageSource): BaseMessage {
        val message = try {
            resourceBundleMessageSource.getMessage(
                errorCode().name, getArguments(), LocaleContextHolder.getLocale()
            )
        } catch (e: Exception) {
            e.message
        }

        return BaseMessage(errorCode().code, message)
    }
}

class UserNameAlreadyExistsException : DemoExceptionHandler() {
    override fun errorCode() = ErrorCodes.USERNAME_ALREADY_EXISTS
}


class UserNotFoundException : DemoExceptionHandler() {
    override fun errorCode() = ErrorCodes.USER_NOT_FOUND
}


class CommentNotFoundException(val commentId: Long) : DemoExceptionHandler() {
    override fun errorCode() = ErrorCodes.COMMENT_NOT_FOUND
    override fun getArguments(): Array<Any?>? = arrayOf(commentId)
}

class CommentNotAllowedException(val userId: Long) : DemoExceptionHandler() {
    override fun errorCode() = ErrorCodes.COMMENT_NOT_ALLOWED
    override fun getArguments(): Array<Any?>? = arrayOf(userId)
}


class ReactionNotFoundException(val reactionId: Long) : DemoExceptionHandler() {
    override fun errorCode() = ErrorCodes.REACTION_NOT_FOUND
    override fun getArguments(): Array<Any?>? = arrayOf(reactionId)
}

class ReactionNotAllowedException(val userId: Long) : DemoExceptionHandler() {
    override fun errorCode() = ErrorCodes.REACTION_NOT_ALLOWED
    override fun getArguments(): Array<Any?>? = arrayOf(userId)
}
