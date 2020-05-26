/**
 * 在 .m 文件里面允许添加一个匿名 category 定义私有属性
 */
@interface STBaseTabBarController ()
// 私有属性
@end

/**
 * 在 .m 文件里面允许在 @implementation 中定义私有变量
 */
@implementation STBaseTabBarController {
// 私有变量
}

- (instancetype)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil {
    if (self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil]) {
        NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
    }
    return self;
}

- (instancetype)initWithCoder:(NSCoder *)aDecoder {
    if (self == [super initWithCoder:aDecoder]) {
        NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
    }
    return self;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)loadView {
    [super loadView];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)viewDidLoad {
    [super viewDidLoad];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);

    self.tabBar.tintColor = UIColor.systemBlueColor; // tabBar 选中时高亮的颜色
    self.delegate = self;

    [self setViewControllers:nil animated:NO];
    self.selectedIndex = 0; // 默认显示第几个
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)viewWillLayoutSubviews {
    [super viewWillLayoutSubviews];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)dealloc {
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)tabBar:(UITabBar *)tabBar didSelectItem:(UITabBarItem *)item {
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)tabBar:(UITabBar *)tabBar willBeginCustomizingItems:(NSArray<UITabBarItem *> *)items {
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)tabBar:(UITabBar *)tabBar didBeginCustomizingItems:(NSArray<UITabBarItem *> *)items {
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)tabBar:(UITabBar *)tabBar willEndCustomizingItems:(NSArray<UITabBarItem *> *)items changed:(BOOL)changed {
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)tabBar:(UITabBar *)tabBar didEndCustomizingItems:(NSArray<UITabBarItem *> *)items changed:(BOOL)changed {
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (BOOL)tabBarController:(UITabBarController *)tabBarController shouldSelectViewController:(UIViewController *)viewController {
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
    return YES;
}

- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController {
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)tabBarController:(UITabBarController *)tabBarController willBeginCustomizingViewControllers:(NSArray<__kindof UIViewController *> *)viewControllers {
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

- (void)tabBarController:(UITabBarController *)tabBarController willEndCustomizingViewControllers:(NSArray<__kindof UIViewController *> *)viewControllers changed:(BOOL)changed {
    NSLog(@"%s, %lu", __FUNCTION__, (unsigned long) self.navigationController.viewControllers.count);
}

@end