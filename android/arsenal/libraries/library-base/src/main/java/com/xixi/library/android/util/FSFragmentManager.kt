package com.xixi.library.android.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.Window

object FSFragmentManager {

    fun addFragment(fm: FragmentManager?, fragment: Fragment?) {
        if (fm == null || fragment == null)
            return

        val transaction = fm.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
        transaction.add(Window.ID_ANDROID_CONTENT, fragment, fragment.javaClass.name)
        transaction.addToBackStack(fragment.javaClass.name)
        transaction.commitAllowingStateLoss()
    }

    fun addFragment(fm: FragmentManager?, fragment: Fragment?, enterAnimation: Int, exitAnimation: Int) {
        if (fm == null || fragment == null)
            return

        val transaction = fm.beginTransaction()
        transaction.setCustomAnimations(enterAnimation, exitAnimation, enterAnimation, exitAnimation)
        transaction.add(Window.ID_ANDROID_CONTENT, fragment, fragment.javaClass.name)
        transaction.addToBackStack(fragment.javaClass.name)
        transaction.commitAllowingStateLoss()
    }

    fun addFragment(fm: FragmentManager?, fragment: Fragment?, id: Int) {
        if (fm == null || fragment == null)
            return

        val transaction = fm.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
        transaction.add(id, fragment, fragment.javaClass.name)
        transaction.addToBackStack(fragment.javaClass.name)
        transaction.commitAllowingStateLoss()
    }

    fun addFragment(fm: FragmentManager?, fragment: Fragment?, tag: String) {
        if (fm == null || fragment == null)
            return

        val transaction = fm.beginTransaction()
        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_out)
        transaction.add(Window.ID_ANDROID_CONTENT, fragment, tag)
        transaction.addToBackStack(fragment.javaClass.name)
        transaction.commitAllowingStateLoss()
    }

    fun addFragmentNotBackStack(fm: FragmentManager?, fragment: Fragment?) {
        if (fm == null || fragment == null)
            return

        val transaction = fm.beginTransaction()
        transaction.add(android.R.id.content, fragment)
        transaction.commitAllowingStateLoss()
    }

    fun addFragmentNotBackStack(fm: FragmentManager?, fragment: Fragment?, id: Int) {
        if (fm == null || fragment == null)
            return

        val transaction = fm.beginTransaction()
        transaction.add(id, fragment)
        transaction.commitAllowingStateLoss()
    }

    fun removeFragment(fm: FragmentManager?, fragment: Fragment?) {
        if (fm == null || fragment == null)
            return

        val transaction = fm.beginTransaction()
        transaction.remove(fragment)
        transaction.commitAllowingStateLoss()
    }
}
