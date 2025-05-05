package ru.projektio.boardservice.database.entity

import jakarta.persistence.*

@Entity
@Table(name="board_columns")
class BoardColumnEntity (

    @Column(name="column_id")
    val columnId: Long,

    @Column(name="board_id")
    val boardId: Long
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

}