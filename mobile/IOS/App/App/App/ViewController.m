//
//  ViewController.m
//  App
//
//  Created by krmao on 2019/6/18.
//  Copyright © 2019 smart. All rights reserved.
//

#import "ViewController.h"
#import "AppDelegate.h"
#import <Flutter/Flutter.h>
#import "FlutterRouter.h"

@interface ViewController ()

@end

@implementation ViewController
    
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    UIButton *nativeButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    nativeButton.frame = [UIScreen.mainScreen bounds];
    nativeButton.backgroundColor = [UIColor redColor];
    [nativeButton setTitle:@"open flutter order page" forState:UIControlStateNormal];
    [nativeButton addTarget:self action:@selector(onClick) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:nativeButton];
    
}

- (void)onClick {
    NSLog(@"you clicked");
    NSDictionary *params=@{@"aaa":@"bbb"};
    
    [[FlutterRouter sharedInstance] open:@"flutter://flutter/settings" urlParams:params exts:@{} completion:^(BOOL finished) {
        NSLog(@"completion:finished=%d", finished);
    }];
}

@end
