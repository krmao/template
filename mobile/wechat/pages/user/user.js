import ApiManager from '../../repository/ApiManager';

Page({
  data: {
    currentPage: 1, // 1开始 currentPage=totalPage  就是最后一页 
    pageSize: 20,
    /*
      {
        id:"",
        userName:"",
        userPhone:"",     
        userAddress:""
      }
    */
    userAddressList: []
  },
  //事件处理函数
  bindViewTap: function () {
    wx.navigateTo({
      url: '../index/index'
    })
  },
  onLoad: function () {
    console.log("index onLoad")
    this.requestData()
  },
  onReady: function () {
    console.log("user onReady")
  },
  onShow: function () {
    console.log("user onShow")
  },
  onHide: function () {
    console.log("user onHide")
  },
  onPullDownRefresh: function () {
    console.log("user onPullDownRefresh")
  },
  onPageScroll: function () {
    console.log("user onPageScroll")
  },
  onShareAppMessage: function () {
    console.log("user onShareAppMessage")
  },
  onReachBottom: function () {
    console.log("user onReachBottom")
    this.requestData()
  },
  requestData: function () {
    let that = this
    ApiManager.requestQueryUsers(
      that.data.currentPage,
      that.data.pageSize,
      function onSuccess(response) {
        console.log("request onSuccess", response)
        let newCurrentPage = response.currentPage
        let totalPage = response.totalPage
        let newDataList = response.userAddressList

        if (newCurrentPage > totalPage) {
          wx.showToast({
            title: '没有更多了',
          })
        } else {
          that.setData({
            currentPage: newCurrentPage + 1,
            userAddressList: that.data.userAddressList.concat(newDataList)
          })
        }
      },
      function onFailure(errorCode, errorMessage) {
        console.log("request onFailure", errorCode, errorMessage)
      }
    );
  }
})