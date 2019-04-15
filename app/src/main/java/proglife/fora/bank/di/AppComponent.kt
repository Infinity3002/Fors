package proglife.fora.bank.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import proglife.fora.bank.TestFragment
import proglife.fora.bank.di.module.AppModule
import proglife.fora.bank.ui.main.MainActivity
import proglife.fora.bank.di.module.NavigationModule
import proglife.fora.bank.di.module.NetworkModule
import proglife.fora.bank.features.auth.AuthInteractor
import proglife.fora.bank.features.auth.di.AuthModule
import proglife.fora.bank.ui.auth.confirm.ConfirmCodePresenter
import proglife.fora.bank.ui.auth.login.LoginFormPresenter
import proglife.fora.bank.ui.auth.need.NeedAuthPresenter
import proglife.fora.bank.ui.feed.FeedHostPresenter
import proglife.fora.bank.ui.finances.bills.BillsPresenter
import proglife.fora.bank.ui.finances.bills.detail.BillDetailPresenter
import proglife.fora.bank.ui.finances.cards.block.CardBlockPresenter
import proglife.fora.bank.ui.finances.cards.statement.list.CardStatementPresenter
import proglife.fora.bank.ui.finances.history.FinanceHistoryPresenter
import proglife.fora.bank.ui.finances.history.detail.HistoryDetailPresenter
import proglife.fora.bank.ui.finances.statistic.FinanceStatisticPresenter
import proglife.fora.bank.ui.main.MainPresenter
import proglife.fora.bank.ui.message.chats.ChatsPresenter
import proglife.fora.bank.ui.profile.ProfilePresenter
import proglife.fora.bank.ui.services.map.google.MapPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Singleton
@Component(modules = [NavigationModule::class, AuthModule::class, AppModule::class, NetworkModule::class])
interface AppComponent {

    fun provideContext(): Context
    fun provideRouter(): Router
    fun provideAuthInteractor(): AuthInteractor

    fun inject(activity: MainActivity)
    fun inject(presenter: MainPresenter)
    fun inject(presenter: ProfilePresenter)
    fun inject(presenter: BillsPresenter)
    fun inject(presenter: BillDetailPresenter)
    fun inject(fragment: TestFragment)
    fun inject(presenter: HistoryDetailPresenter)
    fun inject(presenter: CardStatementPresenter)
    fun inject(presenter: LoginFormPresenter)
    fun inject(presenter: FinanceHistoryPresenter)
    fun inject(presenter: MapPresenter)
    fun inject(presenter: FinanceStatisticPresenter)
    fun inject(presenter: FeedHostPresenter)
    fun inject(presenter: CardBlockPresenter)
    fun inject(presenter: ConfirmCodePresenter)
    fun inject(presenter: NeedAuthPresenter)

    @Component.Builder
    interface Builder {
        fun build(): AppComponent
        @BindsInstance
        fun context(context: Context): Builder
    }

    class Initializer private constructor() {
        companion object {
            fun init(context: Context): AppComponent =
                    DaggerAppComponent.builder()
                            .context(context)
                            .build()
        }
    }

}