package proglife.fora.bank.features.card.di

import android.content.Context
import dagger.Module
import dagger.Provides
import proglife.fora.bank.features.auth.di.AuthModule
import proglife.fora.bank.features.card.*

/**
 * Created by Evhenyi Shcherbyna on 17.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@Module
class CardModule {

    @Provides
    fun provideCardInteractor(cardRepository: CardRepository, cardUtils: CardUtils, cardValidator: CardValidator): CardInteractor =
            CardInteractor(cardRepository, cardUtils, cardValidator)

    @Provides
    fun provideCardValidator(): CardValidator = CardValidator()

    @Provides
    fun provideCardUtils(context: Context): CardUtils = CardUtils(context)

    @Provides
    fun provideCardRepository(): CardRepository = CardRepositoryTest()

}