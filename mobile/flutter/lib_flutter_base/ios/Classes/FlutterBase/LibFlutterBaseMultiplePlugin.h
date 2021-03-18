#import <Flutter/Flutter.h>
#import "STFlutterBridge.h"

NS_ASSUME_NONNULL_BEGIN
@interface LibFlutterBaseMultiplePlugin : STFlutterBridge

+ (instancetype)sharedInstance;

+ (void) sendEventToDart:(FlutterEngine*)engine eventKey:(NSString*) eventKey eventInfo:(NSDictionary*) eventInfo;
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar;
+ (LibFlutterBaseMultiplePlugin* )getPlugin:(FlutterEngine*)engine;

@end
NS_ASSUME_NONNULL_END
