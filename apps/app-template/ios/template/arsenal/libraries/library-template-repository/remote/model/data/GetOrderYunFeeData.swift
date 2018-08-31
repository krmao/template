//
// Created by smart on 2017/12/27.
// Copyright (c) 2017 ZGW. All rights reserved.
//

import Foundation

class GetYunFeeResponseData: NSObject, Codable {
    var communityId: Int = -1 //社区ID
    var lowLimitMoney: Float = 0 //配送费减免订单金额
    var benefitMoney: Float = 0//配送费金额
}