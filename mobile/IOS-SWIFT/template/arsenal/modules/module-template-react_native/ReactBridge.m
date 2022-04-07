#import <Foundation/Foundation.h>
#import "React/RCTBridgeModule.h"

@interface RCT_EXTERN_MODULE(NativeManager, NSObject)

RCT_EXTERN_METHOD(
            callNative: (NSString *) data
            callNative2: (NSString *) data2
            resolver: (RCTPromiseResolveBlock) resolve
            rejecter: (RCTPromiseRejectBlock) reject)
@end