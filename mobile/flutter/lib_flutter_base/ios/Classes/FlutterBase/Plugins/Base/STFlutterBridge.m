#import "STFlutterBridge.h"
#import "STFlutterPlugin.h"

@implementation STFlutterBridge

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    NSArray *array = [call.method componentsSeparatedByString:@"-"];
    NSString *moduleName = [array firstObject];
    NSString *functionName = [call.method stringByReplacingOccurrencesOfString:[NSString stringWithFormat:@"%@-",moduleName] withString:@""];
    NSLog(@"handleMethodCall moduleName=%@, functionName=%@, arguments=%@", moduleName, functionName, call.arguments);
    [STFlutterPlugin callModule:moduleName function:functionName arguments:call.arguments result:result];
}


@end
