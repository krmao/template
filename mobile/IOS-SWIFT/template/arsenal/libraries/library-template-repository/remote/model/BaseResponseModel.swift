//
// Created by smart on 2017/12/26.
// Copyright (c) 2017 ZGW. All rights reserved.
//

import Foundation

public class BaseResponseModel<T: Codable>:NSObject, Codable {
    var version: Int?;
    var category: Int?
    var platform: Int?      //1.ios 2.安卓 3.微信
    var status: Int?        //0：成功  1：失败 2: 未登录
    var message: String?
    var token: String?
    var data: T?
}
