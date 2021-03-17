//
//  STFlutterInitializer.m
//  lib_flutter_base
//
//  Created by krmao on 2021/3/9.
//

#import "STFlutterInitializer.h"
#import "STFlutterBoostInitializer.h"
#import "STFlutterMultipleInitializer.h"

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
    _enableMultiple = YES;
    
    if(_enableMultiple){
        [[STFlutterMultipleInitializer sharedInstance] initial:application debug:debug];
    }else{
        [[STFlutterBoostInitializer sharedInstance] initial:application debug:debug];
    }
    
    _isInitialized = YES;
}

@end
