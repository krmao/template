#import "LibFlutterBaseBoostPlugin.h"
#import "FlutterBoostPlugin.h"

@interface LibFlutterBaseBoostPlugin()
@end

@implementation LibFlutterBaseBoostPlugin

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    [FlutterBoostPlugin registerWithRegistrar:registrar];
}
@end
