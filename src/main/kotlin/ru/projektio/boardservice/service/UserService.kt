package ru.projektio.boardservice.service

import org.springframework.stereotype.Service
import ru.projektio.boardservice.database.entity.BoardUserEntity
import ru.projektio.boardservice.database.mapper.BoardMapper
import ru.projektio.boardservice.database.repository.BoardDao
import ru.projektio.boardservice.dto.response.BoardDataResponse
import ru.projektio.boardservice.exception.NoContentException
import ru.projektio.boardservice.exception.RestrictedUserException

@Service
class UserService(
    private val boardDao: BoardDao,
    private val boardMapper: BoardMapper
) {
    fun addUser(issuerId: Long, userId: Long, boardId: Long): BoardDataResponse {
        val board = boardDao.findById(boardId)
            .orElseThrow{ NoContentException("There is no such board.") }
        val userIds = board.boardUsers.map { it.id }
        if (issuerId !in userIds) {
            throw RestrictedUserException("You can't add users to that board.")
        }

        board.apply {
            boardUsers.add(BoardUserEntity(userId = userId, boardId = boardId))
        }
        boardDao.save(board)
        return boardMapper.boardData(board)
    }

    fun changeOwner(issuerId: Long, newOwnerId: Long, boardId: Long): BoardDataResponse {
        val board = boardDao.findById(boardId)
            .orElseThrow{ NoContentException("There is no such board.") }
        if (issuerId != board.ownerId) {
            throw RestrictedUserException("Only owner of the board can change the owner.")
        }

        board.apply {
            ownerId = newOwnerId
        }
        boardDao.save(board)
        return boardMapper.boardData(board)
    }

    fun removeUser(issuerId: Long, userId: Long, boardId: Long): BoardDataResponse {
        val board = boardDao.findById(boardId)
            .orElseThrow{ NoContentException("There is no such board.") }
        val userIds = board.boardUsers.map { it.id }
        if (issuerId !in userIds) {
            throw RestrictedUserException("You can't remove users from that board.")
        }

        board.apply {
            val indexToDelete = userIds.indexOf(userId)
            boardUsers.removeAt(indexToDelete)
        }
        boardDao.save(board)
        return boardMapper.boardData(board)
    }
}