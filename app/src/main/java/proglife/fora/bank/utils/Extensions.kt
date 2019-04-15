package proglife.fora.bank.utils

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.gson.Gson
import retrofit2.HttpException


/**
 * Created by Evhenyi Shcherbyna on 07.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun ImageView.loadImage(url : String, options: RequestOptions.() -> RequestOptions = { this } ) {
    Glide.with(this)
            .load(url)
            .apply(options(RequestOptions()))
            .into(this)
}

fun ImageView.loadImage(resource : Int, options: RequestOptions.() -> RequestOptions = { this } ) {
    Glide.with(this)
            .load(resource)
            .apply(options(RequestOptions()))
            .into(this)
}

fun Activity.hideKeyboard() {
    val view = currentFocus
    if (view != null) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun EditText.showKeyboard() {
    requestFocus()
    postDelayed({
        val inputMethodManager = (context as Activity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this,0)
    }, 300)
}

fun <K, V> Map<out K, V>.findByValue(value: V?): Map.Entry<K, V>? {
    forEach {
        if (it.value == value) return it
    }
    return null
}

fun View.blockButton(delay: Long = 200){
    isEnabled = false
    postDelayed({
        isEnabled = true
    }, delay)
}

fun View.isVisibly(visibly: Boolean) {
    visibility = if (visibly) View.VISIBLE else View.GONE
}

inline fun <reified T> HttpException.fromJson(gson: Gson): T {
    return gson.fromJson(response().errorBody()?.string(), T::class.java)
}