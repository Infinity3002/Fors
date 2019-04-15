package proglife.fora.bank.extensions.fcm

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

/**
 * Created by Evhenyi Shcherbyna on 16.09.2018.
 * Copyright (c) 2018 ProgLife. All rights reserved.
 */
class FCMService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage?) {
        super.onMessageReceived(p0)
        Timber.i("FCM message: %s", p0)
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
        Timber.i("FCM token: %s", p0)
    }
}