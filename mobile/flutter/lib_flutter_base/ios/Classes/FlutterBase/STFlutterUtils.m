//
//  STFlutterUtils.m
//  lib_flutter_base
//
//  Created by krmao on 2021/3/9.
//

#import "STFlutterUtils.h"
#import "STFlutterInitializer.h"
#import "STFlutterMultipleUtils.h"
#import "STFlutterBoostUtils.h"

@implementation STFlutterUtils

+(void) openNewFlutterViewControllerBySchema:(UIViewController* _Nullable)fromViewController schemaUrl:(NSString* _Nullable) schemaUrl{
    if ([STFlutterInitializer sharedInstance].enableMultiple == YES) {
        [STFlutterMultipleUtils openNewFlutterViewControllerBySchema:fromViewController schemaUrl:schemaUrl];
    }else{
        [STFlutterBoostUtils openNewFlutterViewControllerBySchema:fromViewController schemaUrl:schemaUrl];
    }
}

+(void) openNewFlutterViewControllerByName:(UIViewController* _Nullable)fromViewController pageName:(NSString* _Nullable) pageName pageParams:(NSDictionary* _Nullable) pageParams{
    if ([STFlutterInitializer sharedInstance].enableMultiple == YES) {
        [STFlutterMultipleUtils openNewFlutterViewControllerByName:fromViewController pageName:pageName pageParams:pageParams];
    }else{
        [STFlutterBoostUtils openNewFlutterViewControllerByName:fromViewController pageName:pageName pageParams:pageParams];
    }
}

+(void) openNewFlutterHomeViewControllerByName:(UIViewController* _Nullable)fromViewController pageName:(NSString* _Nullable) pageName pageParams:(NSDictionary* _Nullable) pageParams{
    NSLog(@"openHomeByName");
    if ([STFlutterInitializer sharedInstance].enableMultiple == YES) {
        [STFlutterMultipleUtils openNewFlutterHomeViewControllerByName:fromViewController pageName:pageName pageParams:pageParams];
    }else{
        [STFlutterBoostUtils openNewFlutterHomeViewControllerByName:fromViewController pageName:pageName pageParams:pageParams];
    }
}

+ (void) popRoute:(UIViewController* _Nullable)fromViewController uniqueId:(NSString * _Nullable)uniqueId{
    NSLog(@"popRoute");
    if ([STFlutterInitializer sharedInstance].enableMultiple == YES) {
        [STFlutterMultipleUtils popRoute:fromViewController];
    }else{
        [STFlutterBoostUtils popRoute:fromViewController uniqueId:uniqueId];
    }
}

@end
