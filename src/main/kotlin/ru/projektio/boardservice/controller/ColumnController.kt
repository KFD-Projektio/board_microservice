package ru.projektio.boardservice.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.projektio.boardservice.dto.request.CreateColumnRequest
import ru.projektio.boardservice.dto.request.NewColumnPositionsRequest
import ru.projektio.boardservice.dto.request.NewColumnTitleRequest
import ru.projektio.boardservice.service.ColumnService

@RestController
@RequestMapping("/api/v1/boards/{boardId}/columns")
class ColumnController (
    private val columnService: ColumnService
) {

    @GetMapping
    fun getAllBoardColumns(@PathVariable("boardId") boardId: Long) = ResponseEntity
        .status(HttpStatus.OK)
        .body(columnService.getBoardColumns(boardId))

    @PostMapping
    fun addColumn(@PathVariable("boardId") boardId: Long, data: CreateColumnRequest) = ResponseEntity
        .status(HttpStatus.CREATED)
        .body(columnService.addColumn(boardId, data))

    @PutMapping("/{columnPosition}")
    fun renameColumn(
        @PathVariable("boardId") boardId: Long,
        @PathVariable("columnPosition") columnPosition: Int,
        data: NewColumnTitleRequest
    ) = ResponseEntity
        .status(HttpStatus.CREATED)
        .body(columnService.changeColumnTitle(boardId, columnPosition, data))

    @PutMapping("/reorder")
    fun reorderColumns(@PathVariable("boardId") boardId: Long, newOrder: NewColumnPositionsRequest) = ResponseEntity
        .status(HttpStatus.CREATED)
        .body(columnService.reorderColumns(boardId, newOrder.positions))

    @DeleteMapping("/{columnPosition}")
    fun deleteColumn(
        @PathVariable("boardId") boardId: Long,
        @PathVariable("columnPosition") columnPosition: Int
    ) = ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(columnService.deleteColumn(boardId, columnPosition))
}