/**
 * 格式化代码 shift+alt+f+enter
 */

import ApiManager from '../../repository/ApiManager';
import UserManager from '../../utils/UserManager';
import {
    String
} from '../../utils/util';

//login.js
//获取应用实例
const app = getApp()

Page({
    // userInfo: {},
    // hasUserInfo: false,
    // canIUse: wx.canIUse('button.open-type.getUserInfo'),
    data: {
        userName: !String.isBlank(UserManager.getUserName()) ? UserManager.getUserName() : "",
        password: !String.isBlank(UserManager.getUserPwd()) ? UserManager.getUserPwd() : ""
    },
    userNameInput: function (e) {
        this.setData({
            userName: e.detail.value
        })
    },
    passwordInput: function (e) {
        this.setData({
            password: e.detail.value
        })
    },
    //事件处理函数
    bindViewTap: function () {
        let that = this
        ApiManager.requestLogin(
            this.data.userName,
            this.data.password,
            function onSuccess(data) {
                console.log("request onSuccess", data)
                UserManager.saveUserName(that.data.userName)
                UserManager.saveUserPwd(that.data.password)
                UserManager.saveUserToken(data.token)
                wx.showToast({
                    title: '登录成功',
                    duration: 2000
                });
                wx.switchTab({
                    url: '../index/index'
                })
            },
            function onFailure(errorCode, errorMessage) {
                console.log("request onFailure", errorCode, errorMessage)
                wx.showToast({
                    title: '请求失败:' + errorCode + '\n' + errorMessage,
                    icon: 'none',
                    duration: 2000
                  });
            }
        );
    },
    onShow: function () {
        console.log("login onShow");
    },
    onLoad: function () {
        console.log("login onLoad");

        this.setData({
            userName: UserManager.getUserName()
        })
        this.setData({
            password: UserManager.getUserPwd()
        })

        // wx.login({
        //     success (res) {
        //       if (res.code) {
        //         //发起网络请求
        //         console.log('登录成功! '+ res.code)
        //       } else {
        //         console.log('登录失败！' + res.errMsg)
        //       }
        //     }
        //   })

        // 必须点击按钮才能获取用户信息 https://developers.weixin.qq.com/community/develop/doc/000c2424654c40bd9c960e71e5b009
        // console.log("userInfo1 start")
        // if (app.globalData.userInfo) {
        //     console.log("userInfo1 " + app.globalData.userInfo)

        //     this.setData({
        //       userInfo: app.globalData.userInfo,
        //       hasUserInfo: true
        //     })
        //   } else if (this.data.canIUse){
        //     console.log("userInfo2 start")
        //     // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
        //     // 所以此处加入 callback 以防止这种情况
        //     app.userInfoReadyCallback = res => {
        //       console.log("userInfo2 " + res.userInfo)

        //       this.setData({
        //         userInfo: res.userInfo,
        //         hasUserInfo: true
        //       })
        //     }
        //   } else {
        //     console.log("userInfo3 start")
        //     // 在没有 open-type=getUserInfo 版本的兼容处理
        //     wx.getUserInfo({
        //         withCredentials: false,
        //       success: res => {
        //         app.globalData.userInfo = res.userInfo
        //         console.log("userInfo3 " + res.userInfo)
        //         this.setData({
        //           userInfo: res.userInfo,
        //           hasUserInfo: true
        //         })
        //       }
        //     })
        //   }

        if (!String.isBlank(UserManager.getUserToken())) {
            console.log('有 token 判断登录成功! 自动跳转到 index 页面')
            wx.reLaunch({
                url: '/pages/index/index'
            });
        } else {
            console.log('无 token 需要重新登录！')
        }
    }
})