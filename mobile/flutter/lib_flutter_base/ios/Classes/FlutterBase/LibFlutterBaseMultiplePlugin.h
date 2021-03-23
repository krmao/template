#import <Flutter/Flutter.h>
#import <Foundation/Foundation.h>
#import <Flutter/Flutter.h>

NS_ASSUME_NONNULL_BEGIN
@interface LibFlutterBaseMultiplePlugin : NSObject<FlutterPlugin>

@property (nonatomic,strong) FlutterMethodChannel *methodChannel;

+ (void)sendEventToDart:(LibFlutterBaseMultiplePlugin *)bridgePlugin eventKey:(NSString*) eventKey eventInfo:(NSDictionary*) eventInfo;

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar;

+ (LibFlutterBaseMultiplePlugin* )getPlugin:(FlutterEngine*)engine;

- (void)setCurrentViewController:(UIViewController *_Nullable)currentViewController;
- (UIViewController *)currentViewController;

@end
NS_ASSUME_NONNULL_END
