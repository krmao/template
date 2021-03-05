#import "LibFlutterBasePlugin.h"
#import "FlutterBoostPlugin.h"

@interface LibFlutterBasePlugin()
@end

@implementation LibFlutterBasePlugin

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    [FlutterBoostPlugin registerWithRegistrar:registrar];
}
@end
