//
//  STFlutterBusHandler.m
//  lib_flutter_base
//
//  Created by krmao on 2021/3/9.
//

#import "STFlutterBusHandler.h"
#import "STFlutterUtils.h"
#import <LibIosBase/STSystemUtil.h>
#import <STFlutterInitializer.h>
#import <LibIosBase/STInitializer.h>

@interface STFlutterBusHandler()
@end

@implementation STFlutterBusHandler

- (id)initWithHost:(NSString *)host{
    if (self = [super initWithHost:host]) {
        NSLog(@"STFlutterBusHandler initWithHost host=%@", host);
        [[STFlutterInitializer sharedInstance] initial:[STInitializer sharedInstance].config.application debug:YES];
    }
    return self;
}

- (id)doDataJob:(NSString *)businessName params:(NSArray *)params{
    NSLog(@"STFlutterBusHandler doDataJob businessName=%@, params=%@", businessName, params);
    if ([businessName isEqual:@"flutter/open"] ) {
        NSString * schemaUrl = (NSString *)params.firstObject;
        if(schemaUrl == nil){
            schemaUrl= @"smart://template/flutter?page=flutter_settings&params=";
        }
        [STFlutterUtils openNewFlutterViewControllerBySchema:[STSystemUtil topViewController] schemaUrl:schemaUrl];
    }
    return NULL;
}

@end
