#import "STFlutterEventPlugin.h"
#import "STValueUtil.h"
#import "STEventManager.h"
#import "LibFlutterBaseMultiplePlugin.h"
#import "STFlutterMultipleViewController.h"

@implementation STFlutterEventPlugin

- (void)callFunction:(UIViewController *)currentViewController
        functionName:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    NSDictionary *parameters = arguments;
    
    if ([functionName isEqualToString:@"addEventListener"]) {
        NSString * eventId = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"eventId"]];
        NSString * eventKey = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"eventKey"]];

        [[STEventManager sharedInstance] registerListener:eventId eventName:eventKey callback:^(NSDictionary * _Nullable data) {
            if (result) {
                NSMutableDictionary *resultInfo = [NSMutableDictionary dictionary];
               
                NSDictionary *eventInfo = [NSMutableDictionary dictionaryWithDictionary:data];
                if (eventId && eventId.length > 0) {
                    [eventInfo setValue:eventId forKey:@"eventId"];
                }
                if (eventKey && eventKey.length > 0){
                    [eventInfo setValue:eventKey forKey:@"eventKey"];
                }
                
                [resultInfo setValue:eventKey forKey:@"eventKey"];
                [resultInfo setValue:eventInfo forKey:@"eventInfo"];
                
                if ([currentViewController isKindOfClass:[STFlutterMultipleViewController class]]) {
                    STFlutterMultipleViewController *flutterViewController = (STFlutterMultipleViewController *)currentViewController;
                    FlutterMethodChannel * methodChannel = [flutterViewController getBridgePlugin].methodChannel;
                    if(methodChannel){
                        [methodChannel invokeMethod:@"__codesdancing_flutter_event__" arguments:resultInfo];
                    }else{
                        NSLog(@"callFunction methodChannel == nil");
                    }
                }else{
                    NSLog(@"callFunction currentViewController != STFlutterMultipleViewController");
                }
            }
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
