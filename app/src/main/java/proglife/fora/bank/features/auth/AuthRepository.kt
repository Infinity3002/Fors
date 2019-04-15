package proglife.fora.bank.features.auth

import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import proglife.fora.bank.data.Storage
import proglife.fora.bank.extensions.network.ApiResponse
import proglife.fora.bank.features.auth.models.LoginInfo
import proglife.fora.bank.features.auth.models.RegInfo
import proglife.fora.bank.features.auth.models.VerifyCode

/**
 * Created by Evhenyi Shcherbyna on 17.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class AuthRepository(private val authApi: AuthApi, private val storage: Storage) {

    private var authenticated: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    fun getToken(): Single<Any>{
        return authApi.getToken()
                .map {
                    storage.mHeaderName = it.data.headerName
                    storage.mToken = it.data.token
                    storage.save()
                }
    }

    fun isAuthenticated(): Subject<Boolean> {
        return authenticated
    }

    fun authorize(): Single<Unit> {
        return Single.fromCallable {
            authenticated.onNext(true)
        }
    }

    fun logout(): Single<Unit> {
        return Single.fromCallable {
            authenticated.onNext(false)
        }
    }

    fun login(loginInfo: LoginInfo): Single<ApiResponse<Any>> {
        return authApi.login(loginInfo)
    }

    fun verifyLoginCode(loginInfo: LoginInfo): Single<ApiResponse<Any>> {
        return authApi.loginVerifyCode(loginInfo)
    }

    fun checkClient(reg: RegInfo): Single<ApiResponse<Any>>{
        return authApi.checkClient(reg)
    }

    fun verifyCode(verifyCode: VerifyCode): Single<ApiResponse<Any>>{
        return authApi.verifyCode(verifyCode)
    }

    fun registration(reg: RegInfo): Single<ApiResponse<Any>> {
        return authApi.registration(reg)
    }

}