#import "CodesDancingPlugin.h"
#import "FlutterBoostPlugin.h"

@interface CodesDancingPlugin()
@end

@implementation CodesDancingPlugin

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    [FlutterBoostPlugin registerWithRegistrar:registrar];
}
@end
