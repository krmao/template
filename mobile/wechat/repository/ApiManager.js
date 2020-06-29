const requestURL = "http://api.smart.com/jdhome-server/appservice";

export default class ApiManager{

    static requestLogin(userName, password, onSuccess, onFailure) {
        return ApiManager.request(
          requestURL + "/userWechatLogin",
          {
            userName:userName,
            passWord:password
          },
          onSuccess,
          onFailure
        )
    };

    static request(url, data, onSuccess, onFailure) {
      console.log("---- request", url, data);
        return wx.request({
          url: url,
          method: "POST",
          data: {
            version:1,
            category:1,
            platform:1,
            token:"",
            data:data
          },
          header: {'content-type': 'application/json'},
          timeout:20000,
          dataType:"json",
          responseType:"text",
          success:(res)=>{
            console.log("---- request success", res);
            let data = res.data
            if(data && data.status == 0){
              if(onSuccess && ('function'== (typeof onSuccess))){
                onSuccess(data.data)
              }
            }else{
              if(onFailure && ('function'== (typeof onFailure))){
                onFailure((data && data.status ?data.status: -1), (data && data.message ?data.message:"request success but parse data failure"))
              }
            }
          },
          fail:(res)=>{
            console.log("---- request fail", res);
            if(onFailure && ('function'== (typeof onFailure))){
              onFailure(-1, res.errMsg)
            }
          },
          complete:(res)=>{}
        })
    };

}