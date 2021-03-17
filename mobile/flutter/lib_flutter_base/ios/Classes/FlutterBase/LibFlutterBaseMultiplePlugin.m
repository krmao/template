#import "LibFlutterBaseMultiplePlugin.h"
#import "FlutterBoostPlugin.h"

@interface LibFlutterBaseMultiplePlugin()
@end

@implementation LibFlutterBaseMultiplePlugin

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    NSLog(@"LibFlutterBaseMultiplePlugin registerWithRegistrar");
    [STFlutterBridge registerWithRegistrar:registrar];
}
@end
