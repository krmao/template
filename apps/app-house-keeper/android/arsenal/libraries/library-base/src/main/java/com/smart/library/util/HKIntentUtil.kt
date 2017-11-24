package com.smart.library.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.Settings
import android.support.annotation.RequiresPermission
import android.support.v4.app.ActivityCompat
import android.text.TextUtils
import com.smart.library.base.HKBaseApplication
import java.io.File
import java.util.*

@Suppress("MemberVisibilityCanPrivate", "unused")
object HKIntentUtil {
    /**
     * @param content  短信内容
     * @param phoneNum String "num,num,num"
     * @return Intent
     */
    fun getSmsIntent(content: String, phoneNum: String): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.type = "vnd.android-dir/mms-sms"// 对双卡双待手机管用
        intent.putExtra("address", phoneNum)
        intent.putExtra("sms_body", content)
        intent.data = Uri.parse("smsto:" + phoneNum)
        return intent
    }

    /**
     * 分享信息到 短信，微信，QQ
     * startActivity(HKIntent.getShareTextIntent(this,
     * "this is a share message!I love you !", "13771839951" +
     * ",13771839952" + ",13771839953" + ",15051473195"));
     */
    fun getShareTextIntent(context: Context, content: String, phoneNum: String): Intent? {
        val targetedShareIntents = ArrayList<Intent>()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val resInfo = context.packageManager.queryIntentActivities(shareIntent, 0)
        if (!resInfo.isEmpty()) {
            for (resolveInfo in resInfo) {
                val packageName = resolveInfo.activityInfo.packageName
                val activityName = resolveInfo.activityInfo.name
                if ("com.tencent.mobileqq" == packageName && "com.tencent.mobileqq.activity.JumpActivity" == activityName) {
                    val targetedShareIntent = Intent(Intent.ACTION_SEND)
                    targetedShareIntent.type = "text/plain"
                    targetedShareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享到好友")
                    targetedShareIntent.putExtra(Intent.EXTRA_TEXT, content)
                    targetedShareIntent.setClassName(packageName, activityName)
                    targetedShareIntents.add(targetedShareIntent)
                } else if ("com.tencent.mm" == packageName) {
                    val targetedShareIntent = Intent(Intent.ACTION_SEND)
                    targetedShareIntent.type = "text/plain"
                    targetedShareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享到好友")
                    targetedShareIntent.putExtra(Intent.EXTRA_TEXT, content)
                    targetedShareIntent.`package` = packageName
                    targetedShareIntents.add(targetedShareIntent)
                } else if ("com.android.mms" == packageName) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.putExtra("address", phoneNum)
                    intent.putExtra("sms_body", content)
                    intent.data = Uri.parse("smsto:" + phoneNum)
                    targetedShareIntents.add(intent)
                }
            }
            val chooserIntent = Intent.createChooser(targetedShareIntents.removeAt(0), "分享到好友")
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toTypedArray<Parcelable>())
            return chooserIntent
        } else
            return null
    }

    /**
     * @param context 上下文
     * @param content 内容信息
     * @param type    类型，1为qq，2为微信
     * @return Intent
     */
    fun getShareTextIntent(context: Context, content: String, type: Int): Intent? {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val resInfo = context.packageManager.queryIntentActivities(shareIntent, 0)
        if (!resInfo.isEmpty()) {
            for (resolveInfo in resInfo) {
                val packageName = resolveInfo.activityInfo.packageName
                val activityName = resolveInfo.activityInfo.name

                when (type) {
                    1// qq
                    -> {
                        if ("com.tencent.mobileqq" == packageName && "com.tencent.mobileqq.activity.JumpActivity" == activityName) {
                            val intent = Intent(Intent.ACTION_SEND)
                            intent.type = "text/plain"
                            intent.putExtra(Intent.EXTRA_SUBJECT, "分享到好友")
                            intent.putExtra(Intent.EXTRA_TEXT, content)
                            intent.setClassName(packageName, activityName)
                            return intent
                        }
                    }
                    2// weixin
                    -> {
                        if ("com.tencent.mm" == packageName) {
                            val intent = Intent(Intent.ACTION_SEND)
                            intent.type = "text/plain"
                            intent.putExtra(Intent.EXTRA_SUBJECT, "分享到好友")
                            intent.putExtra(Intent.EXTRA_TEXT, content)
                            intent.`package` = packageName
                            return intent
                        }
                    }
                }

            }
        }
        return null
    }

    fun goToAppDetails(context: Context) = goToDefault(context, getInstalledAppDetailsIntent(HKBaseApplication.INSTANCE.packageName))

    @SuppressLint("ObsoleteSdkInt")
    fun getInstalledAppDetailsIntent(packageName: String): Intent {
        val intent = Intent()
        if (Build.VERSION.SDK_INT >= 9) {
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", packageName, null)
        } else {
            val appPkgName = if (Build.VERSION.SDK_INT == 8) "pkg" else "com.android.settings.ApplicationPkgName"
            intent.action = Intent.ACTION_VIEW
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
            intent.putExtra(appPkgName, packageName)
        }
        return intent
    }

    fun getImageIntent(imageFile: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addCategory("android.intent.category.DEFAULT")
        intent.setDataAndType(HKUriUtil.fromFileProvider(imageFile), "image/*")
        return intent
    }

    fun getPdfIntent(pdfFile: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addCategory("android.intent.category.DEFAULT")
        intent.setDataAndType(HKUriUtil.fromFileProvider(pdfFile), "application/pdf")
        return intent
    }

    fun getTextIntent(textFile: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addCategory("android.intent.category.DEFAULT")
        intent.setDataAndType(HKUriUtil.fromFileProvider(textFile), "text/plain")
        return intent
    }

    fun getAudioIntent(audioFile: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addCategory("android.intent.category.DEFAULT")
        intent.setDataAndType(HKUriUtil.fromFileProvider(audioFile), "audio/*")
        return intent
    }

    fun getVideoIntent(videoFile: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.putExtra("oneshot", 0)
        intent.putExtra("configchange", 0)
        intent.addCategory("android.intent.category.DEFAULT")
        intent.setDataAndType(HKUriUtil.fromFileProvider(videoFile), "video/*")
        return intent
    }

    fun getChmIntent(chmFile: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addCategory("android.intent.category.DEFAULT")
        intent.setDataAndType(HKUriUtil.fromFileProvider(chmFile), "application/x-chm")
        return intent
    }

    fun getExcelIntent(excelFile: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addCategory("android.intent.category.DEFAULT")
        intent.setDataAndType(HKUriUtil.fromFileProvider(excelFile), "application/vnd.ms-excel")
        return intent
    }

    fun getPptIntent(pptFile: File): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addCategory("android.intent.category.DEFAULT")
        intent.setDataAndType(HKUriUtil.fromFileProvider(pptFile), "application/vnd.ms-powerpoint")
        return intent
    }

    fun getUnInstallIntent(packageName: String): Intent =
        Intent.createChooser(Intent(Intent.ACTION_DELETE, Uri.fromParts("package", packageName, null)), "uninstall app")

    fun getHtmlIntent(htmlUrl: String): Intent = Intent(Intent.ACTION_VIEW, Uri.parse(htmlUrl))

    fun goToDefault(context: Context, intent: Intent?) {
        if (intent != null) {
            try {
                context.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    fun goToNewTask(intent: Intent?) {
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                HKBaseApplication.INSTANCE.startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 启动外部应用程序
     */
    fun callOpenApp(context: Context, packageName: String): Boolean = try {
        val pm = context.packageManager
        val intent = pm.getLaunchIntentForPackage(packageName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        true
    } catch (e: Exception) {
        HKLogUtil.e(javaClass.name, "Not Found the app.....", e)
        false
    }

    /**
     * 拨打电话
     * return false 代表 拨号页面不存在
     */
    fun openPhone(context: Context, phoneNo: String): Boolean {
        var isOpenSuccess = true
        try {

            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNo))
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            isOpenSuccess = false
        }

        return isOpenSuccess
    }

    /**
     * 拨打电话
     */
    @RequiresPermission(value = "android.permission.CALL_PHONE")
    fun openPhoneAutoCall(context: Context, phoneNo: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNo))
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        context.startActivity(intent)
    }

    /**
     * @param context   上下文
     * *
     * @param emails    邮件地址
     * *
     * @param ccEmails  抄送地址
     * *
     * @param bccEmails 密送地址
     * *
     * @param subject   邮件标题
     * *
     * @param content   邮件内容
     * *
     * @param file      附件
     */
    fun callOpenEmail(context: Context, emails: ArrayList<String>?, ccEmails: ArrayList<String>?, bccEmails: ArrayList<String>?, subject: String, content: String, file: File?) {
        val intent = Intent(Intent.ACTION_SEND)
        if (file == null || !file.isFile || !file.exists()) {
            intent.type = "text/plain" //纯文本
        } else {
            intent.type = "application/octet-stream"//发送附件
            intent.putExtra(Intent.EXTRA_STREAM, HKUriUtil.fromFileProvider(file))//附件
        }
        if (emails != null && emails.size > 0)
            intent.putExtra(Intent.EXTRA_EMAIL, emails) //设置对方邮件地址
        if (ccEmails != null && ccEmails.size > 0)
            intent.putExtra(Intent.EXTRA_CC, ccEmails) //抄送
        if (bccEmails != null && bccEmails.size > 0)
            intent.putExtra(Intent.EXTRA_BCC, bccEmails) //密送
        if (!TextUtils.isEmpty(subject))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject) //设置标题内容
        if (!TextUtils.isEmpty(content))
            intent.putExtra(Intent.EXTRA_TEXT, content) //设置邮件文本内容
        context.startActivity(Intent.createChooser(intent, "请选择"))
    }

    fun callOpenInstallApk(context: Context, apkFilePath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.parse("file://" + apkFilePath), "application/vnd.android.package-archive")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 查询已安装的应用
     */
    fun getAppsList(mContext: Context): List<PackageInfo> =
        mContext.packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)
}
