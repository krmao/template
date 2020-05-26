#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface STSystemUtil : NSObject
+ (UIWindow *)firstWindow;

+ (void)printSystemInfo;

+ (BOOL)isIphone;

+ (BOOL)isIphone4OrLess;

+ (BOOL)isIphone5;

+ (BOOL)isIphone6;

+ (BOOL)isIphone6Plus;

+ (BOOL)isNeedSafeArea;

+ (CGFloat)navigationBarHeight;

+ (CGFloat)statusBarHeight;

+ (CGFloat)navigationBarAndStatusBarHeight;

+ (CGFloat)tabBarHeight;

+ (CGFloat)safeAreaBottom;

+ (CGFloat)screenHeight;

+ (CGFloat)screenWidth;

+ (double)systemVersion;

+ (NSString *)systemName;

+ (UIViewController *)rootViewController;

+ (UIViewController *)topViewController;

// 计算单行文字的size
+ (CGSize)getTextSize:(NSString *)text withFont:(UIFont *)font;

// 计算多行文字的CGRect
+ (CGRect)getTextSizeWithBoundingRect:(NSString *)str withSize:(CGSize)size withFont:(UIFont *)font;

+ (void)hideKeyboard:(UITextField *)textField;

+ (void)addHideButtonForKeyboard:(UITextField *)textField target:(nullable id)target action:(nullable SEL)action;
@end

NS_ASSUME_NONNULL_END
