#import "STDragNavigationController.h"
#import <objc/message.h>
#import "STLeftDragPanGestureRecognizer.h"
#import <UIKit/UIGestureRecognizerSubclass.h>
#define kGuestureViewTag 0xae42

@interface STDragNavigationController ()<UIGestureRecognizerDelegate> {
    CGPoint startTouch;
    CGFloat animateTime;
    BOOL dragAnimated;
    BOOL dragBackBegin;
}

@property (nonatomic, strong) UIImageView *lastScreenShotView;
@property (nonatomic, strong) UIView *backgroundView;
@property (nonatomic, strong) NSMutableArray *screenShotsList;
@property (nonatomic, assign) BOOL isMoving;
//之前的逻辑没有处理好shouldCancelDragAction返回YES的时候，在向右滑动中以及滑动停止之后，对shouldInvokeCTRootBackItem的多次调用
@property (nonatomic, assign) BOOL isBackSelectorInvoked;
@property (nonatomic, weak)   UIViewController *toViewController;
@property (nonatomic, strong) CAGradientLayer *shadowLayer;
@property (nonatomic, strong) STLeftDragPanGestureRecognizer *panGuesture;
@end

@implementation STDragNavigationController

- (void)dealloc
{
    self.screenShotsList = nil;
    [self.backgroundView removeFromSuperview];
    self.backgroundView = nil;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
        if ([[UIDevice currentDevice] systemVersion].floatValue < 9.0) {
            self.canDragBack = NO;
        } else {
            self.screenShotsList = [NSMutableArray array];
            
            self.panGuesture = [[STLeftDragPanGestureRecognizer alloc]initWithTarget:self action:@selector(paningGestureReceive:)];
            self.panGuesture.delegate = self;
            [_panGuesture delaysTouchesBegan];
            animateTime = 0.3;
            [self.view addGestureRecognizer:_panGuesture];
            self.canDragBack = YES;
        }
   
}

#pragma mark - ----

- (void)addBackgroundMaskView
{
    CGRect frame = self.view.bounds;
    
    if (!self.backgroundView) {
        self.backgroundView = [[UIView alloc]initWithFrame:frame];
        [self.view.superview insertSubview:self.backgroundView belowSubview:self.view];
    }
    
    self.backgroundView.hidden = NO;
    
    if (self.lastScreenShotView == NULL) {
        self.lastScreenShotView = [[UIImageView alloc] initWithFrame:frame];
        [self.backgroundView addSubview:self.lastScreenShotView];
        self.shadowLayer = [CAGradientLayer layer];
        self.shadowLayer.startPoint = CGPointMake(1.0, 0.5);
        self.shadowLayer.frame = frame;
        self.shadowLayer.endPoint = CGPointMake(0, 0.5);
        self.shadowLayer.colors = [NSArray arrayWithObjects:(id)[[UIColor colorWithWhite:0.0 alpha:0.2f] CGColor], (id)[[UIColor clearColor] CGColor], nil];

        [self.backgroundView.layer addSublayer:self.shadowLayer];
    }
    
    if ([self.screenShotsList count] > 0) {
        if ([self.dragDelegate respondsToSelector:@selector(dragNavigationControllerShouldPopToRoot:)]) {
            BOOL isBackToRoot = [self.dragDelegate dragNavigationControllerShouldPopToRoot:self];
            if (isBackToRoot) {
                self.lastScreenShotView.image = [self.screenShotsList firstObject] ;
            } else {
                self.lastScreenShotView.image = [self.screenShotsList lastObject];
            }
        } else if([self.dragDelegate respondsToSelector:@selector(dragNavigationControllerShouldPopToViewController:)]) {
            UIViewController *ctr = [self.dragDelegate dragNavigationControllerShouldPopToViewController:self];
            
            if (ctr != NULL) {
                NSInteger popIndex = [self.viewControllers indexOfObject:ctr];
                //A->B->C->D
                //D->B, popIndex=1, length = 4,image index=1
                if ([self.screenShotsList count] > popIndex) {
                    self.lastScreenShotView.image = self.screenShotsList[popIndex];
                }
            } else {
                self.lastScreenShotView.image = [self.screenShotsList lastObject];
            }
        } else {
            self.lastScreenShotView.image = [self.screenShotsList lastObject];
        }
    }
}


- (void)addShadowLayerIfNeed
{
    if ([self.backgroundView.layer.sublayers containsObject:self.shadowLayer]) {
        return;
    }
    [self.backgroundView.layer addSublayer:self.shadowLayer];
}

#pragma mark - --- 重载系统方法

- (void)pushViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    
    if ([[UIDevice currentDevice] systemVersion].floatValue < 9.0) {
        // do nothing.
    } else {
        UIImage *capturedImg = [self captureSnapShot_g];
        if (capturedImg) {
            [self.screenShotsList addObject:capturedImg];
        }
        self.canDragBack = YES;
        dragAnimated = NO;
    }
   
    [super pushViewController:viewController animated:animated];
}

- (UIViewController *)popViewControllerAnimated:(BOOL)animated
{
    
    if ([[UIDevice currentDevice] systemVersion].floatValue < 9.0) {
        // do nothing.
    } else {
        [self removeLastImage];
        if (dragAnimated) {
            animated = NO;
        }
    }

    UIViewController *ctr = [super popViewControllerAnimated:animated];
    return ctr;
}

- (NSArray *)popToViewController:(UIViewController *)viewController animated:(BOOL)animated
{
    if ([[UIDevice currentDevice] systemVersion].floatValue < 9.0) {
        // do nothing.
    } else {
        NSInteger index = [self.viewControllers indexOfObject:viewController];
        NSInteger imgCount = [self.screenShotsList count];
        if (imgCount >= 1) {
            for (NSInteger i = imgCount - 1; i >= index; i--) {
                [self.screenShotsList removeObjectAtIndex:i];
            }
        }
        
        if (dragAnimated) {
            animated = NO;
        }
    }
    
    NSArray *array = nil;
    @try {
        array = [super popToViewController:viewController animated:animated];
    }
    @catch (NSException *exception) {
        NSLog(@"TODO:popToViewController crash:%@", [exception userInfo]);
    }
    return array;
}

- (NSArray *)popToRootViewControllerAnimated:(BOOL)animated
{
    
    if ([[UIDevice currentDevice] systemVersion].floatValue < 9.0) {
        // do nothing.
    } else {
        NSInteger imgCount = [self.screenShotsList count];
        
        for (NSInteger i = imgCount-1; i >= 1; i--) {
            [self.screenShotsList removeObjectAtIndex:i];
        }
        
        if (dragAnimated) {
            animated = NO;
        }
    }
    
    NSArray *array = [super popToRootViewControllerAnimated:animated];
    return array;
}

#pragma mark - Private Methods -

+ (UIImage *)imageWithView:(UIView *)view
{
    CGRect rect = view.bounds;
    if (CGRectIsEmpty(rect) ||
        CGRectIsNull(rect) ||
        CGRectIsInfinite(rect) ||
        view.superview == nil ||
        view.window == nil ||
        view.window.hidden ) { //window hidden的时候，直接retrun
        return nil;
    }
    
    //https://github.com/kif-framework/KIF/issues/679
    
    UIGraphicsBeginImageContextWithOptions(view.bounds.size, view.opaque, 0.0f);
    
    UIImage * snapshotImage = NULL;
    
    if ([view respondsToSelector:@selector(drawViewHierarchyInRect:afterScreenUpdates:)]) {
        BOOL drawSuccess = [view drawViewHierarchyInRect:view.bounds afterScreenUpdates:NO];
        if (drawSuccess) {
            snapshotImage = UIGraphicsGetImageFromCurrentImageContext();
        }
    } else {
        [view.layer renderInContext:UIGraphicsGetCurrentContext()];
        snapshotImage = UIGraphicsGetImageFromCurrentImageContext();
    }
    
    UIGraphicsEndImageContext();
    
    if (!snapshotImage){
        //解决bu提的在iphone 6上，行程点击机票卡片再侧滑返回空白的问题
        snapshotImage = [self snapshotImage:view];
    }
    
    return snapshotImage;
}

+ (UIImage *)snapshotImage:(UIView *)view {
    UIGraphicsBeginImageContextWithOptions(view.bounds.size, view.opaque, 0);
    [view.layer renderInContext:UIGraphicsGetCurrentContext()];
    UIImage *snap = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return snap;
}

- (UIImage *)captureSnapShot_g
{
    UIImage * img = [STDragNavigationController imageWithView:self.view];
    return img;
}

- (void)moveViewWithX:(float)x isTransaction:(BOOL)isTransaction
{
    x = x>self.view.bounds.size.width?self.view.bounds.size.width:x;
    x = x<0?0:x;
    if (isTransaction) {
        [CATransaction begin];
        [CATransaction setValue:(id)kCFBooleanTrue forKey:kCATransactionDisableActions];
    }
    
    CGRect frame = self.view.frame;
    frame.origin.x = x;
    self.view.frame = frame;
    
    self.lastScreenShotView.center = CGPointMake(x/2, self.view.center.y);
    
    CGFloat shadowWidth = 10;
    
    self.shadowLayer.frame = CGRectMake(x-shadowWidth,  0, shadowWidth, self.shadowLayer.frame.size.height);
    
    if (isTransaction) {
        [CATransaction commit];
    }
}

#pragma mark - Gesture Recognizer -

- (void)removeLastImage
{
    id lastObj = [self.screenShotsList lastObject];
    if ([self.screenShotsList count] >= 1) {
        [self.screenShotsList removeObject:lastObj];
    }
}

- (void)restorePopAction
{
    [self.shadowLayer removeFromSuperlayer];
    [UIView animateWithDuration:animateTime animations:^{
        [self moveViewWithX:0 isTransaction:NO];
    } completion:^(BOOL finished) {
        self.isMoving = NO;
    }];
}

- (BOOL)shouldCancelDragAction
{
    if ([self.dragDelegate respondsToSelector:@selector(dragNavigationControllerShouldCancelDragAction:)]) {
        BOOL shouldCancel = [self.dragDelegate dragNavigationControllerShouldCancelDragAction:self];
        if (shouldCancel) {
            [self restorePopAction];
            [self shouldInvokeCTRootBackItem];
            
            return YES;
        }
    }
    
    return NO;
}

- (BOOL)shouldInvokeCTRootBackItem
{
    CTBaseRootViewController *tmpViewCtr = (CTBaseRootViewController*)self.visibleViewController;
    BOOL isUseCTRootBackItem = NO;
//    if ([tmpViewCtr isKindOfClass:[CTBaseRootViewController class]]) {
        UIBarButtonItem *backItem = self.visibleViewController.navigationItem.leftBarButtonItem;
        UIButton *backBtn = (UIButton *)[backItem customView];
//        if (![backBtn isKindOfClass:[UIButton class]]) {
//            backBtn = [self backButton:backBtn];
//        }
        
        if ([backBtn isKindOfClass:[UIButton class]]) {
            SEL tmpSelector = NSSelectorFromString(@"handleBack:");
//            if ([tmpViewCtr isKindOfClass:NSClassFromString(@"CTH5ViewController")] && [tmpViewCtr respondsToSelector:tmpSelector]) {
                if (!_isBackSelectorInvoked){
                    _isBackSelectorInvoked = YES;
                    void (*objcMsgSend)(id, SEL, BOOL) = (void*)objc_msgSend;
                    objcMsgSend(tmpViewCtr, tmpSelector, YES);
                    isUseCTRootBackItem = YES;
                }

//            }else{
//                NSSet *targets = [backBtn allTargets];
//                for (id target in targets) {
//                    NSArray *actions = [backBtn actionsForTarget:target forControlEvent:UIControlEventTouchUpInside];
//                    for (NSString *actionName in actions) {
//                        SEL selector = NSSelectorFromString(actionName);
//                        if (selector != NULL) {
//                            isUseCTRootBackItem = YES;
//
//                            if (!_isBackSelectorInvoked){
//                                _isBackSelectorInvoked = YES;
//                            }else{
//                                break;
//                            }
//
//                            void (*objcMsgSend)(id, SEL, id) = (void*)objc_msgSend;
//                            objcMsgSend(target, selector, backBtn);
//                        }
//                    }
//                }
//            }
            
        }
//    }
    
    return isUseCTRootBackItem;
}

- (UIButton *)backButton:(UIView*)view{
    for (UIView* sbView in view.subviews) {
        if ([sbView isKindOfClass:[UIButton class]]) {
            return (UIButton *)sbView;
        }
    }
    return nil;
}

- (void)paningGestureReceive:(UIPanGestureRecognizer *)recoginzer
{
    if (self.viewControllers.count <= 1 || !self.canDragBack) {
        return;
    }
    
    CGPoint touchPoint =  [recoginzer locationInView:[[UIApplication sharedApplication] keyWindow]];

    if (recoginzer.state == UIGestureRecognizerStateBegan) {
        if (touchPoint.x > 50 || touchPoint.y < 64) {
            return;
        }
        dragBackBegin = YES;
        self.view.userInteractionEnabled = NO;
        _isMoving = YES;
        _isBackSelectorInvoked = NO;
        startTouch = touchPoint;
        [self addBackgroundMaskView];
        [[NSNotificationCenter defaultCenter] postNotificationName:kDragToBackGuestureBegin object:nil];

    } else if (recoginzer.state == UIGestureRecognizerStateEnded) {
        if (!dragBackBegin) {
            return;
        }
        self.view.userInteractionEnabled = YES;
        CGFloat velocityX = [recoginzer velocityInView:self.view].x;
        
        if (velocityX < 200) {
            animateTime = 0.3;
        } else {
            animateTime = 0.2;
        }
        
        if (touchPoint.x - startTouch.x > self.view.frame.size.width/3) {
            
            CGFloat x = self.view.frame.size.width;
            [self addShadowLayerIfNeed];
            
            [UIView animateWithDuration:animateTime animations:^{
                [self moveViewWithX:x isTransaction:NO];
            } completion:^(BOOL finished){
                
                self->dragAnimated = YES;
                
                BOOL isUseCTRootBackItem = [self shouldInvokeCTRootBackItem];
                
                if (!isUseCTRootBackItem) {
                    [[NSNotificationCenter defaultCenter] postNotificationName:kDragToBackWithDefaultPop object:nil];
                    [self popViewControllerAnimated:NO];
                }
                
                [[NSNotificationCenter defaultCenter] postNotificationName:kDragToBackGuestureEnd object:nil];

                CGRect frame = self.view.frame;
                frame.origin.x = 0;
                self.view.frame = frame;

                self->dragAnimated = NO;
                
                self->_isMoving = NO;
            }];
        } else {
            [self restorePopAction];
        }
        dragBackBegin = NO;
        return;
    } else if (recoginzer.state == UIGestureRecognizerStateCancelled){
        if (!dragBackBegin) {
            return;
        }
        self.view.userInteractionEnabled = YES;
        [self restorePopAction];
        dragBackBegin = NO;
        return;
    } else {
        if (!dragBackBegin) {
            return;
        }
        if (_isMoving) {
            if (touchPoint.x - startTouch.x > (self.view.frame.size.width/6)){
                if ([self shouldCancelDragAction]) {
                    
                    return;
                }
            }
            
            [self addShadowLayerIfNeed];
            [self moveViewWithX:touchPoint.x - startTouch.x isTransaction:YES];
        }
    }
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer
{
    if (gestureRecognizer != self.panGuesture) {
        return NO;
    }
    if ([otherGestureRecognizer isKindOfClass:NSClassFromString(@"RCTTouchHandler")]) {
        return YES;
    }
//    if (self.panGuesture.state != UIGestureRecognizerStateBegan) return NO;
//
//    if (otherGestureRecognizer.state != UIGestureRecognizerStateBegan) {
//        return YES;
//    }
    CGPoint touchPoint = [self.panGuesture beganLocationInView:[[UIApplication sharedApplication] keyWindow]];
    // 点击区域判断 如果在左边 30 以内, 强制手势后退
    if (touchPoint.x < 30 && touchPoint.y > 64 && ![otherGestureRecognizer isKindOfClass:[UILongPressGestureRecognizer class]]) {
        [self cancelOtherGestureRecognizer:otherGestureRecognizer];
        return YES;
    }
    
    return NO;
}

- (BOOL)gestureRecognizerShouldBegin:(UIGestureRecognizer *)gestureRecognizer{

    return [self gestureRecognizerShouldBeginImpl:gestureRecognizer];
}

- (void)cancelOtherGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer
{
    NSSet *touchs = [self.panGuesture.event touchesForGestureRecognizer:otherGestureRecognizer];
    [otherGestureRecognizer touchesCancelled:touchs withEvent:self.panGuesture.event];
}



- (BOOL)gestureRecognizerShouldBeginImpl:(UIGestureRecognizer *)gestureRecognizer{
    
    CGPoint touchPoint = [self.panGuesture beganLocationInView:[[UIApplication sharedApplication] keyWindow]];
    // 点击区域判断 如果不在左边 50 以内, 不往下识别
    if (touchPoint.x > 50 || touchPoint.y < 64) {
        return NO;
    }
    return YES;
}
@end
