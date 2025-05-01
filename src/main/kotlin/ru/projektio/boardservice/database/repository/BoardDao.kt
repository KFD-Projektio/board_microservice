package ru.projektio.boardservice.database.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import ru.projektio.boardservice.database.entity.BoardEntity

interface BoardDao: CrudRepository<BoardEntity, Long> {
    fun findBoardEntityById(id: Long): MutableList<BoardEntity>

    @Query("""
        SELECT b from BoardEntity b
        JOIN b.userIDs uid
        WHERE uid = :userId
    """)
    fun findAllByUserIDsContains(@Param("userId") userId: Long): List<BoardEntity>

    fun findBoardEntityByBoardNameContainingIgnoreCaseAndUserIDsContaining(
        boardName: String,
        userIDs: MutableList<Long>,
        pageable: Pageable
    ): Page<BoardEntity>

    fun findAllByUserIDsContains(userIDs: MutableList<Long>, pageable: Pageable): Page<BoardEntity>
    fun findBoardEntityByBoardNameContainingIgnoreCaseAndUserIDsContaining(
        boardName: String,
        userIDs: MutableList<Long>
    ): MutableList<BoardEntity>
}
