#import "STFlutterEventPlugin.h"
#import "STFlutterBridge.h"
#import "STFlutterViewController.h"

@implementation STFlutterEventPlugin

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    NSDictionary *parameters = arguments;
    if ([functionName isEqualToString:@"addEventListener"]) {
        
//        NSString *eventName = parameters[@"eventName"];
//        NSString *sequenceId = parameters[@"sequenceId"];
//        NSString *containerId = parameters[@"containerId"];
    }
    else if ([functionName isEqualToString:@"removeEventListener"]) {
//        NSString *name = parameters[@"eventName"];
//        NSString *containerId = parameters[@"containerId"];
    }
    else if ([functionName isEqualToString:@"sendEvent"]){
//        NSString *name = parameters[@"eventName"];
//        NSDictionary *eventInfo = parameters[@"eventInfo"];
    }
}
@end
