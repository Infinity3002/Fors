package proglife.fora.bank.features.auth

import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.Subject
import proglife.fora.bank.extensions.network.ApiResponse
import proglife.fora.bank.features.auth.models.LoginInfo
import proglife.fora.bank.features.auth.models.RegInfo
import proglife.fora.bank.features.auth.models.VerifyCode
import proglife.fora.bank.ui.auth.reg.models.CardFM
import proglife.fora.bank.ui.auth.reg.models.CredentialsFM

/**
 * Created by Evhenyi Shcherbyna on 17.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class AuthInteractor(private val authValidator: AuthValidator, private val authRepository: AuthRepository) {

    fun getPasswordStrength(password: String): Single<Int> {
        return Single.fromCallable {
            val trimmedPassword = password.trim()
            var score = 0
            if (trimmedPassword.isNotEmpty()) score++
            if (trimmedPassword.length > 8) score++
            if (trimmedPassword.length > 12) score++
            if (trimmedPassword.length > 18) score++
            if (trimmedPassword.contains(Regex("([a-z])"))) score++
            if (trimmedPassword.contains(Regex("([A-Z])"))) score++
            if (trimmedPassword.contains(Regex("([0-9])"))) score++
            if (trimmedPassword.contains(Regex("[^A-Za-z0-9 ]"))) score++
            when (score) {
                in 1..3 -> 1    // WEAK
                in 4..6 -> 2    // MEDIUM
                in 7..10 -> 3   // STRONG
                else -> 0       // EMPTY
            }
        }
    }

    fun getToken(): Single<Any>{
        return authRepository.getToken()
    }

    fun isAuthenticated(): Subject<Boolean> {
        return authRepository.isAuthenticated()
    }

    fun authorize(): Single<Unit> {
        return authRepository.authorize()
    }

    fun logout(): Single<Unit> {
        return authRepository.logout()
    }

    fun login(loginInfo: LoginInfo): Single<ApiResponse<Any>> {
        return Single
                .create<String> { emitter ->
                    FirebaseInstanceId.getInstance().instanceId
                            .addOnSuccessListener { res -> emitter.onSuccess(res.token) }
                            .addOnFailureListener { t -> emitter.onError(t) }
                }
                .map { loginInfo.copy(token = it) }
                .flatMap { authRepository.login(it).subscribeOn(Schedulers.io()) }
                //.map { loginInfo.copy(verificationCode = it.data) }
    }

    fun verifyLoginCode(loginInfo: LoginInfo, code: String): Single<ApiResponse<Any>> {
        return authRepository.verifyLoginCode(loginInfo.copy(verificationCode = code.toInt()))
    }

    fun checkClient(credentialsFM: CredentialsFM, cardFM: CardFM): Single<Any> {
        return authValidator.validateReg(credentialsFM, cardFM).rx(credentialsFM)
                .flatMap {
                    authRepository.checkClient(RegInfo(cardFM.number, credentialsFM.login, credentialsFM.password, credentialsFM.phone, 0))
                }
    }

    fun verifyCode(login: String, pass: String, code: Int): Single<ApiResponse<Any>> {
        return Single
                .create<String> { emitter ->
                    FirebaseInstanceId.getInstance().instanceId
                            .addOnSuccessListener { res -> emitter.onSuccess(res.token) }
                            .addOnFailureListener { t -> emitter.onError(t) }
                }
                .flatMap { authRepository.verifyCode(VerifyCode(login = login, password = pass, token = it, verificationCode = code)).subscribeOn(Schedulers.io()) }
    }

    fun restration(credentialsFM: CredentialsFM, cardFM: CardFM, code: Int): Single<ApiResponse<Any>> {
        return verifyCode(credentialsFM.login, credentialsFM.password, code)
                .flatMap {
                    authRepository.registration(
                            RegInfo(cardFM.number, credentialsFM.login, credentialsFM.password, credentialsFM.phone, code)).subscribeOn(Schedulers.io())
        }
    }

}