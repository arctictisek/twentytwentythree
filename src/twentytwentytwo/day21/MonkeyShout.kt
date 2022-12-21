package twentytwentytwo.day21

sealed interface MonkeyShout

class ActualShout(val value: Long) : MonkeyShout
class OperationShout(val operation: Triple<String, Char, String>) : MonkeyShout
