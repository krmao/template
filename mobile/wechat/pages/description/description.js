Page({
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
        console.log("description onShow");
    },
    onLoad: function () {
        console.log("description onLoad");
    }
})