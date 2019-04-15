package proglife.fora.bank.features.card.di

import dagger.Component
import proglife.fora.bank.App
import proglife.fora.bank.di.AppComponent
import proglife.fora.bank.di.FeatureScope
import proglife.fora.bank.ui.auth.reg.RegPresenter
import proglife.fora.bank.ui.finances.cards.detail.CardDetailPresenter
import proglife.fora.bank.ui.finances.cards.list.CardsPresenter

/**
 * Created by Evhenyi Shcherbyna on 17.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */

@FeatureScope
@Component(dependencies = [AppComponent::class], modules = [CardModule::class])
interface CardComponent {

    fun inject(presenter: RegPresenter)
    fun inject(presenter: CardsPresenter)
    fun inject(presenter: CardDetailPresenter)

    class Initializer {
        companion object {
            fun init(): CardComponent {
                return DaggerCardComponent.builder()
                        .appComponent(App.appComponent)
                        .build()
            }
        }
    }

}