#import "STNavViewController.h"

@interface STNavViewController () <UIGestureRecognizerDelegate, UINavigationControllerDelegate>

@property(nonatomic, strong) UIViewController *currentShowVc;

@end

@implementation STNavViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // 禁止原生侧滑 防止与RN侧滑冲突
    self.interactivePopGestureRecognizer.enabled = NO;
    self.interactivePopGestureRecognizer.delegate = self;
    self.delegate = self;
}


- (void)navigationController:(UINavigationController *)navigationController didShowViewController:(UIViewController *)viewController animated:(BOOL)animated {
    if (navigationController.viewControllers.count <= 1) {
        _currentShowVc = nil;
    } else {
        _currentShowVc = viewController;
    }
}


- (BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gestureRecognizer {

    if (gestureRecognizer == self.interactivePopGestureRecognizer) {

        if (self.topViewController) {
            return YES;
        }
        return NO;
    }

    return YES;

}


@end
