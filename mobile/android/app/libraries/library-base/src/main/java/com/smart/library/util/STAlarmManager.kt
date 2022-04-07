package com.smart.library.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import androidx.annotation.Keep
import com.smart.library.STInitializer

@Suppress("unused")
//@Keep
object STAlarmManager {

    /*
        Intent intent = new Intent(MAlarmBroadcastReceiver.ACTION);//必须静态注册
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mActivity, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        MAlarmManagerUtil.setNormalAlarm(calendar.getTimeInMillis(), pendingIntent);
     */
    fun setNormalAlarm(timeInMillis: Long, pendingIntent: PendingIntent): PendingIntent {
        val context = STInitializer.application()?.applicationContext
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        return pendingIntent
    }

    fun setRepeatAlarm(timeInMillis: Long, intervalMillis: Long, pendingIntent: PendingIntent): PendingIntent {
        val context = STInitializer.application()?.applicationContext
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, intervalMillis, pendingIntent)
        return pendingIntent
    }

    fun cancelAlarm(pendingIntent: PendingIntent) {
        val context = STInitializer.application()?.applicationContext
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    /*
    必须静态注册，动态注册没有任何作用
    <receiver android:name=".modules.mine.MAlarmBroadcastReceiver">
        <intent-filter>
            <action android:name="org.smartrobot.action.alarm" />
        </intent-filter>
    </receiver>
 */
    abstract class CXAlarmBroadcastReceiver : BroadcastReceiver() {
        protected var TAG: String? = javaClass.simpleName

        companion object {
            val ACTION = "org.smartrobot.action.alarm"
        }
    }

}
