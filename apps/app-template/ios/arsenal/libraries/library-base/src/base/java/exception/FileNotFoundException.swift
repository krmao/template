//
// Created by smart on 2018/2/2.
// Copyright (c) 2018 com.smart. All rights reserved.
//

import Foundation

class FileNotFoundException: NSObject, Error {

    private(set) var detailMessage: String

    init(_ message: String?) {
        self.detailMessage = message ?? "file not found !!!"
    }

    public override var description: String {
        return self.detailMessage
    }
}
