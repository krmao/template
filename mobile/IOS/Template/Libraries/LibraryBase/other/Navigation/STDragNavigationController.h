#import <UIKit/UIKit.h>


#define kDragToBackGuestureBegin @"kDragToBackGuestureBegin"
#define kDragToBackGuestureEnd @"kDragToBackGuestureEnd"

#define kDragToBackWithDefaultPop @"kDragToBackWithDefaultPop"  //默认的pop返回，没调到backBarButtonClicked的时候

@class STDragNavigationController;
@class CTBaseRootViewController;

@protocol STDragNavigationControllerDelegate <NSObject>

@optional

/**
 *  手势滑动过程中，需要弹出Alert的处理
 *
 *  @param navCtr 当前导航栏VC
 *
 *  @return 弹出alert之后，返回YES，cancel滑动。否则不需要实现该回调
 */
- (BOOL)dragNavigationControllerShouldCancelDragAction:(STDragNavigationController *)navCtr;


/**
 *  是否需要Pop到Root的判断，类似于PopToRootViewController:
 *
 *  @param navCtr 当前导航栏VC
 *
 *  @return 需要返回到RootVC的时候，该回调返回YES。否则不需要实现
 */
- (BOOL)dragNavigationControllerShouldPopToRoot:(STDragNavigationController *)navCtr;


/**
 *  需要Pop到指定VC的处理
 *
 *  @param navCtr 当前导航栏VC
 *
 *  @return 需要pop到的VC
 */
- (UIViewController *)dragNavigationControllerShouldPopToViewController:(STDragNavigationController *)navCtr;


@end

@interface STDragNavigationController : UINavigationController {

}

@property(nonatomic, weak) id <STDragNavigationControllerDelegate> dragDelegate;

@property(nonatomic, assign) BOOL canDragBack;

- (BOOL)gestureRecognizerShouldBeginImpl:(UIGestureRecognizer *)gestureRecognizer;

@end
