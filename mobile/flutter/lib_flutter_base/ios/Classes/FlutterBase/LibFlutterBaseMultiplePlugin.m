#import "LibFlutterBaseMultiplePlugin.h"
#import "FlutterBoostPlugin.h"
#import "STFlutterPlugin.h"

@interface LibFlutterBaseMultiplePlugin()
@end

@implementation LibFlutterBaseMultiplePlugin

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    NSArray *array = [call.method componentsSeparatedByString:@"-"];
    NSString *moduleName = [array firstObject];
    NSString *functionName = [call.method stringByReplacingOccurrencesOfString:[NSString stringWithFormat:@"%@-",moduleName] withString:@""];
    NSLog(@"handleMethodCall moduleName=%@, functionName=%@, arguments=%@", moduleName, functionName, call.arguments);
    [STFlutterPlugin callModule:moduleName function:functionName arguments:call.arguments result:result];
}

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    NSLog(@"LibFlutterBaseMultiplePlugin registerWithRegistrar start");
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:@"codesdancing.flutter.bridge/callNative"
                                     binaryMessenger:[registrar messenger]
                                     codec:[FlutterJSONMethodCodec sharedInstance]];
    NSLog(@"LibFlutterBaseMultiplePlugin registerWithRegistrar start");
    LibFlutterBaseMultiplePlugin* instance = [LibFlutterBaseMultiplePlugin sharedInstance];
    instance.methodChannel = channel;
    
    [registrar addMethodCallDelegate:instance channel:channel];
    NSLog(@"LibFlutterBaseMultiplePlugin registerWithRegistrar end");
}

+ (LibFlutterBaseMultiplePlugin *)registerWithRegistrar2:(NSObject<FlutterPluginRegistrar>*)registrar {
    NSLog(@"LibFlutterBaseMultiplePlugin registerWithRegistrar start");
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:@"codesdancing.flutter.bridge/callNative"
                                     binaryMessenger:[registrar messenger]
                                     codec:[FlutterJSONMethodCodec sharedInstance]];
    NSLog(@"LibFlutterBaseMultiplePlugin registerWithRegistrar start");
    LibFlutterBaseMultiplePlugin* newPlugin = [LibFlutterBaseMultiplePlugin new]; // 没有必要使用 sharedInstance, 因为下一步 methodChannel 已经被替换, 当前页面回收后, 上一个页面的 methodChannel 将为 nil
    newPlugin.methodChannel = channel;
    
    [registrar addMethodCallDelegate:newPlugin channel:channel];
    NSLog(@"LibFlutterBaseMultiplePlugin registerWithRegistrar end");
    return newPlugin;
}

+ (instancetype)sharedInstance{
    static id _instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _instance = [self.class new];
    });
    return _instance;
}

+ (LibFlutterBaseMultiplePlugin* )getPlugin:(FlutterEngine*)engine{
    BOOL hasPlugin = [engine hasPlugin:@"LibFlutterBaseMultiplePlugin"];
    NSObject *published = [engine valuePublishedByPlugin:@"LibFlutterBaseMultiplePlugin"];
    NSLog(@"getPlugin hasPlugin=%d, engine=%@, published=%@",hasPlugin, engine, published);
    
    if ([published isKindOfClass:[LibFlutterBaseMultiplePlugin class]]) {
        LibFlutterBaseMultiplePlugin *plugin = (LibFlutterBaseMultiplePlugin *)published;
        NSLog(@"getPlugin return plugin=%@",plugin);
        return plugin;
    }
    
    NSLog(@"getPlugin return plugin nil !");
    return nil;
}

+ (void) sendEventToDart:(FlutterEngine*)engine eventKey:(NSString*) eventKey eventInfo:(NSDictionary*) eventInfo {
    NSLog(@"sendEventToDart engine=%@, eventKey=%@, eventInfo=%@", engine, eventKey, eventInfo);

    LibFlutterBaseMultiplePlugin* plugin = [LibFlutterBaseMultiplePlugin getPlugin:engine];
    FlutterMethodChannel * methodChannel = plugin.methodChannel;
    
    NSLog(@"sendEventToDart plugin=%@, methodChannel=%@",plugin, methodChannel);
    
    NSString * BRIDGE_EVENT_NAME = @"__codesdancing_flutter_event__";
    if (methodChannel == nil || [methodChannel isKindOfClass:[NSNull class]]) {
        methodChannel = [LibFlutterBaseMultiplePlugin sharedInstance].methodChannel;
    }
    NSLog(@"sendEventToDart install plugin=%@, methodChannel=%@",[LibFlutterBaseMultiplePlugin sharedInstance], methodChannel);
    
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

+ (void) sendEventToDart2:(LibFlutterBaseMultiplePlugin *)bridgePlugin eventKey:(NSString*) eventKey eventInfo:(NSDictionary*) eventInfo {
    FlutterMethodChannel *methodChannel = bridgePlugin.methodChannel;
    NSLog(@"sendEventToDart2 methodChannel=%@, eventKey=%@, eventInfo=%@", methodChannel, eventKey, eventInfo);
    NSString * BRIDGE_EVENT_NAME = @"__codesdancing_flutter_event__";
    if (methodChannel != nil) {
        NSMutableDictionary* eventData = NSMutableDictionary.new;
        [eventData setValue:eventKey forKey:@"eventName"];
        [eventData setValue:eventInfo forKey:@"eventInfo"];
  
        NSLog(@"sendEventToDart2 invokeMethod start BRIDGE_EVENT_NAME=%@",BRIDGE_EVENT_NAME);
        
        [methodChannel invokeMethod:BRIDGE_EVENT_NAME arguments:eventData result:^(id  _Nullable result) {
                    NSLog(@"invokeMethod2 success result=%@", result);
        }];
    } else {
        NSLog(@"invokeMethod2 methodChannel == null");
    }
}
@end
