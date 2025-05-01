package ru.projektio.boardservice.exception

class NoColumnException(message: String) : RuntimeException(message)
class WrongColumnOrderException(message: String) : RuntimeException(message)
class NoContentException(message: String) : RuntimeException(message)
class RestrictedUserException(message: String) : RuntimeException(message)
