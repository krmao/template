//
// Created by smart on 2017/12/27.
// Copyright (c) 2017 ZGW. All rights reserved.
//

import Foundation

class RecommendData: Codable {
    var recommendCategoryList: [RecommendDateItem]? = []
}

class RecommendDateItem: NSObject, Codable {
    var id: Int = 0
    var recommendCategoryName: String = ""
}

class RecommendListData: NSObject, Codable {
    var goodsList: [GoodModel] = []
}

class RecommendIdData: Codable {
    var recommendCategoryId: Int = 0

    init(_ id: Int) {
        self.recommendCategoryId = id
    }

}