#import "STCommunication.h"
#import <objc/message.h>
#import "STJSONSerialization.h"

@implementation STCommunication

+ (void)callFunction:(NSString *)functionName jsonStr:(NSString *)jsonStr callBackId:(nullable NSString *)callBackId callback:(NativeHybirdCallBackBlcok)callback {
    
    Class cls = NSClassFromString(@"STCommunicationPlugin");
    if (cls != NULL) {
        SEL selector = NSSelectorFromString([NSString stringWithFormat:@"%@WithJsonStr:callBackId:callback:",functionName]);
        
        if (selector != NULL && [cls respondsToSelector:selector]) {
            void (*objcMsgSend)(id,SEL,id,id,id) = (void*)objc_msgSend;
            objcMsgSend(cls,selector,jsonStr,callBackId,callback);
        } else {
#if DEBUG
            
            callback([STJSONSerialization dictionaryToJsonString:@{@"result":[NSString stringWithFormat:@"native 暂不支持 %@",functionName]}],callBackId);
#endif
        }
    }
}

+ (void)callFunction:(NSString *)functionName jsonStr:(NSString *)jsonStr callback:(NativeRNCallBackBlcok)callback {
    
    [STCommunication callFunction:functionName jsonStr:jsonStr callBackId:nil callback:^(id result, NSString *str) {
        if (callback) {
            callback(result);
        } else {
            NSLog(@"无需回调");
        }
    }];
    
}

@end
