package proglife.fora.bank.ui.message.chat

data class TempChat(val name: String, val date: Long, val state: State){

    enum class State{
        CURRENT_USER,
        USER,
        REQUEST,
        REQUEST_SUCCESS
    }
}