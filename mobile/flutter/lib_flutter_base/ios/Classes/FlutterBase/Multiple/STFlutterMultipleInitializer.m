//
//  STFlutterMultipleInitializer.m
//  lib_flutter_base
//
//  Created by krmao on 2021/3/9.
//

#import "STFlutterMultipleInitializer.h"
#import <lib_flutter_base/FlutterBoost.h>
#import <LibIosBase/STInitializer.h>
#import <LibIosBase/STJsonUtil.h>
#import "STFlutterBridge.h"
#import "STFlutterUtils.h"

static STFlutterMultipleInitializer *instance = nil;

@implementation STFlutterMultipleInitializer

+(instancetype)sharedInstance{
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[STFlutterMultipleInitializer alloc] init];
    });
    return instance;
}

-(void) initial:(UIApplication*)application debug:(BOOL)debug{
    NSLog(@"STFlutterMultipleInitializer initial application=%@ _isInitialized=%d", application, _isInitialized);
    
    if(_isInitialized == YES){
        return;
    }
    _debug = debug;
    _application = application;
    _flutterEngineGroup = [[FlutterEngineGroup alloc] initWithName:@"multiple-flutters" project:nil];
    
    _isInitialized = YES;
}

@end
