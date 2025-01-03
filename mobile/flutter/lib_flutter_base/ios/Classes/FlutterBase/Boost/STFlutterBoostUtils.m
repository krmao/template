//
//  STFlutterUtils.m
//  lib_flutter_base
//
//  Created by krmao on 2021/3/9.
//

#import "STFlutterBoostUtils.h"
#import "FlutterBoost.h"
#import "STFlutterBoostViewController.h"
#import "STFlutterBoostHomeViewController.h"
#import <LibIosBase/STJsonUtil.h>

@implementation STFlutterBoostUtils

+(void) openNewFlutterViewControllerBySchema:(UIViewController* _Nullable)fromViewController schemaUrl:(NSString* _Nullable) schemaUrl{
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
        [self openNewFlutterViewControllerByName:fromViewController pageName:pageName pageParams: @{
            @"argumentsJsonString":pageParamsJson
        }];
    }else{
        NSLog(@"openBySchema false");
    }
}

+(void) openNewFlutterViewControllerByName:(UIViewController* _Nullable)fromViewController pageName:(NSString* _Nullable) pageName pageParams:(NSDictionary* _Nullable) pageParams{
    NSLog(@"openByName pageName=%@, pageParams=%@", pageName, pageParams);
    UINavigationController *navigationController = [fromViewController isKindOfClass:[UINavigationController class]] ? (UINavigationController*)fromViewController : fromViewController.navigationController;
    
    FlutterEngine* engine =  [[FlutterBoost instance ] engine];
    engine.viewController = nil;
    
    STFlutterBoostViewController *flutterViewController = STFlutterBoostViewController.new;
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

+(void) openNewFlutterHomeViewControllerByName:(UIViewController* _Nullable)fromViewController pageName:(NSString* _Nullable) pageName pageParams:(NSDictionary* _Nullable) pageParams{
    NSLog(@"openHomeByName");
    UINavigationController *navigationController = [fromViewController isKindOfClass:[UINavigationController class]] ? (UINavigationController*)fromViewController : fromViewController.navigationController;
    
    FlutterEngine* engine =  [[FlutterBoost instance ] engine];
    engine.viewController = nil;
    
    STFlutterBoostHomeViewController *flutterViewController = STFlutterBoostHomeViewController.new;
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
