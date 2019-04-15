package proglife.fora.bank.ui.message.chat

import proglife.fora.bank.features.chat.models.Chat
import proglife.fora.bank.ui.base.BaseView

/**
 * Created by Evhenyi Shcherbyna on 25.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface ChatView: BaseView {
    fun showChat(chat: Chat)
}