//
//  STNetworkUtil.m
//  Template
//
//  Created by krmao on 2020/12/25.
//  Copyright © 2020 smart. All rights reserved.
//

#import "STNetworkUtil.h"

#import "STNetworkUtil.h"
#import <CoreTelephony/CTTelephonyNetworkInfo.h>
#import <CoreTelephony/CTCarrier.h>
#import <UIKit/UIKit.h>
#import "AFNetworkReachabilityManager.h"
#include <ifaddrs.h>
#include <arpa/inet.h>
#import <resolv.h>
#import <netdb.h>
#include <arpa/inet.h>

@interface STNetworkUtil() {
}

@property (nonatomic, strong) CTTelephonyNetworkInfo *telephonyInfo;
@property (nonatomic, assign) STNetworkType innerNetworkType;
@property (nonatomic, strong) AFNetworkReachabilityManager *reachabilityManager;

@end

static STNetworkUtil *instance = nil;

@implementation STNetworkUtil

+ (STNetworkUtil *)sharedInstance {
    if (instance == NULL) {
        static dispatch_once_t onceToken;
        dispatch_once(&onceToken, ^{
            instance = [[STNetworkUtil alloc] init];
        });
    }
    return instance;
}

- (id)init {
    if (self = [super init]) {
        _telephonyInfo = [[CTTelephonyNetworkInfo alloc] init];
        _reachabilityManager = [AFNetworkReachabilityManager sharedManager];
        __weak STNetworkUtil *weakSelf = self;
        [_reachabilityManager setReachabilityStatusChangeBlock:^(AFNetworkReachabilityStatus status) {
            __strong __typeof(&*self)strongSelf = weakSelf;
            [strongSelf networkChangedActionRA];
        }];
        [_reachabilityManager startMonitoring];
        _innerNetworkType = [self networkType];
    }
    return self;
}

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)networkChangedActionRA {
    STNetworkType tmpNetworkType = [self networkType];
    if (self.innerNetworkType != tmpNetworkType) {
        self.innerNetworkType = tmpNetworkType;
        [self performSelectorOnMainThread:@selector(notifyNetworkChanged) withObject:NULL waitUntilDone:NO];
    }
}

- (void)notifyNetworkChanged{
    [[NSNotificationCenter defaultCenter] postNotificationName:KEY_NETWORK_DID_CHANGED_NOTIFICATION object:NULL userInfo:NULL];
}

- (STNetworkType)networkType {
    STNetworkType networkType = NetworkType_None;
    AFNetworkReachabilityStatus status = [self.reachabilityManager networkReachabilityStatus];
    
    if (status == AFNetworkReachabilityStatusReachableViaWiFi) {
        networkType = NetworkType_WIFI;
    }
    else if (status == AFNetworkReachabilityStatusReachableViaWWAN) {
        BOOL isFast = [self isFast:self.telephonyInfo.currentRadioAccessTechnology];
        if (isFast) {
            if ([self.telephonyInfo.currentRadioAccessTechnology isEqualToString:CTRadioAccessTechnologyLTE]) {
                networkType = NetworkType_4G;
            }else {
                if (@available(iOS 14.0, *)){
                    if([self.telephonyInfo.currentRadioAccessTechnology isEqualToString:CTRadioAccessTechnologyNRNSA] ||
                       [self.telephonyInfo.currentRadioAccessTechnology isEqualToString:CTRadioAccessTechnologyNR] ) {
                        networkType = NetworkType_5G;
                    }else{
                        networkType = NetworkType_3G;
                    }
                }else{
                    networkType = NetworkType_3G;
                }
            }
        } else {
            networkType = NetworkType_2G;
        }
    }
    else if (status == AFNetworkReachabilityStatusUnknown) {
        networkType = NetworkType_Unknown;
        NSString *ipAddress = [STNetworkUtil getWIFIIPAddress]; //获取wifi端口的ip地址，取到则认为是wifi网络
        if (ipAddress.length > 0 && ![ipAddress isEqualToString:@"error"] && [self needUseIpAddress]) {
            networkType = NetworkType_WIFI;
        }
    }
    else if (status == AFNetworkReachabilityStatusNotReachable) {
        networkType = NetworkType_None;
    }
    return networkType;
}

- (BOOL)needUseIpAddress{
    return YES;
}

- (NSString *)networkTypeInfo
{
    NSString *ret = @"None";
    STNetworkType netType = [self networkType];
    switch (netType ) {
        case NetworkType_Unknown:
            ret = @"Unknown";
            break;
        case NetworkType_None:
            ret = @"None";
            break;
        case NetworkType_WIFI:
            ret = @"WIFI";
            break;
        case NetworkType_2G:
            ret = @"2G";
            break;
        case NetworkType_3G:
            ret = @"3G";
            break;
        case NetworkType_4G:
            ret = @"4G";
            break;
        case NetworkType_5G:
            ret = @"5G";
            break;
        default:
            break;
    }
    return ret;
}

- (BOOL)isNetworkAvailable {
    BOOL isReachable = [self.reachabilityManager isReachable];
    return isReachable || (self.networkType != NetworkType_None);
}

- (NSString *)carrierName {
    NSString *provider;
    CTCarrier *carrier = self.telephonyInfo.subscriberCellularProvider;
    NSString *providerCode = [carrier mobileNetworkCode];
    if (providerCode == nil) {
        provider = @"无运营商信息";
    }
    else {
        provider = carrier.carrierName;
    }
    return provider;
}

- (BOOL)isFast:(NSString*)radioAccessTechnology {
    if ([radioAccessTechnology isEqualToString:CTRadioAccessTechnologyGPRS]) {
        return NO;
    } else if ([radioAccessTechnology isEqualToString:CTRadioAccessTechnologyEdge]) {
        return NO;
    } else if ([radioAccessTechnology isEqualToString:CTRadioAccessTechnologyWCDMA]) {
        return YES;
    } else if ([radioAccessTechnology isEqualToString:CTRadioAccessTechnologyHSDPA]) {
        return YES;
    } else if ([radioAccessTechnology isEqualToString:CTRadioAccessTechnologyHSUPA]) {
        return YES;
    } else if ([radioAccessTechnology isEqualToString:CTRadioAccessTechnologyCDMA1x]) {
        return YES;
    } else if ([radioAccessTechnology isEqualToString:CTRadioAccessTechnologyCDMAEVDORev0]) {
        return YES;
    } else if ([radioAccessTechnology isEqualToString:CTRadioAccessTechnologyCDMAEVDORevA]) {
        return YES;
    } else if ([radioAccessTechnology isEqualToString:CTRadioAccessTechnologyCDMAEVDORevB]) {
        return YES;
    } else if ([radioAccessTechnology isEqualToString:CTRadioAccessTechnologyeHRPD]) {
        return YES;
    } else if ([radioAccessTechnology isEqualToString:CTRadioAccessTechnologyLTE]) {
        return YES;
    }
    return YES;
}

+ (NSString *)getWIFIIPAddress
{
    NSString *address = @"error";
    struct ifaddrs *interfaces = NULL;
    struct ifaddrs *temp_addr = NULL;
    int success = 0;
    
    // retrieve the current interfaces - returns 0 on success
    success = getifaddrs(&interfaces);
    if (success == 0) {
        // Loop through linked list of interfaces
        temp_addr = interfaces;
        while (temp_addr != NULL) {
            if( temp_addr->ifa_addr->sa_family == AF_INET) {
                // Check if interface is en0 which is the wifi connection on the iPhone
                if ([[NSString stringWithUTF8String:temp_addr->ifa_name] isEqualToString:@"en0"]) {
                    // Get NSString from C String
                    char ip[INET_ADDRSTRLEN] = {0};
                    address = [NSString stringWithUTF8String:inet_ntop(AF_INET, &(((struct sockaddr_in *)(temp_addr->ifa_addr))->sin_addr), ip, INET_ADDRSTRLEN)];
                }
            }else if(temp_addr->ifa_addr->sa_family == AF_INET6){
                if ([[NSString stringWithUTF8String:temp_addr->ifa_name] isEqualToString:@"en0"]) {
                    char ip[INET6_ADDRSTRLEN] = {0};
                    address = [NSString stringWithUTF8String:inet_ntop(AF_INET6, &(((struct sockaddr_in6 *)(temp_addr->ifa_addr)) -> sin6_addr), ip, INET6_ADDRSTRLEN)];
                }
            }
            temp_addr = temp_addr->ifa_next;
        }
    }
    
    // Free memory
    freeifaddrs(interfaces);
    return address;
}

+ (NSString *)lookupHostName:(NSString *)hostName {
    NSString *resolvedIP = nil;
    
    CFHostRef hostRef = CFHostCreateWithName(kCFAllocatorDefault, (__bridge CFStringRef)hostName);
    Boolean lookup = CFHostStartInfoResolution(hostRef, kCFHostAddresses, NULL);
    NSArray *addresses = (__bridge NSArray*)CFHostGetAddressing(hostRef, &lookup);
    
    if (addresses.count > 0) {
        resolvedIP = [NSString stringWithUTF8String:inet_ntoa(*((__bridge struct in_addr *)[addresses objectAtIndex:0]))];
    }
    if (hostRef) {
        CFRelease(hostRef);
    }
    return resolvedIP;
}

// 获取运营商类型
+ (STNetworkCarrierType)getCarrierType{
    CTTelephonyNetworkInfo *info = [[CTTelephonyNetworkInfo alloc] init];
    __block CTCarrier *avalibleCarrier;
    if (@available(iOS 12.0, *)) {
        if (@available(iOS 13.0, *)) {
            avalibleCarrier = info.serviceSubscriberCellularProviders[info.dataServiceIdentifier?:@""];
        }
        if (!avalibleCarrier) {
            [info.serviceSubscriberCellularProviders enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, CTCarrier * _Nonnull obj, BOOL * _Nonnull stop) {
                if ([obj.mobileCountryCode length] > 0) {
                    avalibleCarrier = obj;
                    *stop = YES;
                }
            }];
        }
        
    }
    
    if(!avalibleCarrier) {
        avalibleCarrier = info.subscriberCellularProvider;
    }

    NSString *currentCountryCode = [avalibleCarrier mobileCountryCode];
    NSString *mobileNetWorkCode = [avalibleCarrier mobileNetworkCode];
    if ([currentCountryCode isEqualToString:@"460"]) {
        // 参考 https://en.wikipedia.org/wiki/Mobile_country_code
        if ([mobileNetWorkCode isEqualToString:@"00"] ||
            [mobileNetWorkCode isEqualToString:@"02"] ||
            [mobileNetWorkCode isEqualToString:@"07"] ||
            [mobileNetWorkCode isEqualToString:@"08"]) {
            // 中国移动
            return NetworkCarrierTypeChinaMobile;
        }
        if ([mobileNetWorkCode isEqualToString:@"01"] ||
            [mobileNetWorkCode isEqualToString:@"06"] ||
            [mobileNetWorkCode isEqualToString:@"09"]) {
            // 中国联通
            return NetworkCarrierTypeChinaUnicom;
        }
        if ([mobileNetWorkCode isEqualToString:@"03"] ||
             [mobileNetWorkCode isEqualToString:@"05"] ||
             [mobileNetWorkCode isEqualToString:@"11"]) {
            // 中国电信
            return NetworkCarrierTypeTelecom;
        }
    }
    return NetworkCarrierTypeUnknown;
}

@end
