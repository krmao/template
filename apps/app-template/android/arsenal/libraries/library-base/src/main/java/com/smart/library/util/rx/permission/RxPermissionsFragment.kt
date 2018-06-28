package com.smart.library.util.rx.permission

import android.annotation.TargetApi
import android.app.Fragment
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import com.smart.library.base.CXBaseApplication
import com.smart.library.util.CXLogUtil
import io.reactivex.subjects.PublishSubject
import java.util.*

@Suppress("unused")
class RxPermissionsFragment : Fragment() {

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 42
        private const val TAG = RxPermissions.TAG
    }

    // Contains all the current permission requests.
    // Once granted or denied, they are removed from it.
    private val mSubjects = HashMap<String, PublishSubject<Permission>>()
    var enableLog: Boolean = CXBaseApplication.DEBUG

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermissions(permissions: Array<String>) = requestPermissions(permissions, PERMISSIONS_REQUEST_CODE)

    @TargetApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != PERMISSIONS_REQUEST_CODE) return
        val shouldShowRequestPermissionRationale = BooleanArray(permissions.size)
        for (i in permissions.indices) shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i])
        onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale)
    }

    fun onRequestPermissionsResult(permissions: Array<String>, grantResults: IntArray, shouldShowRequestPermissionRationale: BooleanArray) {
        var i = 0
        val size = permissions.size
        while (i < size) {
            CXLogUtil.d(TAG, "onRequestPermissionsResult  " + permissions[i])
            // Find the corresponding subject
            val subject = mSubjects[permissions[i]]
            if (subject == null) {
                // No subject found
                CXLogUtil.e(TAG, "RxPermissions.onRequestPermissionsResult invoked but didn't find the corresponding permission request.")
                return
            }
            mSubjects.remove(permissions[i])
            val granted = grantResults[i] == PackageManager.PERMISSION_GRANTED
            subject.onNext(Permission(permissions[i], granted, shouldShowRequestPermissionRationale[i]))
            subject.onComplete()
            i++
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun isGranted(permission: String) = activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    @TargetApi(Build.VERSION_CODES.M)
    fun isRevoked(permission: String) = activity.packageManager.isPermissionRevokedByPolicy(permission, activity.packageName)

    fun getSubjectByPermission(permission: String) = mSubjects[permission]

    fun containsByPermission(permission: String) = mSubjects.containsKey(permission)

    fun setSubjectForPermission(permission: String, subject: PublishSubject<Permission>) = mSubjects.put(permission, subject)

}
