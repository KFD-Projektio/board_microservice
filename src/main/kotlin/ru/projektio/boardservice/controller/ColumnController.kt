package ru.projektio.boardservice.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.projektio.boardservice.dto.request.CreateColumnRequest
import ru.projektio.boardservice.dto.request.NewColumnPositionsRequest
import ru.projektio.boardservice.dto.request.NewColumnTitleRequest
import ru.projektio.boardservice.service.ColumnService

@RestController
@RequestMapping("/boards/{boardId}/columns")
class ColumnController (
    private val columnService: ColumnService
) {

    @GetMapping
    fun getAllBoardColumns(@RequestHeader("X-User-Id") userId: Long,
                           @PathVariable("boardId") boardId: Long
    ) = ResponseEntity
        .status(HttpStatus.OK)
        .body(columnService.getBoardColumns(userId, boardId))

    @PostMapping
    fun addColumn(@RequestHeader("X-User-Id") userId: Long,
                  @PathVariable("boardId") boardId: Long,
                  @RequestBody data: CreateColumnRequest
    ) = ResponseEntity
        .status(HttpStatus.CREATED)
        .body(columnService.addColumn(userId, boardId, data))

    @PutMapping("/{columnPosition}")
    fun renameColumn(
        @RequestHeader("X-User-Id") userId: Long,
        @PathVariable("boardId") boardId: Long,
        @PathVariable("columnPosition") columnPosition: Int,
        @RequestBody data: NewColumnTitleRequest
    ) = ResponseEntity
        .status(HttpStatus.CREATED)
        .body(columnService.changeColumnTitle(userId, boardId, columnPosition, data))

    @PutMapping("/reorder")
    fun reorderColumns(@RequestHeader("X-User-Id") userId: Long,
                       @PathVariable("boardId") boardId: Long,
                       @RequestBody newOrder: NewColumnPositionsRequest
    ) = ResponseEntity
        .status(HttpStatus.CREATED)
        .body(columnService.reorderColumns(userId, boardId, newOrder.positions))

    @DeleteMapping("/{columnPosition}")
    fun deleteColumn(
        @RequestHeader("X-User-Id") userId: Long,
        @PathVariable("boardId") boardId: Long,
        @PathVariable("columnPosition") columnPosition: Int
    ) = ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(columnService.deleteColumn(userId, boardId, columnPosition))
}
