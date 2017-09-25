package com.xixi.library.android.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

class FSHomeKeyUtil(private val context: Context, listener: OnClickListener) {
    private val intentFilter = IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
    private val innerReceiver: InnerReceiver?

    interface OnClickListener {
        fun onClicked()

        fun onLongClicked()
    }

    init {
        innerReceiver = InnerReceiver(listener)
    }

    fun registerReceiver() {
        if (innerReceiver != null) {
            try {
                context.registerReceiver(innerReceiver, intentFilter)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun unregisterReceiver() {
        if (innerReceiver != null) {
            try {
                context.unregisterReceiver(innerReceiver)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    class InnerReceiver(protected var listener: OnClickListener?) : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_CLOSE_SYSTEM_DIALOGS) {
                val reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY)
                if (reason != null) {
                    if (listener != null) {
                        if (reason == SYSTEM_DIALOG_REASON_HOME_KEY) {
                            listener!!.onClicked()//短按home键
                        } else if (reason == SYSTEM_DIALOG_REASON_RECENT_APPS) {
                            listener!!.onLongClicked()//长按home键
                        }
                    }
                }
            }
        }

        companion object {
            val SYSTEM_DIALOG_REASON_KEY = "reason"
            val SYSTEM_DIALOG_REASON_GLOBAL_ACTIONS = "globalactions"
            val SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps"
            val SYSTEM_DIALOG_REASON_HOME_KEY = "homekey"
        }
    }
}