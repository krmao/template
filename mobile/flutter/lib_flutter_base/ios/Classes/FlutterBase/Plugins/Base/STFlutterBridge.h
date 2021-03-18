#import <Foundation/Foundation.h>
#import <Flutter/Flutter.h>

NS_ASSUME_NONNULL_BEGIN

@interface STFlutterBridge : NSObject<FlutterPlugin>

@property (nonatomic,strong) FlutterMethodChannel *methodChannel;

@end

NS_ASSUME_NONNULL_END
