package proglife.fora.bank

import android.app.Application
import com.google.firebase.FirebaseApp
import com.jakewharton.threetenabp.AndroidThreeTen
import io.reactivex.plugins.RxJavaPlugins
import proglife.fora.bank.di.AppComponent
import timber.log.Timber
import timber.log.Timber.DebugTree

class App: Application(){

    companion object {
        @JvmStatic lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
        initTimber()
        AndroidThreeTen.init(this)
        RxJavaPlugins.setErrorHandler { e -> Timber.e(e)}
    }

    private fun initDagger() {
        appComponent = AppComponent.Initializer.init(this)
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

}