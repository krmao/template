import ApiManager from '../../repository/ApiManager';

Page({
  data: {
    currentPage: 1, // 1开始 currentPage=totalPage  就是最后一页
    pageSize: 20,
    /*
     {
        id:int  订单ID
        “orderCode”:string 订单编号
        “createdDate”: date下单时间       
        “goodsNumber”: int数量
        “totalAmount”:double 订单金额    
        “consignee” :string 收货人
        “consignPhone” :string 收货人电话
        “consignAddress”:string 收货地址
        “deliveryType”:int 配送方式(1:门店自提 2:送货上门)
        “orderStatus”:int 订单状态 (0:待支付 1:待成团 2:待发货 3：已发货 4:已完成 5：已取消 6：已退货 7：待退款 8：已退款)
        “separateAmount”:double 提成金额    
        }
     */
    orderList: []
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
    console.log("order onReady")
  },
  onShow: function () {
    console.log("order onShow")
  },
  onHide: function () {
    console.log("order onHide")
  },
  onPullDownRefresh: function () {
    console.log("order onPullDownRefresh")
  },
  onPageScroll: function () {
    console.log("order onPageScroll")
  },
  onShareAppMessage: function () {
    console.log("order onShareAppMessage")
  },
  onReachBottom: function () {
    console.log("order onReachBottom")
    this.requestData()
  },
  requestData: function () {
    let that = this
    ApiManager.requestQueryMyOrders(
      that.data.currentPage,
      that.data.pageSize,
      function onSuccess(response) {
        console.log("request onSuccess", response)
        let newCurrentPage = response.currentPage
        let totalPage = response.totalPage
        let newDataList = response.orderList

        if (newCurrentPage > totalPage) {
          wx.showToast({
            title: '没有更多了',
          })
        } else {
          that.setData({
            currentPage: newCurrentPage + 1,
            orderList: that.data.orderList.concat(newDataList)
          })
        }
      },
      function onFailure(errorCode, errorMessage) {
        console.log("request onFailure", errorCode, errorMessage)
      }
    );
  }
})