package ru.projektio.boardservice.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.projektio.boardservice.service.BoardService
import ru.projektio.boardservice.service.ColumnService

@RestController
@RequestMapping("/internal/api/v1/boards/{boardId}")
class InternalController(
    private val boardService: BoardService,
    private val columnService: ColumnService
) {
    @GetMapping("/exists")
    fun boardExists(@PathVariable("boardId") boardId: Long) = ResponseEntity
        .status(HttpStatus.OK)
        .body(boardService.boardExists(boardId))

    @GetMapping("/columns")
    fun getBoardColumns(@PathVariable("boardId") boardId: Long) = ResponseEntity
        .status(HttpStatus.OK)
        .body(columnService.getBoardColumns(boardId))
}