package ru.projektio.boardservice.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.projektio.boardservice.dto.response.ExceptionResponse

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(IndexOutOfBoundsException::class)
    fun handleIOOBE(e: IndexOutOfBoundsException) = ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ExceptionResponse(e.cause, e.message))

    @ExceptionHandler(WrongColumnOrderException::class)
    fun handleWrongColumnOrdering(e: WrongColumnOrderException) = ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(ExceptionResponse(e.cause, e.message))

    @ExceptionHandler(NoContentException::class)
    fun handleNoContentException(e: NoContentException) = ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ExceptionResponse(e.cause, e.message))

    @ExceptionHandler(RestrictedUserException::class)
    fun handleRestrictedUserException(e: RestrictedUserException) = ResponseEntity
        .status(HttpStatus.FORBIDDEN)
        .body(ExceptionResponse(e.cause, e.message))
}
