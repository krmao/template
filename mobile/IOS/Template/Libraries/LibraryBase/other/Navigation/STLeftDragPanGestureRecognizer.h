#import <UIKit/UIKit.h>

@interface STLeftDragPanGestureRecognizer : UIPanGestureRecognizer

@property (readonly, nonatomic) UIEvent *event;
- (CGPoint)beganLocationInView:(UIView *)view;

@end
