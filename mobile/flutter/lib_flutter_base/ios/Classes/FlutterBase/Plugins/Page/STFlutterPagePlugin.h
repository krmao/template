#import <Foundation/Foundation.h>
#import "STFlutterPlugin.h"

NS_ASSUME_NONNULL_BEGIN

@interface STFlutterPagePlugin : STFlutterPlugin

+ (NSString *)getUniqueId:(UIViewController *) viewController;

+ (NSString *)getCurrentPageInitArguments:(UIViewController *) viewController;
    
@end

NS_ASSUME_NONNULL_END
