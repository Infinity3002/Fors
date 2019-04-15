package proglife.fora.bank

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_test.*
import proglife.fora.bank.navigation.Screens
import proglife.fora.bank.ui.base.BaseFragment
import ru.terrakok.cicerone.Router
import javax.inject.Inject


class TestFragment : BaseFragment() {

    companion object {
        fun newInstance() = TestFragment()
    }

    @Inject
    lateinit var mRouter: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomBarState(true)
        //*rvList.addItemDecoration(TopDividerDecoration(ContextCompat.getDrawable(activity!!, R.drawable.drawable_divider)!!, 1))
        //rvList.statementAdapter = TestAdapter()*/
    }
}