package proglife.fora.bank.features.auth.di

import android.content.Context
import dagger.Component
import proglife.fora.bank.App
import proglife.fora.bank.di.AppComponent
import proglife.fora.bank.di.GlobalFeatureScope
import proglife.fora.bank.ui.main.MainPresenter
import ru.terrakok.cicerone.Router

/**
 * Created by Evhenyi Shcherbyna on 17.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
//@GlobalFeatureScope
//@Component(dependencies = [AppComponent::class], modules = [AuthModule::class])
//interface AuthComponent {
//
//    fun inject(presenter: MainPresenter)
//
//    fun provideContext(): Context
//    fun provideRouter(): Router
//
//    class Initializer {
//        companion object {
//            fun init(): AuthComponent {
//                return DaggerAuthComponent.builder()
//                        .appComponent(App.appComponent)
//                        .build()
//            }
//        }
//    }
//}