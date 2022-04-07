#import "STFlutterEventPlugin.h"
#import "LibFlutterBaseMultiplePlugin.h"
#import "STFlutterMultipleViewController.h"

#import <LibIosBase/STInitializer.h>
#import <LibIosBase/STJsonUtil.h>
#import <LibIosBase/STThreadUtil.h>
#import <LibIosBase/STStringUtil.h>
#import <LibIosBase/STSystemUtil.h>
#import <LibIosBase/STToastUtil.h>
#import <LibIosBase/STValueUtil.h>
#import <LibIosBase/STBridgeDefaultCommunication.h>
#import <LibIosBase/STNetworkUtil.h>
#import <LibIosBase/STURLManager.h>
#import <LibIosBase/STEventManager.h>

static NSString *BRIDGE_EVENT_NAME = @"__codesdancing_flutter_event__";

@implementation STFlutterEventPlugin

- (void)callFunction:(UIViewController *)currentViewController
        functionName:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    NSDictionary *parameters = arguments;
    
    if ([functionName isEqualToString:@"addEventListener"]) {
        NSString * eventId = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"eventId"]];
        NSString * eventKey = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"eventKey"]];
        
        [[STEventManager sharedInstance] register:eventId eventKey:eventKey callbackListener:^(NSString * _Nullable eventKey, NSDictionary<NSString *,id> * _Nullable value) {
            [STFlutterEventPlugin sendEventToDart:currentViewController eventKey:eventKey eventInfo:value];
        }];
        
        result(nil);
    } else if ([functionName isEqualToString:@"removeEventListener"]) {
        NSString * eventId = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"eventId"]];
        NSString * eventKey = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"eventKey"]];

        if([STStringUtil emptyOrNull:eventKey] ){
            [[STEventManager sharedInstance] unregisterAll:eventId];
        }else{
            [[STEventManager sharedInstance] unregister:eventId eventKey:eventKey];
        }
        result(nil);
    } else if ([functionName isEqualToString:@"sendEvent"]){
        NSString * eventKey = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"eventKey"]];
        NSDictionary * eventInfo = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"eventInfo"]];
        [[STEventManager sharedInstance] sendEvent:eventKey value:eventInfo];
        result(nil);
    }
}

+ (void)sendEventToDart:(UIViewController *)currentViewController eventKey:(NSString *) eventKey eventInfo:(NSDictionary *) eventInfo{
    NSMutableDictionary *resultInfo = [NSMutableDictionary dictionary];
   
    // NSDictionary *innerEventInfo = [NSMutableDictionary dictionaryWithDictionary:eventInfo];
    // if (eventKey && eventKey.length > 0) [innerEventInfo setValue:eventKey forKey:@"eventKey"];
    
    [resultInfo setValue:eventKey forKey:@"eventKey"];
    [resultInfo setValue:eventInfo forKey:@"eventInfo"];
    
    if ([currentViewController isKindOfClass:[STFlutterMultipleViewController class]]) {
        STFlutterMultipleViewController *flutterViewController = (STFlutterMultipleViewController *)currentViewController;
        FlutterMethodChannel *methodChannel = [flutterViewController getBridgePlugin].methodChannel;
        if(methodChannel){
            [methodChannel invokeMethod:BRIDGE_EVENT_NAME arguments:resultInfo];
        }else{
            NSLog(@"callFunction methodChannel == nil");
        }
    }else{
        NSLog(@"callFunction currentViewController != STFlutterMultipleViewController");
    }
}
@end
