//
//  STNetworkUtil.h
//  Template
//
//  Created by krmao on 2020/12/25.
//  Copyright © 2020 smart. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

typedef enum eNetworkType {
    NetworkType_Unknown = -1,
    NetworkType_None = 0,
    NetworkType_WIFI = 1,
    NetworkType_2G = 2,
    NetworkType_3G = 3,
    NetworkType_4G = 4,
    NetworkType_5G = 5
} eNetworkType;

typedef enum eNetworkCarrierType {
    eNetworkCarrierTypeUnknown = 0,
    eNetworkCarrierTypeChinaMobile = 1,
    eNetworkCarrierTypeChinaUnicom = 2,
    eNetworkCarrierTypeTelecom = 3,
} eNetworkCarrierType;

//网络状态变更的时候通知
#define KEY_NETWORK_DID_CHANGED_NOTIFICATION @"KEY_NETWORK_DID_CHANGED_NOTIFICATION"

@interface STNetworkUtil : NSObject

+ (STNetworkUtil *)sharedInstance;

/*当前网络类型，None,WIFI,2G,3G,4G*/
@property (nonatomic, readonly) eNetworkType networkType;

/*当前网络类型描述，None,WIFI,2G,3G,4G*/
@property (nonatomic, readonly) NSString *networkTypeInfo;

/*当前网络是否可用*/
@property (nonatomic, readonly) BOOL isNetworkAvailable;

/*当前运营商*/
@property (nonatomic, readonly) NSString *carrierName;

//获取WIFI地址
+ (NSString *)getWIFIIPAddress;

//DNS从域名获取IP地址（返回DNS查询后的第一个IP地址，仅返回IPv4地址；注意是同步方法，可能耗时较久）
+ (NSString *)lookupHostName:(NSString *)hostName;

//返回运营商信息，只支持移动，联通，电信
+ (eNetworkCarrierType)getCarrierType;
@end

NS_ASSUME_NONNULL_END
