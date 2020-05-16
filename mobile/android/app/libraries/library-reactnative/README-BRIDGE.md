# communication with native
> react native and html5 hybird 与 native 通信桥梁
> *注意:dialog 不要放在 bridge 里*

##### 0. 全局 schema

* smart://template/schemaId?param0=0&param1=1
    * 外部浏览器可以呼起 app
* smart://template/flutter?page=demo&params=jsonString
    * 外部直接呼起 flutter
* smart://template/rn?component=smart-travel&page=home&params=jsonString
    * 外部直接呼起 rn
    
* smart://template/h5?url=
    * 外部直接呼起 h5
    
* smart://hybird/bridge/functionName?params=jsonString&callbackId=callbackId
    * hybird bridge 协议
    * native 回传值给调用方
        ```
        webview.executeJS("window.bridge.onCallback($callbackId, $jsonString)")
        ```
        
---

##### 1. open new page
> 根据传过来的url判断打开native/webview/rn/第三方

* *functionName*:**open**
* *params*:
    ```json
    {
      "url": ""
    }
    ```
* *return*:  
    ```json
    {
        "result": "true/false"
    }
    ```  
    
##### 2. close current page with result
> 关闭当前UIViewController/Activity

* *functionName*:**close**
* *params*:
    ```json
    {
      "result": ""
    }
    ```
* *return*:  
    ```json
    {
        "result": "true/false"
    }
    ```  
   
##### 3. show toast 
> 显示一个toast

* *functionName*:**showToast**
* *params*:
    ```json
    {
      "message": ""
    }
    ```
* *return*:  
    ```json
    {
        "result": "true/false"
    }
    ```  
       
##### 4. put value
> 存储变量到本地

* *functionName*:**put**
* *params*:
    ```json
    {
      "key": "xxx",
      "value": "xxx"
    }
    ```
* *return*:  
    ```json
    {
        "result": "true/false"
    }
    ```  
       
##### 5. get value
> 从本地读取变量

* *functionName*:**get**
* *params*:
    ```json
    {
      "key": "xxx"
    }
    ```       
* *return*:  
    ```json
    {
        "result": "valueForKey"
    }
    ```  
       
##### 6. get user info
> 获取用户信息

* *functionName*:**getUserInfo**
* *return*:
    ```json
    {
        "result": {
            "name": "xxx"
        }
    }
    ```       
     
##### 7. get location info
> 获取定位信息

* *functionName*:**getLocationInfo**
* *return*:
    ```json
    {
        "result": {
            "currentLatLng": "xxx",
            "currentCity": "xxx"
        }
    }
    ```      
        
##### 8. get device info
> 获取设备信息

* *functionName*:**getDeviceInfo**
* *return*:
    ```json
    {
        "result": {
            "platform": "android/ios"
        }
    }
    ```  
        
---
