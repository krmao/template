//
// Created by smart on 2017/12/26.
// Copyright (c) 2017 ZGW. All rights reserved.
//

import Foundation

class BaseRequestModel {
    var version = 1;
    var category = 1;
    var platform = 2; //1.ios 2.安卓 3.微信
    var token = ""; //协议令牌

    init() {
        //self.token = "xxx"
    }

}
