import Foundation

extension URL {
    var queryParamsUnDecoded: [String: String] {
        get {
            var queryMap = [String: String]()
            self.query?.split(separator: "&").forEach { it in
                let tmp = it.split(separator: "=")
                if (tmp.count > 0) {
                    queryMap[tmp.getOrNull(0)?.string ?? ""] = tmp.getOrNull(1)?.string
                }
            }
            return queryMap
        }
    }

    public func queryUnDecoded(_ paramName: String) -> String? {
        return self.queryParamsUnDecoded[paramName]
    }

    var queryParams: [String: String]? {
        get {
            guard let components = URLComponents(url: self, resolvingAgainstBaseURL: true), let queryItems = components.queryItems else {
                return nil
            }
            var parameters = [String: String]()
            for item in queryItems {
                parameters[item.name] = item.value
            }
            return parameters
        }
    }

    public func query(_ paramName: String) -> String? {
        return self.queryParams?[paramName]
    }

}
