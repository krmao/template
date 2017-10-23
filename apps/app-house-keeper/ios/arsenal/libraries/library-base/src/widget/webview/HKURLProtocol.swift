//
// Created by maokangren on 2017/10/23.
// Copyright (c) 2017 com.xixi. All rights reserved.
//

import Foundation

class HKURLProtocol: URLProtocol {
    override class func canInit(with request: URLRequest) -> Bool {
        print("canInit:request: \(request.url?.absoluteString ?? "")")
        return false
    }
}