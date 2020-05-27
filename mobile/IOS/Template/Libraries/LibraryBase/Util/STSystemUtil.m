#import <sys/utsname.h>

#pragma clang diagnostic push
#pragma ide diagnostic ignored "UnusedMethodInspection"

@implementation STSystemUtil

+ (BOOL)isIphone {
    return [[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPhone;
}

+ (BOOL)isIphone4OrLess {
    return [self isIphone] && ([self deviceScreenHeight] < 568.0);
}

+ (BOOL)isIphone5Or5S {
    return [self isIphone] && ([self deviceScreenHeight] == 568.0);
}

+ (BOOL)isIphone6Or7Or8 {
    return [self isIphone] && ([self deviceScreenHeight] == 667.0);
}

+ (BOOL)isIphone6POr7POr8P {
    return [self isIphone] && ([self deviceScreenHeight] == 736.0);
}

+ (BOOL)isIphoneXOrMore {
    return [self isHaveSafeArea];
}

+ (CGFloat)deviceScreenWidth {
    return [UIScreen mainScreen].bounds.size.width;
}

+ (CGFloat)deviceScreenMaxLength {
    return MAX([self deviceScreenHeight], [self deviceScreenWidth]);
}

+ (CGFloat)deviceScreenMinLength {
    return MIN([self deviceScreenHeight], [self deviceScreenWidth]);
}

+ (CGFloat)deviceScreenHeight {
    return [UIScreen mainScreen].bounds.size.height;
}

+ (CGFloat)deviceNavigationBarHeight {
    return 44;
}

+ (CGFloat)deviceNavigationBarAndStatusBarHeight {
    return [self deviceNavigationBarHeight] + [self deviceStatusBarHeight];
}

+ (CGFloat)deviceStatusBarHeight {
    if (@available(iOS 13.0, *)) {
        return [[[self firstWindow] windowScene] statusBarManager].statusBarFrame.size.height;
    } else {
        return [UIApplication sharedApplication].statusBarFrame.size.height;
    }
}

+ (CGFloat)deviceTabBarHeight {
    return [self deviceStatusBarHeight] > 20 ? 83 : ([self isIphone6POr7POr8P] ? 64 : 49); // 适配iPhone x 底部高度
}

+ (CGFloat)deviceBatteryLevel {
    return [[UIDevice currentDevice] batteryLevel];
}

+ (BOOL)isHaveSafeArea {
    if (@available(iOS 11.0, *)) {
        return [self isIphone] && ([self firstWindow].safeAreaInsets.bottom > 0.0);
    } else {
        return NO;
    }
}

+ (CGFloat)deviceSafeAreaBottom {
    if (@available(iOS 11.0, *)) {
        return [self firstWindow].safeAreaInsets.bottom;
    } else {
        return 0.0;
    }
}

+ (CGFloat)deviceSafeAreaTop {
    if (@available(iOS 11.0, *)) {
        return [self firstWindow].safeAreaInsets.top;
    } else {
        return 0.0;
    }
}

+ (CGFloat)deviceSafeAreaLeft {
    if (@available(iOS 11.0, *)) {
        return [self firstWindow].safeAreaInsets.left;
    } else {
        return 0.0;
    }
}

+ (CGFloat)deviceSafeAreaRight {
    if (@available(iOS 11.0, *)) {
        return [self firstWindow].safeAreaInsets.right;
    } else {
        return 0.0;
    }
}

+ (UIEdgeInsets)deviceSafeAreaInsets {
    if (@available(iOS 11.0, *)) {
        return [self firstWindow].safeAreaInsets;
    } else {
        return UIEdgeInsetsMake(0, 0, 0, 0);
    }
}

+ (double)systemVersion {
    return [[UIDevice currentDevice].systemVersion doubleValue];
}

+ (NSString *)deviceMachineType {
    struct utsname systemInfo;
    uname(&systemInfo);
    return [NSString stringWithCString:systemInfo.machine encoding:NSASCIIStringEncoding];
}

+ (NSString *)deviceName; {
    return [UIDevice currentDevice].name;
}

+ (NSString *)deviceModel; {
    return [UIDevice currentDevice].model;
}

+ (NSString *)deviceUUID; {
    return [[[UIDevice currentDevice] identifierForVendor] UUIDString];
}

+ (NSString *)systemName; {
    return [UIDevice currentDevice].systemName;
}

+ (NSString *)appVersion; {
    return [[NSBundle mainBundle] infoDictionary][@"CFBundleShortVersionString"];
}

+ (NSString *)appBuildVersion; {
    return [[NSBundle mainBundle] infoDictionary][@"CFBundleVersion"];
}

+ (NSString *)appName; {
    return [[NSBundle mainBundle] infoDictionary][@"CFBundleDisplayName"];
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
 */
+ (void)hideKeyboard:(UITextField *)textField {
    [textField resignFirstResponder];
}

/**
 * 为键盘添加隐藏按钮
 */
+ (void)addHideButtonForKeyboard:(UITextField *)textField target:(nullable id)target action:(nullable SEL)action {
    UIBarButtonItem *emptyBtn = [[UIBarButtonItem alloc] initWithBarButtonSystemItem:UIBarButtonSystemItemFlexibleSpace target:nil action:nil];
    UIBarButtonItem *hideBtn = [[UIBarButtonItem alloc] initWithTitle:@"完成" style:UIBarButtonItemStyleDone target:target action:action];
    UIToolbar *toolbar = [[UIToolbar alloc] initWithFrame:CGRectMake(0, 0, [self deviceScreenWidth], 30)];
    [toolbar setItems:@[emptyBtn, hideBtn] animated:false];
    [toolbar sizeToFit];
    [textField setInputAccessoryView:toolbar];
}

+ (void)printSystemInfo {
    NSLog(@"%s, ----------------------------------------", __FUNCTION__);
    NSLog(@"%s, systemName=%@", __FUNCTION__, [STSystemUtil systemName]);
    NSLog(@"%s, systemVersion=%f", __FUNCTION__, [STSystemUtil systemVersion]);

    NSLog(@"%s, --------------------", __FUNCTION__);
    NSLog(@"%s, appName=%@", __FUNCTION__, [STSystemUtil appName]);
    NSLog(@"%s, appVersion=%@", __FUNCTION__, [STSystemUtil appVersion]);
    NSLog(@"%s, appBuildVersion=%@", __FUNCTION__, [STSystemUtil appBuildVersion]);

    NSLog(@"%s, --------------------", __FUNCTION__);
    NSLog(@"%s, deviceName=%@", __FUNCTION__, [STSystemUtil deviceName]);
    NSLog(@"%s, deviceModel=%@", __FUNCTION__, [STSystemUtil deviceModel]);
    NSLog(@"%s, deviceUUID=%@", __FUNCTION__, [STSystemUtil deviceUUID]);
    NSLog(@"%s, deviceMachineType=%@", __FUNCTION__, [STSystemUtil deviceMachineType]);
    NSLog(@"%s, deviceMachineTypeName=%@", __FUNCTION__, [STSystemUtil deviceMachineTypeName]);
    NSLog(@"%s, deviceBatteryLevel=%f", __FUNCTION__, [STSystemUtil deviceBatteryLevel]);
    NSLog(@"%s, deviceScreenWidth=%f", __FUNCTION__, [STSystemUtil deviceScreenWidth]);
    NSLog(@"%s, deviceScreenHeight=%f", __FUNCTION__, [STSystemUtil deviceScreenHeight]);
    NSLog(@"%s, deviceScreenMaxLength=%f", __FUNCTION__, [STSystemUtil deviceScreenMaxLength]);
    NSLog(@"%s, deviceScreenMinLength=%f", __FUNCTION__, [STSystemUtil deviceScreenMinLength]);
    NSLog(@"%s, deviceNavigationBarHeight=%f", __FUNCTION__, [STSystemUtil deviceNavigationBarHeight]);
    NSLog(@"%s, deviceSafeAreaBottom=%f", __FUNCTION__, [STSystemUtil deviceSafeAreaBottom]);
    NSLog(@"%s, deviceSafeAreaTop=%f", __FUNCTION__, [STSystemUtil deviceSafeAreaTop]);
    NSLog(@"%s, deviceSafeAreaLeft=%f", __FUNCTION__, [STSystemUtil deviceSafeAreaLeft]);
    NSLog(@"%s, deviceSafeAreaRight=%f", __FUNCTION__, [STSystemUtil deviceSafeAreaRight]);
    NSLog(@"%s, deviceTabBarHeight=%f", __FUNCTION__, [STSystemUtil deviceTabBarHeight]);
    NSLog(@"%s, deviceStatusBarHeight=%f", __FUNCTION__, [STSystemUtil deviceStatusBarHeight]);
    NSLog(@"%s, deviceNavigationBarAndStatusBarHeight=%f", __FUNCTION__, [STSystemUtil deviceNavigationBarAndStatusBarHeight]);

    NSLog(@"%s, --------------------", __FUNCTION__);
    NSLog(@"%s, isIphone=%d", __FUNCTION__, [STSystemUtil isIphone]);
    NSLog(@"%s, isIphone4OrLess=%d", __FUNCTION__, [STSystemUtil isIphone4OrLess]);
    NSLog(@"%s, isIphone5Or5S=%d", __FUNCTION__, [STSystemUtil isIphone5Or5S]);
    NSLog(@"%s, isIphone6Or7Or8=%d", __FUNCTION__, [STSystemUtil isIphone6Or7Or8]);
    NSLog(@"%s, isIphone6POr7POr8P=%d", __FUNCTION__, [STSystemUtil isIphone6POr7POr8P]);
    NSLog(@"%s, isIphoneXOrMore=%d", __FUNCTION__, [STSystemUtil isIphoneXOrMore]);
    NSLog(@"%s, isHaveSafeArea=%d", __FUNCTION__, [STSystemUtil isHaveSafeArea]);

    NSLog(@"%s, --------------------", __FUNCTION__);
    NSLog(@"%s, firstWindow=%@", __FUNCTION__, [STSystemUtil firstWindow]);
    NSLog(@"%s, rootViewController=%@", __FUNCTION__, [STSystemUtil rootViewController]);
    NSLog(@"%s, topViewController=%@\n", __FUNCTION__, [STSystemUtil topViewController]);
    NSLog(@"%s, ----------------------------------------", __FUNCTION__);
}

/**
 * GSM 不支持 CDMA/TD-LTE
 * GLOBAL 全网通版本
 */
+ (NSString *)deviceMachineTypeName; {
    NSString *deviceMachineType = [self deviceMachineType];

    if ([deviceMachineType isEqualToString:@"iPhone1,1"]) return @"iPhone 1G";
    if ([deviceMachineType isEqualToString:@"iPhone1,2"]) return @"iPhone 3G";
    if ([deviceMachineType isEqualToString:@"iPhone2,1"]) return @"iPhone 3GS";
    if ([deviceMachineType isEqualToString:@"iPhone3,1"]) return @"iPhone 4";
    if ([deviceMachineType isEqualToString:@"iPhone3,2"]) return @"iPhone 4 Verizon";
    if ([deviceMachineType isEqualToString:@"iPhone4,1"]) return @"iPhone 4S";
    if ([deviceMachineType isEqualToString:@"iPhone5,2"]) return @"iPhone 5";
    if ([deviceMachineType isEqualToString:@"iPhone5,3"]) return @"iPhone 5c";
    if ([deviceMachineType isEqualToString:@"iPhone5,4"]) return @"iPhone 5c";
    if ([deviceMachineType isEqualToString:@"iPhone6,1"]) return @"iPhone 5s";
    if ([deviceMachineType isEqualToString:@"iPhone6,2"]) return @"iPhone 5s";
    if ([deviceMachineType isEqualToString:@"iPhone7,1"]) return @"iPhone 6 Plus";
    if ([deviceMachineType isEqualToString:@"iPhone7,2"]) return @"iPhone 6";
    if ([deviceMachineType isEqualToString:@"iPhone8,1"]) return @"iPhone 6s";
    if ([deviceMachineType isEqualToString:@"iPhone8,2"]) return @"iPhone 6s Plus";
    if ([deviceMachineType isEqualToString:@"iPhone8,4"]) return @"iPhone SE";
    if ([deviceMachineType isEqualToString:@"iPhone9,1"]) return @"iPhone 7";
    if ([deviceMachineType isEqualToString:@"iPhone9,2"]) return @"iPhone 7 Plus";
    if ([deviceMachineType isEqualToString:@"iPhone9,3"]) return @"iPhone 7";
    if ([deviceMachineType isEqualToString:@"iPhone9,4"]) return @"iPhone 7 Plus";
    if ([deviceMachineType isEqualToString:@"iPhone10,1"]) return @"iPhone 8 Global";
    if ([deviceMachineType isEqualToString:@"iPhone10,2"]) return @"iPhone 8 Plus Global";
    if ([deviceMachineType isEqualToString:@"iPhone10,3"]) return @"iPhone X Global";
    if ([deviceMachineType isEqualToString:@"iPhone10,4"]) return @"iPhone 8 GSM";
    if ([deviceMachineType isEqualToString:@"iPhone10,5"]) return @"iPhone 8 Plus GSM";
    if ([deviceMachineType isEqualToString:@"iPhone10,6"]) return @"iPhone X GSM";

    if ([deviceMachineType isEqualToString:@"iPhone11,2"]) return @"iPhone XS";
    if ([deviceMachineType isEqualToString:@"iPhone11,4"]) return @"iPhone XS Max (China)";
    if ([deviceMachineType isEqualToString:@"iPhone11,6"]) return @"iPhone XS Max";
    if ([deviceMachineType isEqualToString:@"iPhone11,8"]) return @"iPhone XR";

    if ([deviceMachineType isEqualToString:@"i386"]) return @"Simulator 32";
    if ([deviceMachineType isEqualToString:@"x86_64"]) return @"Simulator 64";

    if ([deviceMachineType isEqualToString:@"iPad1,1"]) return @"iPad";
    if ([deviceMachineType isEqualToString:@"iPad2,1"] || [deviceMachineType isEqualToString:@"iPad2,2"] || [deviceMachineType isEqualToString:@"iPad2,3"] || [deviceMachineType isEqualToString:@"iPad2,4"])
        return @"iPad 2";
    if ([deviceMachineType isEqualToString:@"iPad3,1"] || [deviceMachineType isEqualToString:@"iPad3,2"] || [deviceMachineType isEqualToString:@"iPad3,3"])
        return @"iPad 3";
    if ([deviceMachineType isEqualToString:@"iPad3,4"] || [deviceMachineType isEqualToString:@"iPad3,5"] || [deviceMachineType isEqualToString:@"iPad3,6"])
        return @"iPad 4";
    if ([deviceMachineType isEqualToString:@"iPad4,1"] || [deviceMachineType isEqualToString:@"iPad4,2"] || [deviceMachineType isEqualToString:@"iPad4,3"])
        return @"iPad Air";
    if ([deviceMachineType isEqualToString:@"iPad5,3"] || [deviceMachineType isEqualToString:@"iPad5,4"])
        return @"iPad Air 2";
    if ([deviceMachineType isEqualToString:@"iPad6,3"] || [deviceMachineType isEqualToString:@"iPad6,4"])
        return @"iPad Pro 9.7-inch";
    if ([deviceMachineType isEqualToString:@"iPad6,7"] || [deviceMachineType isEqualToString:@"iPad6,8"])
        return @"iPad Pro 12.9-inch";
    if ([deviceMachineType isEqualToString:@"iPad6,11"] || [deviceMachineType isEqualToString:@"iPad6,12"])
        return @"iPad 5";
    if ([deviceMachineType isEqualToString:@"iPad7,1"] || [deviceMachineType isEqualToString:@"iPad7,2"])
        return @"iPad Pro 12.9-inch 2";
    if ([deviceMachineType isEqualToString:@"iPad7,3"] || [deviceMachineType isEqualToString:@"iPad7,4"])
        return @"iPad Pro 10.5-inch";

    if ([deviceMachineType isEqualToString:@"iPad2,5"] || [deviceMachineType isEqualToString:@"iPad2,6"] || [deviceMachineType isEqualToString:@"iPad2,7"])
        return @"iPad mini";
    if ([deviceMachineType isEqualToString:@"iPad4,4"] || [deviceMachineType isEqualToString:@"iPad4,5"] || [deviceMachineType isEqualToString:@"iPad4,6"])
        return @"iPad mini 2";
    if ([deviceMachineType isEqualToString:@"iPad4,7"] || [deviceMachineType isEqualToString:@"iPad4,8"] || [deviceMachineType isEqualToString:@"iPad4,9"])
        return @"iPad mini 3";
    if ([deviceMachineType isEqualToString:@"iPad5,1"] || [deviceMachineType isEqualToString:@"iPad5,2"])
        return @"iPad mini 4";

    if ([deviceMachineType isEqualToString:@"iPod1,1"]) return @"iTouch";
    if ([deviceMachineType isEqualToString:@"iPod2,1"]) return @"iTouch2";
    if ([deviceMachineType isEqualToString:@"iPod3,1"]) return @"iTouch3";
    if ([deviceMachineType isEqualToString:@"iPod4,1"]) return @"iTouch4";
    if ([deviceMachineType isEqualToString:@"iPod5,1"]) return @"iTouch5";
    if ([deviceMachineType isEqualToString:@"iPod7,1"]) return @"iTouch6";

    return deviceMachineType;
}

@end

#pragma clang diagnostic pop
