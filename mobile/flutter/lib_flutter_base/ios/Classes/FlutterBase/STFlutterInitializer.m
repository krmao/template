//
//  STFlutterInitializer.m
//  lib_flutter_base
//
//  Created by krmao on 2021/3/9.
//

#import "STFlutterInitializer.h"
#import <lib_flutter_base/FlutterBoost.h>
#import <LibIosBase/STInitializer.h>
#import <LibIosBase/STJsonUtil.h>
#import "STFlutterViewController.h"
#import "STFlutterBridge.h"
#import "STFlutterUtils.h"

@interface BoostDelegete : NSObject<FlutterBoostDelegate>

@end

@implementation BoostDelegete

- (void) pushNativeRoute:(NSString *) pageName arguments:(NSDictionary *) arguments {
    NSString* schemaUrl = [NSString stringWithFormat:@"smart://template/flutter?page=%@&params=%@", pageName, [STJsonUtil dictionaryToJsonString:arguments]]; // native page 没有 uniqueId 字段
    NSLog(@"pushNativeRoute pageName=%@, schemaUrl=%@, arguments=%@", pageName, schemaUrl, arguments);
    
    [STInitializer openSchema:[FlutterBoost instance].currentViewController  url:schemaUrl callback:^(NSString * _Nullable callBackId, NSString * _Nullable resultJsonString) {
        NSLog(@"pushNativeRoute callback ignore");
    }];
}

- (void) pushFlutterRoute:(NSString *) pageName arguments:(NSDictionary *) arguments {
    NSString* schemaUrl = [NSString stringWithFormat:@"smart://template/flutter?page=%@&params=%@&uniqueId=", pageName, [STJsonUtil dictionaryToJsonString:arguments]]; // native page 没有 uniqueId 字段
    NSLog(@"pushNativeRoute pageName=%@, schemaUrl=%@, arguments=%@", pageName, schemaUrl, arguments);
    
    [STInitializer openSchema:[FlutterBoost instance].currentViewController  url:schemaUrl callback:^(NSString * _Nullable callBackId, NSString * _Nullable resultJsonString) {
        NSLog(@"pushFlutterRoute callback ignore");
    }];
}

- (void) popRoute:(NSString *)uniqueId {
    NSLog(@"popRoute uniqueId=%@", uniqueId);
    [STFlutterUtils popRoute:[FlutterBoost instance].currentViewController uniqueId:uniqueId];
}

@end

static STFlutterInitializer *instance = nil;

@implementation STFlutterInitializer

+(instancetype)sharedInstance{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[STFlutterInitializer alloc] init];
    });
    return instance;
}

-(void) initial:(UIApplication*)application debug:(BOOL)debug{
    NSLog(@"STFlutterInitializer initial application=%@ _isInitialized=%d", application, _isInitialized);
    
    if(_isInitialized == YES){
        return;
    }
    _debug = debug;
    _application = application;
    
    BoostDelegete* delegate=[[BoostDelegete alloc ] init];
    [[FlutterBoost instance] setup:application delegate:delegate callback:^(FlutterEngine *engine) {
        NSLog(@"FlutterBoostPlugin onStart");
        [STFlutterBridge registerWithRegistrar:[engine registrarForPlugin:@"STFlutterBridge"]];
    }];
    
    _isInitialized = YES;
}

@end
