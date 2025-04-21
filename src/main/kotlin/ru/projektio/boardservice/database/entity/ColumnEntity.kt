package ru.projektio.boardservice.database.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

@Entity
@Table(name = "columns")
class ColumnEntity (
    @Column(name = "title")
    var columnTitle: String,

    @Column(name = "board_id")
    val boardId: Long
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "created_at")
    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at")
    @CreationTimestamp
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "position")
    var columnPosition: Int = 0
}