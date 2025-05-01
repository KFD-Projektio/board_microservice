package ru.projektio.boardservice.service

import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.projektio.boardservice.database.entity.BoardEntity
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
        return boardDao.findAllByUserIDsContains(userId).map { boardMapper.boardData(it) }
    }

    // плохо, что any, но хочу в один метод
    fun getBoards(userId: Long, searchTerm: String?, pageable: Pageable?): Iterable<BoardDataResponse> {
        return when {
            pageable != null -> {
                if (searchTerm != null) {
                    boardDao.findBoardEntityByBoardNameContainingIgnoreCaseAndUserIDsContaining(searchTerm, mutableListOf(userId) ,pageable)
                        .map {boardMapper.boardData(it)}
                }
                else {
                    boardDao.findAllByUserIDsContains(mutableListOf(userId), pageable)
                        .map { boardMapper.boardData(it) }
                }
            }
            searchTerm != null -> {
                boardDao.findBoardEntityByBoardNameContainingIgnoreCaseAndUserIDsContaining(searchTerm, mutableListOf(userId))
                    .map { boardMapper.boardData(it) }
            }
            else -> {
                boardDao.findAllByUserIDsContains(userId)
                    .map { boardMapper.boardData(it) }
            }
        }
    }
    fun boardExists(boardId: Long): Boolean {
        try {boardDao.findBoardEntityById(boardId)[0]; return true}
        catch (e: IndexOutOfBoundsException) {return false}
    }
    fun getBoardById(userId: Long, boardId: Long): BoardDataResponse {
        if (boardExists(boardId)) {
            val board = boardDao.findBoardEntityById(boardId).map {boardMapper.boardData(it)}[0]
            if (userId in board.userIds) {
                return board
            }
            else throw RestrictedUserException("You can't check this board.")
        }
        else throw NoContentException("There is no such board")
    }

    @Transactional
    fun addBoard(userId: Long, data: CreateBoardRequest): BoardDataResponse {
        val board = BoardEntity(
            boardName = data.title,
            boardDescription = data.description,
            ownerId = userId
        )
        if (data.isPrivate != null) { board.isPrivate = data.isPrivate }
        board.userIDs.add(board.ownerId)
        boardDao.save(board)
        return boardMapper.boardData(board)
    }

    @Transactional
    fun updateBoardData(userId: Long, boardId: Long, data: UpdateBoardRequest): BoardDataResponse {
        if (boardExists(boardId)) {
            val board = boardDao.findBoardEntityById(boardId)[0]
            if (userId in board.userIDs) {
                board.boardName = data.title
                board.boardDescription = data.description
                board.userIDs = data.userIds.toMutableList()
                boardDao.save(board)
                return boardMapper.boardData(board)
            }
            else throw RestrictedUserException("You can't edit this board.")
        }
        else throw NoContentException("There is no such board")
    }

    fun deleteBoard(userId: Long, boardId: Long) {
        try {getBoardById(userId, boardId); boardDao.deleteById(boardId)}
        catch (e: RestrictedUserException) { throw RestrictedUserException("You can't delete this board") }
    }
}
