#import "STFlutterBridge.h"
#import "STFlutterPlugin.h"

@implementation STFlutterBridge

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:@"codesdancing.flutter.bridge/callNative"
                                     binaryMessenger:[registrar messenger]
                                     codec:[FlutterJSONMethodCodec sharedInstance]];
    NSLog(@"STFlutterBridge registerWithRegistrar start");
    STFlutterBridge* instance = [STFlutterBridge sharedInstance];
    instance.methodChannel = channel;
    
    [registrar addMethodCallDelegate:instance channel:channel];
    NSLog(@"STFlutterBridge registerWithRegistrar end");
}


- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    NSArray *array = [call.method componentsSeparatedByString:@"-"];
    NSString *moduleName = [array firstObject];
    NSString *functionName = [call.method stringByReplacingOccurrencesOfString:[NSString stringWithFormat:@"%@-",moduleName] withString:@""];
    NSLog(@"handleMethodCall moduleName=%@, functionName=%@", moduleName, functionName);
    [STFlutterPlugin callModule:moduleName function:functionName arguments:call.arguments result:result];
}

+ (instancetype)sharedInstance
{
    static id _instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _instance = [self.class new];
    });
    
    return _instance;
}
@end
