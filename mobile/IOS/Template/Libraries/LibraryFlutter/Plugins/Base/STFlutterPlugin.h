#import <Foundation/Foundation.h>
#import <Flutter/Flutter.h>
#import "STFlutterViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface STFlutterPlugin : NSObject

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result;

- (UIViewController *)currentViewController;
- (UIViewController *)currentFlutterViewController;

+ (void)callModule:(NSString *)moduleName
          function:(NSString *)functionName
         arguments:(id)arguments
            result:(FlutterResult)result;

@end

NS_ASSUME_NONNULL_END
