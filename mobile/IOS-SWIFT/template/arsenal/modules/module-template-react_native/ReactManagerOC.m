//
// Created by smart on 2017/10/27.
// Copyright (c) 2017 com.smart. All rights reserved.
//

#import "ReactManagerOC.h"
#import "CXReflectUtilOC.h"

#if RCT_DEV && __has_include("RCTDevLoadingView.h")
#import "React/RCTDEVLoadingView.h"
#endif

@implementation ReactManagerOC {

}

+ (void)loadBundle:(RCTBridge *)bridge bundleFullName:(NSString *)bundleFullName {
    [self loadBundle:bridge bundleFullName:bundleFullName callback:nil];
}

+ (void)loadBundle:(RCTBridge *)bridge bundleFullName:(NSString *)bundleFullName callback:(void (^)(BOOL))callback {
    NSLog(@"loadBundle bundleFullName=%@", bundleFullName);
    if (bundleFullName != nil && bundleFullName.length > 0 && bridge != nil) {

        NSURL *bundleURL = [NSBundle.mainBundle URLForResource:bundleFullName withExtension:nil];
        if (bundleURL != nil) {
            bridge.bundleURL = bundleURL;

            __weak RCTCxxBridge *weakSelf = (RCTCxxBridge *) bridge.batchedBridge;
            __block NSData *sourceCode;

            NSLog(@"loadBundle start loadSource");
            [CXReflectUtilOC invokeObjectMethod:weakSelf methodName:@"loadSource" params:@[
                    [^(NSError *error, RCTSource *source) {
                        if (error) {
                            NSLog(@"loadBundle loadSource failure %@", error);
                            [CXReflectUtilOC invokeObjectMethod:weakSelf methodName:@"handleError" params:@[error]];
                        }

                        sourceCode = source.data;
                        RCTCxxBridge *strongSelf = weakSelf;
                        if (sourceCode) {
                            NSLog(@"loadBundle loadSource success");
                            NSLog(@"loadBundle start executeSourceCode");
                            [CXReflectUtilOC invokeObjectMethod:strongSelf methodName:@"executeSourceCode" params:@[sourceCode, @NO]];
                            if (callback) callback(true);
                        } else {
                            if (callback) callback(false);
                        }
                    } copy],
                    [(id) ^(RCTLoadingProgress *progressData) { // http://matrixzk.github.io/blog/20150518/store_blocks_in_NSArray/
                        NSLog(@"loadBundle loadSource progress %d/%d", [progressData.done intValue], [progressData.total intValue]);
#if RCT_DEV && __has_include("RCTDevLoadingView.h")
                        RCTDevLoadingView *loadingView = [weakSelf moduleForClass:[RCTDevLoadingView class]];
                [loadingView updateProgress:progressData];
#endif
                    } copy]
            ]];
        } else {
            NSLog(@"loadBundle failure bundleURL is null");
            if (callback) callback(false);
        }
    } else {
        NSLog(@"loadBundle failure bridge or bundleFullName is null");
        if (callback) callback(false);
    }
}

@end
    

