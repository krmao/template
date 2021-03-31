package com.smart.library.deploy.client

import androidx.annotation.Keep
import com.smart.library.deploy.model.STIDeployCheckUpdateCallback
import java.io.File

@Keep
interface STIDeployClient {

    fun getRootDir(): File?

    fun tryApply()

    fun tryApply(applyCallback: ((applySuccess: Boolean) -> Unit?)?)

    fun checkUpdate()

    fun checkUpdate(checkUpdateCallback: STIDeployCheckUpdateCallback?)
}