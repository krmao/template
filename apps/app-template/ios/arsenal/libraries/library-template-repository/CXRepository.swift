import Foundation
import Alamofire

@objc
public class CXRepository: NSObject {

    static func downloadHybirdAllModuleConfigurations(url: String, timeoutInterval: Double = 60, success: @escaping (_ responseData: MutableList<CXHybirdModuleConfigModel>?) -> Void, failure: @escaping (_ message: String?) -> Void) {
        return CXApiManager.downloadByFullURL(url: url, timeoutInterval: timeoutInterval, success: success, failure: failure)
    }

    static func downloadHybirdModuleConfiguration(url: String, timeoutInterval: Double = 60, success: @escaping (_ responseData: CXHybirdModuleConfigModel?) -> Void, failure: @escaping (_ message: String?) -> Void) {
        return CXApiManager.downloadByFullURL(url: url, timeoutInterval: timeoutInterval, success: success, failure: failure)
    }

    /*static func addGoodsCar<T: Codable>(requestData: AddGoodCarRequestData, success: @escaping (_ responseData: BaseResponseModel<T>?) -> Void, failure: @escaping (_ message: String?) -> Void) {
        return CXApiManager.request(path: "addGoodsCar", requestData: requestData.toDictionary(), success: success, failure: failure)
    }

    static func requestRecommendCategory<T: Codable>(requestData: Parameters? = [:], success: @escaping (_ responseData: BaseResponseModel<T>?) -> Void, failure: @escaping (_ message: String?) -> Void) {
        return CXApiManager.request(path: "getGoodsRecommendCategory", requestData: requestData, success: success, failure: failure)
    }*/

    /*@objc
    public static func getYunFee(callback: @escaping (_ communityId: Int, _ lowLimitMoney: Float, _ benefitMoney: Float) -> Void) {
        return CXApiManager.request(path: "getOrderBalanceDeliveryFee",
                requestData: [:],
                success: { (responseModel: BaseResponseModel<GetYunFeeResponseData>?) in
                    var yunFeeModel: GetYunFeeResponseData = responseModel?.data ?? GetYunFeeResponseData()
                    callback(yunFeeModel.communityId, yunFeeModel.lowLimitMoney, yunFeeModel.benefitMoney)
                },
                failure: { message in
                    print(message)
                    callback(-1, 0, 0)
                }
        )
    }*/
}
