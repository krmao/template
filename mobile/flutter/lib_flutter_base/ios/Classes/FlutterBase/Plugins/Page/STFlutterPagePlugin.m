#import "STFlutterPagePlugin.h"
#import "STThreadUtil.h"
#import "STFlutterMultipleViewController.h"

@implementation STFlutterPagePlugin

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    NSLog(@"[page]-[STFlutterPagePlugin] functionName=%@, arguments=%@", functionName, arguments);
    
    NSDictionary *parameters = arguments;
    
    if ([functionName isEqualToString:@"enableExitWithDoubleBackPressed"]) {
        [STThreadUtil runInMainThread:^{
           UIViewController *vc = [self currentViewController];
           if ([vc isKindOfClass:[STFlutterMultipleViewController class]]) {
               BOOL enable = [parameters valueForKey:@"enable"];
               vc.navigationController.interactivePopGestureRecognizer.enabled = enable;
           }
        }];
    }else if ([functionName isEqualToString:@"genUniqueId"]) {
        UIViewController *vc = [self currentViewController];
        result([STFlutterPagePlugin getUniqueId:vc]);
    }else if ([functionName isEqualToString:@"popPage"]) {
        [STFlutterPagePlugin popPage:[self currentViewController] argumentsJsonString:arguments[@"argumentsJsonString"]];
    }else if ([functionName isEqualToString:@"getCurrentPageInitArguments"]) {
        UIViewController *vc = [self currentViewController];
        result([STFlutterPagePlugin getCurrentPageInitArguments:vc]);
    }else {
        result(FlutterMethodNotImplemented);
    }
}

/**
 * Never quit an iOS application programmatically because people tend to interpret this as a crash. https://stackoverflow.com/a/8197043/4348530
 * Not support present viewController.
 */
+ (void)popPage:(UIViewController * _Nullable) currentViewController argumentsJsonString:(NSString * _Nullable)argumentsJsonString{
    UINavigationController *currentNavigationController = [currentViewController isKindOfClass:[UINavigationController class]] ? ((UINavigationController *)currentViewController) : currentViewController.navigationController;
    
    UIViewController *preViewController = (currentNavigationController.viewControllers.count>=2)? [currentNavigationController.viewControllers objectAtIndex:currentNavigationController.viewControllers.count -2] : nil;
    
    NSLog(@"[page]-[STFlutterPagePlugin] popPage viewControllers.count=%lu, viewControllers=%@", (unsigned long)currentNavigationController.viewControllers.count, currentNavigationController.viewControllers);
    NSLog(@"[page]-[STFlutterPagePlugin] popPage currentViewController=%@, argumentsJsonString=%@", currentViewController, argumentsJsonString);
    NSLog(@"[page]-[STFlutterPagePlugin] popPage currentNavigationController=%@, preViewController=%@", currentNavigationController, preViewController);
    
     [currentNavigationController popViewControllerAnimated:YES];
                   
    if (preViewController != nil && ![preViewController isKindOfClass:[NSNull class]] &&
        [preViewController isKindOfClass:[NSClassFromString(@"STFlutterMultipleViewController") class]]){
        
        // 可以通过改变队列来改变线程
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.0 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                   // 需要延迟执行的代码
            [((STFlutterMultipleViewController*) preViewController) onViewControllerResult:argumentsJsonString];
        });
    }
}

+ (NSString *)getUniqueId:(UIViewController *) viewController{
    if ([viewController isKindOfClass:[STFlutterMultipleViewController class]]) {
        NSString * uniqueId =((STFlutterMultipleViewController*) viewController).uniqueId;
        return uniqueId;
    }else{
        return nil;
    }
}

+ (NSString *)getCurrentPageInitArguments:(UIViewController *) viewController{
    if ([viewController isKindOfClass:[STFlutterMultipleViewController class]]) {
        NSString * argumentsJsonString =((STFlutterMultipleViewController*) viewController).argumentsJsonString;
        return argumentsJsonString;
    }else{
        return nil;
    }
}
@end
