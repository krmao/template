package com.smart.library.user

import androidx.annotation.Keep

@Keep
data class STLoginStateChangedEvent(val isLogin: Boolean)
