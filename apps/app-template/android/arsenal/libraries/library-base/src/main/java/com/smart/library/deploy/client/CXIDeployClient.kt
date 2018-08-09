package com.smart.library.deploy.client

import com.smart.library.deploy.model.CXIDeployCheckUpdateCallback
import java.io.File

interface CXIDeployClient {

    fun getRootDir(): File

    fun tryApply()

    fun tryApply(applyCallback: ((applySuccess: Boolean) -> Unit?)?)

    fun checkUpdate()

    fun checkUpdate(checkUpdateCallback: CXIDeployCheckUpdateCallback?)

}