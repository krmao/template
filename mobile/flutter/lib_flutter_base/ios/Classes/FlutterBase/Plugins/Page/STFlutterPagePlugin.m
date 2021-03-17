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
        UIViewController *vc = [self currentViewController];
        
        UIViewController *preVc = [vc.navigationController.viewControllers objectAtIndex:vc.navigationController.viewControllers.count -1];
        if ([preVc isKindOfClass:[STFlutterMultipleViewController class]]){
            ((STFlutterMultipleViewController*) preVc).onViewControllerResult(arguments[@"argumentsJsonString"]);
        }
        [vc.navigationController popViewControllerAnimated:YES];
    }else if ([functionName isEqualToString:@"getCurrentPageInitArguments"]) {
        UIViewController *vc = [self currentViewController];
        result([STFlutterPagePlugin getCurrentPageInitArguments:vc]);
    }else {
        result(FlutterMethodNotImplemented);
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
