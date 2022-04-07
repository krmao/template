import UserManager from '../utils/UserManager';

const requestURL = "https://api.smart.com/jdhome-server/appservice";
// const requestURL = "http://xxx1987.oicp.net/jdhome-server/appservice";

export default class ApiManager {

  static requestQueryMyOrderIncome(currentPage, pageSize, onSuccess, onFailure) {
    return ApiManager.request(
      requestURL + "/getMyOrderIncome", {
        currentPage: currentPage,
        pageSize: pageSize
      },
      onSuccess,
      onFailure
    )
  };

  static requestQueryMyOrders(currentPage, pageSize, onSuccess, onFailure) {
    return ApiManager.request(
      requestURL + "/getMyUserOrder", {
        currentPage: currentPage,
        pageSize: pageSize
      },
      onSuccess,
      onFailure
    )
  };

  static requestQueryMyGoods(currentPage, pageSize, onSuccess, onFailure) {
    return ApiManager.request(
      requestURL + "/getMyCommunityGoodsGroupon", {
        currentPage: currentPage,
        pageSize: pageSize
      },
      onSuccess,
      onFailure
    )
  };

  static requestQueryUsers(currentPage, pageSize, onSuccess, onFailure) {
    return ApiManager.request(
      requestURL + "/getMyCommunityUserAddress", {
        currentPage: currentPage,
        pageSize: pageSize
      },
      onSuccess,
      onFailure
    )
  };
  static requestLogin(userName, password, onSuccess, onFailure) {
    return ApiManager.request(
      requestURL + "/userWechatLogin", {
        userName: userName,
        passWord: password
      },
      onSuccess,
      onFailure
    )
  };

  static request(url, data, onSuccess, onFailure) {
    let timeout = 20000
    wx.showLoading({
      title: '加载中...',
      icon: 'loading',
      duration: timeout + 1000
    });

    let requestData = {
      version: 1,
      category: 1,
      platform: 1,
      token: UserManager.getUserToken(),
      data: data
    }

    console.log("---- request", url, requestData);
    return wx.request({
      url: url,
      method: "POST",
      data: requestData,
      header: {
        'content-type': 'application/json'
      },
      timeout: timeout,
      dataType: "json",
      responseType: "text",
      success: (res) => {
        wx.hideLoading()
        console.log("---- request success", res);
        let data = res.data
        if (data && data.status == 0) {
          if (onSuccess && ('function' == (typeof onSuccess))) {
            onSuccess(data.data)
          }
        } else if (data && data.status == 2) {
          // 重新登录
          UserManager.saveUserToken("")
          wx.reLaunch({
            url: '/pages/login/login'
          });
        } else {
          if (onFailure && ('function' == (typeof onFailure))) {
            onFailure((data && data.status ? data.status : -1), (data && data.message ? data.message : "request success but parse data failure"))
          }
        }
      },
      fail: (res) => {
        wx.hideLoading()
        console.log("---- request fail", res);
        if (onFailure && ('function' == (typeof onFailure))) {
          onFailure(-1, res.errMsg)
        }
      },
      complete: (res) => {
      }
    })
  };

}