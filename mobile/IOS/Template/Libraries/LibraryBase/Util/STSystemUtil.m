#pragma clang diagnostic push
#pragma ide diagnostic ignored "UnusedMethodInspection"

@implementation STSystemUtil

+ (BOOL)isIphone {
    return [[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone;
}

+ (BOOL)isIphone4OrLess {
    return [self isIphone] && ([self screenHeight] < 568);
}

+ (BOOL)isIphone5 {
    return [self isIphone] && ([self screenHeight] == 568);
}

+ (BOOL)isIphone6 {
    return [self isIphone] && ([self screenHeight] == 667);
}

+ (BOOL)isIphone6Plus {
    return [self isIphone] && ([self screenHeight] == 736);
}

+ (BOOL)isNeedSafeArea {
    if (@available(iOS 11.0, *)) {
        return [self isIphone] && ([self firstWindow].safeAreaInsets.bottom > 0.0);
    } else {
        return NO;
    }
}

+ (CGFloat)screenWidth {
    return [UIScreen mainScreen].bounds.size.width;
}

+ (CGFloat)screenHeight {
    return [UIScreen mainScreen].bounds.size.height;
}

+ (CGFloat)navigationBarHeight {
    return 44;
}

+ (CGFloat)navigationBarAndStatusBarHeight {
    return [self navigationBarHeight] + [self statusBarHeight];
}

+ (CGFloat)statusBarHeight {
    if (@available(iOS 13.0, *)) {
        return [[[self firstWindow] windowScene] statusBarManager].statusBarFrame.size.height;
    } else {
        return [UIApplication sharedApplication].statusBarFrame.size.height;
    }
}

+ (CGFloat)tabBarHeight {
    return [self statusBarHeight] > 20 ? 83 : ([self isIphone6Plus] ? 64 : 49); // 适配iPhone x 底部高度
}

+ (CGFloat)safeAreaBottom {
    return [self statusBarHeight] > 20 ? 34 : 0; // 适配iPhone x 底部高度
}

+ (double)systemVersion {
    return [[UIDevice currentDevice].systemVersion doubleValue];
}

+ (NSString *)systemName; {
    return [UIDevice currentDevice].systemName;
}

+ (UIWindow *)firstWindow {
    return [UIApplication sharedApplication].windows.firstObject;
}

+ (UIViewController *)rootViewController {
    if (@available(iOS 13.0, *)) {
        return [(SceneDelegate *) [[self firstWindow] windowScene].delegate window].rootViewController;
    } else {
        return [(AppDelegate *) [UIApplication sharedApplication].delegate window].rootViewController;
    }
}

+ (UIViewController *)topMostController {
    UIViewController *topMostController = [self rootViewController];
    while ([topMostController presentedViewController]) {
        topMostController = [topMostController presentedViewController];
        NSLog(@"topMostController=%@", topMostController);
    }
    return topMostController;
}

+ (UIViewController *)topViewController {
    UIViewController *topController = [self topMostController];
    while ([topController isKindOfClass:[UITabBarController class]] && [(UITabBarController *) topController selectedViewController]) {
        topController = [(UITabBarController *) topController selectedViewController];
        NSLog(@"topController tab 选中=%@", topController);
    }
    while ([topController isKindOfClass:[UINavigationController class]] && [(UINavigationController *) topController topViewController]) {
        topController = [(UINavigationController *) topController topViewController];
        NSLog(@"topController tab 选中最上面=%@", topController);
    }
    return topController;
}


/**
 计算单行文字的size
 @parms  文本
 @parms  字体
 @return  字体的CGSize
 */
+ (CGSize)getTextSize:(NSString *)text withFont:(UIFont *)font {
    return [text sizeWithAttributes:@{NSFontAttributeName: font}];
}

/**
 计算多行文字的CGRect
 @parms  文本
 @parms  字体
 @return  字体的CGRect
 */
+ (CGRect)getTextSizeWithBoundingRect:(NSString *)str withSize:(CGSize)size withFont:(UIFont *)font {
    return [str boundingRectWithSize:size options:NSStringDrawingUsesLineFragmentOrigin | NSStringDrawingUsesFontLeading attributes:@{NSFontAttributeName: font} context:nil];
}

/**
 * 隐藏键盘
 * @param textField
 */
+ (void)hideKeyboard:(UITextField *)textField {
    [textField resignFirstResponder];
}

/**
 * 为键盘添加隐藏按钮
 * @param textField
 * @param action
 */
+ (void)addHideButtonForKeyboard:(UITextField *)textField target:(nullable id)target action:(nullable SEL)action {
    UIBarButtonItem *emptyBtn = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    UIBarButtonItem *hideBtn = [[UIBarButtonItem alloc] initWithTitle:@"完成" style:UIBarButtonItemStyleDone target:target action:action];
    UIToolbar *toolbar = [[UIToolbar alloc] initWithFrame:CGRectMake(0, 0, [self screenWidth], 30)];
    [toolbar setItems:@[emptyBtn, hideBtn] animated:false];
    [toolbar sizeToFit];
    [textField setInputAccessoryView:toolbar];
}

+ (void)printSystemInfo {
    NSLog(@"%s, isIphone=%d", __FUNCTION__, [STSystemUtil isIphone]);
    NSLog(@"%s, isIphone4OrLess=%d", __FUNCTION__, [STSystemUtil isIphone4OrLess]);
    NSLog(@"%s, isIphone5=%d", __FUNCTION__, [STSystemUtil isIphone5]);
    NSLog(@"%s, isIphone6=%d", __FUNCTION__, [STSystemUtil isIphone6]);
    NSLog(@"%s, isIphone6Plus=%d", __FUNCTION__, [STSystemUtil isIphone6Plus]);
    NSLog(@"%s, isNeedSafeArea=%d", __FUNCTION__, [STSystemUtil isNeedSafeArea]);
    NSLog(@"%s, navigationBarHeight=%f", __FUNCTION__, [STSystemUtil navigationBarHeight]);
    NSLog(@"%s, firstWindow=%@", __FUNCTION__, [STSystemUtil firstWindow]);
    NSLog(@"%s, screenWidth=%f", __FUNCTION__, [STSystemUtil screenWidth]);
    NSLog(@"%s, screenHeight=%f", __FUNCTION__, [STSystemUtil screenHeight]);
    NSLog(@"%s, statusBarHeight=%f", __FUNCTION__, [STSystemUtil statusBarHeight]);
    NSLog(@"%s, navigationBarAndStatusBarHeight=%f", __FUNCTION__, [STSystemUtil navigationBarAndStatusBarHeight]);
    NSLog(@"%s, tabBarHeight=%f", __FUNCTION__, [STSystemUtil tabBarHeight]);
    NSLog(@"%s, safeAreaBottom=%f", __FUNCTION__, [STSystemUtil safeAreaBottom]);
    NSLog(@"%s, systemVersion=%f", __FUNCTION__, [STSystemUtil systemVersion]);
    NSLog(@"%s, systemName=%@", __FUNCTION__, [STSystemUtil systemName]);
    NSLog(@"%s, rootViewController=%@", __FUNCTION__, [STSystemUtil rootViewController]);
    NSLog(@"%s, topViewController=%@", __FUNCTION__, [STSystemUtil topViewController]);
}

@end

#pragma clang diagnostic pop