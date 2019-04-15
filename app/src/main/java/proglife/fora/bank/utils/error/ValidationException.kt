package proglife.fora.bank.utils.error

class ValidationException(val bag: Bag) : RuntimeException() {

    override fun toString(): String {
        return "ValidationException${bag.title?.let { "($it)" }}: " + bag.keys.joinToString(",")
    }

}