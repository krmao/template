#import <Flutter/Flutter.h>
#import "STFlutterBridge.h"

@protocol STProtocolViewControllerResult
- (void) onViewControllerResult:(NSString *_Nullable) requestCode resultCode:(NSString *_Nullable) resultCode data:(NSMutableDictionary *_Nullable) data;
@end

NS_ASSUME_NONNULL_BEGIN
@interface LibFlutterBaseMultiplePlugin : STFlutterBridge

+ (instancetype)sharedInstance;

+ (void) sendEventToDart:(FlutterEngine*)engine eventKey:(NSString*) eventKey eventInfo:(NSDictionary*) eventInfo;
+ (void) sendEventToDart2:(FlutterMethodChannel *)methodChannel eventKey:(NSString*) eventKey eventInfo:(NSDictionary*) eventInfo ;
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar;
+ (LibFlutterBaseMultiplePlugin* )getPlugin:(FlutterEngine*)engine;
+ (FlutterMethodChannel *)registerWithRegistrar2:(NSObject<FlutterPluginRegistrar>*)registrar;
@end
NS_ASSUME_NONNULL_END
