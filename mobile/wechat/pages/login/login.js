import ApiManager from '../../repository/ApiManager';

//index.js
//获取应用实例
const app = getApp()

Page({
    data: {
        userName: "",
        password: "",
        code: "",
        userInfo: {},
        hasUserInfo: false,
        // canIUse: wx.canIUse('button.open-type.getUserInfo')
    },
    userNameInput:function(e){
        this.setData({userName: e.detail.value})
    },
    passwordInput:function(e){
        this.setData({password: e.detail.value})
    },
    //事件处理函数
    bindViewTap: function () {
        ApiManager.requestLogin(
            this.data.userName,
            this.data.password,
            function onSuccess(data){
                console.log("request onSuccess",data)
                wx.showToast({title: '登录成功'});
                wx.switchTab({url: '../index/index'})
            },
            function onFailure(errorCode,errorMessage){
                console.log("request onFailure",errorCode, errorMessage)
                wx.showToast({title: '登录失败', icon: 'none'});
            }
        );
    },
    onLoad: function () {
        // var that = this;
        // wx.login({
        //     success(res) {
        //         if (res.code) {
        //             //请求自己的服务端, 服务端通过 auth.code2Session 获取 openID 以及 session_key
        //             console.log('登录成功 code=' + res.code)
        //             that.setData({
        //                 code: res.code
        //             });
        //             // wx.request({
        //             //     url: 'https://test.com/onLogin',
        //             //     data: {
        //             //         code: res.code
        //             //     }
        //             // })
        //         } else {
        //             console.log('登录失败 errMsg=' + res.errMsg)
        //         }

        //         console.log('请求登录 end');
        //     }
        // })

        // // 获取微信开放用户信息 
        // if (app.globalData.userInfo) {
        //     console.log(app.globalData.userInfo);
        //     this.setData({
        //         userInfo: app.globalData.userInfo,
        //         hasUserInfo: true
        //     })
        // } else if (this.data.canIUse) {
        //     // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
        //     // 所以此处加入 callback 以防止这种情况
        //     app.userInfoReadyCallback = res => {
        //         console.log(res);
        //         this.setData({
        //             userInfo: res.userInfo,
        //             hasUserInfo: true
        //         })
        //     }
        // } else {
        //     // 在没有 open-type=getUserInfo 版本的兼容处理
        //     wx.getUserInfo({
        //         success: res => {
        //             app.globalData.userInfo = res.userInfo
        //             console.log(app.globalData.userInfo);
        //             this.setData({
        //                 userInfo: res.userInfo,
        //                 hasUserInfo: true
        //             })
        //         }
        //     })
        // }
    },
    // getUserInfo: function (e) {
    //     console.log(e)
    //     app.globalData.userInfo = e.detail.userInfo
    //     this.setData({
    //         userInfo: e.detail.userInfo,
    //         hasUserInfo: true
    //     })
    // }
})
