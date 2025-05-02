package ru.projektio.boardservice.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.projektio.boardservice.database.entity.BoardEntity
import ru.projektio.boardservice.database.entity.BoardUserEntity
import ru.projektio.boardservice.database.mapper.BoardMapper
import ru.projektio.boardservice.database.repository.BoardDao
import ru.projektio.boardservice.dto.request.CreateBoardRequest
import ru.projektio.boardservice.dto.request.UpdateBoardRequest
import ru.projektio.boardservice.dto.response.BoardDataResponse
import ru.projektio.boardservice.exception.NoContentException
import ru.projektio.boardservice.exception.RestrictedUserException

@Service
class BoardService (
    private val boardDao: BoardDao,
    private val boardMapper: BoardMapper
) {
    fun getBoardsByUserId(userId: Long): List<BoardDataResponse> {
        return boardDao.findAllByUserId(userId).map { boardMapper.boardData(it) }
    }

    fun getBoards(userId: Long, searchTerm: String?, pageable: Pageable?): Iterable<BoardDataResponse> {
        return when {
            pageable != null -> {
                if (searchTerm != null) {
                    boardDao.findByUserWithPaginationAndTitle(userId, searchTerm ,pageable)
                        .map {boardMapper.boardData(it)}
                }
                else {
                    boardDao.findByUserWithPagination(userId, pageable)
                        .map { boardMapper.boardData(it) }
                }
            }
            searchTerm != null -> {
                boardDao.findByUserAndTitle(userId, searchTerm)
                    .map { boardMapper.boardData(it) }
            }
            else -> {
                boardDao.findAllByUserId(userId)
                    .map { boardMapper.boardData(it) }
            }
        }
    }

    fun boardExists(boardId: Long): Boolean {
        return boardDao.existsById(boardId)
    }

    fun getBoardById(userId: Long, boardId: Long): BoardDataResponse {
        if (boardExists(boardId)) {
            val board = boardDao.findBoardEntityById(boardId).map {boardMapper.boardData(it)}[0]
            if (userId in board.userIds) {
                return board
            }
            else throw RestrictedUserException("You can't perform actions this board.")
        }
        else throw NoContentException("There is no such board")
    }

    @Transactional
    fun addBoard(userId: Long, data: CreateBoardRequest): BoardDataResponse {
        val board = BoardEntity(
            boardName = data.title,
            boardDescription = data.description,
            ownerId = userId
        ).apply {
            if (data.isPrivate != null) {
                this.isPrivate = data.isPrivate
            }
        }
        val savedBoard = boardDao.save(board)

        savedBoard.boardUsers.add(BoardUserEntity(
            boardId = savedBoard.id,
            userId = userId
        ))
        boardDao.save(savedBoard)

        return boardMapper.boardData(savedBoard)
    }

    @Transactional
    fun updateBoardData(userId: Long, boardId: Long, data: UpdateBoardRequest): BoardDataResponse {
        val board = boardDao.findById(boardId)
            .orElseThrow { NoContentException("There is no such board") }

        if (userId != board.ownerId && userId !in board.boardUsers.map { it.userId }) {
            throw RestrictedUserException("You can't edit this board.")
        }

        board.apply {
            boardName = data.title
            boardDescription = data.description ?: boardDescription
        }
        board.boardUsers = data.userIds
            .map { BoardUserEntity(boardId = boardId, userId = it) }
            .toMutableList()
        boardDao.save(board)
        return boardMapper.boardData(board)
    }

    fun deleteBoard(userId: Long, boardId: Long) {
        if (!boardDao.existsById(boardId)) {
            throw NoContentException("There is no such board")
        }
        val board = boardDao.findById(boardId).get()
        if (userId !in board.boardUsers.map { it.userId }) {
            throw RestrictedUserException("You can't delete this board")
        }
        boardDao.delete(board)
    }
}
