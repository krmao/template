//
//  STFlutterUtils.m
//  lib_flutter_base
//
//  Created by krmao on 2021/3/9.
//

#import "STFlutterUtils.h"
#import "FlutterBoost.h"
#import "STFlutterViewController.h"
#import "STFlutterHomeViewController.h"
#import "STJsonUtil.h"

@implementation STFlutterUtils

+(void) openBySchema:(UIViewController* _Nullable)fromViewController schemaUrl:(NSString* _Nullable) schemaUrl{
    if ([schemaUrl hasPrefix:@"smart://template/flutter"]){
        NSLog(@"openBySchema true schemaUrl=%@", schemaUrl);
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
        [self openByName:fromViewController pageName:pageName pageParams: (NSDictionary*)[STJsonUtil jsonStringToArrayOrDictionary:pageParamsJson]];
    }else{
        NSLog(@"openBySchema false");
    }
}

+(void) openByName:(UIViewController* _Nullable)fromViewController pageName:(NSString* _Nullable) pageName pageParams:(NSDictionary* _Nullable) pageParams{
    NSLog(@"openByName pageName=%@, pageParams=%@", pageName, pageParams);
    UINavigationController *navigationController = [fromViewController isKindOfClass:[UINavigationController class]] ? (UINavigationController*)fromViewController : fromViewController.navigationController;
    
    FlutterEngine* engine =  [[FlutterBoost instance ] engine];
    engine.viewController = nil;
    
    STFlutterViewController *flutterViewController = STFlutterViewController.new;
    [flutterViewController setName:pageName params:pageParams];
    BOOL animated = [pageParams[@"animated"] boolValue];
    BOOL present= [pageParams[@"present"] boolValue];
    if(present){
        [navigationController presentViewController:flutterViewController animated:animated completion:^{
        }];
    }else{
        [navigationController pushViewController:flutterViewController animated:animated];
    }
}

+(void) openHomeByName:(UIViewController* _Nullable)fromViewController pageName:(NSString* _Nullable) pageName pageParams:(NSDictionary* _Nullable) pageParams{
    NSLog(@"openHomeByName");
    UINavigationController *navigationController = [fromViewController isKindOfClass:[UINavigationController class]] ? (UINavigationController*)fromViewController : fromViewController.navigationController;
    
    FlutterEngine* engine =  [[FlutterBoost instance ] engine];
    engine.viewController = nil;
    
    STFlutterHomeViewController *flutterViewController = STFlutterHomeViewController.new;
    [flutterViewController setName:pageName params:pageParams];
    BOOL animated = [pageParams[@"animated"] boolValue];
    BOOL present= [pageParams[@"present"] boolValue];
    if(present){
        [navigationController presentViewController:flutterViewController animated:animated completion:^{
        }];
    }else{
        [navigationController pushViewController:flutterViewController animated:animated];
    }
}

+ (void) popRoute:(UIViewController* _Nullable)fromViewController uniqueId:(NSString * _Nullable)uniqueId{
    NSLog(@"popRoute");
    UINavigationController *navigationController = [fromViewController isKindOfClass:[UINavigationController class]] ? (UINavigationController*)fromViewController : fromViewController.navigationController;
    
    FBFlutterViewContainer *flutterViewController = (id) navigationController.presentedViewController;
    if([flutterViewController isKindOfClass:FBFlutterViewContainer.class] && [flutterViewController.uniqueIDString isEqual: uniqueId]){
        [flutterViewController dismissViewControllerAnimated:YES completion:^{}];
    }else{
        [navigationController popViewControllerAnimated:YES];
    }
}

@end
