package proglife.fora.bank.features.auth.models

data class VerifyCodeResponse(
        val result: String,
        val errorMessage: String,
        val data: Int
)