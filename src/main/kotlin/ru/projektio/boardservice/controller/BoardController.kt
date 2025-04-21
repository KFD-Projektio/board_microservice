package ru.projektio.boardservice.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.projektio.boardservice.dto.request.CreateBoardRequest
import ru.projektio.boardservice.dto.request.UpdateBoardRequest
import ru.projektio.boardservice.service.BoardService

@RestController
@RequestMapping("/api/v1/boards")
class BoardController(private val boardService: BoardService) {

    @GetMapping
    fun getBoards(
        @RequestParam(required = false) search: String?,
        @PageableDefault(size = 10, sort = ["id"], direction = Sort.Direction.ASC) pageable: Pageable
    ): ResponseEntity<Any> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(boardService.getBoards(search, pageable))
    }

    @PostMapping
    fun createBoard(@RequestBody data: CreateBoardRequest) = ResponseEntity
            .status(HttpStatus.CREATED)
            .body(boardService.addBoard(data))

    @GetMapping("/{boardId}")
    fun getBoardById(@PathVariable("boardId") boardId: Long) = ResponseEntity
        .status(HttpStatus.OK)
        .body(boardService.getBoardById(boardId))

    @PutMapping("/{boardId}")
    fun updateBoard(@PathVariable("boardId") boardId: Long, @RequestBody data: UpdateBoardRequest) = ResponseEntity
        .status(HttpStatus.CREATED)
        .body(boardService.updateBoardData(boardId, data))

    @DeleteMapping("/{boardId}")
    fun deleteBoard(@PathVariable("boardId") boardId: Long) = ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(boardService.deleteBoard(boardId))
}