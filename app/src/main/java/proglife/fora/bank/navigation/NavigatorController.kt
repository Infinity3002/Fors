package proglife.fora.bank.navigation

import android.annotation.SuppressLint
import android.support.annotation.IdRes
import android.support.transition.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.animation.LinearInterpolator
import proglife.fora.bank.R
import proglife.fora.bank.TestFragment
import proglife.fora.bank.features.auth.models.LoginInfo
import proglife.fora.bank.features.card.models.CardWithInfo
import proglife.fora.bank.features.chat.models.Chat
import proglife.fora.bank.ui.auth.confirm.ConfirmCodeFragment
import proglife.fora.bank.ui.auth.login.LoginFormFragment
import proglife.fora.bank.ui.auth.need.NeedAuthFragment
import proglife.fora.bank.ui.auth.reg.RegFragment
import proglife.fora.bank.ui.auth.reg.complete.RegCompleteFragment
import proglife.fora.bank.ui.auth.reg.touchid.TouchIdFragment
import proglife.fora.bank.ui.finances.cards.info.CardInfoFragment
import proglife.fora.bank.ui.finances.cards.detail.CardDetailFragment
import proglife.fora.bank.ui.finances.cards.statement.details.StatementDetailsFragment
import proglife.fora.bank.ui.finances.cards.statement.list.CardStatementFragment
import proglife.fora.bank.ui.feed.FeedHostFragment
import proglife.fora.bank.ui.finances.FinancesFragment
import proglife.fora.bank.ui.finances.bills.detail.BillDetailFragment
import proglife.fora.bank.ui.finances.cards.block.CardBlockFragment
import proglife.fora.bank.ui.finances.history.detail.HistoryDetailFragment
import proglife.fora.bank.ui.finances.transfer.TransferSuccessFragment
import proglife.fora.bank.ui.message.chat.ChatFragment
import proglife.fora.bank.ui.message.chats.ChatsFragment
import proglife.fora.bank.ui.message.request.ChatRequestFragment
import proglife.fora.bank.ui.message.success.SuccessSMSFragment
import proglife.fora.bank.ui.payments.PaymentsFragment
import proglife.fora.bank.ui.profile.ProfileFragment
import proglife.fora.bank.ui.services.ServicesFragment
import proglife.fora.bank.ui.services.map.google.GoogleMapFragment
import proglife.fora.bank.utils.ChangeColorTransition
import proglife.fora.bank.widgets.arc.ArcTrait
import proglife.fora.bank.widgets.arc.ArcView
import ru.terrakok.cicerone.android.SupportFragmentNavigator
import ru.terrakok.cicerone.commands.Command

class NavigatorController(
        private val activity: AppCompatActivity,
        fragmentManager: FragmentManager,
        containerId: Int
) : SupportFragmentNavigator(fragmentManager, containerId) {

    override fun createFragment(screenKey: String, data: Any?): Fragment {
        return when (screenKey) {
            Screens.NEED_AUTH -> NeedAuthFragment()

            Screens.REG -> RegFragment()
            Screens.REG_COMPLETE -> RegCompleteFragment()
            Screens.LOGIN -> LoginFormFragment()
            Screens.TOUCH_ID -> TouchIdFragment()

            Screens.FEED -> FeedHostFragment()
            Screens.TRANSFERS -> PaymentsFragment()
            Screens.SERVICES -> ServicesFragment()
            Screens.GOOGLE_MAP -> GoogleMapFragment()

            Screens.CARDS_DETAIL -> CardDetailFragment.newInstance(data as CardWithInfo)
            Screens.CARD_BLOCK -> CardBlockFragment.newInstance(data as CardWithInfo)

            Screens.CHAT -> ChatFragment.newInstance(data as Chat)
            Screens.CHATS -> ChatsFragment()
            Screens.REFILL_REQUEST -> ChatRequestFragment.newInstanceRefill(data as String)
            Screens.CHAT_REQUEST -> ChatRequestFragment.newInstance(data as String)
            Screens.CHAT_SUCCESS -> SuccessSMSFragment.newInstance(data as String)

            Screens.CARD_STATEMENT -> CardStatementFragment.newInstance()
            Screens.STATEMENT_DETAILS -> StatementDetailsFragment.newInstance()
            Screens.CARD_ABOUT -> CardInfoFragment.newInstance()
            Screens.PROFILE -> ProfileFragment.newInstance()
            Screens.FINANCES -> FinancesFragment.newInstance()
            Screens.BILL_DETAIL -> BillDetailFragment.newInstance(data!!)
            Screens.TRANSFER_SUCCESS -> TransferSuccessFragment.newInstance()
            Screens.HISTORY_DETAIL -> HistoryDetailFragment.newInstance()

            Screens.TEST_AREA -> TestFragment.newInstance()

            Screens.LOGIN_CONFIRM_CODE -> ConfirmCodeFragment.newInstance(data as LoginInfo)

            else -> throw IllegalArgumentException("Unknown screen key: $screenKey")
        }
    }

    override fun setupFragmentTransactionAnimation(command: Command?, currentFragment: Fragment?, nextFragment: Fragment, fragmentTransaction: FragmentTransaction) {
        setupArcToolbarTransition(currentFragment, nextFragment, fragmentTransaction)
        when {
            currentFragment is NeedAuthFragment ->
                fragmentTransaction.setCustomAnimations(0, R.animator.collapse, R.animator.expand, R.animator.fade)
            nextFragment is NeedAuthFragment ->
                fragmentTransaction.setCustomAnimations(R.animator.expand, R.animator.fade, R.animator.expand, R.animator.fade)
            nextFragment is RegFragment ->
                fragmentTransaction.setCustomAnimations(0, R.animator.collapse, R.animator.expand, R.animator.fade)
            nextFragment is TouchIdFragment ->
                fragmentTransaction.setCustomAnimations(R.animator.expand, R.animator.fade, 0, R.animator.collapse)
            nextFragment is RegCompleteFragment ->
                fragmentTransaction.setCustomAnimations(R.animator.expand, R.animator.fade, 0, 0)
            nextFragment is LoginFormFragment ->
                fragmentTransaction.setCustomAnimations(0, R.animator.collapse, R.animator.expand, R.animator.fade)
            currentFragment is FinancesFragment && nextFragment is CardDetailFragment ->
                setupSharedElementsForCardsToCard(currentFragment, nextFragment, fragmentTransaction)
            currentFragment is FinancesFragment && nextFragment is CardBlockFragment ->
                setupSharedElementsForCardsToCard(currentFragment, nextFragment, fragmentTransaction)
            currentFragment is ChatsFragment && nextFragment is ChatFragment -> {
//                setupSlideAnimation(currentFragment, nextFragment, fragmentTransaction, R.id.rvMessages, R.id.rvChat)
            }
        }
    }

    override fun showSystemMessage(message: String) {
    }

    override fun exit() {
        activity.finish()
    }

    private fun setupSharedElementsForCardsToCard(
            cardsFragment: FinancesFragment,
            cardDetailFragment: Fragment,
            fragmentTransaction: FragmentTransaction
    ) {
        val cardView = cardsFragment.getCardViewForTransition()
        cardView?.let { fragmentTransaction.addSharedElement(cardView, cardView.transitionName) }
    }

    private fun setupArcToolbarTransition(currentFragment: Fragment?, nextFragment: Fragment, fragmentTransaction: FragmentTransaction) {
        val current = (currentFragment as? ArcTrait.HasArcView)?.getArcView() ?: return

        val set = TransitionSet()
        set.addTransition(ChangeBounds())
        set.addTransition(ChangeTransform())
        set.addTransition(ChangeColorTransition())
        currentFragment.sharedElementEnterTransition = set
        currentFragment.sharedElementReturnTransition = set
        nextFragment.sharedElementEnterTransition = set
        nextFragment.sharedElementReturnTransition = set

        fragmentTransaction.addSharedElement(current, ArcView.TRANSITION_NAME)
    }

    @SuppressLint("RtlHardcoded")
    private fun setupSlideAnimation(currentFragment: Fragment, nextFragment: Fragment,
                                    fragmentTransaction: FragmentTransaction,
                                    @IdRes currentContent: Int, @IdRes nextContent: Int) {
        val exitSet = TransitionSet()
        exitSet.addTransition(Slide(Gravity.LEFT).addTarget(currentContent))

        val enterSet = TransitionSet()
        enterSet.addTransition(Slide(Gravity.RIGHT).setInterpolator(LinearInterpolator()).addTarget(nextContent))

        currentFragment.exitTransition = exitSet
        nextFragment.enterTransition = enterSet
    }

}