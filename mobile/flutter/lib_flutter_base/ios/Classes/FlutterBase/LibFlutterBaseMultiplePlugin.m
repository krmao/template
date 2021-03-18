#import "LibFlutterBaseMultiplePlugin.h"
#import "FlutterBoostPlugin.h"

@interface LibFlutterBaseMultiplePlugin()
@end

@implementation LibFlutterBaseMultiplePlugin

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    NSLog(@"LibFlutterBaseMultiplePlugin registerWithRegistrar");
    [STFlutterBridge registerWithRegistrar:registrar];
}

+ (LibFlutterBaseMultiplePlugin* )getPlugin:(FlutterEngine*)engine{
    NSObject *published = [engine valuePublishedByPlugin:@"LibFlutterBaseMultiplePlugin"];
    if ([published isKindOfClass:[LibFlutterBaseMultiplePlugin class]]) {
        LibFlutterBaseMultiplePlugin *plugin = (LibFlutterBaseMultiplePlugin *)published;
        return plugin;
    }
    return nil;
}

+ (void) sendEventToDart:(FlutterEngine*)engine eventKey:(NSString*) eventKey eventInfo:(NSDictionary*) eventInfo {
    NSLog(@"sendEventToDart eventKey=%@, eventInfo=%@", eventKey, eventInfo);

    LibFlutterBaseMultiplePlugin* plugin = [LibFlutterBaseMultiplePlugin getPlugin:engine];
    FlutterMethodChannel * methodChannel = plugin.methodChannel;
    
    NSString * BRIDGE_EVENT_NAME = @"__codesdancing_flutter_event__";
    if (methodChannel != nil) {
        NSMutableDictionary* eventData = NSMutableDictionary.new;
        [eventData setValue:eventKey forKey:@"eventName"];
        [eventData setValue:eventInfo forKey:@"eventInfo"];
  
        NSLog(@"sendEventToDart invokeMethod start BRIDGE_EVENT_NAME=%@",BRIDGE_EVENT_NAME);
        
        [methodChannel invokeMethod:BRIDGE_EVENT_NAME arguments:eventData result:^(id  _Nullable result) {
                    NSLog(@"invokeMethod success result=%@", result);
        }];
    } else {
        NSLog(@"invokeMethod methodChannel == null");
    }
}
@end
