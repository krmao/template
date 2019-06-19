//
//  HomeViewController.m
//  Runner
//
//  Created by krmao on 2019/6/19.
//  Copyright © 2019 The Chromium Authors. All rights reserved.
//

#import "HomeViewController.h"
#import <Flutter/Flutter.h>
#import "DemoRouter.h"

@interface HomeViewController ()

@end

@implementation HomeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = UIColor.darkGrayColor;
    
    float margin = 20;
    float screenWidth = [[UIScreen mainScreen] bounds].size.width;
    float buttonWidth = screenWidth - margin * 2;
    
    UIButton *pushOrderBtn = [[UIButton alloc] initWithFrame:CGRectMake(20, 200, buttonWidth, 60)];
    pushOrderBtn.backgroundColor = UIColor.blueColor;
    [pushOrderBtn setTitle:@"PUSH FLUTTER ORDER" forState:UIControlStateNormal];
    [pushOrderBtn addTarget:self action:@selector(onOrderBtnClicked) forControlEvents:UIControlEventTouchUpInside];
    
    UIButton *presentSettingBtn = [[UIButton alloc] initWithFrame:CGRectMake(20, 280, buttonWidth, 60)];
    presentSettingBtn.backgroundColor = UIColor.blackColor;
    [presentSettingBtn setTitle:@"PRESENT FLUTTER SETTING" forState:UIControlStateNormal];
    [presentSettingBtn addTarget:self action:@selector(onSettingBtnClicked) forControlEvents:UIControlEventTouchUpInside];
    
     UIButton *pushNativeBtn = [[UIButton alloc] initWithFrame:CGRectMake(20, 360, buttonWidth, 60)];
    pushNativeBtn.backgroundColor = UIColor.orangeColor;
    [pushNativeBtn setTitle:@"PUSH NATIVE" forState:UIControlStateNormal];
    [pushNativeBtn addTarget:self action:@selector(onNativeBtnClicked) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:pushOrderBtn];
    [self.view addSubview:presentSettingBtn];
    [self.view addSubview:pushNativeBtn];
}

- (void)onOrderBtnClicked {
    [DemoRouter.sharedRouter openPage:@"flutter://flutter/order" params:@{} animated:YES completion:^(BOOL f){}];
}

- (void)onSettingBtnClicked {
    [DemoRouter.sharedRouter openPage:@"flutter://flutter/settings" params:@{@"present":@(YES)} animated:YES completion:^(BOOL f){}];
}

- (void)onNativeBtnClicked {
    [self.navigationController pushViewController: [HomeViewController new] animated:YES];
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
