//
// Created by smart on 2017/12/27.
// Copyright (c) 2017 ZGW. All rights reserved.
//

import Foundation


class GetGoodsBySearchNameData: NSObject, Codable {
    var currentPage: Int? = 0
    var totalPage: Int? = 0
    var totalItems: Int? = 0
    var pageSize: Int? = 0
    var goodsList: [GoodModel]? = []
}

class GoodModel: NSObject, Codable {
    var id: Int = 0
    var grouponId: Int = 0
    var goodsName: String = ""
    var goodsSubName: String = ""
    var goodsPicture: String = ""
    var goodsPrice: Double = 0.0
    var goodsUnits: String = ""
    var currentGrouponRestrictions: Int = 0
    var currentRestrictionsNumber: Int = 0
}

class GetGoodsBySearchNameRequestData: NSObject, Codable {
    var parentCategoryId: Int? = 0
    var categoryId: Int? = 0
    var searchName: String? = ""
    var currentPage: Int? = 0
    var pageSize: Int? = 0
}