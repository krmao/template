//
//  STNavigationController.m
//  App
//
//  Created by krmao on 2019/9/4.
//  Copyright © 2019 smart. All rights reserved.
//

#import "STNavigationController.h"

@interface STNavigationController ()<UINavigationControllerDelegate>

@end

@implementation STNavigationController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.delegate = self;
    self.interactivePopGestureRecognizer.delegate = (id)self.interactivePopGestureRecognizer;
}

// 当设置隐藏 导航栏时, 侧滑退出功能失效, 重写该方法解决失效问题
- (void)navigationController:(UINavigationController *)navigationController didShowViewController:(UIViewController *)viewController animated:(BOOL)animated {
        self.interactivePopGestureRecognizer.enabled = navigationController.viewControllers.count > 1;
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