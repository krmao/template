//
// Created by smart on 2017/12/27.
// Copyright (c) 2017 ZGW. All rights reserved.
//

import Foundation

class AddGoodCarRequestData: NSObject, Codable {
    var grouponId: Int = 0
    var goodsId: Int = 0
    var goodsNumber: Int = 0
}

class AddGoodCarResponseData: NSObject, Codable {
    var goodsCarId: Int? = 0
}