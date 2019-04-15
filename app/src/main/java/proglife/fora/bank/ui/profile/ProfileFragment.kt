package proglife.fora.bank.ui.profile

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_profile.*
import proglife.fora.bank.R
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.ui.main.MainActivity
import proglife.fora.bank.utils.loadImage
import proglife.fora.bank.widgets.TopDividerDecoration

class ProfileFragment : BaseFragment(), ProfileView {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    @InjectPresenter
    lateinit var mPresenter: ProfilePresenter

    private lateinit var mAdapter: ProfileControlAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = ProfileControlAdapter {
            when (it) {
                ProfileControlAdapter.CardMenu.PROFILE_MESSAGE -> mPresenter.chats()
                ProfileControlAdapter.CardMenu.PROFILE_SETTING -> {}
                ProfileControlAdapter.CardMenu.PROFILE_EXIT -> mPresenter.logout()
            }
        }
        ivPhoto.loadImage(R.mipmap.tmp_profile_photo) { circleCrop() }
        rvItems.addItemDecoration(TopDividerDecoration(ContextCompat.getDrawable(activity!!, R.drawable.drawable_divider)!!, 1))
        rvItems.adapter = mAdapter

        (context as MainActivity).bottomBarState(false)
    }

}