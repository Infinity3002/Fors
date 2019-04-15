package proglife.fora.bank.ui.auth.reg.touchid

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.KeyguardManager
import android.content.Context
import android.content.Context.KEYGUARD_SERVICE
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.AnimationDrawable
import android.hardware.fingerprint.FingerprintManager
import android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_LOCKOUT
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.fragment_reg_touch_id.*
import proglife.fora.bank.ui.main.MainActivity
import proglife.fora.bank.R
import proglife.fora.bank.ui.base.BaseFragment
import proglife.fora.bank.utils.FingerprintUtil
import javax.crypto.Cipher

class TouchIdFragment : BaseFragment() {

    companion object {
        val RESULT_CODE = 123
        val RESULT_OK = -1
    }

    private var anim: Animation? = null
    private var blocked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        anim = AnimationUtils.loadAnimation(context, R.anim.scale)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_reg_touch_id, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivCheck.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.avd_check))
        anim!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                if (ivTouch != null && !blocked)
                    ivTouch.startAnimation(animation)
            }

            override fun onAnimationStart(animation: Animation?) {
            }

        })

        ivTouch.postDelayed({ circleWave.startAnimation() }, 700)
        ivTouch.startAnimation(anim)
        bottomBarState(true)

        initFingerprintScanner()
    }

    private var mFingerprintHelper: FingerprintHelper? = null

    private fun initFingerprintScanner() {
        mFingerprintHelper = FingerprintHelper(view!!.context)
        mFingerprintHelper?.startAuth(FingerprintUtil.newCryptoObjectInstance(Cipher.DECRYPT_MODE)!!)
    }

    override fun onDetach() {
        super.onDetach()
        anim?.cancel()
        mFingerprintHelper?.cancel()
    }

    @TargetApi(Build.VERSION_CODES.M)
    inner class FingerprintHelper(private val context: Context): FingerprintManager.AuthenticationCallback() {

        private var mCancellationSignal: CancellationSignal? = null

        fun startAuth(cryptoObject: FingerprintManager.CryptoObject) {
            mCancellationSignal = CancellationSignal()
            val manager: FingerprintManager? = FingerprintUtil.from(context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                manager?.authenticate(cryptoObject, mCancellationSignal, 0, this, null)
            }
        }

        fun cancel() {
            mCancellationSignal?.cancel()
        }

        @SuppressLint("InlinedApi")
        override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
            val message = when (errMsgId) {
                FINGERPRINT_ERROR_LOCKOUT -> "FINGERPRINT_ERROR_LOCKOUT_PERMANENT"
                9 -> "FINGERPRINT_ERROR_LOCKOUT_PERMANENT" // FINGERPRINT_ERROR_LOCKOUT_PERMANENT
                else -> errString
            }
            tvText?.text = message
        }

        override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
            tvText?.text = helpString
        }

        override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
            (ivCheck.drawable as AnimatedVectorDrawable).start()
            blocked = true
            anim?.cancel()
            circleWave.visibility = View.INVISIBLE
            view?.postDelayed({
                (activity as MainActivity).router.exitWithResult(RESULT_CODE, RESULT_OK)
            }, 1400L)
        }

        override fun onAuthenticationFailed() {
            tvText?.text = "Authentication failed"
        }
    }

    enum class SensorState {
        NOT_SUPPORTED,
        NOT_BLOCKED, // если устройство не защищено пином, рисунком или паролем
        NO_FINGERPRINTS, // если на устройстве нет отпечатков
        READY
    }
}

