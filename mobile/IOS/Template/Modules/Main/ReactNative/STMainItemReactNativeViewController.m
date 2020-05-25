//
//  STMainItemReactNativeViewController.m
//  Template
//
//  Created by krmao on 2020/5/22.
//  Copyright © 2020 smart. All rights reserved.
//

#import "STMainItemReactNativeViewController.h"

@interface STMainItemReactNativeViewController ()

@end

@implementation STMainItemReactNativeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.view.backgroundColor = UIColor.blueColor;
}

- (void) viewWillAppear:(BOOL)animated{
    NSLog(@"lifecycle main tab item rn viewWillAppear, %lu", (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewDidAppear:(BOOL)animated{
    NSLog(@"lifecycle main tab item rn viewDidAppear, %lu", (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewWillDisappear:(BOOL)animated{
    NSLog(@"lifecycle main tab item rn tab viewWillDisappear, %lu", (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewDidDisappear:(BOOL)animated{
    NSLog(@"lifecycle main tab item rn tab viewDidDisappear, %lu", (unsigned long)self.navigationController.viewControllers.count);
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
