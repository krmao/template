import Foundation
import Alamofire

class CXApiManager {

    private static let headers: HTTPHeaders = [
        "Accept": "application/json"
    ]

    static func requestByFullURL<T: Codable>(url: String, method: HTTPMethod = .get, requestData: Parameters? = [:], success: @escaping (_ response: T?) -> Void, failure: @escaping (_ message: String?) -> Void) {

        let start = Date()
        Alamofire.request(url, method: method, parameters: requestData, encoding: JSONEncoding.default, headers: headers)
                //.validate(statusCode: 200..<300)
                .responseJSON { dataResponse in
                    print("\n\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                    print("request url: ", dataResponse.request?.url ?? "")
                    print("\nrequest headers:\n")
                    dataResponse.request?.allHTTPHeaderFields?.forEach { key, value in
                        print("\t", key, value)
                    }
                    print("\nrequest body:\n")
                    print("\t", requestData)
                    print("\n<<<<<<<<<<---------->>>>>>>>>>\n")
                    print("response headers:\n")
                    dataResponse.response?.allHeaderFields.forEach { key, value in
                        print("\t", key, value)
                    }

                    print("\nresponse body:\n\n\t\(dataResponse.result.value ?? "")\n")

                    let ms = round(Date().timeIntervalSince(start) * 1000)
                    print("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< 耗时: \(ms) ms \n\n")
                    if (dataResponse.result.isSuccess && dataResponse.result.value != nil) {
                        let responseModel: T? = CXJsonUtil.parse(T.self, withJSONObject: dataResponse.result.value)
                        print("\nresponse success:\n")
                        success(responseModel)
                    } else {
                        let failureMessage = dataResponse.result.error?.localizedDescription.lowercased() ?? "网络错误"
                        print("\nresponse failure:\n\n\t\(failureMessage)\n")
                        failure(failureMessage)
                    }
                }
    }

    static func downloadByFullURL<T: Codable>(url: String, timeoutInterval: Double = 60, success: @escaping (_ response: T?) -> Void, failure: @escaping (_ message: String?) -> Void) {
        let start = Date()
        Alamofire.download(URLRequest(url: NSURL.init(string: url) as! URL, timeoutInterval: timeoutInterval))
                .responseJSON { dataResponse in
                    print("\n\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                    print("request url: ", dataResponse.request?.url ?? "")
                    print("\nrequest headers:\n")
                    dataResponse.request?.allHTTPHeaderFields?.forEach { key, value in
                        print("\t", key, value)
                    }
                    print("\n<<<<<<<<<<---------->>>>>>>>>>\n")
                    print("response headers:\n")
                    dataResponse.response?.allHeaderFields.forEach { key, value in
                        print("\t", key, value)
                    }

                    print("\nresponse body:\n\n\t\(dataResponse.result.value ?? "")\n")

                    let ms = round(Date().timeIntervalSince(start) * 1000)
                    print("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< 耗时: \(ms) ms \n\n")
                    if (dataResponse.result.isSuccess && dataResponse.result.value != nil) {
                        let responseModel: T? = CXJsonUtil.parse(T.self, withJSONObject: dataResponse.result.value)
                        print("\nresponse success:\n")
                        success(responseModel)
                    } else {
                        let failureMessage = dataResponse.result.error?.localizedDescription.lowercased() ?? "网络错误"
                        print("\nresponse failure:\n\n\t\(failureMessage)\n")
                        failure(failureMessage)
                    }
                }
    }

    /*private static let SERVER_RESPONSE_STATUS_SUCCESS = 0; //成功
    private static let SERVER_RESPONSE_STATUS_UN_LOGIN = 1; //未登录

    private static var _baseUrl: String = "http://10.47.58.14:8080/background"
    static var baseUrl: String {
        get {
            return _baseUrl
        }

        set {
            _baseUrl = newValue
        }
    }
    private static var baseParameters: [String: Any] = [
    ]

    static func requestByFullURL<T: Codable>(url: String, requestData: Parameters? = [:], success: @escaping (_ responseModel: BaseResponseModel<T>?) -> Void, failure: @escaping (_ message: String?) -> Void) {

        baseParameters["data"] = requestData

        let start = Date()
        Alamofire.request(url, method: .post, parameters: baseParameters, encoding: JSONEncoding.default, headers: headers)
                //.validate(statusCode: 200..<300)
                .responseJSON { dataResponse in
                    print("\n\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
                    print("request url: ", dataResponse.request?.url ?? "")
                    print("\nrequest headers:\n")
                    dataResponse.request?.allHTTPHeaderFields?.forEach { key, value in
                        print("\t", key, value)
                    }
                    print("\nrequest body:\n")
                    print("\t", baseParameters)
                    print("\n<<<<<<<<<<---------->>>>>>>>>>\n")
                    print("response headers:\n")
                    dataResponse.response?.allHeaderFields.forEach { key, value in
                        print("\t", key, value)
                    }
                    let responseModel: BaseResponseModel<T>? = CXJsonUtil.parse(BaseResponseModel<T>.self, withJSONObject: dataResponse.result.value)

                    print("\nresponse body:\n")
                    print(responseModel?.toString())

                    let ms = round(Date().timeIntervalSince(start) * 1000)
                    print("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< 耗时: \(ms) ms \n\n")
                    if (dataResponse.result.isSuccess) {
                        if (responseModel?.status == SERVER_RESPONSE_STATUS_SUCCESS) {
                            success(responseModel)
                        } else if (responseModel?.status == SERVER_RESPONSE_STATUS_UN_LOGIN) {
                            failure(responseModel?.message ?? "未登录")
                            //强制注销
                            //跳转登录
                        } else {
                            failure(responseModel?.message ?? "网络错误")
                        }
                    } else {
                        failure(responseModel?.message ?? "网络错误")
                    }
                }
    }

    static func request<T: Codable>(path: String, requestData: Parameters? = [:], success: @escaping (_ responseModel: BaseResponseModel<T>?) -> Void, failure: @escaping (_ message: String?) -> Void) {
        requestByFullURL(url: requestData, requestData: requestData, success: success, failure: failure)
    }
*/
}
