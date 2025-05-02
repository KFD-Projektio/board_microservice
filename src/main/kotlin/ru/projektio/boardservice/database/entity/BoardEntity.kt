package ru.projektio.boardservice.database.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.jetbrains.annotations.NotNull
import java.time.LocalDateTime

@Entity
@Table(name="boards")
class BoardEntity (
    @Column(name = "title")
    var boardName: String,

    @Column(name = "description")
    var boardDescription: String?,

    @Column(name = "owner_id")
    var ownerId: Long,

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @Column(name = "is_private")
    var isPrivate: Boolean = false

    @Column(name = "created_at")
    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at")
    @CreationTimestamp
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "board_id")
    var boardUsers: MutableList<BoardUserEntity> = mutableListOf()

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "board_id")
    var boardColumns: MutableList<BoardColumnEntity> = mutableListOf()
}
