package com.smart.springcloud.appb.database.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*

open class UserModel(
    var userId: Int = -1,
    var userName: String? = null,
    var roleId: Int? = null,
    @JsonIgnore
    var password: String? = null,
    @JsonIgnore
    var email: String? = null,
    @JsonIgnore
    var birthday: String? = null,
    var phone: String? = null,
    @JsonIgnore
    var identityNo: String? = null,
    @JsonIgnore
    var sex: Int? = null,
    var image: String? = null,
    @JsonIgnore
    var joinTime: String? = null,
    @JsonIgnore
    var loginTime: String? = null,
    @JsonIgnore
    var status: Int? = null,
    @JsonIgnore
    var lastPasswordResetTime: Date? = null
)

class UserModelWithToken(userId: Int, userName: String?, roleId: Int?, password: String?, email: String?, birthday: String?, phone: String?, identityNo: String?, sex: Int?, image: String?, joinTime: String?, loginTime: String?, status: Int?, lastPasswordResetTime: Date?
                         , val accessToken: String? = ""
) : UserModel(userId, userName, roleId, password, email, birthday, phone, identityNo, sex, image, joinTime, loginTime, status, lastPasswordResetTime) {
    constructor(user: UserModel, accessToken: String? = "") : this(user.userId, user.userName, user.roleId, user.password, user.email, user.birthday, user.phone, user.identityNo, user.sex, user.image, user.joinTime, user.loginTime, user.status, user.lastPasswordResetTime, accessToken)

    override fun toString(): String {
        return """

            UserModelWithToken(

                userId=$userId
                userName=$userName
                roleId=$roleId
                phone=$phone
                accessToken=$accessToken

            )

        """
    }


}
