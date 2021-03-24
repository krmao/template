#import <Foundation/Foundation.h>
#import "STFlutterPlugin.h"

NS_ASSUME_NONNULL_BEGIN

@interface STFlutterPagePlugin : STFlutterPlugin

+ (void)popPage:(UIViewController * _Nullable) currentViewController argumentsJsonString:(NSString * _Nullable)argumentsJsonString;

+ (NSString *)getUniqueId:(UIViewController *) viewController;

+ (NSString *)getCurrentPageInitArguments:(UIViewController *) viewController;
    
@end

NS_ASSUME_NONNULL_END
