package proglife.fora.bank.di.module

import com.google.gson.Gson

import java.util.concurrent.TimeUnit

import dagger.Module
import dagger.Provides
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import proglife.fora.bank.BuildConfig
import proglife.fora.bank.data.Storage
import proglife.fora.bank.utils.error.ServerException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.net.CookiePolicy
import javax.inject.Singleton




/**
 * Created by Evhenyi Shcherbyna on 23.10.2017.
 * Copyright (c) 2017 ProgLife. All rights reserved.
 */
@Module
class NetworkModule {

    companion object {
        const val TOKEN_FIELD = "token"
        const val CONNECTION_TIMEOUT = 30L
        const val SERVER_ERROR_FIELD = "errorMessage"
        const val SERVER_RESULT_FIELD = "result"
        const val SERVER_RESULT_ERROR = "ERROR"
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, storage: Storage): Retrofit {
        val builder = OkHttpClient.Builder()
        builder.retryOnConnectionFailure(true)
        builder.readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        builder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        builder.writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        builder.addInterceptor {
            val request = it.request()
            val response = it.proceed(request)
            val rawJson = response.body()!!.string()
            if (rawJson.isNotEmpty() && response.code() >= 200 && response.code() < 400) {
                val jsonObject = JSONTokener(rawJson).nextValue()
                if (jsonObject is JSONObject && jsonObject.has(SERVER_RESULT_FIELD)) {
                    try {
                        if (jsonObject.getString(SERVER_RESULT_FIELD) == SERVER_RESULT_ERROR) {
                            throw ServerException(jsonObject.getString(SERVER_ERROR_FIELD))
                        }
                    } catch (e: JSONException) {
                        throw ServerException(jsonObject.getString(SERVER_ERROR_FIELD))
                    }
                }
            }
            response.newBuilder().body(ResponseBody.create(response.body()?.contentType(), rawJson)).build()
        }

        builder.addInterceptor(HeaderInterceptor(storage))
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        builder.cookieJar(JavaNetCookieJar(cookieManager))

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        val okHttpClient = builder.build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        return retrofit
    }

}