package com.smart.library.base

import androidx.annotation.Keep

@Suppress("unused")
@Keep
interface STMvpContract {
    @Keep
    interface View

    @Keep
    interface Presenter

    @Keep
    interface Repository
}