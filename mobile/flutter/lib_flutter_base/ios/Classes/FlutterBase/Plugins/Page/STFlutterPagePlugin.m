#import "STFlutterPagePlugin.h"
#import "STThreadUtil.h"
#import "STViewControllerDelegete.h"

@implementation STFlutterPagePlugin

- (void)callFunction:(UIViewController *)currentViewController
        functionName:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    NSLog(@"[page]-[STFlutterPagePlugin] functionName=%@, arguments=%@", functionName, arguments);
    
    if ([functionName isEqualToString:@"genUniqueId"]) {
        UIViewController *vc = currentViewController;
        result([STFlutterPagePlugin getUniqueId:vc]);
    }else if ([functionName isEqualToString:@"popPage"]) {
        [STFlutterPagePlugin popPage:currentViewController argumentsJsonString:arguments[@"argumentsJsonString"]];
    }else if ([functionName isEqualToString:@"getCurrentPageInitArguments"]) {
        UIViewController *vc = currentViewController;
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
    
    int requestCode = 0;
    
    if (currentViewController != nil && ![currentViewController isKindOfClass:[NSNull class]] &&
        [currentViewController conformsToProtocol:@protocol(STViewControllerDelegete)]){
        requestCode = [((id<STViewControllerDelegete>) currentViewController) getRequestCode];
    }
    
    if (preViewController != nil && ![preViewController isKindOfClass:[NSNull class]] &&
        [preViewController conformsToProtocol:@protocol(STViewControllerDelegete)]){
        dispatch_async(dispatch_get_main_queue(), ^{
            NSDictionary *resultData = NSMutableDictionary.new;
            [resultData setValue:argumentsJsonString forKey:@"argumentsJsonString"];
            [((id<STViewControllerDelegete>) preViewController) onViewControllerResult:requestCode resultCode:RESULT_OK resultData:resultData];
        });
    }
    [currentNavigationController popViewControllerAnimated:YES];
}

+ (NSString *)getUniqueId:(UIViewController *) viewController{
    if ([viewController conformsToProtocol:@protocol(STViewControllerDelegete)]) {
        return [((id<STViewControllerDelegete>) viewController) getUniqueId];
    }else{
        return nil;
    }
}

+ (NSString *)getCurrentPageInitArguments:(UIViewController *) viewController{
    if ([viewController conformsToProtocol:@protocol(STViewControllerDelegete)]) {
        NSString * argumentsJsonString =[((id<STViewControllerDelegete>) viewController) getRequestData][@"argumentsJsonString"];
        return argumentsJsonString;
    }else{
        return nil;
    }
}
@end
