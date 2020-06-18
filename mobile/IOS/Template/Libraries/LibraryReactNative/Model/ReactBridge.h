#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>
#import "STRNURL.h"

NS_ASSUME_NONNULL_BEGIN

@interface ReactBridge : RCTBridge
@property(nonatomic, strong) STRNURL *url;

+ (instancetype)sharedManager;
@end

@interface ReactBridgeDelegate : NSObject <RCTBridgeDelegate>
@property(nonatomic, strong) STRNURL *url;
@end

NS_ASSUME_NONNULL_END
