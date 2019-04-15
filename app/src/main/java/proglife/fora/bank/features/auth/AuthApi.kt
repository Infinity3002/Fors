package proglife.fora.bank.features.auth

import io.reactivex.Single
import proglife.fora.bank.extensions.network.ApiResponse
import proglife.fora.bank.features.auth.models.LoginInfo
import proglife.fora.bank.features.auth.models.RegInfo
import proglife.fora.bank.features.auth.models.VerifyCode
import proglife.fora.bank.features.auth.models.VerifyCodeResponse
import proglife.fora.bank.models.TokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Created by Evhenyi Shcherbyna on 17.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
interface AuthApi {

    @GET("csrf")
    fun getToken(): Single<ApiResponse<TokenResponse>>

    @POST("login.do")
    fun login(
            @Body body: LoginInfo
    ): Single<ApiResponse<Any>>

    @POST("verify/checkVerificationCode")
    fun loginVerifyCode(
            @Body body: LoginInfo
    ): Single<ApiResponse<Any>>

    @POST("registration/checkClient")
    fun checkClient(@Body body: RegInfo): Single<ApiResponse<Any>>

    @POST("registration/verifyCode")
    fun verifyCode(@Body body: VerifyCode): Single<ApiResponse<Any>>

    @POST("registration/doRegistration")
    fun registration(@Body body: RegInfo): Single<ApiResponse<Any>>

}