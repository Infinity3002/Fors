package proglife.fora.bank.utils

import android.annotation.TargetApi
import android.app.KeyguardManager
import android.content.Context
import android.content.Context.KEYGUARD_SERVICE
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.security.*
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.spec.MGF1ParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource

/**
 * Created by Evhenyi Shcherbyna on 15.01.2018.
 */
class FingerprintUtil {

    enum class SensorState {
        NOT_SUPPORTED,
        NOT_BLOCKED, // если устройство не защищено пином, рисунком или паролем
        NO_FINGERPRINTS, // если на устройстве нет отпечатков
        READY
    }

    companion object {

        private val KEY_ALIAS = "327b6be8260b096327b60968260b27be"
        private val ANDROID_KEY_STORE = "AndroidKeyStore"

        private var canRetry: Boolean = true

        /**
         * Проверяем статус сенсора отпечатка пальца
         *
         * @param context контекст
         * @return статус
         */
        fun checkSensorState(context: Context): SensorState {
            if (isHardwareDetected(context) && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                val keyguardManager = context.getSystemService(KEYGUARD_SERVICE) as KeyguardManager
                if (!keyguardManager.isKeyguardSecure) {
                    return SensorState.NOT_BLOCKED
                }
                val fingerprintManager = context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
                return if (!fingerprintManager.hasEnrolledFingerprints()) {
                    SensorState.NO_FINGERPRINTS
                } else SensorState.READY
            } else {
                return SensorState.NOT_SUPPORTED
            }
        }

        private fun isHardwareDetected(context: Context): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return false
            val fm = from(context)
            return fm != null && fm.isHardwareDetected
        }

        fun from(context: Context): FingerprintManager? {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) return null
            val obj = context.getSystemService(Context.FINGERPRINT_SERVICE)
            return if (obj != null) obj as FingerprintManager else null
        }

        fun newCryptoObjectInstance(mode: Int): FingerprintManager.CryptoObject? {
            val cipher = FingerprintUtil().getCipher(mode)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && cipher != null) {
                return FingerprintManager.CryptoObject(cipher)
            }
            return null
        }

    }

    private var mKeyStore: KeyStore? = null
    private fun initKey(): Boolean {
        try {
            if (mKeyStore == null) {
                mKeyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
            }
            mKeyStore!!.load(null)
            if (!mKeyStore!!.isKeyEntry(KEY_ALIAS)) {
                generatePrivateKey()
            }
            return true
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        }
        return false
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun getCipher(mode: Int): Cipher? {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && initKey()) {
                val cipher = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding")
                } else null
                if (cipher != null) when (mode) {
                    Cipher.ENCRYPT_MODE -> {
                        val certificate = mKeyStore!!.getCertificate(KEY_ALIAS)
                        val key: PublicKey = certificate.publicKey
                        val unrestricted: PublicKey = KeyFactory.getInstance(key.algorithm)
                                .generatePublic(X509EncodedKeySpec(key.encoded))
                        val spec: OAEPParameterSpec = OAEPParameterSpec(
                                "SHA-256", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT)
                        cipher.init(mode, unrestricted, spec)
                    }
                    Cipher.DECRYPT_MODE -> {
                        val key: PrivateKey = mKeyStore!!.getKey(KEY_ALIAS, null) as PrivateKey
                        cipher.init(mode, key)
                    }
                }
                return cipher
            }
        } catch (e: Exception) {
            e.printStackTrace()
//            if (canRetry && e is UnrecoverableKeyException) {
//                canRetry = false
//                mKeyStore?.deleteEntry(KEY_ALIAS)
//                getCipher(mode)
//            }
        }
        return null
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun generatePrivateKey(): Certificate {
        val keyPairGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEY_STORE)
        val builder: KeyGenParameterSpec.Builder = KeyGenParameterSpec.Builder(KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                .setUserAuthenticationRequired(false) // TEMP - Android 8 incorrect auth
        keyPairGenerator.initialize(builder.build())
        keyPairGenerator.generateKeyPair()
        return mKeyStore!!.getCertificate(KEY_ALIAS)
    }

    fun encode(inputString: String?): String? {
        try {
            clearKey()
            val cipher = getCipher(Cipher.ENCRYPT_MODE)
            if (cipher != null) {
                val bytes = cipher.doFinal(inputString?.toByteArray())
                return String(Base64.encode(bytes, Base64.NO_WRAP))
            }
        } catch (exception: IllegalBlockSizeException) {
            exception.printStackTrace()
        } catch (exception: BadPaddingException) {
            exception.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return null
    }

    fun decode(encodedString: String?, cipher: Cipher): String? {
        try {
            val bytes = Base64.decode(encodedString?.toByteArray(), Base64.NO_WRAP)
            return String(cipher.doFinal(bytes))
        } catch (exception: IllegalBlockSizeException) {
            exception.printStackTrace()
        } catch (exception: BadPaddingException) {
            exception.printStackTrace()
        }
        return null
    }

    private fun clearKey() {
        initKey()
        mKeyStore?.deleteEntry(KEY_ALIAS)
    }

}