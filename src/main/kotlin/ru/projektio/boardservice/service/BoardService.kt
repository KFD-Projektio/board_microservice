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

@Service
class BoardService (
    private val boardDao: BoardDao,
    private val boardMapper: BoardMapper
) {
    // плохо, что any, но хочу в один метод
    fun getBoards(searchTerm: String?, pageable: Pageable?): Any {
        return when {
            pageable != null -> {
                if (searchTerm != null) {
                    boardDao.findBoardEntityByBoardNameContainingIgnoreCase(searchTerm, pageable)
                        .map {boardMapper.boardData(it)}
                }
                else {
                    boardDao.findAll(pageable)
                        .map { boardMapper.boardData(it) }
                }
            }
            searchTerm != null -> {
                boardDao.findBoardEntityByBoardNameContainingIgnoreCase(searchTerm)
                    .map { boardMapper.boardData(it) }
            }
            else -> {
                boardDao.findAll()
                    .map { boardMapper.boardData(it) }
            }
        }
    }
    fun boardExists(boardId: Long): Boolean {
        try {val board = boardDao.findBoardEntityById(boardId)[0]; return true}
        catch (e: IndexOutOfBoundsException) {return false}
    }
    fun getBoardById(boardId: Long): BoardDataResponse {
        if (boardExists(boardId)) {
            return boardDao.findBoardEntityById(boardId).map {boardMapper.boardData(it)}[0]
        }
        else throw NoContentException("There is no such board")
    }

    @Transactional
    fun addBoard(data: CreateBoardRequest): BoardDataResponse {
        val board = BoardEntity(
            boardName = data.title,
            boardDescription = data.description,
            ownerId = data.ownerId
        )
        if (data.isPrivate != null) { board.isPrivate = data.isPrivate }
        board.userIDs.add(board.ownerId)
        boardDao.save(board)
        return boardMapper.boardData(board)
    }

    @Transactional
    fun updateBoardData(boardId: Long, data: UpdateBoardRequest): BoardDataResponse {
        if (boardExists(boardId)) {
            val board = boardDao.findBoardEntityById(boardId)[0]
            board.boardName = data.title
            board.boardDescription = data.description
            board.userIDs = data.userIds.toMutableList()
            boardDao.save(board)
            return boardMapper.boardData(board)
        }
        else throw NoContentException("There is no such board")
    }

    fun deleteBoard(boardId: Long) {
        boardDao.deleteById(boardId)
    }
}