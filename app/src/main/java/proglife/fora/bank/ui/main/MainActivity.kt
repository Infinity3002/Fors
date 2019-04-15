package proglife.fora.bank.ui.main

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.bottom_toolbar.*
import proglife.fora.bank.App
import proglife.fora.bank.R
import proglife.fora.bank.navigation.NavigatorController
import proglife.fora.bank.utils.OnBackPressedListener
import proglife.fora.bank.widgets.BottomToolbarView
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MainActivity : MvpAppCompatActivity(), MainView {

    @Inject lateinit var navigatorHolder: NavigatorHolder
    @Inject lateinit var router: Router
    private val containerId = R.id.nav_host_fragment
    private val navigator = NavigatorController(this, supportFragmentManager, containerId)

    @InjectPresenter
    lateinit var mPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        App.appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPresenter.init()
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)

    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(containerId)
        if (fragment != null
                && fragment is OnBackPressedListener
                && fragment.onBackPressed()) {
            return
        } else {
            router.exit()
        }
    }

    fun bottomBarState(hide : Boolean) {
        bottomToolbar.post {
            val barHeight = bottomToolbar.measuredHeight.toFloat()
            val value = if (hide) barHeight else 0f
            val animation = bottomToolbar.animate().translationY(value)
            animation.duration = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            animation.start()
        }
    }

    override fun onAuthenticationChanged(authorized: Boolean) {
        (bottomToolbar as BottomToolbarView).initNavigation(router, authorized)
    }

}
