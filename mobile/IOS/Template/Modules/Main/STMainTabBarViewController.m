//
//  STMainTabBarViewController.m
//  Template
//
//  Created by krmao on 2020/5/22.
//  Copyright © 2020 smart. All rights reserved.
//

#import "STMainTabBarViewController.h"
#import "STMainItemHomeViewController.h"
#import "STMainItemReactNativeViewController.h"
#import "STMainItemHybirdViewController.h"
#import "STMainItemFlutterViewController.h"

@interface STMainTabBarViewController ()

@end

@implementation STMainTabBarViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.view.backgroundColor = UIColor.whiteColor;
    
    STMainItemHomeViewController *itemHomeViewController = [STMainItemHomeViewController new];
    itemHomeViewController.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"首页" image:[UIImage imageNamed:@"icon_home"] tag:0];

    STMainItemReactNativeViewController *itemReactNativeViewController = [STMainItemReactNativeViewController new];
    itemReactNativeViewController.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"react-native" image:[UIImage imageNamed:@"icon_react_native"] tag:1];
    
    STMainItemHybirdViewController *itemHybirdViewController = [STMainItemHybirdViewController new];
    itemHybirdViewController.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"hybird" image:[UIImage imageNamed:@"icon_hybird"] tag:2];
    
    STMainItemFlutterViewController *itemFlutterViewController = [STMainItemFlutterViewController new];
    itemFlutterViewController.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"flutter" image:[UIImage imageNamed:@"icon_flutter"] tag:3];
    
    self.viewControllers = @[itemHomeViewController, itemReactNativeViewController, itemHybirdViewController, itemFlutterViewController];
    
    self.tabBar.tintColor = UIColor.systemBlueColor; // tabBar 选中时高亮的颜色
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
