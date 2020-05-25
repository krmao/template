//
//  STMainItemHybirdViewController.m
//  Template
//
//  Created by krmao on 2020/5/22.
//  Copyright © 2020 smart. All rights reserved.
//

#import "STMainItemHybirdViewController.h"

@interface STMainItemHybirdViewController ()

@end

@implementation STMainItemHybirdViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.view.backgroundColor = UIColor.orangeColor;
}

- (void) viewWillAppear:(BOOL)animated{
    NSLog(@"lifecycle main tab item hybird viewWillAppear, %lu", (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewDidAppear:(BOOL)animated{
    NSLog(@"lifecycle main tab item hybird viewDidAppear, %lu", (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewWillDisappear:(BOOL)animated{
    NSLog(@"lifecycle main tab item hybird tab viewWillDisappear, %lu", (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewDidDisappear:(BOOL)animated{
    NSLog(@"lifecycle main tab item hybird tab viewDidDisappear, %lu", (unsigned long)self.navigationController.viewControllers.count);
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
