package com.smart.library.user

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
