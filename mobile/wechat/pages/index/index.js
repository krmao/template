import ApiManager from '../../repository/ApiManager';

Page({
  data: {
    currentPage: 1, // 1开始 currentPage=totalPage  就是最后一页
    pageSize: 20,
    goodsGrouponList: [],
  },
  //事件处理函数
  bindViewTap: function () {
    wx.navigateTo({
      url: '../index/index'
    })
  },
  formatDate: function (dateTime) {
    return formatDate(dateTime);
  },
  onLoad: function () {
    console.log("index onLoad")
    this.requestData()
  },
  onReady: function () {
    console.log("index onReady")
  },
  onShow: function () {
    console.log("index onShow")
  },
  onHide: function () {
    console.log("index onHide")
  },
  onPullDownRefresh: function () {
    console.log("index onPullDownRefresh")
  },
  onPageScroll: function () {
    console.log("index onPageScroll")
  },
  onShareAppMessage: function () {
    console.log("index onShareAppMessage")
  },
  onReachBottom: function () {
    console.log("index onReachBottom")
    this.requestData()
  },
  requestData: function () {
    let that = this
    ApiManager.requestQueryMyGoods(
      that.data.currentPage,
      that.data.pageSize,
      function onSuccess(response) {
        console.log("request onSuccess", response)
        let newCurrentPage = response.currentPage + 1
        let totalPage = response.totalPage
        let newDataList = response.goodsGrouponList

        if (newCurrentPage > totalPage) {
          wx.showToast({
            title: '没有更多了',
            duration: 2000
          })
        } else {
          that.setData({
            currentPage: newCurrentPage,
            goodsGrouponList: that.data.goodsGrouponList.concat(newDataList)
          })
        }
      },
      function onFailure(errorCode, errorMessage) {
        console.log("request onFailure", errorCode, errorMessage)
      }
    );
  }
})