package ru.projektio.boardservice.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.projektio.boardservice.dto.request.UserIdRequest
import ru.projektio.boardservice.service.UserService

@RestController
@RequestMapping("/boards/{boardId}/users")
class UserController (
    private val userService: UserService
) {
    @PostMapping
    fun addUser(@RequestHeader("X-User-Id") issuerId: Long,
                @RequestBody data: UserIdRequest,
                @PathVariable("boardId") boardId: Long
    ) = ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userService.addUser(issuerId, data.userId, boardId))

    @DeleteMapping
    fun removeUser(@RequestHeader("X-User-Id") issuerId: Long,
                   @RequestBody data: UserIdRequest,
                   @PathVariable("boardId") boardId: Long
    ) = ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.removeUser(issuerId, data.userId, boardId))

    @PutMapping
    fun changeOwner(@RequestHeader("X-User-Id") issuerId: Long,
                    @RequestBody data: UserIdRequest,
                    @PathVariable("boardId") boardId: Long
    ) = ResponseEntity
        .status(HttpStatus.OK)
        .body(userService.changeOwner(issuerId, data.userId, boardId))
}