//
//  STFlutterUtils.m
//  lib_flutter_base
//
//  Created by krmao on 2021/3/9.
//

#import "STFlutterMultipleUtils.h"
#import "STFlutterInitializer.h"
#import "STFlutterMultipleInitializer.h"
#import "STFlutterMultipleViewController.h"
#import "STFlutterMultipleHomeViewController.h"
#import "LibFlutterBaseMultiplePlugin.h"
#import <LibIosBase/STInitializer.h>
#import <LibIosBase/STJsonUtil.h>
#import <LibIosBase/STThreadUtil.h>

@implementation STFlutterMultipleUtils

+(void) openNewFlutterViewControllerBySchema:(UIViewController* _Nullable)fromViewController schemaUrl:(NSString* _Nullable) schemaUrl{
    if ([schemaUrl hasPrefix:@"smart://template/flutter"]){
        NSLog(@"openNewFlutterViewControllerBySchema true schemaUrl=%@", schemaUrl);
        NSMutableDictionary *urlParamsDic = [[NSMutableDictionary alloc]init];
        NSURL * schemaURL = [NSURL URLWithString:[schemaUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
        
        NSURLComponents *urlComponents = [NSURLComponents componentsWithString:[schemaUrl stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
        [urlComponents.queryItems enumerateObjectsUsingBlock:^(NSURLQueryItem * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            [urlParamsDic setObject:obj.value forKey:obj.name];
        }];
        NSLog(@"schemaURL=%@",schemaURL);
        NSLog(@"urlComponents=%@",urlComponents);
        NSLog(@"urlComponents.queryItems=%@",urlComponents.queryItems);
        NSString* pageName = [urlParamsDic valueForKey:@"page"];
        NSString* pageParamsJson = [urlParamsDic valueForKey:@"params"];
        NSString* uniqueId = [urlParamsDic valueForKey:@"uniqueId"];
        NSLog(@"pageName=%@",pageName);
        NSLog(@"pageParamsJson=%@",pageParamsJson);
        NSLog(@"uniqueId=%@",uniqueId);
        [self openNewFlutterViewControllerByName:fromViewController pageName:pageName pageParams: @{
            @"argumentsJsonString":pageParamsJson
        }];
    }else{
        NSLog(@"openNewFlutterViewControllerBySchema false");
    }
}

+(void) openNewFlutterViewControllerByName:(UIViewController* _Nullable)fromViewController pageName:(NSString* _Nullable) pageName pageParams:(NSDictionary* _Nullable) pageParams{
    NSLog(@"openNewFlutterViewControllerByName pageName=%@, pageParams=%@", pageName, pageParams);
    UINavigationController *navigationController = [fromViewController isKindOfClass:[UINavigationController class]] ? (UINavigationController*)fromViewController : fromViewController.navigationController;
    
    NSString* initialRoute = pageName;
    NSString* finalDartEntrypointFunctionName = (![STFlutterInitializer sharedInstance].enableMultiEnginesWithSingleRoute || [initialRoute isEqualToString: @"/"]) ? @"main" : [NSString stringWithFormat:@"main%@",initialRoute];
    // NSString* finalInitialRoute = (![STFlutterInitializer sharedInstance].enableMultiEnginesWithSingleRoute)? initialRoute : @"/";
    
    STFlutterMultipleViewController *flutterViewController = [[STFlutterMultipleViewController alloc] initWithDartEntrypointFunctionName:finalDartEntrypointFunctionName];
    
    [flutterViewController setRequestData:0 requestData: @{@"argumentsJsonString":pageParams[@"argumentsJsonString"]}];
    
    // 如果不加延时, push 的 viewController 还没有开始加载 flutter 代码(引擎尚未初始化成功), 此时会看到 viewController 的背景, 如果背景为透明将看到 window 的背景,
    // 加一个短暂的延时使得 flutter 引擎初始化成功, 再 push 则可以实现无缝切换, 不需要 splash 或者 loading 去加载 flutter 代码, 此处效果同 android 一致
//    int delta = 20 * NSEC_PER_MSEC;
//    #if defined(DEBUG)
//        NSLog(@"BUILD-TYPE -> DEBUG");
//        delta = 300 * NSEC_PER_MSEC; // DEBUG >= 300
//    #else
//        NSLog(@"BUILD-TYPE -> RELEASE");
//        delta = 20 * NSEC_PER_MSEC; // RELEASE >= 20
//    #endif
//    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, delta), dispatch_get_main_queue(), ^{
        [navigationController pushViewController:flutterViewController animated:YES];
//    });
}

+(void) openNewFlutterHomeViewControllerByName:(UIViewController* _Nullable)fromViewController pageName:(NSString* _Nullable) pageName pageParams:(NSDictionary* _Nullable) pageParams{
    NSLog(@"openNewFlutterViewControllerByName pageName=%@, pageParams=%@", pageName, pageParams);
    UINavigationController *navigationController = [fromViewController isKindOfClass:[UINavigationController class]] ? (UINavigationController*)fromViewController : fromViewController.navigationController;
    
    STFlutterMultipleHomeViewController *flutterViewController = [[STFlutterMultipleHomeViewController alloc] initWithDartEntrypointFunctionName:@"main"];
    [flutterViewController setRequestData:0 requestData: @{@"argumentsJsonString":pageParams[@"argumentsJsonString"]}];
    [navigationController pushViewController:flutterViewController animated:YES];
}

+(void) popRoute:(UIViewController* _Nullable)fromViewController{
    NSLog(@"popRoute");
    UINavigationController *navigationController = [fromViewController isKindOfClass:[UINavigationController class]] ? (UINavigationController*)fromViewController : fromViewController.navigationController;
    
    [navigationController popViewControllerAnimated:YES];
}

@end
