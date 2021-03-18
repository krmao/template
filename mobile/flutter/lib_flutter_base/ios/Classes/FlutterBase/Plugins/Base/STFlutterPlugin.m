#import "STFlutterPlugin.h"
#import "STFlutterPluginManager.h"
#import <Flutter/Flutter.h>
#import "STSystemUtil.h"

@implementation STFlutterPlugin

+ (void)callModule:(NSString *)moduleName
          function:(NSString *)functionName
         arguments:(id)arguments
            result:(FlutterResult)result {
    NSLog(@"[page]-[STFlutterPlugin] callModule functionName=%@, arguments=%@", functionName, arguments);
    NSString *moduleClassName = [[@"STFlutter" stringByAppendingString:moduleName] stringByAppendingString:@"Plugin"];
    STFlutterPlugin *object = [STFlutterPluginManager pluginObjectForModuleClass:moduleClassName];
    if (object){
        [object callFunction:functionName arguments:arguments result:result];
    }
    else {
        result(FlutterMethodNotImplemented);
    }
}

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result {
    //subclass override.
}

- (UIViewController *)currentViewController{
    UIViewController *currentViewController = [STSystemUtil topViewController];
    return currentViewController;
}

- (UIViewController *)currentFlutterViewController{
    UIViewController *currentViewController = [STSystemUtil topViewController];
    return currentViewController;
}

+ (UINavigationController *)visibleNavigationController {
    UIViewController *controller = [UIApplication sharedApplication].keyWindow.rootViewController;
    while (controller.presentedViewController) {
        controller = controller.presentedViewController;
    }

    if ([controller isKindOfClass:[UITabBarController class]]){
        controller = [(UITabBarController *)controller selectedViewController];
    }
    
    if ([controller isKindOfClass:[UINavigationController class]]) {
        return (UINavigationController*)controller;
    } else if ([controller isKindOfClass:[UIViewController class]]) {
        return controller.navigationController;
    }
    return nil;
}

@end
