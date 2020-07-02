export default class UserManager{

    static getUserName() {
      let currentUserName = wx.getStorageSync('userName')
      console.log("getUserName", currentUserName)
      return currentUserName;
    }
    static saveUserName(userName) {
      return wx.setStorageSync('userName', userName);
    }
    static getUserPwd() {
      let currentPwd = wx.getStorageSync('pwd')
      console.log("getUserPwd pwd", currentPwd)
      return currentPwd;
    }
    static saveUserPwd(pwd) {
      return wx.setStorageSync('pwd', pwd);
    }

    static saveUserToken(token) {
      console.log("saveUserToken start current token", UserManager.getUserToken())
      let currentUserName = UserManager.getUserName()
      console.log("saveUserToken middle current userName", currentUserName)
      wx.setStorageSync(currentUserName, token);
      console.log("saveUserToken end current token", UserManager.getUserToken())
    }
    static getUserToken() {
      let currentUserName= UserManager.getUserName()
      let currentToken = wx.getStorageSync(currentUserName)
      console.log("getUserToken token", currentToken)
      return currentToken;
    }
}