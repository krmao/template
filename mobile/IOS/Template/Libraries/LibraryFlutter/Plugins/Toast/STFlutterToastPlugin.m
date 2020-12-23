#import "STFlutterToastPlugin.h"
#import "STToastUtil.h"

@implementation STFlutterToastPlugin

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    if ([functionName isEqualToString:@"show"]) {
        NSDictionary *args = arguments;
        // TODO SHOW TOAST
        [STToastUtil show:@"toast plugin"];
        result(nil);
    }
    else {
        result(FlutterMethodNotImplemented);
    }
}
@end
