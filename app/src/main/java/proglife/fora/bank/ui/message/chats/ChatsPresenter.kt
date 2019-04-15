package proglife.fora.bank.ui.message.chats

import android.annotation.SuppressLint
import com.arellomobile.mvp.InjectViewState
import proglife.fora.bank.features.chat.ChatInteractor
import proglife.fora.bank.features.chat.di.ChatComponent
import proglife.fora.bank.features.chat.models.Chat
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.base.BasePresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * Created by Evhenyi Shcherbyna on 22.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@SuppressLint("CheckResult")
@InjectViewState
class ChatsPresenter : BasePresenter<ChatsView>() {

    @Inject
    lateinit var mRouter: Router
    @Inject
    lateinit var mChatInteractor: ChatInteractor

    init {
        ChatComponent.Initializer.init().inject(this)
        mChatInteractor.getChats()
                .compose(applySingleSchedulers())
                .doOnSubscribe { viewState.showLoading() }
                .doFinally { viewState.dismissLoading() }
                .subscribe(
                        {
                            viewState.showChats(it)
                        },
                        {

                        }
                )
    }

    fun back() {
        mRouter.exit()
    }

    fun select(chat: Chat) {
        mRouter.navigateTo(Screens.CHAT, chat)
    }

}