package proglife.fora.bank.utils.error

import android.content.Context
import android.support.annotation.StringRes
import io.reactivex.Single

/**
 * Created by Evhenyi Shcherbyna on 17.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class Bag constructor(val title: String? = null) : HashMap<String, MutableList<Any>>() {

    companion object {
        const val COMMON = "common"
    }

    fun add(key: String, value: String) {
        getOrPut(key) { mutableListOf() }.add(value)
    }

    fun add(key: String, @StringRes value: Int) {
        getOrPut(key) { mutableListOf() }.add(value)
    }

    fun getErrorString(context: Context, field: String): String {
        val list = get(field)
        return list?.fold("") {
            total, next -> "$total${if (total.isEmpty()) "" else "\n"}${ if (next is Int) context.getString(next) else next.toString() }"
        } ?: ""
    }

    fun getErrorString(context: Context): String {
        return keys.fold("") {
            total, next -> "$total${getErrorString(context, next)}"
        } ?: ""
    }

    fun throwIfNeed() {
        if (isNotEmpty()) throw ValidationException(this)
    }

    fun <T> rx(value: T): Single<T> {
        return if (isEmpty()) Single.just(value) else Single.error { ValidationException(this) }
    }

}