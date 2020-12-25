#import "STFlutterEventPlugin.h"
#import "STFlutterBridge.h"
#import "STFlutterViewController.h"

@implementation STFlutterEventPlugin

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    NSDictionary *parameters = arguments;
    
    if ([functionName isEqualToString:@"addEventListener"]) {
        NSString * eventId = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"eventId"]];
        NSString * eventKey = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"eventKey"]];

        [[STEventManager sharedInstance] registerListener:eventId eventName:eventKey callback:^(NSDictionary * _Nullable data) {
                    
        }];
    }
    else if ([functionName isEqualToString:@"removeEventListener"]) {
        NSString * eventId = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"eventId"]];
        NSString * eventKey = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"eventKey"]];

        if(!eventKey || eventKey.length == 0 ){
            [[STEventManager sharedInstance] unRegisterListener:eventId];
        }else{
            [[STEventManager sharedInstance] unRegisterListener:eventId eventName:eventKey];
        }
    }
    else if ([functionName isEqualToString:@"sendEvent"]){
        NSString * eventName = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"eventName"]];
        NSDictionary * eventInfo = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"eventInfo"]];
        [[STEventManager sharedInstance] sendEvent:eventName eventInfo:eventInfo];
    }
}
@end
