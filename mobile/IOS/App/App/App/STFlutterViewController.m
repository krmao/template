//
//  FlutterViewController.m
//  App
//
//  Created by krmao on 2019/9/4.
//  Copyright © 2019 smart. All rights reserved.
//

#import "STFlutterViewController.h"

@interface STFlutterViewController ()

@end

@implementation STFlutterViewController

+ (STFlutterViewController *) create: (nonnull NSString *)url urlParams:(nonnull NSDictionary *)urlParams{
    STFlutterViewController *vc = STFlutterViewController.new;
    [vc setName:url params:urlParams];
    return vc;
}

@end
