#import <Flutter/Flutter.h>
#import "STFlutterBridge.h"

NS_ASSUME_NONNULL_BEGIN
@interface LibFlutterBaseMultiplePlugin : STFlutterBridge

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar;


@end
NS_ASSUME_NONNULL_END
