package proglife.fora.bank.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import proglife.fora.bank.R

class NotificationUtils {
    companion object {

        const val CHANNEL_ID = "fora_notify"
        const val CHANNEL_NAME = "Fora bank"
        const val DESCRIPTION_CHANNEL = "Fora back"

        fun showNotifi(context: Context, title: String? = null, message: String) {
            createNotificationChannel(context)
            val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_bb_fora_logo_active)
                    .setContentTitle(title)
                    .setContentText(message)

            val notificationManager = NotificationManagerCompat.from(context)

            notificationManager.notify(26478, mBuilder.build())
        }

        private fun createNotificationChannel(context: Context) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = CHANNEL_NAME
                val description = DESCRIPTION_CHANNEL
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance)
                channel.description = description
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                val notificationManager = context.getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }
        }
    }


}