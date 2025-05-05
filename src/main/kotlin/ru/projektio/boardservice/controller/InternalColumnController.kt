import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.projektio.boardservice.service.BoardService
import ru.projektio.boardservice.service.ColumnService

@RestController
@RequestMapping("/internal/boards/{boardId}")
class InternalColumnController(
    private val boardService: BoardService,
    private val columnService: ColumnService
) {
    @GetMapping("/columns/{columnId}")
    fun columnInfo(@PathVariable("columnId") columnId: Long) = ResponseEntity
        .status(HttpStatus.OK)
        .body(columnService.getColumnByIdInternal(columnId))
}