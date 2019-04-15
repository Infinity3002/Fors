package proglife.fora.bank.di.module

import dagger.Module
import dagger.Provides
import proglife.fora.bank.navigation.AppRouter
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Module
class NavigationModule {

    private var cicerone: Cicerone<AppRouter> = Cicerone.create<AppRouter>(AppRouter())

    @Singleton
    @Provides
    fun provideRouter(): Router {
        return cicerone.router
    }

    @Singleton
    @Provides
    internal fun provideNavigatorHolder(): NavigatorHolder {
        return cicerone.navigatorHolder
    }
}