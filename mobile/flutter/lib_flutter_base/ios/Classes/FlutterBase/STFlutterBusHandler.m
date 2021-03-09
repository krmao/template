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

@interface STFlutterBusHandler() {
    NSString *bizNamePrefix;
    NSString *urlHost;
}
@end

@implementation STFlutterBusHandler

- (id)initWithHost:(NSString *)host{
    if (self = [super init]) {
        urlHost = [host lowercaseString];
        [[STFlutterInitializer sharedInstance] initial:[STInitializer sharedInstance].config.application debug:YES];
    }
    return self;
}

- (id)doDataJob:(NSString *)businessName params:(NSArray *)params{
    NSLog(@"flutter doDataJob businessName=%@, params=%@", businessName, params);
    if ([businessName isEqual:@"flutter/open"] ) {
        NSString * schemaUrl = (NSString *)params.firstObject;
        if(schemaUrl == nil){
            schemaUrl= @"smart://template/flutter?page=flutter_settings&params=";
        }
        [STFlutterUtils openBySchema:[STSystemUtil topViewController] schemaUrl:schemaUrl];
    }
    return NULL;
}

@end
