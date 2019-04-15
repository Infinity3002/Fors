package proglife.fora.bank.di.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import org.threeten.bp.LocalDateTime
import proglife.fora.bank.data.Storage
import retrofit2.Call
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

/**
 * Created by Evhenyi Shcherbyna on 27.06.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .create()
    }

    @Provides
    @Singleton
    fun provideStorage(context: Context): Storage {
        return Storage(context)
    }

}