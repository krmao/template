#import "STFlutterPagePlugin.h"
#import "STThreadUtil.h"
#import "STFlutterMultipleViewController.h"

@implementation STFlutterPagePlugin

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
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
    
    UIViewController *preViewController = [currentNavigationController.viewControllers objectAtIndex:currentNavigationController.viewControllers.count -1];
    
    if ([preViewController isKindOfClass:[NSClassFromString(@"STFlutterMultipleViewController") class]]){
        ((STFlutterMultipleViewController*) preViewController).onViewControllerResult(argumentsJsonString);
    }
    [currentNavigationController popViewControllerAnimated:YES];
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
