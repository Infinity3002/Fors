package proglife.fora.bank.ui.message.chat

import com.arellomobile.mvp.InjectViewState
import proglife.fora.bank.features.chat.models.Chat
import proglife.fora.bank.ui.base.BasePresenter

/**
 * Created by Evhenyi Shcherbyna on 25.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@InjectViewState
class ChatPresenter(private val chat: Chat): BasePresenter<ChatView>() {

    init {
        viewState.showChat(chat)
    }

}