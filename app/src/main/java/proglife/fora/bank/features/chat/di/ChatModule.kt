package proglife.fora.bank.features.chat.di

import dagger.Module
import dagger.Provides
import proglife.fora.bank.di.FeatureScope
import proglife.fora.bank.features.chat.ChatInteractor
import proglife.fora.bank.features.chat.ChatRepository
import proglife.fora.bank.features.chat.ChatRepositoryTest

/**
 * Created by Evhenyi Shcherbyna on 25.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@Module
class ChatModule {

    @Provides
    @FeatureScope
    fun provideChatInteractor(chatRepository: ChatRepository): ChatInteractor {
        return ChatInteractor(chatRepository)
    }

    @Provides
    @FeatureScope
    fun provideChatRepository(): ChatRepository {
        return ChatRepositoryTest()
    }

}