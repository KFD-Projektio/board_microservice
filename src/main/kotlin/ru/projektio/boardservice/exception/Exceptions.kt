package ru.projektio.boardservice.exception

class NoColumnException(message: String) : RuntimeException(message)
class WrongColumnOrderException(message: String) : RuntimeException(message)
class InvalidPaginationException(message: String) : RuntimeException(message)