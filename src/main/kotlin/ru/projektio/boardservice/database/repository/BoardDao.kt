package ru.projektio.boardservice.database.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import ru.projektio.boardservice.database.entity.BoardEntity

interface BoardDao: CrudRepository<BoardEntity, Long> {
    fun findBoardEntityByBoardNameContainingIgnoreCase(boardName: String, pageable: Pageable): MutableList<BoardEntity>
    fun findBoardEntityByBoardNameContainingIgnoreCase(boardName: String): MutableList<BoardEntity>
    fun findAll(pageable: Pageable): Page<BoardEntity>
    fun getBoardEntityById(id: Long): MutableList<BoardEntity>
    fun findBoardEntityById(id: Long): MutableList<BoardEntity>

    @Query("""
        SELECT b from BoardEntity b
        JOIN b.userIDs uid
        WHERE uid = :userId
    """)
    fun findAllByUserIDsContains(@Param("userId") userId: Long): List<BoardEntity>
}