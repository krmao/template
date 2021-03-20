#import <Flutter/Flutter.h>
#import <Foundation/Foundation.h>
#import <Flutter/Flutter.h>

@protocol STProtocolViewControllerResult
- (void) onViewControllerResult:(NSString *_Nullable) requestCode resultCode:(NSString *_Nullable) resultCode data:(NSMutableDictionary *_Nullable) data;
@end

NS_ASSUME_NONNULL_BEGIN
@interface LibFlutterBaseMultiplePlugin : NSObject<FlutterPlugin>

@property (nonatomic,strong) FlutterMethodChannel *methodChannel;

+ (instancetype)sharedInstance;

+ (void) sendEventToDart:(FlutterEngine*)engine eventKey:(NSString*) eventKey eventInfo:(NSDictionary*) eventInfo;
+ (void) sendEventToDart2:(LibFlutterBaseMultiplePlugin *)bridgePlugin eventKey:(NSString*) eventKey eventInfo:(NSDictionary*) eventInfo ;

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar;
+ (LibFlutterBaseMultiplePlugin *)registerWithRegistrar2:(NSObject<FlutterPluginRegistrar>*)registrar;

+ (LibFlutterBaseMultiplePlugin* )getPlugin:(FlutterEngine*)engine;
@end
NS_ASSUME_NONNULL_END
