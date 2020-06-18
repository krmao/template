#import <Foundation/Foundation.h>

typedef void (^NativeHybirdCallBackBlcok)(id result, NSString *callBackId);

typedef void (^NativeRNCallBackBlcok)(id result);


NS_ASSUME_NONNULL_BEGIN

@interface STCommunication : NSObject

+ (void)callFunction:(NSString *)functionName
             jsonStr:(NSString *)jsonStr callBackId:(nullable NSString *)callBackId
            callback:(NativeHybirdCallBackBlcok)callback;

+ (void)callFunction:(NSString *)functionName
             jsonStr:(NSString *)jsonStr
            callback:(NativeRNCallBackBlcok)callback;

@end

NS_ASSUME_NONNULL_END
