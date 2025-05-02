package ru.projektio.boardservice.database.entity

import jakarta.persistence.*

@Entity
@Table(name="board_users")
class BoardUserEntity (

    @Column(name="user_id")
    val userId: Long,

    @Column(name="board_id")
    val boardId: Long
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
}