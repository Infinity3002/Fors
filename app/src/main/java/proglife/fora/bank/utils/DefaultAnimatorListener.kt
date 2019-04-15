package proglife.fora.bank.utils

import android.animation.Animator

abstract class DefaultAnimatorListener: Animator.AnimatorListener{
    override fun onAnimationRepeat(animation: Animator) {}

    override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {}

    override fun onAnimationEnd(animation: Animator) {}

    override fun onAnimationCancel(animation: Animator?) {}

    override fun onAnimationStart(animation: Animator?, isReverse: Boolean) {}

    override fun onAnimationStart(animation: Animator?) {}
}