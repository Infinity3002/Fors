package proglife.fora.bank.features.auth

import io.reactivex.Single
import proglife.fora.bank.ui.auth.reg.models.CardFM
import proglife.fora.bank.ui.auth.reg.models.CredentialsFM
import proglife.fora.bank.utils.error.Bag

import proglife.fora.bank.R

/**
 * Created by Evhenyi Shcherbyna on 17.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class AuthValidator {

    companion object {
        const val LOGIN = "login"
        const val PHONE = "phone"
        const val PASS = "pass"
        const val CONFIRM_PASS = "confirm_pass"
    }

    fun validateReg(credentialsFM: CredentialsFM, cardFM: CardFM): Bag {
        val bag = Bag()

        if (credentialsFM.login.isEmpty()) bag.add(LOGIN, R.string.error_field_empty)
        if (credentialsFM.phone.isEmpty()) bag.add(PHONE,  R.string.error_field_empty)
        if (credentialsFM.password.isEmpty()) bag.add(PASS,  R.string.error_field_empty)
        if (credentialsFM.confirmationPassword.isEmpty()) bag.add(CONFIRM_PASS,  R.string.error_field_empty)

        return bag
    }

}