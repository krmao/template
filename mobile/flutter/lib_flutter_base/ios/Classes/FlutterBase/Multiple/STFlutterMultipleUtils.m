//
//  STFlutterUtils.m
//  lib_flutter_base
//
//  Created by krmao on 2021/3/9.
//

#import "STFlutterMultipleUtils.h"
#import "STFlutterInitializer.h"
#import "STJsonUtil.h"
#import "STFlutterMultipleInitializer.h"
#import "STFlutterMultipleViewController.h"
#import "STFlutterMultipleHomeViewController.h"
#import "LibFlutterBaseMultiplePlugin.h"

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
    NSString* finalInitialRoute = (![STFlutterInitializer sharedInstance].enableMultiEnginesWithSingleRoute)? initialRoute : @"/";
    
    STFlutterMultipleViewController *flutterViewController = [[STFlutterMultipleViewController alloc] initWithDartEntrypointFunctionName:finalDartEntrypointFunctionName argumentsJsonString:pageParams[@"argumentsJsonString"]];
    [navigationController pushViewController:flutterViewController animated:YES];
}

+(void) openNewFlutterHomeViewControllerByName:(UIViewController* _Nullable)fromViewController pageName:(NSString* _Nullable) pageName pageParams:(NSDictionary* _Nullable) pageParams{
    NSLog(@"openNewFlutterViewControllerByName pageName=%@, pageParams=%@", pageName, pageParams);
    UINavigationController *navigationController = [fromViewController isKindOfClass:[UINavigationController class]] ? (UINavigationController*)fromViewController : fromViewController.navigationController;
    
    STFlutterMultipleHomeViewController *flutterViewController = [[STFlutterMultipleHomeViewController alloc] initWithDartEntrypointFunctionName:@"main" argumentsJsonString:pageParams[@"argumentsJsonString"]];
    [navigationController pushViewController:flutterViewController animated:YES];
}

+(void) popRoute:(UIViewController* _Nullable)fromViewController{
    NSLog(@"popRoute");
    UINavigationController *navigationController = [fromViewController isKindOfClass:[UINavigationController class]] ? (UINavigationController*)fromViewController : fromViewController.navigationController;
    
    [navigationController popViewControllerAnimated:YES];
}

@end
