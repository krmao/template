#import "ReactBridge.h"
#import "STRNFileManager.h"

//dev模式下:RCTBridge required dispatch_sync to load RCTDevLoadingView Error Fix
#if RCT_DEV

#import <React/RCTDevLoadingView.h>

#endif

@interface ReactBridge ()
@property(nonatomic, strong) ReactBridgeDelegate *bridgeDelegate;
@end


@implementation ReactBridge

+ (instancetype)sharedManager {
    static ReactBridge *reactBridge;
    static dispatch_once_t onceToken;

    dispatch_once(&onceToken, ^{
        ReactBridgeDelegate *bridgeDelegate = [[ReactBridgeDelegate alloc] init];
        reactBridge = [[ReactBridge alloc] initWithDelegate:bridgeDelegate launchOptions:nil];
        reactBridge.bridgeDelegate = bridgeDelegate;
#if RCT_DEV
        [reactBridge moduleForClass:[RCTDevLoadingView class]];
#endif
    });
    return reactBridge;
}

- (void)setUrl:(STRNURL *)url {
    self.bridgeDelegate.url = url;
    [self.delegate sourceURLForBridge:self];
}


@end

@implementation ReactBridgeDelegate

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge {
    if (!self.url) {
        NSURL *tmpUrl = [NSURL fileURLWithPath:[[STRNFileManager fileNameBaseUnzip] stringByAppendingPathComponent:@"ios/index.ios.bundle"]];
        NSLog(@"sourceURLForBridge tmpUrl = %@", tmpUrl);
        return tmpUrl;
    }

    if (self.url.urlType == STRNURLNetURL) {
        NSLog(@"sourceURLForBridge rnNetURL = %@", self.url.rnNetURL);
        return self.url.rnNetURL;
    } else {
        NSLog(@"sourceURLForBridge rnBundleURL = %@", self.url.rnBundleURL);
        return self.url.rnBundleURL;
    }
}
@end

