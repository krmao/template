#import "STFlutterToastPlugin.h"
#import "STToastUtil.h"
#import "STValueUtil.h"

@implementation STFlutterToastPlugin

- (void)callFunction:(UIViewController *)currentViewController
        functionName:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    NSDictionary *parameters = arguments;
    
    if ([functionName isEqualToString:@"show"]) {
        NSString * message = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"message"]];
        [STToastUtil show:message];
        result(nil);
    }
    else {
        result(FlutterMethodNotImplemented);
    }
}
@end
