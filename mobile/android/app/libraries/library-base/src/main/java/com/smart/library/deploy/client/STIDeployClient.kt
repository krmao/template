package com.smart.library.deploy.client

import com.smart.library.deploy.model.STIDeployCheckUpdateCallback
import java.io.File

interface STIDeployClient {

    fun getRootDir(): File?

    fun tryApply()

    fun tryApply(applyCallback: ((applySuccess: Boolean) -> Unit?)?)

    fun checkUpdate()

    fun checkUpdate(checkUpdateCallback: STIDeployCheckUpdateCallback?)

}