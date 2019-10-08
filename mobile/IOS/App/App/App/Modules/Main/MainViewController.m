//
//  ViewController.m
//  App
//
//  Created by krmao on 2019/6/18.
//  Copyright © 2019 smart. All rights reserved.
//

#import "MainViewController.h"
#import "AppDelegate.h"
#import "UIButton+OnClickListener.h"

//#import <Flutter/Flutter.h>
//#import "FlutterRouter.h"

@interface MainViewController ()

@end

@implementation MainViewController
    
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    UIButton *nativeButton = [UIButton buttonWithType:UIButtonTypeCustom];
    
    nativeButton.frame = [UIScreen.mainScreen bounds];
    nativeButton.backgroundColor = [UIColor redColor];
    [nativeButton setTitle:@"open flutter order page" forState:UIControlStateNormal];
    
    [nativeButton setOnClickListener:^(UIButton * _Nullable button) {
        NSLog(@"you clicked");
    }];
    
    [self.view addSubview:nativeButton];
}

@end
