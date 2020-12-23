#import <Foundation/Foundation.h>
#import <Flutter/Flutter.h>

NS_ASSUME_NONNULL_BEGIN

@interface STFlutterBridge : NSObject<FlutterPlugin>

+ (instancetype)sharedInstance;

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar;

@property (nonatomic,strong) FlutterMethodChannel *methodChannel;

@end

NS_ASSUME_NONNULL_END
