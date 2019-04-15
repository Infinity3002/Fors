package proglife.fora.bank.features.auth.models

data class VerifyCode(
        val appId: String = "AND",
        val fingerprint: Boolean = true,
        val login: String,
        val password: String,
        val token: String,
        val verificationCode: Int = 0
)