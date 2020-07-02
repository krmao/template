import ApiManager from '../../repository/ApiManager';

Page({
  data: {
    currentPage: 1, // 1开始 currentPage=totalPage  就是最后一页
    pageSize: 20,
    /*
    {  //我的收入
        “orderNumber”:int 订单数量
        “createdDate”: date订单时间       
        “totalAmount”:double 订单金额
        “separateAmount”:double 提成金额     
        }
    */
    orderIncomeList: []
  },
  //事件处理函数
  bindViewTap: function () {
    wx.navigateTo({
      url: '../index/index'
    })
  },
  onLoad: function () {
    console.log("order onLoad")
    this.requestData()
  },
  onReady: function () {
    console.log("income onReady")
  },
  onShow: function () {
    console.log("income onShow")
  },
  onHide: function () {
    console.log("income onHide")
  },
  onPullDownRefresh: function () {
    console.log("income onPullDownRefresh")
  },
  onPageScroll: function () {
    console.log("income onPageScroll")
  },
  onShareAppMessage: function () {
    console.log("income onShareAppMessage")
  },
  onReachBottom: function () {
    console.log("income onReachBottom")
    this.requestData()
  },
  requestData: function () {
    let that = this
    ApiManager.requestQueryMyOrderIncome(
      that.data.currentPage,
      that.data.pageSize,
      function onSuccess(response) {
        console.log("request onSuccess", response)
        let newCurrentPage = response.currentPage
        let totalPage = response.totalPage
        let newDataList = response.orderIncomeList

        if (newCurrentPage > totalPage) {
          wx.showToast({
            title: '没有更多了',
          })
        } else {
          that.setData({
            currentPage: newCurrentPage + 1,
            orderIncomeList: that.data.orderIncomeList.concat(newDataList)
          })
        }
      },
      function onFailure(errorCode, errorMessage) {
        console.log("request onFailure", errorCode, errorMessage)
      }
    );
  }
})