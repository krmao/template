//
//  MineViewController.m
//  Runner
//
//  Created by krmao on 2019/6/19.
//  Copyright © 2019 The Chromium Authors. All rights reserved.
//

#import "MineViewController.h"
#import "HomeViewController.h"

@interface MineViewController ()

@end

@implementation MineViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = UIColor.redColor;
    
    float margin = 20;
    float screenWidth = [[UIScreen mainScreen] bounds].size.width;
    float buttonWidth = screenWidth - margin * 2;
    
    UIButton *homeBtn= [[UIButton alloc] initWithFrame:CGRectMake(20, 200, buttonWidth, 60)];
    homeBtn.backgroundColor = UIColor.blueColor;
    [homeBtn setTitle:@"OPEN NATIVE HOME" forState:UIControlStateNormal];
    [homeBtn addTarget:self action:@selector(onHomeBtnClicked) forControlEvents:UIControlEventTouchUpInside];
    
    UIButton *closeBtn = [[UIButton alloc] initWithFrame:CGRectMake(20, 280, buttonWidth, 60)];
    closeBtn.backgroundColor = UIColor.blackColor;
    [closeBtn setTitle:@"CLOSE" forState:UIControlStateNormal];
    [closeBtn addTarget:self action:@selector(onCloseBtnClicked) forControlEvents:UIControlEventTouchUpInside];
    
    [self.view addSubview:homeBtn];
    [self.view addSubview:closeBtn];
}

- (void)onHomeBtnClicked {
    if(self.navigationController){
        [self.navigationController pushViewController:[HomeViewController new] animated:YES];
    }else{
        [self presentViewController:[HomeViewController new] animated:YES completion:^{}];
    }
}

- (void)onCloseBtnClicked {
    if(self.navigationController){
        [self.navigationController popViewControllerAnimated:YES];
    }else{
        [self dismissViewControllerAnimated:YES completion:^{}];
    }
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
