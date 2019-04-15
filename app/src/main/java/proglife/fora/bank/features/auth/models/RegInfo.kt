package proglife.fora.bank.features.auth.models


data class RegInfo(
        val cardNumber: String,
        val login: String,
        val password: String,
        val phone: String,
        val verificationCode: Int = 0
)