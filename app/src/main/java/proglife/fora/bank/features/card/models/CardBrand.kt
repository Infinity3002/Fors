package proglife.fora.bank.features.card.models

enum class CardBrand(val pattern: Regex, val resource: String) {
        VISA(Regex("^4\\d*$"), "visa-colored.png"),
        MASTER_CARD(Regex("^(5[1-5]|222[1-9]|2[3-6]|27[0-1]|2720)\\d*\$"), "master-card-colored.png"),
        AMERICAN_EXPRESS(Regex("^3[47]\\d*\$"), "american-express-colored.png"),
        DINERS_CLUB(Regex("^3(0[0-5]|[689])\\d*\$"), "diners-club-colored.png"),
        DISCOVER(Regex("^(6011|65|64[4-9])\\d*\$"), "discover-colored.png"),
        JCB(Regex("^(2131|1800|35)\\d*\$"), "jcb-colored.png"),
        UNION_PAY(Regex("^62[0-5]\\d*\$"), "unionpay-colored.png"),
        MAESTRO(Regex("^(5[0678]|6304|6390|6054|6271|67)\\d*\$"), "maestro-colored.png"),
        MIR(Regex("^22\\d*\$"), "mir-colored.png")
    }