#import "UIViewController+TopMostViewController.h"
#import "STRNUnZip.h"
@implementation UIViewController (TopMostViewController)

+ (UIViewController *)topMostViewController {

    UIViewController *vc = [[UIApplication sharedApplication] keyWindow].rootViewController;
    if (vc) {
        return [self topMostViewController:vc];
    } else {
        return nil;
    }
}

+ (UIViewController *)topMostViewController:(UIViewController *)viewController {
    if ([viewController isKindOfClass:[UITabBarController class]]) {
      return [self topMostViewController:[(UITabBarController *)viewController selectedViewController]];
    }
    
    if ([viewController isKindOfClass:[UINavigationController class]]) {
        return [self topMostViewController:[(UINavigationController *)viewController visibleViewController]];
    }
    
    return viewController;
}

+ (UIImage *)testImage {
    
    NSString *path = [[NSBundle mainBundle] pathForResource:@"LibraryFoundation" ofType:@"framework" inDirectory:@"Frameworks"];
    NSBundle *bundle = [NSBundle bundleWithPath:path];
    UIImage *img = [UIImage imageNamed:@"test.jpg" inBundle:bundle compatibleWithTraitCollection:nil];

    return img;
    
}

@end
