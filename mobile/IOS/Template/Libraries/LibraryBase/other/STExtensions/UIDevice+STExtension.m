#import "UIDevice+STExtension.h"

#import <sys/utsname.h>
#include <sys/socket.h>
#include <sys/sysctl.h>
#include <net/if.h>
#include <net/if_dl.h>
#import <AdSupport/AdSupport.h>
#import "NSString+STExtension.h"
#import <LocalAuthentication/LAContext.h>
#include <mach-o/dyld.h>
#import "STMacros.h"

const char *jailbreak_tool_pathes[] = {
        "/Applications/Cydia.app",
        "/Library/MobileSubstrate/MobileSubstrate.dylib",
        "/bin/bash",
        "/usr/sbin/sshd",
        "/etc/apt"
};

@implementation UIDevice (STExtension)

static NSString *CurrentMachineString = nil;

- (NSString *)modelDetails {
    if (CurrentMachineString == nil) {
        // allocate memory
        size_t len = 0;
        sysctlbyname("hw.machine", NULL, &len, NULL, 0);
        char *machine = malloc(len);

        // read machine properties
        sysctlbyname("hw.machine", machine, &len, NULL, 0);

        // return the machine properties
        CurrentMachineString = [NSString stringWithCString:machine encoding:NSUTF8StringEncoding];

        // free memory
        free(machine);
    }

    return CurrentMachineString;
}

@end


#pragma mark - CTDevice

@implementation CTDevice

+ (BOOL)isJailBreak {
    static NSInteger isJailBreakInternal = 0;

    if (isJailBreakInternal > 0) {
        return isJailBreakInternal == 1;
    }

    const uint32_t imageCount = _dyld_image_count();

    for (uint32_t iImg = 0; iImg < imageCount; iImg++) {
        const char *name = _dyld_get_image_name(iImg);

        if (strstr(name, "MobileSubstrate") != NULL) {
            isJailBreakInternal = 1;
            return YES;
        }
    }

    isJailBreakInternal = 2;
    return NO;
}

#pragma mark 带冒号的mac地址

+ (NSString *)macaddress {
    int mib[6];
    size_t len;
    char *buf;
    unsigned char *ptr;
    struct if_msghdr *ifm;
    struct sockaddr_dl *sdl;

    mib[0] = CTL_NET;
    mib[1] = AF_ROUTE;
    mib[2] = 0;
    mib[3] = AF_LINK;
    mib[4] = NET_RT_IFLIST;

    if ((mib[5] = if_nametoindex("en0")) == 0) {
        printf("Error: if_nametoindex error\n");
        return NULL;
    }

    if (sysctl(mib, 6, NULL, &len, NULL, 0) < 0) {
        printf("Error: sysctl, take 1\n");
        return NULL;
    }

    if ((buf = (char *) malloc(len)) == NULL) {
        printf("Could not allocate memory. error!\n");
        return NULL;
    }

    if (sysctl(mib, 6, buf, &len, NULL, 0) < 0) {
        printf("Error: sysctl, take 2");
        free(buf);
        return NULL;
    }

    ifm = (struct if_msghdr *) buf;
    sdl = (struct sockaddr_dl *) (ifm + 1);
    ptr = (unsigned char *) LLADDR(sdl);
    NSString *outstring = [NSString stringWithFormat:@"%02X:%02X:%02X:%02X:%02X:%02X",
                                                     *ptr, *(ptr + 1), *(ptr + 2), *(ptr + 3), *(ptr + 4), *(ptr + 5)];
    free(buf);

    return [outstring uppercaseString];
}

#pragma mark 不带冒号的mac地址

+ (NSString *)macaddressWithoutColon {
    return [[CTDevice macaddress] stringByReplacingOccurrencesOfString:@":" withString:@""];
}

#pragma mark 判断是否iphone5

+ (BOOL)isIPad {
    return (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad);
}


#pragma mark 判断是否是Retina设备

+ (BOOL)isDeviceRetina {
    if ([UIScreen instancesRespondToSelector:@selector(scale)]) {
        CGFloat scale = [[UIScreen mainScreen] scale];
        if (scale > 1.0) {
            return YES;
        }
    }
    return NO;
}

#pragma mark 判断是否模拟器

+ (BOOL)isSimulator {
    return ([[CTDevice deviceString] containsString:@"Simulator"]);
}

#pragma mark 判断当前软件消息开关是否打开

//+ (BOOL)isRemoteNotificationEnable{
//    
//    
//    //http://bugly.qq.com/detail?app=900006494&pid=2&ii=238606#stack
//    //some 8.0.2 device has -[UIApplication currentUserNotificationSettings]: unrecognized selector sent to instance 0x17251c10
//    
//    __block BOOL result = NO;
//    
//    dispatch_block_t block = ^{
//        NSString *osVersion = [[UIDevice currentDevice] systemVersion];
//        
//        if ([osVersion floatValue] >= 8.0) {
//            if ([[UIApplication sharedApplication] respondsToSelector:@selector(currentUserNotificationSettings)]) {
//                UIUserNotificationType types = [[UIApplication sharedApplication] currentUserNotificationSettings].types;
//                result = (types != UIUserNotificationTypeNone);
//            }
//        }else{
//            UIRemoteNotificationType types;
//            types = [[UIApplication sharedApplication] enabledRemoteNotificationTypes];
//            result = (types != UIRemoteNotificationTypeNone);
//        }
//    };
//    
//    if ([NSThread isMainThread]) {
//        block();
//    }else{
//        dispatch_sync(dispatch_get_main_queue(), block);
//    }
//    return result;
//    
//}

#pragma mark 获取版本型号

+ (NSString *)deviceString {
    NSDictionary *deviceDict = @{
            //iphone
            @"iPhone1,1": @"iPhone 1G",
            @"iPhone1,2": @"iPhone 3G",
            @"iPhone2,": @"iPhone 3GS",
            @"iPhone3,": @"iPhone 4",
            @"iPhone4,": @"iPhone 4S",
            @"iPhone5,1": @"iPhone 5",
            @"iPhone5,2": @"iPhone 5",
            @"iPhone5,3": @"iPhone 5c",
            @"iPhone5,4": @"iPhone 5c",
            @"iPhone6,": @"iPhone 5s",
            @"iPhone7,1": @"iPhone 6 Plus",
            @"iPhone7,2": @"iPhone 6",
            @"iPhone8,1": @"iPhone 6s",
            @"iPhone8,2": @"iPhone 6s Plus",
            @"iPhone8,4": @"iPhone SE",
            @"iPhone9,1": @"iPhone 7(CDMA)",
            @"iPhone9,2": @"iPhone 7 Plus(CDMA)",
            @"iPhone9,3": @"iPhone 7(GSM)",
            @"iPhone9,4": @"iPhone 7 Plus(GSM)",
            @"iPhone10,1": @"iPhone 8",
            @"iPhone10,4": @"iPhone 8",
            @"iPhone10,2": @"iPhone 8 Plus",
            @"iPhone10,5": @"iPhone 8 Plus",
            @"iPhone10,3": @"iPhone X",
            @"iPhone10,6": @"iPhone X",
            @"iPhone11,2": @"iPhone XS",
            @"iPhone11,4": @"iPhone XS Max",
            @"iPhone11,6": @"iPhone XS Max",
            @"iPhone11,8": @"iPhone XR",

            //ipod
            @"iPod1,": @"iPod Touch 1G",
            @"iPod2,": @"iPod Touch 2G",
            @"iPod3,": @"iPod Touch 3G",
            @"iPod4,": @"iPod Touch 4G",
            @"iPod5,": @"iPod Touch 5G",
            //ipad
            @"iPad1,": @"iPad 1G",
            @"iPad2,1": @"iPad 2",
            @"iPad2,2": @"iPad 2",
            @"iPad2,3": @"iPad 2",
            @"iPad2,4": @"iPad 2",
            @"iPad3,1": @"iPad 3",
            @"iPad3,2": @"iPad 3",
            @"iPad3,3": @"iPad 3",
            @"iPad3,4": @"iPad 4",
            @"iPad3,5": @"iPad 4",
            @"iPad3,6": @"iPad 4",
            @"iPad4,1": @"iPad Air",
            @"iPad4,2": @"iPad Air",
            @"iPad4,3": @"iPad Air",
            @"iPad5,3": @"iPad Air 2",
            @"iPad5,4": @"iPad Air 2",
            @"iPad6,7": @"iPad Pro 12.9",
            @"iPad6,8": @"iPad Pro 12.9",
            @"iPad6,3": @"iPad Pro 9.7",
            @"iPad6,4": @"iPad Pro 9.7",
            @"iPad6,11": @"iPad 5",
            @"iPad6,12": @"iPad 5",
            @"iPad7,1": @"iPad Pro 12.9 inch 2nd gen",
            @"iPad7,2": @"iPad Pro 12.9 inch 2nd gen",
            @"iPad7,3": @"iPad Pro 10.5",
            @"iPad7,4": @"iPad Pro 10.5",
            @"iPad7,5": @"iPad 6",
            @"iPad7,6": @"iPad 6",

            // iPad mini
            @"iPad2,5": @"iPad mini",
            @"iPad2,6": @"iPad mini",
            @"iPad2,7": @"iPad mini",
            @"iPad4,4": @"iPad mini 2",
            @"iPad4,5": @"iPad mini 2",
            @"iPad4,6": @"iPad mini 2",
            @"iPad4,7": @"iPad mini 3",
            @"iPad4,8": @"iPad mini 3",
            @"iPad4,9": @"iPad mini 3",
            @"iPad5,1": @"iPad mini 4",
            @"iPad5,2": @"iPad mini 4",

            //Simulator
            @"i386": @"Simulator",
            @"x86_64": @"Simulator",

    };

    struct utsname systemInfo;
    uname(&systemInfo);
    NSString *deviceString = [NSString stringWithCString:systemInfo.machine encoding:NSUTF8StringEncoding];

    NSString *deviceName = [deviceDict objectForKey:deviceString];

    if ([deviceName isEqualToString:@"Simulator"]) {
        deviceString = [CTDevice deviceStringForSimulator];
        deviceName = [[deviceDict objectForKey:deviceString] stringByAppendingString:@"_Simulator"];
    }

    if ([deviceName length] > 0) {
        return deviceName;
    } else {
        deviceString = [deviceString subStringToIndex:deviceString.length - 1];
        deviceName = [deviceDict objectForKey:deviceString];
        if ([deviceName length] > 0) {
            return deviceName;
        }
    }

    if ([self isIphoneX]) {
        return @"iPhone X";
    }

    SLog(@"NOTE: Unknown device type: %@", deviceString);
    //如果字典中没有，原样返回
    return deviceString;
}

+ (NSString *)deviceStringForSimulator {
    return [[[NSProcessInfo processInfo] environment] valueForKey:@"SIMULATOR_MODEL_IDENTIFIER"];
}


#pragma mark - 自己生成UDID

+ (NSString *)uniqueIdentifier {
    return [CTDevice uniqueIdentifierWithMacAndBundleId];
}

#pragma mark 根据Mac地址和程序id加密取唯一串

+ (NSString *)uniqueIdentifierWithMacAndBundleId {
    NSString *macaddress = [CTDevice macaddress];
    NSString *bundleIdentifier = [[NSBundle mainBundle] bundleIdentifier];

    NSString *stringToHash = [NSString stringWithFormat:@"%@%@", macaddress, bundleIdentifier];
    NSString *uniqueIdentifier = [stringToHash MD5];

    return uniqueIdentifier;
}

#pragma mark 根据Mac地址加密取唯一串

+ (NSString *)uniqueIdentifierWithMac {
    NSString *macaddress = [CTDevice macaddress];
    NSString *uniqueIdentifier = [macaddress MD5];

    return uniqueIdentifier;
}

//+ (NSString *)getCombinationUUID
//{
//    //UUID|MAC|手机型号|厂商|操作系统版本|网络类型(3G,2G,Wifi)|IDFA
//    //uuid_
//    NSString *uuid = @"";
//    //Mac
//    NSString *macAddress = [CTDevice macaddressWithoutColon];//注意:此macAddress需要去掉冒号.使用[CTDevice macaddressWithoutColon]
//    //手机型号
//    NSString *deviceString = [CTDevice deviceString];
//    //厂商
//    NSString *vendor = @"Apple";
//    //操作系统的版本
//    NSString *systemVersion = [UIDevice currentDevice].systemVersion;
//    //网络类型
//    NSString *netName = [CTNetworkUtil sharedInstance].networkTypeInfo;
//
//    //IDFA
//    NSString *idfaString = @"";
//
//    idfaString = [CTDevice idfa];
//    if(idfaString.length <= 0){
//        idfaString = @"";
//    }
//    NSString *retStr = [NSString stringWithFormat:@"%@|%@|%@|%@|%@|%@|%@",uuid,macAddress,deviceString,vendor,systemVersion,netName,idfaString];
//
//    return retStr;
//}

//smart IDFA，通过 open IDFA 生成，解决 iOS10 取不到 IDFA 的问题。
+ (NSString *)idfa {
    NSString *idfa = [[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString];
    if (![self isValidIdfaString:idfa]) {
        NSString *idfv = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
        if (idfv == nil) {
            idfv = [[NSUUID UUID] UUIDString];
            idfa = [@"guid_" stringByAppendingString:idfv];
        } else {
            idfa = [@"idfv_" stringByAppendingString:idfv];
        }
    }
    return idfa;
}

+ (BOOL)isValidIdfaString:(NSString *)s {
    return [s isKindOfClass:[NSString class]] && (s.length > 0) && (![s hasPrefix:@"0000"]);
}

#pragma mark 根据屏幕尺寸判断是否iphoneX

//+ (BOOL)isIphoneX{
//    return CGSizeEqualToSize(CGSizeMake(1125, 2436), [UIScreen mainScreen].currentMode.size);
//}
+ (BOOL)isIphoneX {
    CGSize screenSize = [UIScreen mainScreen].bounds.size;
    CGFloat screenWidth = MIN(screenSize.width, screenSize.height);
    CGFloat screenHeight = MAX(screenSize.width, screenSize.height);
    if ((screenWidth == 375 && screenHeight == 812 && [UIScreen mainScreen].scale == 3)  // iPhone X / iPhone XS  / iPhone XS MAX的放大模式
            || (screenWidth == 414 && screenHeight == 896 && [UIScreen mainScreen].scale == 3) // iPhone XS MAX
            || (screenWidth == 414 && screenHeight == 896 && [UIScreen mainScreen].scale == 2) // iPhone XR
            || (screenWidth == 375 && screenHeight == 812 && [UIScreen mainScreen].scale == 2) // iPhone XR的放大模式
            ) {
        return YES;
    }
    return NO;
}

+ (BOOL)hasHomeKey {
    static NSInteger hasHomeKey = -1;

    if (hasHomeKey != -1) {
        return (hasHomeKey == 1);
    }

    if (@available(iOS 11.0, *)) {
        LAContext *laContext = [[LAContext alloc] init];
        [laContext canEvaluatePolicy:LAPolicyDeviceOwnerAuthenticationWithBiometrics error:nil];
        LABiometryType bioType = laContext.biometryType;
        if (bioType == LABiometryTypeFaceID) {
            hasHomeKey = 1;
        } else {
            hasHomeKey = 0;
        }
    } else {
        hasHomeKey = 0;
    }

    return (hasHomeKey == 1);

}

@end
