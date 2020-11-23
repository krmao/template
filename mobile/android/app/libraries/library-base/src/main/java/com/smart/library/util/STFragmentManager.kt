package com.smart.library.util

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.smart.library.R

@Suppress("unused")
object STFragmentManager {

    @JvmStatic
    fun <T> createFragment(clz: Class<T>): T? = try {
        clz.newInstance()
    } catch (ignore: Exception) {
        null
    }

    @JvmStatic
    fun replaceFragment(supportFragmentManager: FragmentManager?, targetFragment: Fragment?, @IdRes containerViewId: Int, tag: String?) {
        supportFragmentManager ?: return
        targetFragment ?: return

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(containerViewId, targetFragment, tag)
        transaction.commitAllowingStateLoss()
    }

    @JvmStatic
    fun showFragmentWithFadeAnimation(
        supportFragmentManager: FragmentManager?,
        targetFragment: Fragment?,
        @IdRes containerViewId: Int,
        tag: String?,
        addToBackStack: Boolean = true,
        enableCustomAnimations: Boolean = true,
        executePendingTransactions: Boolean = true
    ) {
        showFragment(
            supportFragmentManager = supportFragmentManager,
            targetFragment = targetFragment,
            containerViewId = containerViewId,
            tag = tag,
            addToBackStack = addToBackStack,
            enableCustomAnimations = enableCustomAnimations,
            executePendingTransactions = executePendingTransactions,
            enter = android.R.anim.fade_in,
            exit = android.R.anim.fade_out,
            popEnter = android.R.anim.fade_in,
            popExit = android.R.anim.fade_out
        )
    }

    @JvmStatic
    fun showFragment(
        supportFragmentManager: FragmentManager?,
        targetFragment: Fragment?,
        @IdRes containerViewId: Int,
        tag: String?,
        addToBackStack: Boolean = true,
        enableCustomAnimations: Boolean = true,
        executePendingTransactions: Boolean = true,
        @AnimatorRes @AnimRes enter: Int = R.anim.st_anim_fragment_in,
        @AnimatorRes @AnimRes exit: Int = R.anim.st_anim_fragment_out,
        @AnimatorRes @AnimRes popEnter: Int = R.anim.st_anim_fragment_close_in,
        @AnimatorRes @AnimRes popExit: Int = R.anim.st_anim_fragment_close_out
    ) {
        supportFragmentManager ?: return
        targetFragment ?: return

        if (!targetFragment.isAdded) {
            addFragment(supportFragmentManager, targetFragment, containerViewId, tag)
        } else {
            val transaction = supportFragmentManager.beginTransaction()
            if (enableCustomAnimations) transaction.setCustomAnimations(enter, exit, popEnter, popExit)

            //region hide fragment if exists
            val fragment = supportFragmentManager.findFragmentById(containerViewId)
            if (fragment != null) {
                if (fragment is FragmentManager.OnBackStackChangedListener) supportFragmentManager.addOnBackStackChangedListener(fragment)
                transaction.hide(fragment)
            }
            //endregion

            transaction.show(targetFragment)
            if (addToBackStack) transaction.addToBackStack(tag)
            transaction.commitAllowingStateLoss()
            if (executePendingTransactions) supportFragmentManager.executePendingTransactions()
        }
    }

    @JvmStatic
    fun addFragmentWithFadeAnimation(
        activity: FragmentActivity?,
        targetFragment: Fragment?,
        tag: String?,
        addToBackStack: Boolean = true,
        enableCustomAnimations: Boolean = true,
        executePendingTransactions: Boolean = true
    ) {
        addFragment(
            supportFragmentManager = activity?.supportFragmentManager,
            targetFragment = targetFragment,
            containerViewId = android.R.id.content,
            tag = tag,
            addToBackStack = addToBackStack,
            enableCustomAnimations = enableCustomAnimations,
            executePendingTransactions = executePendingTransactions,
            enter = android.R.anim.fade_in,
            exit = android.R.anim.fade_out,
            popEnter = android.R.anim.fade_in,
            popExit = android.R.anim.fade_out
        )
    }

    @JvmStatic
    fun addFragment(
        activity: FragmentActivity?,
        targetFragment: Fragment?,
        tag: String?,
        addToBackStack: Boolean = true,
        enableCustomAnimations: Boolean = true,
        executePendingTransactions: Boolean = true,
        @AnimatorRes @AnimRes enter: Int = R.anim.st_anim_fragment_in,
        @AnimatorRes @AnimRes exit: Int = R.anim.st_anim_fragment_out,
        @AnimatorRes @AnimRes popEnter: Int = R.anim.st_anim_fragment_close_in,
        @AnimatorRes @AnimRes popExit: Int = R.anim.st_anim_fragment_close_out
    ) {
        addFragment(
            supportFragmentManager = activity?.supportFragmentManager,
            targetFragment = targetFragment,
            containerViewId = android.R.id.content,
            tag = tag,
            addToBackStack = addToBackStack,
            enableCustomAnimations = enableCustomAnimations,
            executePendingTransactions = executePendingTransactions,
            enter = enter,
            exit = exit,
            popEnter = popEnter,
            popExit = popExit
        )
    }

    @JvmStatic
    fun addFragment(
        supportFragmentManager: FragmentManager?,
        targetFragment: Fragment?,
        @IdRes containerViewId: Int = android.R.id.content,
        tag: String?,
        addToBackStack: Boolean = true,
        enableCustomAnimations: Boolean = true,
        executePendingTransactions: Boolean = true,
        @AnimatorRes @AnimRes enter: Int = R.anim.st_anim_fragment_in,
        @AnimatorRes @AnimRes exit: Int = R.anim.st_anim_fragment_out,
        @AnimatorRes @AnimRes popEnter: Int = R.anim.st_anim_fragment_close_in,
        @AnimatorRes @AnimRes popExit: Int = R.anim.st_anim_fragment_close_out
    ) {
        supportFragmentManager ?: return
        targetFragment ?: return

        val transaction = supportFragmentManager.beginTransaction()
        if (enableCustomAnimations) transaction.setCustomAnimations(enter, exit, popEnter, popExit)

        //region hide fragment if exists
        val fragment = supportFragmentManager.findFragmentById(containerViewId)
        if (fragment != null) {
            if (fragment is FragmentManager.OnBackStackChangedListener) supportFragmentManager.addOnBackStackChangedListener(fragment)
            transaction.hide(fragment)
        }
        //endregion

        transaction.add(containerViewId, targetFragment, tag)
        if (addToBackStack) transaction.addToBackStack(tag)
        transaction.commitAllowingStateLoss()
        if (executePendingTransactions) supportFragmentManager.executePendingTransactions()
    }

    /*
       override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
           if (keyCode == KeyEvent.KEYCODE_BACK) {
               val fragmentManager = supportFragmentManager
               if (fragmentManager.backStackEntryCount > 0) {
                   STFragmentManager.removeFragmentByTag(fragmentManager, "fragmentTag")
                   return true
               }
           }
           return super.onKeyDown(keyCode, event)
       }
    */
    /**
     * 注意点:
     *  1. 如果 addFragment 时 没有 addToBackStack, 点击返回键时, 默认的 popBackStackImmediate 不会清除掉该 fragment
     *  2. 如果 addFragment 时 有 addToBackStack, 但是与其它 fragments 即使使用同样的 tag, 点击返回键时, 默认的 popBackStackImmediate 依然会依次删除 fragment
     *  3. 如果 addFragment 时 有 addToBackStack, 但是与其它 fragments 使用同样的 tag, 点击返回键时, 如果使用上述代码中的 removeFragmentByTag, 则会一次性出栈所有通过该 tag 找到的 fragment
     *      3.1 因为 FragmentManager.POP_BACK_STACK_INCLUSIVE 会将找到的 fragment 全部出栈
     */
    @JvmStatic
    fun removeFragmentByTag(fragmentManager: FragmentManager?, tag: String?) {
        if (fragmentManager != null) {
            val targetFragment = fragmentManager.findFragmentByTag(tag)
            //region popBackStack with animation
            try {
                if (fragmentManager.findFragmentByTag(tag) != null) {
                    fragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                }
            } catch (ignore: Exception) {
            }
            //endregion

            //region remove targetFragment
            if (targetFragment != null) {
                val localFragmentTransaction = fragmentManager.beginTransaction()
                localFragmentTransaction.remove(targetFragment)
                localFragmentTransaction.commitAllowingStateLoss()
                fragmentManager.executePendingTransactions()
            }
            //endregion
        }
    }

    @JvmStatic
    fun removeFragment(fragmentManager: FragmentManager?, targetFragment: Fragment?) {
        if (fragmentManager != null && targetFragment != null) {
            val tag = targetFragment.tag
            try {
                removeFragmentByTag(fragmentManager, tag)

                //region remove targetFragment
                val localFragmentTransaction = fragmentManager.beginTransaction()
                localFragmentTransaction.remove(targetFragment)
                localFragmentTransaction.commitAllowingStateLoss()
                fragmentManager.executePendingTransactions()
                //endregion
            } catch (ignore: Exception) {
            }
        }
    }

    @JvmStatic
    fun getFragmentList(fragmentActivity: FragmentActivity?): List<Fragment> = fragmentActivity?.supportFragmentManager?.fragments ?: listOf()
}
