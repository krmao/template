#import <Foundation/Foundation.h>
#import "STCommunication.h"
#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface STCommunicationPlugin : NSObject

+ (void)getLocationInfoWithJsonStr:(NSString *)jsonStr callBackId:(nullable NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback;

+ (void)showToastWithJsonStr:(NSString *)jsonStr callBackId:(nullable NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback;

+ (void)getWithJsonStr:(NSString *)jsonStr callBackId:(nullable NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback;

+ (void)closeWithJsonStr:(NSString *)jsonStr callBackId:(nullable NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback;

+ (void)putWithJsonStr:(NSString *)jsonStr callBackId:(nullable NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback;

+ (void)getDeviceInfoWithJsonStr:(NSString *)jsonStr callBackId:(nullable NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback;

+ (void)getUserInfoWithJsonStr:(NSString *)jsonStr callBackId:(nullable NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback;

+ (void)openWithJsonStr:(NSString *)jsonStr callBackId:(nullable NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback;



@end

NS_ASSUME_NONNULL_END
