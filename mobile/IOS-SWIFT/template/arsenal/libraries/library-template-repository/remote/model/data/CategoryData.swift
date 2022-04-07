//
// Created by smart on 2017/12/27.
// Copyright (c) 2017 ZGW. All rights reserved.
//

import Foundation

public class GetAllGoodsCategoryResponseData: NSObject, Codable {
    var categoryList: [CategoryOneLevelModel]? = []
}

public class CategoryOneLevelModel: NSObject, Codable {
    var id: Int = 0
    var categoryName: String? = ""
    var categoryPic: String? = ""
    var goodsPrice: Double? = 0
    var categoryModels: [CategoryTwoLevelModel]? = []
}

public class CategoryTwoLevelModel: NSObject, Codable {
    var id: Int = 0
    var parentId: Int = 0
    var categoryName: String? = ""
    var categoryPic: String? = ""
    var goodsPrice: Double? = 0
}
