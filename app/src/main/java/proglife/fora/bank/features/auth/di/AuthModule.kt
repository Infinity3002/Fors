package proglife.fora.bank.features.auth.di

import dagger.Module
import dagger.Provides
import proglife.fora.bank.data.Storage
import proglife.fora.bank.features.auth.*
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Evhenyi Shcherbyna on 17.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@Module
class AuthModule {

    @Singleton
    @Provides
    fun provideAuthInteractor(authValidator: AuthValidator, authRepository: AuthRepository): AuthInteractor =
            AuthInteractor(authValidator, authRepository)

    @Singleton
    @Provides
    fun provideAuthValidator(): AuthValidator = AuthValidator()

    @Singleton
    @Provides
    fun provideAuthRepository(authApi: AuthApi, storage: Storage): AuthRepository = AuthRepository(authApi, storage)

    @Singleton
    @Provides
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

}