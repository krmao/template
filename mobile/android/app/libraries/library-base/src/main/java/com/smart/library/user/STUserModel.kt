package com.smart.library.user

import androidx.annotation.Keep

@Suppress("unused")
@Keep
data class STUserModel(
    var id: Int = 0,
    var nickName: String,
    var userName: String,
    var gender: String,
    var age: String,
    var phone: String,
    var email: String,
    var avatar: String,
    var accessToken: String,
    var extras: HashMap<String, Any?> = hashMapOf()
)
