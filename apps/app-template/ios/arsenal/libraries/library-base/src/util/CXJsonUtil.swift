//
// Created by maokangren on 2017/12/27.
// Copyright (c) 2017 ZGW. All rights reserved.
//

import Foundation

class CXJsonUtil {

    static func toJSONString<T: Encodable>(_ value: T, _ outputFormatting: JSONEncoder.OutputFormatting = []) -> String? {
        let encoder = JSONEncoder()
        encoder.outputFormatting = outputFormatting

        var jsonString: String? = nil
        do {
            let data = try encoder.encode(value)
            jsonString = String(data: data, encoding: .utf8)
        } catch {
            print(error)
        }
        return jsonString
    }

    static func parse<T: Decodable>(_ type: T.Type, _ jsonString: String) -> T? {
        let decoder = JSONDecoder()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:sszzz"
        decoder.dateDecodingStrategy = .formatted(dateFormatter)

        var model: T? = nil
        do {
            if let data = jsonString.data(using: .utf8) {
                model = try decoder.decode(type, from: data)
            }
        } catch {
            print(error)
        }
        return model
    }

    static func parse<T: Decodable>(_ type: T.Type, withJSONObject any: Any) -> T? {
        let decoder = JSONDecoder()
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd'T'HH:mm:sszzz"
        decoder.dateDecodingStrategy = .formatted(dateFormatter)

        var model: T? = nil
        do {
            let data: Data = try JSONSerialization.data(withJSONObject: any, options: .prettyPrinted)
            model = try decoder.decode(type, from: data)
        } catch {
            print(error)
        }
        return model
    }
}

extension Encodable {

    func toDictionary() -> [String: Any]? {
        guard let data = try? JSONEncoder().encode(self) else {
            return nil
        }
        return (try? JSONSerialization.jsonObject(with: data, options: .allowFragments)).flatMap {
            $0 as? [String: Any]
        }
    }

    func toString() -> String {
        return JDJsonUtil.toJSONString(self, .prettyPrinted) ?? ""
    }

    func toJSONString() -> String? {
        return JDJsonUtil.toJSONString(self)
    }

}

extension Decodable {

    static func parse(_ jsonString: String) -> Self? {
        return JDJsonUtil.parse(self, jsonString)
    }

    static func parse(withJSONObject any: Any) -> Self? {
        return JDJsonUtil.parse(self, withJSONObject: any)
    }

}