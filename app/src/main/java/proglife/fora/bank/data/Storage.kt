package proglife.fora.bank.data

import android.content.Context
import android.content.SharedPreferences

class Storage(context: Context): Token {

    companion object {
        const val FILENAME = "storage"
        const val TOKEN = "token"
        const val HEADER_NAME = "header_name"
    }
    private val sp: SharedPreferences

    init {
        this.sp = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
        load()
    }

    var mToken: String? = null
    var mHeaderName: String? = null

    private fun load() {
        mToken = sp.getString(TOKEN, null)
        mHeaderName = sp.getString(HEADER_NAME, null)
    }

    fun save() {
        val edit = sp.edit()
        edit.putString(TOKEN, mToken)
        edit.putString(HEADER_NAME, mHeaderName)
        edit.apply()
    }

    override fun getToken(): String? {
        return mToken
    }

    override fun getHeaderName(): String? {
        return mHeaderName
    }

}