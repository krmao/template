#import "LibFlutterBaseMultiplePlugin.h"
#import "FlutterBoostPlugin.h"
#import "STFlutterPlugin.h"
#import "STSystemUtil.h"

static NSString * BRIDGE_EVENT_NAME = @"__codesdancing_flutter_event__";
@interface LibFlutterBaseMultiplePlugin(){
    UIViewController * currentViewController;
}
@end

@implementation LibFlutterBaseMultiplePlugin

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    NSArray *array = [call.method componentsSeparatedByString:@"-"];
    NSString *moduleName = [array firstObject];
    NSString *functionName = [call.method stringByReplacingOccurrencesOfString:[NSString stringWithFormat:@"%@-",moduleName] withString:@""];
    NSLog(@"[page] handleMethodCall self.viewController=%@, moduleName=%@, functionName=%@, arguments=%@", [self currentViewController], moduleName, functionName, call.arguments);
    
    [STFlutterPlugin callModule:[self currentViewController] moduleName:moduleName function:functionName arguments:call.arguments result:result];
}

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    NSLog(@"[page] LibFlutterBaseMultiplePlugin registerWithRegistrar start");
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:@"codesdancing.flutter.bridge/callNative"
                                     binaryMessenger:[registrar messenger]
                                     codec:[FlutterJSONMethodCodec sharedInstance]];
    LibFlutterBaseMultiplePlugin* newPlugin = [LibFlutterBaseMultiplePlugin new];
    newPlugin.methodChannel = channel;
    
    [registrar publish:newPlugin];
    [registrar addMethodCallDelegate:newPlugin channel:channel];
    NSLog(@"[page] LibFlutterBaseMultiplePlugin registerWithRegistrar end");
}

+ (LibFlutterBaseMultiplePlugin* )getPlugin:(FlutterEngine*)engine{
    BOOL hasPlugin = [engine hasPlugin:@"LibFlutterBaseMultiplePlugin"];
    NSObject *published = [engine valuePublishedByPlugin:@"LibFlutterBaseMultiplePlugin"];
    NSLog(@"[page] getPlugin hasPlugin=%d, engine=%@, published=%@",hasPlugin, engine, published);
    
    if ([published isKindOfClass:[LibFlutterBaseMultiplePlugin class]]) {
        LibFlutterBaseMultiplePlugin *plugin = (LibFlutterBaseMultiplePlugin *)published;
        NSLog(@"[page] getPlugin return plugin=%@",plugin);
        return plugin;
    }
    
    NSLog(@"[page] getPlugin return plugin nil !");
    return nil;
}

+ (void) sendEventToDart:(LibFlutterBaseMultiplePlugin *)bridgePlugin eventKey:(NSString*) eventKey eventInfo:(NSDictionary*) eventInfo {
    FlutterMethodChannel *methodChannel = bridgePlugin.methodChannel;
    NSLog(@"[page] sendEventToDart2 methodChannel=%@, eventKey=%@, eventInfo=%@", methodChannel, eventKey, eventInfo);
    if (methodChannel != nil) {
        NSMutableDictionary* eventData = NSMutableDictionary.new;
        [eventData setValue:eventKey forKey:@"eventName"];
        [eventData setValue:eventInfo forKey:@"eventInfo"];
  
        NSLog(@"[page] sendEventToDart2 invokeMethod start BRIDGE_EVENT_NAME=%@",BRIDGE_EVENT_NAME);
        
        [methodChannel invokeMethod:BRIDGE_EVENT_NAME arguments:eventData result:^(id  _Nullable result) {
            NSLog(@"[page] sendEventToDart2 invokeMethod2 success result=%@", result);
        }];
    } else {
        NSLog(@"[page] sendEventToDart2 invokeMethod2 methodChannel == null");
    }
}

- (void)setCurrentViewController:(UIViewController *_Nullable)currentViewController {
    self->currentViewController = currentViewController;
}

- (UIViewController *)currentViewController{
    return (self->currentViewController == nil || [self->currentViewController isKindOfClass:[NSNull class]] )? [STSystemUtil topViewController]: self->currentViewController;
}
@end
