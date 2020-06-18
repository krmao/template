#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIDevice (STExtension)

- (NSString *)modelDetails;

@end


#pragma mark - CTDevice

@interface CTDevice : NSObject

// 带冒号的mac地址
+ (NSString *)macaddress;

// 不带冒号的mac地址
+ (NSString *)macaddressWithoutColon;

// 是否已越狱
+ (BOOL)isJailBreak;

// 自己生成UDID
+ (NSString *)uniqueIdentifier;

// 根据Mac地址和程序id加密取唯一串
+ (NSString *)uniqueIdentifierWithMacAndBundleId;

// 根据Mac地址加密取唯一串
+ (NSString *)uniqueIdentifierWithMac;

// smartUUID合并串：UUID|MAC|手机型号|厂商|操作系统版本|网络类型(3G,2G,Wifi)|IDFA
//+ (NSString *)getCombinationUUID;

//默认取系统IDFA，取不到时通过 open IDFA 生成。
+ (NSString *)idfa;

// 是否是iPad
+ (BOOL)isIPad;

// 判断是否是Retina设备
+ (BOOL)isDeviceRetina;

// 获取版本型号
+ (NSString *)deviceString;

//判断当前运行环境是否模拟器
+ (BOOL)isSimulator;

//判断当前软件消息开关是否打开
//+ (BOOL)isRemoteNotificationEnable;

//根据屏幕尺寸来判断是否是iphoneX
+ (BOOL)isIphoneX;

//设备是否有home键
+ (BOOL)hasHomeKey;

@end

NS_ASSUME_NONNULL_END
