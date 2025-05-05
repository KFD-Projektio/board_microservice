package ru.projektio.boardservice.database.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import ru.projektio.boardservice.database.entity.BoardEntity

interface BoardDao: CrudRepository<BoardEntity, Long> {
    fun findBoardEntityById(id: Long): MutableList<BoardEntity>

    @Query("SELECT b FROM BoardEntity b JOIN b.boardUsers u WHERE u.userId = :userId")
    fun findAllByUserId(@Param("userId") userId: Long): List<BoardEntity>

    @Query("""
        SELECT DISTINCT b FROM BoardEntity b
        LEFT JOIN b.boardUsers u
        WHERE (u.userId = :userId OR b.ownerId = :userId)
        AND (:search IS NULL OR LOWER(b.boardName) LIKE LOWER(CONCAT('%', :search, '%')))
    """)
    fun findByUserWithPaginationAndTitle(
        @Param("userId") userId: Long,
        @Param("search") title: String?,
        pageable: Pageable
    ): Page<BoardEntity>

    @Query("""
        SELECT DISTINCT b FROM BoardEntity b
        LEFT JOIN b.boardUsers u
        WHERE u.userId = :userId OR b.ownerId = :userId
    """)
    fun findByUserWithPagination(
        @Param("userId") userId: Long,
        pageable: Pageable
    ): Page<BoardEntity>

    @Query("""
    SELECT DISTINCT b FROM BoardEntity b
    LEFT JOIN b.boardUsers u
    WHERE (u.userId = :userId OR b.ownerId = :userId)
    AND LOWER(b.boardName) LIKE LOWER(CONCAT('%', :search, '%'))
    """)
    fun findByUserAndTitle(
        @Param("userId") userId: Long,
        @Param("search") title: String
    ): List<BoardEntity>

    fun findFirstById(id: Long): MutableList<BoardEntity>
}
