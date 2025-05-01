package ru.projektio.boardservice.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.projektio.boardservice.dto.request.CreateBoardRequest
import ru.projektio.boardservice.dto.request.UpdateBoardRequest
import ru.projektio.boardservice.dto.response.BoardDataResponse
import ru.projektio.boardservice.service.BoardService

@RestController
@RequestMapping("/boards")
class BoardController(private val boardService: BoardService) {

    @GetMapping("/current")
    fun getCurrentBoards(@RequestHeader("X-User-Id") userId: Long) : ResponseEntity<List<BoardDataResponse>> {
        return ResponseEntity.ok(boardService.getBoardsByUserId(userId))
    }

    @GetMapping
    fun getBoards(
        @RequestHeader("X-User-Id") userId: Long,
        @RequestParam(required = false) search: String?,
        @PageableDefault(size = 10, sort = ["id"], direction = Sort.Direction.ASC) pageable: Pageable
    ): ResponseEntity<Iterable<BoardDataResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(boardService.getBoards(userId, search, pageable))
    }

    @PostMapping
    fun createBoard(@RequestHeader("X-User-Id") userId: Long,
                    @RequestBody data: CreateBoardRequest
    ) = ResponseEntity
            .status(HttpStatus.CREATED)
            .body(boardService.addBoard(userId, data))

    @GetMapping("/{boardId}")
    fun getBoardById(@RequestHeader("X-User-Id") userId: Long,
                     @PathVariable("boardId") boardId: Long
    ) = ResponseEntity
        .status(HttpStatus.OK)
        .body(boardService.getBoardById(userId, boardId))

    @PutMapping("/{boardId}")
    fun updateBoard(@RequestHeader("X-User-Id") userId: Long,
                    @PathVariable("boardId") boardId: Long,
                    @RequestBody data: UpdateBoardRequest
    ) = ResponseEntity
        .status(HttpStatus.CREATED)
        .body(boardService.updateBoardData(userId, boardId, data))

    @DeleteMapping("/{boardId}")
    fun deleteBoard(@RequestHeader("X-User-Id") userId: Long,
                    @PathVariable("boardId") boardId: Long
    ) = ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(boardService.deleteBoard(userId, boardId))
}
