package proglife.fora.bank.features.chat

import io.reactivex.Single
import proglife.fora.bank.features.chat.models.Chat

/**
 * Created by Evhenyi Shcherbyna on 25.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class ChatInteractor(private val chatRepository: ChatRepository) {

    fun getChats(): Single<List<Chat>> {
        return chatRepository.getChats()
    }

}