package proglife.fora.bank.ui.message.chats

import proglife.fora.bank.features.chat.models.Chat
import proglife.fora.bank.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 22.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface ChatsView: BaseView {
    fun showLoading()
    fun dismissLoading()
    fun showChats(list: List<Chat>)
}