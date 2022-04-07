#import "UIView+OnClickListener.h"
#import <objc/runtime.h>

@implementation UIView (OnClickListener)

- (void) setOnClickListener:(OnClickListener)onClickListener{
    if(self.clickListener){
        NSLog(@"%s only can be set once!", __FUNCTION__);
        return;
    }
    
    self.clickListener = onClickListener;
    self.userInteractionEnabled = YES;
    
    if([self isKindOfClass: [UIControl class]]){
        NSLog(@"%s view is UIControl, addTarget", __FUNCTION__);
        [((UIControl *)self) addTarget:self action:@selector(onViewClicked) forControlEvents:UIControlEventTouchUpInside];
    }else{
        NSLog(@"%s view is not UIControl, addGestureRecognizer", __FUNCTION__);
        UITapGestureRecognizer* tapGestureRecognizer =  [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onViewClicked)];
        // tapGestureRecognizer.delegate = self;
        [self addGestureRecognizer:tapGestureRecognizer];
    }
}

- (void)onViewClicked{
    if (self.clickListener){
        // 防止重复点击
        self.userInteractionEnabled = NO;
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            self.userInteractionEnabled = YES;
        });
        
        self.clickListener();
    }
}

- (void)setClickListener:(OnClickListener) clickListener{
    objc_setAssociatedObject(self, @"clickListener", clickListener, OBJC_ASSOCIATION_COPY);
}

- (OnClickListener)clickListener{
    return objc_getAssociatedObject(self, @"clickListener");
}

//- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldBeRequiredToFailByGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer{
//    BOOL shouldBeRequiredToFailByGestureRecognizer = [gestureRecognizer isKindOfClass:UIScreenEdgePanGestureRecognizer.class];
//    NSLog(@"UIView+OnClickListener.h shouldBeRequiredToFailByGestureRecognizer %d", shouldBeRequiredToFailByGestureRecognizer);
//    return shouldBeRequiredToFailByGestureRecognizer;
//}

/**
 * 多个手势可以同时触发
 */
//- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer{
//    NSLog(@"UIView+OnClickListener.h shouldRecognizeSimultaneouslyWithGestureRecognizer YES");
//    return YES;
//}

@end
