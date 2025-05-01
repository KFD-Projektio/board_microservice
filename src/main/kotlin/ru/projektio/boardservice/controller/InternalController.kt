package ru.projektio.boardservice.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.projektio.boardservice.service.BoardService
import ru.projektio.boardservice.service.ColumnService

@RestController
@RequestMapping("/internal/boards/{boardId}")
class InternalController(
    private val boardService: BoardService,
    private val columnService: ColumnService
) {
    @GetMapping("/exists")
    fun boardExists(@PathVariable("boardId") boardId: Long) = ResponseEntity
        .status(HttpStatus.OK)
        .body(boardService.boardExists(boardId))

    @GetMapping("/columns")
    fun getBoardColumns(@RequestHeader("X-User-Id") userId: Long,
                        @PathVariable("boardId") boardId: Long
    ) = ResponseEntity
        .status(HttpStatus.OK)
        .body(columnService.getBoardColumns(userId, boardId))
}
