package ie.wit.carerpatient.utils

import android.app.*
import android.app.Notification.EXTRA_NOTIFICATION_ID

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.provider.Settings.Global.getString
import android.text.format.DateUtils
import androidx.core.app.AlarmManagerCompat
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager


import ie.wit.carerpatient.R
import ie.wit.carerpatient.ui.home.Home
import java.util.*


class AlarmReceiver: BroadcastReceiver() {

    companion object {
        const val CHANNEL_NAME = "CARERPATIENT CHANNEL"
        const val CHANNEL_ID = "CARERPATIENT_CHANNEL_ID"
        private val REQUEST_CODE = 1
        private val FLAGS = 0
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        val triggerTime = SystemClock.elapsedRealtime() + DateUtils.MINUTE_IN_MILLIS
        //create pending intent to open main activity on the notification click
        val activityPendingIntent = PendingIntent.getActivity(context, 0, Intent(context, Home::class.java ), PendingIntent.FLAG_IMMUTABLE )
        val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val snoozeIntent = Intent(context, AlarmReceiver::class.java)
        snoozeIntent.putExtra("action", "ACTION_SNOOZE")
        //snoozeIntent.putExtras(intent)
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context, 0, snoozeIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        //create the notification
        val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(intent!!.getStringExtra("medicineName"))
            .setContentText(intent.getStringExtra("medicineAmount") + " "+ intent.getStringExtra("medicineType"))
            .setSmallIcon(intent.getIntExtra("medicineImage",R.drawable.notifications_active))
            .setContentIntent(activityPendingIntent)
            .addAction(R.drawable.ic_launcher_foreground,"Snooze", snoozePendingIntent)
            //.addAction(R.drawable.ic_launcher_homer,"Snooze", snoozePendingIntent)
            //.setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        //create the notification channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }



        //show the notification
        notificationManager.notify(System.currentTimeMillis().toInt(),notification)

    }
}

