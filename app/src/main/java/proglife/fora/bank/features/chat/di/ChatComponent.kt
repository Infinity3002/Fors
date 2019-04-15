package proglife.fora.bank.features.chat.di

import dagger.Component
import proglife.fora.bank.App
import proglife.fora.bank.di.AppComponent
import proglife.fora.bank.di.FeatureScope
import proglife.fora.bank.ui.message.chats.ChatsPresenter

/**
 * Created by Evhenyi Shcherbyna on 25.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@FeatureScope
@Component(dependencies = [AppComponent::class], modules = [ChatModule::class])
interface ChatComponent {

    fun inject(presenter: ChatsPresenter)

    class Initializer {
        companion object {
            fun init(): ChatComponent {
                return DaggerChatComponent.builder()
                        .appComponent(App.appComponent)
                        .build()
            }
        }
    }

}