package proglife.fora.bank.ui.auth.reg.models

/**
 * Created by Evhenyi Shcherbyna on 11.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
data class CredentialsFM(
     val phone : String,
     val login : String,
     val password : String,
     val confirmationPassword : String
)