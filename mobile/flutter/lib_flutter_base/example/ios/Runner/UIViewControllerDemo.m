//
//  UIViewControllerDemo.m
//  Runner
//
//  Created by Jidong Chen on 2018/10/17.
//  Copyright © 2018年 The Chromium Authors. All rights reserved.
//

#import "UIViewControllerDemo.h"
#import <Flutter/Flutter.h>
#import <lib_flutter_base/FlutterBoost.h>


@interface UIViewControllerDemo ()

@end

@implementation UIViewControllerDemo


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
}

- (IBAction)pushFlutterPage:(id)sender {
    NSLog(@"pushFlutterPage");
    [[FlutterBoost instance] open:@"flutterPage" arguments:@{@"animated":@(YES)}  ];
}

- (IBAction)present:(id)sender {
    NSLog(@"present");
    [[FlutterBoost instance] open:@"secondStateful" arguments:@{@"present":@(YES)}];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
