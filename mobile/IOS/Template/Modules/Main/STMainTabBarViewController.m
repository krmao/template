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
    self.tabBar.tintColor = UIColor.systemBlueColor; // tabBar 选中时高亮的颜色
    self.delegate = self;
    
    STMainItemHomeViewController *itemHomeViewController = [STMainItemHomeViewController new];
    itemHomeViewController.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"首页" image:[UIImage imageNamed:@"icon_home"] tag:0];

    STMainItemReactNativeViewController *itemReactNativeViewController = [STMainItemReactNativeViewController new];
    itemReactNativeViewController.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"react-native" image:[UIImage imageNamed:@"icon_react_native"] tag:1];
    
    STMainItemHybirdViewController *itemHybirdViewController = [STMainItemHybirdViewController new];
    itemHybirdViewController.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"hybird" image:[UIImage imageNamed:@"icon_hybird"] tag:2];
    
    STMainItemFlutterViewController *itemFlutterViewController = [STMainItemFlutterViewController new];
    itemFlutterViewController.tabBarItem = [[UITabBarItem alloc] initWithTitle:@"flutter" image:[UIImage imageNamed:@"icon_flutter"] tag:3];
    
    [self setViewControllers:@[itemHomeViewController, itemReactNativeViewController, itemHybirdViewController, itemFlutterViewController] animated:YES];
    
    self.selectedIndex = 0; // 默认显示第几个
}

- (void) viewWillAppear:(BOOL)animated{
    NSLog(@"lifecycle main tab viewWillAppear, %lu", (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewDidAppear:(BOOL)animated{
    NSLog(@"lifecycle main tab viewDidAppear, %lu", (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewWillDisappear:(BOOL)animated{
    NSLog(@"lifecycle main tab viewWillDisappear, %lu", (unsigned long)self.navigationController.viewControllers.count);
}

- (void)viewDidDisappear:(BOOL)animated{
    NSLog(@"lifecycle main tab viewDidDisappear, %lu", (unsigned long)self.navigationController.viewControllers.count);
}

- (void)tabBar:(UITabBar *)tabBar didSelectItem:(UITabBarItem *)item{
    NSLog(@"lifecycle main tab didSelectItem");
}

- (void)tabBar:(UITabBar *)tabBar willBeginCustomizingItems:(NSArray<UITabBarItem *> *)items{
    NSLog(@"lifecycle main tab willBeginCustomizingItems");
}

- (void)tabBar:(UITabBar *)tabBar didBeginCustomizingItems:(NSArray<UITabBarItem *> *)items{
    NSLog(@"lifecycle main tab didBeginCustomizingItems");
}

- (void)tabBar:(UITabBar *)tabBar willEndCustomizingItems:(NSArray<UITabBarItem *> *)items changed:(BOOL)changed {
    NSLog(@"lifecycle main tab willEndCustomizingItems");
}

- (void)tabBar:(UITabBar *)tabBar didEndCustomizingItems:(NSArray<UITabBarItem *> *)items changed:(BOOL)changed{
    NSLog(@"lifecycle main tab didEndCustomizingItems");
}

- (BOOL)tabBarController:(UITabBarController *)tabBarController shouldSelectViewController:(UIViewController *)viewController{
    NSLog(@"lifecycle main tab shouldSelectViewController");
    return YES;
}

- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController{
    NSLog(@"lifecycle main tab didSelectViewController");
}

- (void)tabBarController:(UITabBarController *)tabBarController willBeginCustomizingViewControllers:(NSArray<__kindof UIViewController *> *)viewControllers{
    NSLog(@"lifecycle main tab willBeginCustomizingViewControllers");
}

- (void)tabBarController:(UITabBarController *)tabBarController willEndCustomizingViewControllers:(NSArray<__kindof UIViewController *> *)viewControllers changed:(BOOL)changed{
    NSLog(@"lifecycle main tab willEndCustomizingViewControllers");
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
