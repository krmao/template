import UserManager from '../../utils/UserManager';

Page({
  data: {},
  //事件处理函数
  bindViewTap: function () {
    // 重新登录
    UserManager.logout()
    wx.reLaunch({
      url: '/pages/login/login'
    });
  },
  onLoad: function () {
    console.log("setting onLoad")
  },
  onReady: function () {
    console.log("setting onReady")
  },
  onShow: function () {
    console.log("setting onShow")
  },
  onHide: function () {
    console.log("setting onHide")
  },
  onPullDownRefresh: function () {
    console.log("setting onPullDownRefresh")
  },
  onPageScroll: function () {
    console.log("setting onPageScroll")
  },
  onShareAppMessage: function () {
    console.log("setting onShareAppMessage")
  },
  onReachBottom: function () {
    console.log("setting onReachBottom")
  }
})